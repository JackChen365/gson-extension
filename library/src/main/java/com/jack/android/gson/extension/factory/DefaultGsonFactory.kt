package com.jack.android.gson.extension.factory

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import com.jack.android.gson.extension.adapter.factory.NullableArrayTypeAdapterFactory
import com.jack.android.gson.extension.adapter.factory.NullableCollectionTypeAdapterFactory
import com.jack.android.gson.extension.adapter.factory.NullablePrimitiveAdapterFactory
import com.jack.android.gson.extension.adapter.factory.NullableStringAdapterFactory
import com.jack.android.gson.extension.logger.DebugLogger
import com.jack.android.gson.extension.logger.DefaultInternalDebugLogger

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
        return GsonBuilder()
            .setLenient()
            .enableComplexMapKeySerialization()
            .registerTypeAdapterFactory(NullableStringAdapterFactory())
            .registerTypeAdapterFactory(NullablePrimitiveAdapterFactory())
            .registerTypeAdapterFactory(NullableArrayTypeAdapterFactory())
            .registerTypeAdapterFactory(NullableCollectionTypeAdapterFactory())
            .disableHtmlEscaping()
            .create()
    }

    override fun createLogger(): DebugLogger {
        return DefaultInternalDebugLogger()
    }
}