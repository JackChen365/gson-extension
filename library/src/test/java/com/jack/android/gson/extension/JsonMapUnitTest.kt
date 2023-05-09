package com.jack.android.gson.extension

import org.junit.Assert
import org.junit.Test

/**
 * unit-test to test Map.
 */
class JsonMapUnitTest {

    @Test
    fun testIntMapDeserialize() {
        val jsonString = """
            {"key1":1,"key2":2}
        """.trimIndent()
        val map = toMap(jsonString, Int::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to 1, "key2" to 2), map)
    }

    @Test
    fun testLongMapDeserialize() {
        val jsonString = """
            {"key1":1,"key2":2}
        """.trimIndent()
        val map = toMap(jsonString, Long::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to 1L, "key2" to 2L), map)
    }

    @Test
    fun testFloatMapDeserialize() {
        val jsonString = """
            {"key1":1.0,"key2":2.0}
        """.trimIndent()
        val map = toMap(jsonString, Float::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to 1f, "key2" to 2f), map)
    }

    @Test
    fun testDoubleListDeserialize() {
        val jsonString = """
            {"key1":1.0,"key2":2.0}
        """.trimIndent()
        val map = toMap(jsonString, Double::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to 1.0, "key2" to 2.0), map)
    }

    @Test
    fun testBooleanMapDeserialize() {
        val jsonString = """
            {"key1":true,"key2":true}
        """.trimIndent()
        val map = toMap(jsonString, Boolean::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to true, "key2" to true), map)
    }

    @Test
    fun testStringMapDeserialize() {
        val jsonString = """
            {"key1":"value1","key2":"value2"}
        """.trimIndent()
        val map = toMap(jsonString, String::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to "value1", "key2" to "value2"), map)
    }

    @Test
    fun testNullableIntMapDeserialize() {
        val jsonString = """
            {"key1":null,"key2":null}
        """.trimIndent()
        val map = toMap(jsonString, Int::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to null, "key2" to null), map)
    }

    @Test
    fun testNullableLongMapDeserialize() {
        val jsonString = """
            {"key1":null,"key2":null}
        """.trimIndent()
        val map = toMap(jsonString, Long::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to null, "key2" to null), map)
    }

    @Test
    fun testNullableFloatMapDeserialize() {
        val jsonString = """
            {"key1":null,"key2":null}
        """.trimIndent()
        val map = toMap(jsonString, Float::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to null, "key2" to null), map)
    }

    @Test
    fun testNullableDoubleMapDeserialize() {
        val jsonString = """
            {"key1":null,"key2":null}
        """.trimIndent()
        val map = toMap(jsonString, Double::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to null, "key2" to null), map)
    }

    @Test
    fun testNullableBooleanMapDeserialize() {
        val jsonString = """
            {"key1":null,"key2":null}
        """.trimIndent()
        val map = toMap(jsonString, Boolean::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to null, "key2" to null), map)
    }

    @Test
    fun testNullableStringMapDeserialize() {
        val jsonString = """
            {"key1":null,"key2":null}
        """.trimIndent()
        val map = toMap(jsonString, String::class.java)
        Assert.assertNotNull(map)
        Assert.assertEquals(2, map!!.size)
        Assert.assertEquals(mapOf("key1" to null, "key2" to null), map)
    }

    //--------------------------------------------------------------------------------

    @Test
    fun testIntMapSerialize() {
        Assert.assertEquals(
            "{\"key1\":1,\"key2\":2}",
            toJson(mapOf("key1" to 1, "key2" to 2))
        )
    }

    @Test
    fun testLongMapSerialize() {
        Assert.assertEquals(
            "{\"key1\":1,\"key2\":2}",
            toJson(mapOf("key1" to 1L, "key2" to 2L))
        )
    }

    @Test
    fun testFloatMapSerialize() {
        Assert.assertEquals(
            "{\"key1\":1.0,\"key2\":2.0}",
            toJson(mapOf("key1" to 1f, "key2" to 2f))
        )
    }

    @Test
    fun testDoubleMapSerialize() {
        Assert.assertEquals(
            "{\"key1\":1.0,\"key2\":2.0}",
            toJson(mapOf("key1" to 1.0, "key2" to 2.0))
        )
    }

    @Test
    fun testBooleanMapSerialize() {
        Assert.assertEquals(
            "{\"key1\":false,\"key2\":true}",
            toJson(mapOf("key1" to false, "key2" to true))
        )
    }

    @Test
    fun testNullableValueMapSerialize() {
        Assert.assertEquals(
            "{}",
            toJson(mapOf("key1" to null, "key2" to null))
        )
    }
}