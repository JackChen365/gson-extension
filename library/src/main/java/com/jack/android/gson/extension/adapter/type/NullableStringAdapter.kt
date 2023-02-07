package com.jack.android.gson.extension.adapter.type

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlin.Throws
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.jack.android.gson.extension.JSON_DEFAULT_STRING
import java.io.IOException
import java.lang.IllegalStateException
import java.util.ArrayList

/**
 * The nullable String type adapter.
 * This [TypeAdapter] will helps us serialize/deserialize the nullable String as "", no matter it is null or other value.
 * For example
 * <p>
 *     StringClass(val value: String?)
 *     // deserialize
 *     {"value":null} -> testClass:StringClass("")
 *     // serialize
 *     testClass:StringClass(null) -> {"value":""}
 * </p>
 */
class NullableStringAdapter(private val gson: Gson) : TypeAdapter<String>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: String?) {
        if (value == null) {
            out.value(JSON_DEFAULT_STRING)
        } else {
            out.value(value)
        }
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): String {
        return when (reader.peek()) {
            JsonToken.NULL -> {
                reader.nextNull()
                JSON_DEFAULT_STRING
            }
            JsonToken.BEGIN_ARRAY -> {
                val list: MutableList<Any?> = ArrayList()
                reader.beginArray()
                while (reader.hasNext()) {
                    list.add(readInside(reader))
                }
                reader.endArray()
                gson.toJson(list)
            }
            JsonToken.BEGIN_OBJECT -> {
                val map: MutableMap<String, Any?> =
                    LinkedTreeMap()
                reader.beginObject()
                while (reader.hasNext()) {
                    map[reader.nextName()] = readInside(reader)
                }
                reader.endObject()
                gson.toJson(map)
            }
            else -> reader.nextString()
        }
    }

    @Throws(IOException::class)
    fun readInside(reader: JsonReader): Any? {
        return when (reader.peek()) {
            JsonToken.BEGIN_ARRAY -> {
                val list: MutableList<Any?> = ArrayList()
                reader.beginArray()
                while (reader.hasNext()) {
                    list.add(readInside(reader))
                }
                reader.endArray()
                list
            }
            JsonToken.BEGIN_OBJECT -> {
                val map: MutableMap<String, Any?> =
                    LinkedTreeMap()
                reader.beginObject()
                while (reader.hasNext()) {
                    map[reader.nextName()] = readInside(reader)
                }
                reader.endObject()
                map
            }
            JsonToken.STRING -> reader.nextString()
            JsonToken.NUMBER -> {
                val value = reader.nextString()
                value.toLongOrNull() ?: value.toDoubleOrNull()
            }
            JsonToken.BOOLEAN -> reader.nextBoolean()
            JsonToken.NULL -> {
                reader.nextNull()
                null
            }
            else -> throw IllegalStateException()
        }
    }
}