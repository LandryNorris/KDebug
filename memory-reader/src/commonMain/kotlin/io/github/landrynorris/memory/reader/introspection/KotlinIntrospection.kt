package io.github.landrynorris.memory.reader.introspection

import io.github.landrynorris.memory.reader.introspection.ObjCIntrospection.isObjcObject
import kotlinx.cinterop.*

object KotlinIntrospection {
    /**
     * Receiver should be a ObjHeader*
     */
    private val COpaquePointer.isKotlinObject: Boolean get() = getTypeInfo() != null

    /**
     * Get the class name of a Kotlin object by an ObjHeader*, or null if the pointer is
     * not a valid kotlin object
     * @param ptr an ObjHeader*
     */
    fun getClassName(ptr: COpaquePointer): String? {
        if(!ptr.isObjcObject || !ptr.isKotlinObject) return null

        val typeInfo = ptr.getTypeInfo()?.reinterpret<TypeInfo>()?.pointed

        val packageNamePtr = typeInfo?.packageName_?.remove3Bit()
        val relativeNamePtr = typeInfo?.relativeName_?.remove3Bit()
        val packageName = packageNamePtr?.readStringObjHeader()
        val relativeName = relativeNamePtr?.readStringObjHeader()

        return "$packageName.$relativeName"
    }

    /**
     * The receiver should be an ObjHeader*
     * The first field of an ObjHeader is a TypeInfo*
     */
    private fun COpaquePointer.getTypeInfo(): COpaquePointer? {
        val typeInfoPtr = reinterpret<COpaquePointerVar>()[0]?.remove3Bit() ?: return null
        if(typeInfoPtr.verifyTypeInfo()) {
            return typeInfoPtr
        }
        return null
    }

    /**
     * All Kotlin TypeInfo values start with a pointer to itself as defined by the ABI.
     * Receiver should be a TypeInfo*
     */
    private fun COpaquePointer.verifyTypeInfo(): Boolean {
        val selfPtr = reinterpret<COpaquePointerVar>()[0]
        return selfPtr == this
    }

    private inline fun COpaquePointer.remove3Bit() = toLong().remove3Bit().toCPointer<CPointed>()

    /**
     * The lower 2 bits are reserved for the Kotlin runtime
     */
    private inline fun Long.remove3Bit() = this and (0x3L).inv()

    /**
     * The receiver should be an ObjHeader* for a String
     */
    private fun COpaquePointer.readStringObjHeader(): String {
        val header = reinterpret<StringHeader>().pointed
        val length = header.count.toInt()
        return (0 until length).map { header.content[it].toInt().toChar() }
            .toCharArray().concatToString()
    }
}

// 1111 1110
// 0000 0001
