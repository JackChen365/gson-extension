package com.jack.android.gson.extension.adapter.type

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.reflect.Array
import java.util.ArrayList
import kotlin.Throws

class NullableArrayTypeAdapter<T>(
    context: Gson,
    componentTypeAdapter: TypeAdapter<T>,
    private val componentType: Class<T>
) : TypeAdapter<T>() {
    private val componentTypeAdapter: TypeAdapter<T>

    init {
        this.componentTypeAdapter = RuntimeAdapterTypeWrapper(context, componentTypeAdapter, componentType)
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): T? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return Array.newInstance(componentType, 0) as? T
        }
        val list: MutableList<T> = ArrayList()
        reader.beginArray()
        while (reader.hasNext()) {
            val instance = componentTypeAdapter.read(reader)
            list.add(instance)
        }
        reader.endArray()
        val size = list.size
        val array = Array.newInstance(componentType, size)
        for (i in 0 until size) {
            Array.set(array, i, list[i])
        }
        return array as? T
    }

    @Throws(IOException::class)
    override fun write(out: JsonWriter, array: T?) {
        if (array == null) {
            out.nullValue()
            return
        }
        out.beginArray()
        var i = 0
        val length = Array.getLength(array)
        while (i < length) {
            val value = Array.get(array, i) as T
            componentTypeAdapter.write(out, value)
            i++
        }
        out.endArray()
    }
}