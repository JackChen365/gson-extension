package com.jack.android.gson.extension.adapter.factory

import com.google.gson.TypeAdapterFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.TypeAdapter
import com.google.gson.internal.`$Gson$Types`
import com.jack.android.gson.extension.adapter.type.NullableArrayTypeAdapter
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Type

class NullableArrayTypeAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, typeToken: TypeToken<T>): TypeAdapter<T>? {
        val type = typeToken.type
        if (!(type is GenericArrayType || type is Class<*> && type.isArray)) {
            return null
        }
        val componentType: Type = `$Gson$Types`.getArrayComponentType(type)
        val componentTypeAdapter = gson.getAdapter(TypeToken.get(componentType))
        val rawType = `$Gson$Types`.getRawType(componentType) as Class<T>
        return NullableArrayTypeAdapter(
            gson,
            componentTypeAdapter as TypeAdapter<T>,
            rawType
        )
    }
}