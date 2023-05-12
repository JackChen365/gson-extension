package com.jack.android.gson.extension

import com.jack.android.gson.extension.rule.ForceCheckEmptyConstructor
import com.jack.android.gson.extension.rule.GsonExtensionRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * unit-test to test List value.
 */
class JsonObjectWithDefaultValueTest {
    @get:Rule
    val extensionRule = GsonExtensionRule()
    private class DataClass(val name: String = "Jack", val id: Int = 2)

    @Test
    fun testDeserializeWithDefaultValue() {
        val jsonString = """
            {"name":null,id:null}
        """.trimIndent()
        val dataClass = fromJson(jsonString, DataClass::class.java)
        Assert.assertNotNull(dataClass)
        Assert.assertEquals("Jack", dataClass?.name)
        Assert.assertEquals(2, dataClass?.id)
    }

    @Test
    fun testDeserializeWithDefaultValueAndResponse() {
        val jsonString = """
            {"name":"Tom",id:null}
        """.trimIndent()
        val dataClass = fromJson(jsonString, DataClass::class.java)
        Assert.assertNotNull(dataClass)
        Assert.assertEquals("Tom", dataClass?.name)
        Assert.assertEquals(2, dataClass?.id)
    }

    private class DataClass2(val name: String, val id: Int)

    @Test
    fun testDeserializeWithoutDefaultValueAndResponse() {
        val jsonString = """
            {"name":null,id:null}
        """.trimIndent()
        val dataClass = fromJson(jsonString, DataClass2::class.java)
        Assert.assertNotNull(dataClass)
        Assert.assertEquals("", dataClass?.name)
        Assert.assertEquals(0, dataClass?.id)
    }

    private class DataClass3(val name: String, val id: Int = 2)

    @Test
    fun testDeserializeDoNotCheckDefaultConstructor() {
        val jsonString = """
            {"name":null,id:null}
        """.trimIndent()
        val dataClass = fromJson(jsonString, DataClass3::class.java)
        Assert.assertNotNull(dataClass)
        Assert.assertEquals("", dataClass?.name)
        Assert.assertEquals(0, dataClass?.id)
    }


    @Test
    @ForceCheckEmptyConstructor
    fun testDeserializeCheckDefaultConstructor() {
        val jsonString = """
            {"name":null,id:null}
        """.trimIndent()
        val dataClass = fromJson(jsonString, DataClass3::class.java)
        Assert.assertEquals(null, dataClass)
    }
}