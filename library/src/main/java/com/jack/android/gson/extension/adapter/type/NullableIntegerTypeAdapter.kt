package com.jack.android.gson.extension.adapter.type

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.jack.android.gson.extension.JSON_DEFAULT_INT

/**
 * The nullable int type adapter.
 * This [TypeAdapter] will helps us serialize/deserialize the nullable int as 0, no matter it is null or other value.
 * For example
 * <p>
 *     IntClass(val value: Int?)
 *     // deserialize
 *     {"value":null} -> testClass:IntClass(0)
 *     // serialize
 *     testClass:IntClass(null) -> {"value":0}
 * </p>
 */
class NullableIntegerTypeAdapter : TypeAdapter<Int>() {
    override fun write(out: JsonWriter, value: Int?) {
        kotlin.runCatching {
            if (value == null) {
                out.value(JSON_DEFAULT_INT)
            } else {
                out.value(value)
            }
        }
    }

    override fun read(reader: JsonReader): Int {
        kotlin.runCatching {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return JSON_DEFAULT_INT
            }
            return if (reader.peek() == JsonToken.STRING) {
                reader.nextString().toIntOrNull() ?: JSON_DEFAULT_INT
            } else {
                reader.nextInt()
            }
        }
        return JSON_DEFAULT_INT
    }
}