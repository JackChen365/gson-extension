package com.jack.android.gson.extension

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

/**
 * Unit-test to test serialize or deserialize with Kotlin coroutines.
 */
class JsonWithCoroutinesUnitTest {
    private class ArrayClass(val value: Array<Int>?)
    private class StringClass(val str: String?)

    @Test
    fun testJsonElementDeserialize() = runBlocking {
        val jsonElement = asyncUseGson {
            toJsonTree("{\"value\":[1,2,3]}")
        }
        Assert.assertNotNull(jsonElement)
    }

    @Test
    fun testArrayDeserialize() = runBlocking {
        val jsonString = """
                {"value":[1,2,3]}
            """.trimIndent()
        val result = asyncFromJson(jsonString, ArrayClass::class.java)
        Assert.assertNotNull(result)
        Assert.assertNotNull(result!!.value)
        Assert.assertEquals(arrayOf(1, 2, 3), result!!.value)
    }

    @Test
    fun testStringDeserialize() = runBlocking {
        val jsonString = """
            {"str":"hello world"}
        """.trimIndent()
        val stringClass = asyncFromJson(jsonString, StringClass::class.java)
        Assert.assertNotNull(stringClass)
        Assert.assertEquals("hello world", stringClass!!.str)
    }

    @Test
    fun testStringMapDeserialize() = runBlocking {
        val jsonString = """
            {"key1":"value1","key2":"value2"}
        """.trimIndent()
        val map = asyncToMap(jsonString, String::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to "value1", "key2" to "value2"), map)
    }

    @Test
    fun testNullableArrayDeserialize() = runBlocking {
        val jsonString = """
            {"value":[null,null,null]}
        """.trimIndent()
        val result = asyncFromJson(jsonString, ArrayClass::class.java)
        Assert.assertNotNull(result)
        Assert.assertNotNull(result!!.value)
        Assert.assertArrayEquals(arrayOf(null, null, null), result!!.value)
    }

    @Test
    fun testDoubleListDeserialize() = runBlocking {
        val jsonString = """
            [1,2,3,4]
        """.trimIndent()
        val doubleList = asyncToList(jsonString, Double::class.java)
        Assert.assertNotNull(doubleList)
        Assert.assertEquals(4, doubleList!!.size)
        Assert.assertEquals(listOf(1.0, 2.0, 3.0, 4.0), doubleList)
    }

    @Test
    fun testArraySerialize() = runBlocking {
        Assert.assertEquals("{\"value\":[1,2,3]}", asyncToJson(ArrayClass(value = arrayOf(1, 2, 3))))
    }

    @Test
    fun testIntMapSerialize() = runBlocking {
        Assert.assertEquals(
            "{\"key1\":1,\"key2\":2}",
            asyncToJson(mapOf("key1" to 1, "key2" to 2))
        )
    }

    @Test
    fun testStringSerialize() = runBlocking {
        Assert.assertEquals("{\"str\":\"hello world\"}", asyncToJson(StringClass("hello world")))
    }
}