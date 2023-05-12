package com.jack.android.gson.extension

import com.jack.android.gson.extension.rule.GsonExtensionRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * unit-test to test Int value.
 */
class JsonLongUnitTest {
    @get:Rule
    val extensionRule = GsonExtensionRule()
    private class LongClass(val number: Long?)

    @Test
    fun testLongDeserialize() {
        val jsonString = """
            {"number":1581068174}
        """.trimIndent()
        val longClass = fromJson(jsonString, LongClass::class.java)
        Assert.assertNotNull(longClass)
        Assert.assertEquals(1581068174L, longClass!!.number)
    }

    @Test
    fun testNullableLongDeserialize() {
        val jsonString = """
            {"number":null}
        """.trimIndent()
        val intClass = fromJson(jsonString, LongClass::class.java)
        Assert.assertNotNull(intClass)
        Assert.assertEquals(0L, intClass!!.number)
    }

    @Test
    fun testLongSerialize() {
        Assert.assertEquals("{\"number\":2}", toJson(LongClass(2)))
    }

    @Test
    fun testNullableLongSerialize() {
        Assert.assertEquals("{\"number\":0}", toJson(LongClass(null)))
    }
}