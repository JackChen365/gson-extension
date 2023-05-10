package com.jack.android.gson.extension

import org.junit.Assert
import org.junit.Test

/**
 * unit-test to test List value.
 */
class JsonListUnitTest {

    @Test
    fun testIntListDeserialize() {
        val jsonString = """
            [1,2,3,4]
        """.trimIndent()
        val intList = toList(jsonString, Int::class.java)
        Assert.assertNotNull(intList)
        Assert.assertEquals(4, intList!!.size)
        Assert.assertEquals(listOf(1, 2, 3, 4), intList)
    }

    @Test
    fun testLongListDeserialize() {
        val jsonString = """
            [1,2,3,4]
        """.trimIndent()
        val longList = toList(jsonString, Long::class.java)
        Assert.assertNotNull(longList)
        Assert.assertEquals(4, longList!!.size)
        Assert.assertEquals(listOf(1L, 2L, 3L, 4L), longList)
    }

    @Test
    fun testFloatListDeserialize() {
        val jsonString = """
            [1,2,3,4]
        """.trimIndent()
        val floatList = toList(jsonString, Float::class.java)
        Assert.assertNotNull(floatList)
        Assert.assertEquals(4, floatList!!.size)
        Assert.assertEquals(listOf(1f, 2f, 3f, 4f), floatList)
    }

    @Test
    fun testDoubleListDeserialize() {
        val jsonString = """
            [1,2,3,4]
        """.trimIndent()
        val doubleList = toList(jsonString, Double::class.java)
        Assert.assertNotNull(doubleList)
        Assert.assertEquals(4, doubleList!!.size)
        Assert.assertEquals(listOf(1.0, 2.0, 3.0, 4.0), doubleList)
    }

    @Test
    fun testBooleanListDeserialize() {
        val jsonString = """
            [true,false,true,false]
        """.trimIndent()
        val booleanList = toList(jsonString, Boolean::class.java)
        Assert.assertNotNull(booleanList)
        Assert.assertEquals(4, booleanList!!.size)
        Assert.assertEquals(listOf(true, false, true, false), booleanList)
    }

    @Test
    fun testStringListDeserialize() {
        val jsonString = """
            ["a","b","c"]
        """.trimIndent()
        val stringList = toList(jsonString, String::class.java)
        Assert.assertNotNull(stringList)
        Assert.assertEquals(3, stringList!!.size)
        Assert.assertEquals(listOf("a", "b", "c"), stringList)
    }

    @Test
    fun testNullableIntListDeserialize() {
        val jsonString = """
            [null,1,null,null]
        """.trimIndent()
        val intList = toList(jsonString, Int::class.java)
        Assert.assertNotNull(intList)
        Assert.assertEquals(4, intList!!.size)
        Assert.assertEquals(listOf(null, 1, null, null), intList)
    }

    @Test
    fun testNullableLongListDeserialize() {
        val jsonString = """
            [null,null,null,null]
        """.trimIndent()
        val longList = toList(jsonString, Long::class.java)
        Assert.assertNotNull(longList)
        Assert.assertEquals(4, longList!!.size)
        Assert.assertEquals(listOf(null, null, null, null), longList)
    }

    @Test
    fun testNullableFloatListDeserialize() {
        val jsonString = """
            [null,null,null,null]
        """.trimIndent()
        val floatList = toList(jsonString, Float::class.java)
        Assert.assertNotNull(floatList)
        Assert.assertEquals(4, floatList!!.size)
        Assert.assertEquals(listOf(null, null, null, null), floatList)
    }

    @Test
    fun testNullableDoubleListDeserialize() {
        val jsonString = """
            [null,null,null,null]
        """.trimIndent()
        val doubleList = toList(jsonString, Double::class.java)
        Assert.assertNotNull(doubleList)
        Assert.assertEquals(4, doubleList!!.size)
        Assert.assertEquals(listOf(null, null, null, null), doubleList)
    }

    @Test
    fun testNullableBooleanListDeserialize() {
        val jsonString = """
            [null,null,null,null]
        """.trimIndent()
        val booleanList = toList(jsonString, Boolean::class.java)
        Assert.assertNotNull(booleanList)
        Assert.assertEquals(4, booleanList!!.size)
        Assert.assertEquals(listOf(null, null, null, null), booleanList)
    }

    @Test
    fun testNullableStringListDeserialize() {
        val jsonString = """
            [null,null,null]
        """.trimIndent()
        val stringList = toList(jsonString, String::class.java)
        Assert.assertNotNull(stringList)
        Assert.assertEquals(3, stringList!!.size)
        Assert.assertEquals(listOf(null, null, null), stringList)
    }

    //--------------------------------------------------------------------------------

    @Test
    fun testIntListSerialize() {
        Assert.assertEquals("[1,2,3,4]", toJson(listOf(1, 2, 3, 4)))
    }

    @Test
    fun testLongListSerialize() {
        Assert.assertEquals("[1,2,3,4]", toJson(listOf(1L, 2L, 3L, 4L)))
    }

    @Test
    fun testFloatListSerialize() {
        Assert.assertEquals("[1.0,2.0,3.0,4.0]", toJson(listOf(1f, 2f, 3f, 4f)))
    }

    @Test
    fun testDoubleListSerialize() {
        Assert.assertEquals("[1.0,2.0,3.0,4.0]", toJson(listOf(1.0, 2.0, 3.0, 4.0)))
    }

    @Test
    fun testBooleanListSerialize() {
        Assert.assertEquals("[true,false,true,false]", toJson(listOf(true, false, true, false)))
    }

    @Test
    fun testNullableIntListSerialize() {
        Assert.assertEquals("[]", toJson(listOfNotNull<Int>(null, null, null, null)))
    }

    @Test
    fun testNullableLongListSerialize() {
        Assert.assertEquals("[]", toJson(listOfNotNull<Long>(null, null, null, null)))
    }

    @Test
    fun testNullableFloatListSerialize() {
        Assert.assertEquals("[]", toJson(listOfNotNull<Float>(null, null, null, null)))
    }

    @Test
    fun testNullableDoubleListSerialize() {
        Assert.assertEquals("[]", toJson(listOfNotNull<Double>(null, null, null, null)))
    }

    @Test
    fun testNullableBooleanListSerialize() {
        Assert.assertEquals("[]", toJson(listOfNotNull<Boolean>(null, null, null, null)))
    }

    @Test
    fun testStringListSerialize() {
        Assert.assertEquals("[\"a\",\"b\",\"c\"]", toJson(listOf("a", "b", "c")))
    }

    @Test
    fun testNullableStringListSerialize() {
        Assert.assertEquals("[]", toJson(listOfNotNull<String>(null, null, null)))
    }
}