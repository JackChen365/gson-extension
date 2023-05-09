package com.jack.android.gson.extension.adapter

import com.google.gson.Gson
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.annotations.JsonAdapter
import com.google.gson.internal.ConstructorConstructor
import com.google.gson.internal.bind.TreeTypeAdapter
import com.google.gson.reflect.TypeToken

/**
 * Given a type T, looks for the annotation [JsonAdapter] and uses an instance of the
 * specified class as the default type adapter.
 *
 * @since 2.3
 */
class JsonAdapterAnnotationTypeAdapterFactory(
    private val constructorConstructor: ConstructorConstructor
) : TypeAdapterFactory {
    override fun <T> create(gson: Gson, targetType: TypeToken<T>): TypeAdapter<T>? {
        val rawType = targetType.rawType
        val annotation = rawType.getAnnotation(JsonAdapter::class.java) ?: return null
        return getTypeAdapter(constructorConstructor, gson, targetType, annotation) as TypeAdapter<T>
    }

    fun <T> getTypeAdapter(
        constructorConstructor: ConstructorConstructor, gson: Gson?,
        type: TypeToken<T>, annotation: JsonAdapter
    ): TypeAdapter<T> {
        val instance: Any = constructorConstructor.get(TypeToken.get(annotation.value.java)).construct()
        var typeAdapter: TypeAdapter<T>
        typeAdapter = when (instance) {
            is TypeAdapter<*> -> {
                instance as TypeAdapter<T>
            }
            is TypeAdapterFactory -> {
                instance.create(gson, type)
            }
            is JsonSerializer<*>, is JsonDeserializer<*> -> {
                val serializer: JsonSerializer<T>? =
                    if (instance is JsonSerializer<*>) instance as JsonSerializer<T> else null
                val deserializer: JsonDeserializer<T>? =
                    if (instance is JsonDeserializer<*>) instance as JsonDeserializer<T> else null
                TreeTypeAdapter(serializer, deserializer, gson, type, null)
            }
            else -> {
                throw IllegalArgumentException(
                    "Invalid attempt to bind an instance of "
                            + instance.javaClass.name + " as a @JsonAdapter for " + type.toString()
                            + ". @JsonAdapter value must be a TypeAdapter, TypeAdapterFactory,"
                            + " JsonSerializer or JsonDeserializer."
                )
            }
        }
        if (typeAdapter != null && annotation.nullSafe) {
            typeAdapter = typeAdapter.nullSafe()
        }
        return typeAdapter
    }
}