import io.github.landrynorris.memory.reader.MemoryReader
import io.github.landrynorris.memory.reader.introspection.KotlinIntrospection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ClassNameTests {
    @Test
    fun testGetStringClassName() {
        val s = "abcd"
        s.usePtr {
            assertNotNull(it)
            assertEquals("kotlin.String", KotlinIntrospection.getClassName(it))
        }
    }

    @Test
    fun testGetReaderClassName() {
        val reader = MemoryReader()

        reader.usePtr {
            assertNotNull(it)
            assertEquals("io.github.landrynorris.memory.reader.MemoryReader", KotlinIntrospection.getClassName(it))
        }
    }
}
