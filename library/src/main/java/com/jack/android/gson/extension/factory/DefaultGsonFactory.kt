package com.jack.android.gson.extension.factory

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import com.google.gson.internal.ConstructorConstructor
import com.jack.android.gson.extension.adapter.ReflectiveTypeAdapterFactory
import com.jack.android.gson.extension.logger.DebugLogger
import com.jack.android.gson.extension.logger.DefaultInternalDebugLogger
import com.jack.android.gson.extension.value.DefaultInitialValueProvider
import com.jack.android.gson.extension.value.InitialValueProvider
import java.util.Collections

/**
 * The default [GsonFactory], we add all the [TypeAdapterFactory] to the GsonBuilder and help you create one Gson instance.
 * If you want to change how we build gson instance. you can implement you own [GsonFactory] and initial it by invoking [initialGsonExtension]
 *
 * We handle the debug log by [DebugLogger] since it all about parse error and this is a pure Kotlin library.
 * You can use the default one or implement it with Android Log.
 *
 * Here is one example of using Kotlin delegate to implement your logger
 * <p>
 *      val androidLogger = DebugLogger{ e->
 *          Log.e(TAG,e)
 *      }
 *      class MyOwnGsonFactory(val gsonFactory:GsonFactory = DefaultGsonFactory()): GsonFactory by gsonFactory{
 *          override fun createLogger(): DebugLogger = androidLogger
 *      }
 * </p>
 */
class DefaultGsonFactory : GsonFactory {
    override fun createGson(): Gson {
        val constructorConstructor = ConstructorConstructor(
            Collections.emptyMap()
        )
        val initialValueProvider = createInitialValueProvider()
        return GsonBuilder()
            .setLenient()
            .enableComplexMapKeySerialization()
            .registerTypeAdapterFactory(
                ReflectiveTypeAdapterFactory(
                    initialValueProvider,
                    constructorConstructor
                )
            )
            .disableHtmlEscaping()
            .create()
    }

    override fun createLogger(): DebugLogger {
        return DefaultInternalDebugLogger()
    }

    override fun createInitialValueProvider(): InitialValueProvider {
        return DefaultInitialValueProvider()
    }
}