package com.jack.android.gson.extension.factory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.jack.android.gson.extension.logger.DebugLogger
import com.jack.android.gson.extension.value.InitialValueProvider

/**
 * Gson create factory.
 * We combine all the internal [TypeAdapterFactory] and [TypeAdapter] in this GsonFactory.
 * However, if you want to build your own Gson object, you can implement your [GsonFactory]
 * For example:
 * If you don't need [NullableMultiDateAdapterFactory], you can build a gson without it.
 */
interface GsonFactory {
    fun createGson(): Gson
    fun createLogger(): DebugLogger
    fun createInitialValueProvider(): InitialValueProvider

    /**
     * If you want the parser to help you detect a class without an empty constructor, you can enable this
     * for example:
     * <p>
     *     data class Item(val name: String)
     * </p>
     * It only has a constructor with string so that Gson will use unsafeAllocator
     * However, for this class, it's the same. Because only the id has a default value, and the name doesn't have
     * <p>
     *     data class Item(val name: String,val id: Int = 0)
     * </p>
     * So enabling it will stop the deserialize when a class without an empty constructor
     */
    fun forceUseEmptyConstructor() : Boolean
}