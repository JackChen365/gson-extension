package com.jack.android.gson.extension

import org.junit.Assert
import org.junit.Test

/**
 * Unit-test to test Array.
 */
class JsonArrayUnitTest {
    private class ArrayClass(val value: Array<Int>?)

    @Test
    fun testArrayDeserialize() {
        val jsonString = """
            {"value":[1,2,3]}
        """.trimIndent()
        val result = fromJson(jsonString, ArrayClass::class.java)
        Assert.assertNotNull(result)
        Assert.assertNotNull(result!!.value)
        Assert.assertEquals(arrayOf(1, 2, 3), result!!.value)
    }

    @Test
    fun testNullableArrayDeserialize() {
        val jsonString = """
            {"value":[null,null,null]}
        """.trimIndent()
        val result = fromJson(jsonString, ArrayClass::class.java)
        Assert.assertNotNull(result)
        Assert.assertNotNull(result!!.value)
        Assert.assertArrayEquals(arrayOf(null, null, null), result!!.value)
    }

    @Test
    fun testNullableArrayDeserialize2() {
        val jsonString = """
            {"value":[null,2,null]}
        """.trimIndent()
        val result = fromJson(jsonString, ArrayClass::class.java)
        Assert.assertNotNull(result)
        Assert.assertNotNull(result!!.value)
        Assert.assertArrayEquals(arrayOf(null,2,null), result!!.value)
    }

    @Test
    fun testArraySerialize() {
        Assert.assertEquals("{\"value\":[1,2,3]}", toJson(ArrayClass(value = arrayOf(1, 2, 3))))
    }

    @Test
    fun testNullableArraySerialize() {
        Assert.assertEquals("{}", toJson(ArrayClass(value = null)))
    }
}