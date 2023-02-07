package com.jack.android.gson.extension.adapter.type

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.jack.android.gson.extension.JSON_DEFAULT_DOUBLE

/**
 * The nullable double type adapter.
 * This [TypeAdapter] will helps us serialize/deserialize the nullable double as true/false, no matter it is null or other value.
 * For example
 * <p>
 *     DoubleClass(val value: Double?)
 *     // deserialize
 *     {"value":null} -> testClass:DoubleClass(0.0)
 *     // serialize
 *     testClass:DoubleClass(null) -> {"value":0.0}
 * </p>
 */
class NullableDoubleTypeAdapter : TypeAdapter<Double?>() {
    override fun write(out: JsonWriter, value: Double?) {
        kotlin.runCatching {
            if (value == null) {
                out.value(JSON_DEFAULT_DOUBLE)
            } else {
                out.value(value)
            }
        }
    }

    override fun read(reader: JsonReader): Double {
        kotlin.runCatching {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return JSON_DEFAULT_DOUBLE
            }
            return if (reader.peek() == JsonToken.STRING) {
                val str = reader.nextString()
                str.toDoubleOrNull() ?: JSON_DEFAULT_DOUBLE
            } else {
                reader.nextDouble()
            }
        }
        return JSON_DEFAULT_DOUBLE
    }
}