package com.jack.android.gson.extension

import org.junit.Assert
import org.junit.Test

/**
 * unit-test to test Int value.
 */
class JsonIntTypeUnitTest {
    private class IntClass(val number: Int?)

    @Test
    fun testIntDeserialize() {
        val jsonString = """
            {"number":2}
        """.trimIndent()
        val intClass = fromJson(jsonString, IntClass::class.java)
        Assert.assertNotNull(intClass)
        Assert.assertEquals(2, intClass!!.number)
    }

    @Test
    fun testNullableIntDeserialize() {
        val jsonString = """
            {"number":null}
        """.trimIndent()
        val intClass = fromJson(jsonString, IntClass::class.java)
        Assert.assertNotNull(intClass)
        Assert.assertEquals(0, intClass!!.number)
    }

    @Test
    fun testIntSerialize() {
        Assert.assertEquals("{\"number\":2}", toJson(IntClass(2)))
    }

    @Test
    fun testNullableIntSerialize() {
        Assert.assertEquals("{\"number\":0}", toJson(IntClass(null)))
    }
}