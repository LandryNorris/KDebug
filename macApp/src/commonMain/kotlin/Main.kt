import io.github.landrynorris.memory.reader.MemoryReader
import io.github.landrynorris.memory.reader.introspection.KotlinIntrospection
import kotlinx.cinterop.*
import models.DataHolder

fun main(args: Array<String>) {
    println("Starting")

    val data = DataHolder(2, 50, 64)
    val reader = MemoryReader()
    val array = intArrayOf(12, 37, 16, 200, 123456)

    data.usePtr {
        println(KotlinIntrospection.getClassName(it!!))
    }

    reader.usePtr {
        println(KotlinIntrospection.getClassName(it!!))
    }

    array.usePtr {
        println(KotlinIntrospection.getClassName(it!!))
    }

    //reader.getZones()
}

fun Any.usePtr(block: (COpaquePointer?) -> Unit) {
    val ref = StableRef.create(this)
    block(ref.asCPointer().reinterpret<COpaquePointerVar>()[0])
    ref.dispose()
}

fun Any.typeInfo() {
    usePtr {
        it?.getTypeInfo()
    }
}

fun COpaquePointer.getTypeInfo() {
    val typeInfoPtr = reinterpret<COpaquePointerVar>()[0] ?: return
    if(typeInfoPtr.verifyTypeInfo()) {
        println("We found a Kotlin Object!")
    }
}

/**
 * All Kotlin TypeInfo values start with a pointer to itself as defined by the ABI.
 */
fun COpaquePointer.verifyTypeInfo(): Boolean {
    val selfPtr = reinterpret<COpaquePointerVar>()[0]
    return selfPtr == this
}
