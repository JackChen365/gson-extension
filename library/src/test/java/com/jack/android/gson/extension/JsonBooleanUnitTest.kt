package com.jack.android.gson.extension

import org.junit.Assert
import org.junit.Test

/**
 * unit-test to test Boolean value.
 */
class JsonBooleanUnitTest {
    private class BooleanClass(val number: Boolean?)

    @Test
    fun testBooleanDeserialize() {
        val jsonString = """
            {"number":false}
        """.trimIndent()
        val booleanClass = fromJson(jsonString, BooleanClass::class.java)
        Assert.assertNotNull(booleanClass)
        Assert.assertEquals(false, booleanClass!!.number)
    }

    @Test
    fun testNullableDeserialize() {
        val jsonString = """
            {"number":null}
        """.trimIndent()
        val booleanClass = fromJson(jsonString, BooleanClass::class.java)
        Assert.assertNotNull(booleanClass)
        Assert.assertEquals(false, booleanClass!!.number)
    }

    @Test
    fun testBooleanSerialize() {
        Assert.assertEquals("{\"number\":true}", toJson(BooleanClass(true)))
    }

    @Test
    fun testNullableBooleanSerialize() {
        Assert.assertEquals("{\"number\":false}", toJson(BooleanClass(null)))
    }
}