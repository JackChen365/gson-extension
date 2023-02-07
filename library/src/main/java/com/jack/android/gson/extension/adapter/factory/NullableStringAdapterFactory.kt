package com.jack.android.gson.extension.adapter.factory

import com.google.gson.TypeAdapterFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.TypeAdapter
import com.jack.android.gson.extension.adapter.type.NullableStringAdapter

/**
 * The nullable String [TypeAdapterFactory] helps us handler the String.
 * For deserialize:
 * <p>
 *     TestClass(val value:String?)
 *     {"value":"Hello world"} will be parse to TestClass(value = "Hello world")
 *     {"value":null} will be parse to TestClass(value = "")
 * </p>
 * For serialize
 * <p>
 *     TestClass(val value:String?)
 *     TestClass("Hello world") -> {"value":"Hello world"}
 *     TestClass(null) -> {"value":""}
 * </p>
 */
class NullableStringAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        return if (String::class.java.isAssignableFrom(type.rawType)) {
            NullableStringAdapter(gson) as? TypeAdapter<T>
        } else null
    }
}