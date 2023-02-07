package com.jack.android.gson.extension.adapter.type

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.internal.ObjectConstructor
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.reflect.Type

class NullableCollectionTypeAdapter<E>(
    context: Gson,
    elementType: Type,
    elementTypeAdapter: TypeAdapter<E>,
    private val constructor: ObjectConstructor<out MutableCollection<E>>
) : TypeAdapter<Collection<E>>() {
    private val elementTypeAdapter: TypeAdapter<E>

    init {
        this.elementTypeAdapter = RuntimeAdapterTypeWrapper(context, elementTypeAdapter, elementType)
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Collection<E>? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return constructor.construct()
        }
        val collection = constructor.construct()
        reader.beginArray()
        while (reader.hasNext()) {
            val instance = elementTypeAdapter.read(reader)
            collection.add(instance)
        }
        reader.endArray()
        return collection
    }

    @Throws(IOException::class)
    override fun write(out: JsonWriter, collection: Collection<E>?) {
        if (collection == null) {
            out.nullValue()
            return
        }
        out.beginArray()
        for (element in collection) {
            elementTypeAdapter.write(out, element)
        }
        out.endArray()
    }
}