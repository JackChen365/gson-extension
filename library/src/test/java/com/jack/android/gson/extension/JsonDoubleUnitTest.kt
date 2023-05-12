package com.jack.android.gson.extension

import com.jack.android.gson.extension.rule.GsonExtensionRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * unit-test to test double value.
 */
class JsonDoubleUnitTest {
    @get:Rule
    val extensionRule = GsonExtensionRule()
    private class DoubleClass(val number: Double?)

    @Test
    fun testDoubleDeserialize() {
        val jsonString = """
            {"number":2.0}
        """.trimIndent()
        val doubleClass = fromJson(jsonString, DoubleClass::class.java)
        Assert.assertNotNull(doubleClass)
        Assert.assertEquals(2.0, doubleClass!!.number)
    }

    @Test
    fun testNullableDoubleDeserialize() {
        val jsonString = """
            {"number":null}
        """.trimIndent()
        val doubleClass = fromJson(jsonString, DoubleClass::class.java)
        Assert.assertNotNull(doubleClass)
        Assert.assertEquals(0.0, doubleClass!!.number)
    }

    @Test
    fun testDoubleSerialize() {
        Assert.assertEquals("{\"number\":0.0}", toJson(DoubleClass(0.0)))
    }

    @Test
    fun testNullableDoubleSerialize() {
        Assert.assertEquals("{\"number\":0.0}", toJson(DoubleClass(null)))
    }
}