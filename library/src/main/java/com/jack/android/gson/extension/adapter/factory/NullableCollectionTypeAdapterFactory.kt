package com.jack.android.gson.extension.adapter.factory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.internal.ConstructorConstructor
import com.google.gson.internal.ObjectConstructor
import com.google.gson.internal.`$Gson$Types`
import com.google.gson.reflect.TypeToken
import com.jack.android.gson.extension.adapter.type.NullableCollectionTypeAdapter
import java.lang.reflect.Type

/**
 * [TypeAdapterFactory] to handle all the [Collection]
 * Such as [List], [Set], [Map] or [Queue], those are all build-in instance creator.
 * If you want to handle your own DataStructure.
 * You can implement the [InstanceCreator] and initial this typeAdapter this way:
 * <p>
 *     NullableCollectionTypeAdapterFactory(mapOf(MyOwnList::class.java, MyOwnListInstanceCreator))
 * </p>
 * @see ConstructorConstructor for more details.
 */
class NullableCollectionTypeAdapterFactory(
    private val constructorConstructor: ConstructorConstructor = ConstructorConstructor(emptyMap())
) : TypeAdapterFactory {

    override fun <T> create(gson: Gson, typeToken: TypeToken<T>): TypeAdapter<T>? {
        val type = typeToken.type
        val rawType = typeToken.rawType
        if (!MutableCollection::class.java.isAssignableFrom(rawType)) {
            return null
        }
        val elementType: Type = `$Gson$Types`.getCollectionElementType(type, rawType)
        val elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType)) as TypeAdapter<T>
        val constructor = constructorConstructor.get(typeToken) as ObjectConstructor<out MutableCollection<T>>
        return NullableCollectionTypeAdapter(
            gson,
            elementType,
            elementTypeAdapter,
            constructor
        ) as? TypeAdapter<T>?
    }
}