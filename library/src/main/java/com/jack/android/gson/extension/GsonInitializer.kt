package com.jack.android.gson.extension

import com.google.gson.Gson
import com.jack.android.gson.extension.factory.GsonFactory
import com.jack.android.gson.extension.factory.DefaultGsonFactory
import com.jack.android.gson.extension.logger.DebugLogger

/**
 * Initial the gson extensions.
 * If you don't want initial manually. we will use [DefaultGsonFactory] which is a default [GsonFactory]
 */
fun initialGsonExtension(factory: GsonFactory) {
    internalGsonFactory = factory
}

private val MUTEX = Object()
private val defaultGsonFactory = DefaultGsonFactory()
private var internalGsonFactory: GsonFactory? = null

/**
 * The internal gson object. You can access it by [useGson]
 */
@Volatile
private var internalGson: Gson? = null

@Volatile
private var internalDebugLogger: DebugLogger? = null

internal fun internalGson(): Gson {
    return synchronized(MUTEX) {
        internalGson ?: synchronized(MUTEX) {
            (internalGsonFactory ?: defaultGsonFactory).createGson().also {
                internalGson = it
            }
        }
    }
}

/**
 * Get the internal logger. We
 */
internal fun internalDebugger(): DebugLogger {
    return synchronized(MUTEX) {
        internalDebugLogger ?: synchronized(MUTEX) {
            defaultGsonFactory.createLogger().also {
                internalDebugLogger = it
            }
        }
    }
}