package com.jack.android.gson.extension.adapter.type

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.jack.android.gson.extension.JSON_DEFAULT_FLOAT

/**
 * The nullable float type adapter.
 * This [TypeAdapter] will helps us serialize/deserialize the nullable float as true/false, no matter it is null or other value.
 * For example
 * <p>
 *     FloatClass(val value: Float?)
 *     // deserialize
 *     {"value":null} -> testClass:FloatClass(0f)
 *     // serialize
 *     testClass:FloatClass(null) -> {"value":0f}
 * </p>
 */
class NullableFloatTypeAdapter : TypeAdapter<Float?>() {

    override fun write(out: JsonWriter, value: Float?) {
        kotlin.runCatching {
            if (value == null) {
                out.value(JSON_DEFAULT_FLOAT)
            } else {
                out.value(value)
            }
        }
    }

    override fun read(reader: JsonReader): Float? {
        kotlin.runCatching {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return JSON_DEFAULT_FLOAT
            }
            return if (reader.peek() == JsonToken.STRING) {
                val str = reader.nextString()
                str.toFloatOrNull() ?: JSON_DEFAULT_FLOAT
            } else {
                val str = reader.nextString()
                str.toFloat()
            }
        }
        return JSON_DEFAULT_FLOAT
    }
}