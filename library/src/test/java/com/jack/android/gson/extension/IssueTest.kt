package com.jack.android.gson.extension

import com.google.gson.Gson
import com.jack.android.gson.extension.rule.GsonExtensionRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class IssueTest {
    @get:Rule
    val extensionRule = GsonExtensionRule()
    data class Foo(val name: String = "")

    @Test
    fun testWithNullableAdapter() {
        val jsonString = """
            {"name": null}
        """.trimIndent()
        val dummy = fromJson(jsonString, Foo::class.java)
        // Even the backend return null, we will give it a default empty string.
        Assert.assertNotNull(dummy?.name)
    }

    @Test
    fun testWithoutNullableAdapter() {
        val jsonString = """
            {"name": null}
        """.trimIndent()
        val foo = Gson().fromJson(jsonString, Foo::class.java)
        Assert.assertThrows(AssertionError::class.java) {
            // The foo's name will be null
            Assert.assertNotNull(foo?.name)
        }
    }
}