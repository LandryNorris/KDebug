import io.github.landrynorris.memory.reader.introspection.KotlinIntrospection
import kotlinx.cinterop.*
import models.DataHolder

fun main(args: Array<String>) {
    println("Starting")

    val data = DataHolder(2, 50, 64)
    val array = intArrayOf(12, 37, 16, 200, 123456)

    data.usePtr {
        println(KotlinIntrospection.getClassName(it!!))
    }

    array.usePtr {
        println(KotlinIntrospection.getClassName(it!!))
    }
}

fun Any.usePtr(block: (COpaquePointer?) -> Unit) {
    val ref = StableRef.create(this)
    block(ref.asCPointer().reinterpret<COpaquePointerVar>()[0])
    ref.dispose()
}
