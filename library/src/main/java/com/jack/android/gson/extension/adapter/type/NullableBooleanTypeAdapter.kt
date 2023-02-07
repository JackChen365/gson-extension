package com.jack.android.gson.extension.adapter.type

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.jack.android.gson.extension.JSON_DEFAULT_BOOLEAN
import com.jack.android.gson.extension.JSON_DEFAULT_DOUBLE
import com.jack.android.gson.extension.JSON_DEFAULT_FLOAT
import java.lang.Exception

/**
 * The nullable boolean type adapter.
 * This [TypeAdapter] will helps us serialize/deserialize the nullable boolean as true/false, no matter it is null or other value.
 * For example
 * <p>
 *     BooleanClass(val value: Boolean?)
 *     // deserialize
 *     {"value":null} -> testClass:BooleanClass(false)
 *     // serialize
 *     testClass:BooleanClass(null) -> {"value":false}
 * </p>
 */
class NullableBooleanTypeAdapter : TypeAdapter<Boolean?>() {
    override fun write(out: JsonWriter, value: Boolean?) {
        kotlin.runCatching {
            if (value == null) {
                out.value(JSON_DEFAULT_BOOLEAN)
            } else {
                out.value(value)
            }
        }
    }

    override fun read(reader: JsonReader): Boolean? {
        kotlin.runCatching {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return JSON_DEFAULT_BOOLEAN
            }
            if (reader.peek() == JsonToken.BOOLEAN) {
                return reader.nextBoolean()
            }
            return if (reader.peek() == JsonToken.STRING) {
                val str = reader.nextString()
                str.toBoolean()
            } else {
                JSON_DEFAULT_BOOLEAN
            }
        }
        return JSON_DEFAULT_BOOLEAN
    }
}