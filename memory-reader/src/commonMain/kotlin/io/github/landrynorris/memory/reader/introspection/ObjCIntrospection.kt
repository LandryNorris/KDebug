package io.github.landrynorris.memory.reader.introspection

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.toLong
import platform.objc.object_getClass
import platform.posix.uintptr_tVar

object ObjCIntrospection {
    // This section is different for x86_64. Consider if we should support this.
    private val OBJC_TAG_MASK = 1UL shl 63
    private const val OBJC_TAG_INDEX_SHIFT = 60
    private const val OBJC_TAG_EXT_INDEX_SHIFT = 52

    val COpaquePointer?.isObjcObject: Boolean get() {
        if(this == null) return false
        if(isObjcTaggedPointer) return true
        if(!isObjcAligned || !highBitsAllZero) return false
        if(object_getClass(this) == null) return false
        return true
    }

    //
    // From LLDB:
    // Objective-C runtime has a rule that pointers in a class_t will only have bits 0 thru 46 set
    // so if any pointer has bits 47 thru 63 high we know that this is not a valid isa
    // See http://llvm.org/svn/llvm-project/lldb/trunk/examples/summaries/cocoa/objc_runtime.py
    //
    private val COpaquePointer.highBitsAllZero: Boolean inline get() {
        val ptr = toLong().toULong()
        return ptr and 0xFFFF800000000000UL == 0UL
    }

    private val COpaquePointer.isObjcAligned: Boolean inline get() {
        val ptr = toLong()
        return ptr % sizeOf<uintptr_tVar>() == 0L
    }

    private val COpaquePointer.isObjcTaggedPointer: Boolean inline get() {
        val ptr = toLong().toULong()
        return ptr and OBJC_TAG_MASK == OBJC_TAG_MASK
    }
}