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
}