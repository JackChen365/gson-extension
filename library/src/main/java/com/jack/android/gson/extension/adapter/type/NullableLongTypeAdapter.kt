package com.jack.android.gson.extension.adapter.type

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.jack.android.gson.extension.JSON_DEFAULT_LONG

/**
 * The nullable long type adapter.
 * This [TypeAdapter] will helps us serialize/deserialize the nullable long as 0L, no matter it is null or other value.
 * For example
 * <p>
 *     LongClass(val value: Long?)
 *     // deserialize
 *     {"value":null} -> testClass:LongClass(0L)
 *     // serialize
 *     testClass:LongClass(null) -> {"value":0L}
 * </p>
 */
class NullableLongTypeAdapter : TypeAdapter<Long?>() {
    override fun write(out: JsonWriter, value: Long?) {
        kotlin.runCatching {
            if (value == null) {
                out.value(JSON_DEFAULT_LONG)
            } else {
                out.value(value)
            }
        }
    }

    override fun read(reader: JsonReader): Long? {
        kotlin.runCatching {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return JSON_DEFAULT_LONG
            }
            return if (reader.peek() == JsonToken.STRING) {
                reader.nextString().toLongOrNull() ?: JSON_DEFAULT_LONG
            } else {
                reader.nextLong()
            }
        }
        return JSON_DEFAULT_LONG
    }
}