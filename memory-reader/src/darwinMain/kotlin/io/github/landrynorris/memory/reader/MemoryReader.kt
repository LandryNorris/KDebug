package io.github.landrynorris.memory.reader

import io.github.landrynorris.memory.reader.introspection.ObjCIntrospection.isObjcObject
import kotlinx.cinterop.*
import memory.*
import objc.memory_object_structure
import platform.Foundation.NSString
import platform.Foundation.stringWithUTF8String
import platform.darwin.*
import platform.objc.class_getName
import platform.objc.objc_copyClassList
import platform.objc.object_getClass
import platform.posix.free

class MemoryReader {
    private val reader: CPointer<memory_reader_t> = staticCFunction(::readerCallback)
    private val recorder: CPointer<vm_range_recorder_t> = staticCFunction(::recorderCallback)

    fun getZones() = memScoped {
        println(registeredClasses.joinToString(", ") { it })
        val zones = alloc<CPointerVar<vm_address_tVar>>()
        val countPtr = alloc<UIntVar>()

        val result = get_zones(TASK_NULL, reader, zones.ptr, countPtr.ptr)

        val count = countPtr.value.toInt()

        println("Number of zones: $count")

        if(result == KERN_SUCCESS) {
            for(i in 0 until count) {
                val zone = zones.value?.get(i)?.toLong()
                    ?.toCPointer<malloc_zone_t>()?.pointed ?: continue

                println("Zone name: ${zone.zone_name?.toKString()}")

                zone.ptr.memoryObjects()
            }
        }
    }

    private fun CPointer<malloc_zone_t>.memoryObjects(): List<MemoryObject> {
        val introspection = pointed.introspect?.pointed ?: return listOf()

        val callback = { o: CValue<memory_object_structure> ->
            introspection.force_unlock?.invoke(this)
            o.useContents {
                val className = NSString.stringWithUTF8String(class_getName(objectClass))
                    ?: return@useContents
                //println("Class name is $className")
                println("Address is $objectMemory")

                if(objectMemory != null && className.contains("kobjc")) {
                    val mem = objectMemory?.reinterpret<COpaquePointerVar>() ?: return@useContents
                    //KotlinIntrospection.getClassName(mem[2] ?: return@useContents)
                }
            }
            introspection.force_lock?.invoke(this)
        }

        val stableCallback = StableRef.create(callback)

        if(introspection.enumerator != null) {
            introspection.force_unlock?.invoke(this)
            introspection_enumerator(introspection.ptr, TASK_NULL,
                stableCallback.asCPointer(),
                MALLOC_PTR_IN_USE_RANGE_TYPE.toUInt(), this.toLong().toULong(), reader, recorder)
        }
        stableCallback.dispose()
        return listOf()
    }
}

fun readerCallback(task: task_t, address: vm_address_t, size: vm_size_t, memory: CPointer<COpaquePointerVar>?): kern_return_t {
    memory?.set(0, address.toLong().toCPointer())
    return KERN_SUCCESS
}

fun recorderCallback(task: task_t, context: COpaquePointer?, type: UInt,
                     ranges: CPointer<vm_range_t>?, count: UInt) {
    if(context == null || ranges == null) return

    val callback = context.asStableRef<(CValue<memory_object_structure>) -> Unit>().get()

    for(i in 0 until count.toInt()) {
        val range = ranges[i]
        val rawObject = range.address.toLong().toCPointer<CPointed>() ?: continue

        if(rawObject.isObjcObject) {
            val memory = cValue<memory_object_structure> {
                this.objectMemory = rawObject
                this.objectClass = object_getClass(rawObject)
            }
            callback(memory)
        }
    }
}

val registeredClasses by lazy {
    memScoped {
        val count = alloc<UIntVar>()
        val classesPtr = objc_copyClassList(count.ptr)
        val classes = (0 until count.value.toInt()).mapNotNull {
            classesPtr?.get(it)
        }

        return@lazy (classes).distinct().map { it.toString() }.also {
            free(classesPtr)
        }
    }
}
