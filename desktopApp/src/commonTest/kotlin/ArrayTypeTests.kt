import io.github.landrynorris.memory.reader.introspection.KotlinIntrospection
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ArrayTypeTests {
    @Test
    fun testIsStringArrayType() {
        val s = "abcd" //String is a type of array
        s.usePtr {
            assertNotNull(it)
            assertTrue(KotlinIntrospection.isArrayType(it))
        }
    }

    @Test
    fun testIsIntArrayAnArrayType() {
        val arr = intArrayOf(21, 100, 40, 32, 100)
        arr.usePtr {
            assertNotNull(it)
            assertTrue(KotlinIntrospection.isArrayType(it))
        }
    }

    @Test
    fun testIsDoubleArrayAnArrayType() {
        val arr = doubleArrayOf(21.0, 100.0, 40.0, 32.0, 100.0)
        arr.usePtr {
            assertNotNull(it)
            assertTrue(KotlinIntrospection.isArrayType(it))
        }
    }

    @Test
    fun testIsStringArrayAnArrayType() {
        val arr = arrayOf("", "abcdefg", "gfds")
        arr.usePtr {
            assertNotNull(it)
            assertTrue(KotlinIntrospection.isArrayType(it))
        }
    }

    @Test
    fun testIsIntAnArrayType() {
        val i = 0
        i.usePtr {
            assertNotNull(it)
            assertFalse(KotlinIntrospection.isArrayType(it))
        }
    }

    @Test
    fun testIsUnitAnArrayType() {
        val u = Unit
        u.usePtr {
            assertNotNull(it)
            assertFalse(KotlinIntrospection.isArrayType(it))
        }
    }

    @Test
    fun testIsIntrospectionAnArrayType() {
        val o = KotlinIntrospection
        o.usePtr {
            assertNotNull(it)
            assertFalse(KotlinIntrospection.isArrayType(it))
        }
    }
}