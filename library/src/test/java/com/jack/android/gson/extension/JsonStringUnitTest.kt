package com.jack.android.gson.extension

import com.jack.android.gson.extension.rule.GsonExtensionRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * unit-test to test String value.
 */
class JsonStringUnitTest {
    @get:Rule
    val extensionRule = GsonExtensionRule()
    private class StringClass(val str: String?)

    @Test
    fun testStringDeserialize() {
        val jsonString = """
            {"str":"hello world"}
        """.trimIndent()
        val stringClass = fromJson(jsonString, StringClass::class.java)
        Assert.assertNotNull(stringClass)
        Assert.assertEquals("hello world", stringClass!!.str)
    }

    @Test
    fun testNullableStringDeserialize() {
        val jsonString = """
            {"str":null}
        """.trimIndent()
        val stringClass = fromJson(jsonString, StringClass::class.java)
        Assert.assertNotNull(stringClass)
        Assert.assertEquals("", stringClass!!.str)
    }

    @Test
    fun testStringSerialize() {
        Assert.assertEquals("{\"str\":\"hello world\"}", toJson(StringClass("hello world")))
    }

    @Test
    fun testNullableStringSerialize() {
        Assert.assertEquals("{\"str\":\"\"}", toJson(StringClass(null)))
    }
}