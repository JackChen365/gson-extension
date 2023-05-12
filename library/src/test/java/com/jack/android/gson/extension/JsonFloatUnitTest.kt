package com.jack.android.gson.extension

import com.jack.android.gson.extension.rule.GsonExtensionRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * unit-test to test Int value.
 */
class JsonFloatUnitTest {
    @get:Rule
    val extensionRule = GsonExtensionRule()
    private class FloatClass(val number: Float?)

    @Test
    fun testFloatDeserialize() {
        val jsonString = """
            {"number":1.2}
        """.trimIndent()
        val floatClass = fromJson(jsonString, FloatClass::class.java)
        Assert.assertNotNull(floatClass)
        Assert.assertEquals(1.2f, floatClass!!.number)
    }

    @Test
    fun testNullableFloatDeserialize() {
        val jsonString = """
            {"number":null}
        """.trimIndent()
        val floatClass = fromJson(jsonString, FloatClass::class.java)
        Assert.assertNotNull(floatClass)
        Assert.assertEquals(0f, floatClass!!.number)
    }

    @Test
    fun testFloatSerialize() {
        Assert.assertEquals("{\"number\":2.0}", toJson(FloatClass(2f)))
    }

    @Test
    fun testNullableFloatSerialize() {
        Assert.assertEquals("{\"number\":0.0}", toJson(FloatClass(null)))
    }
}