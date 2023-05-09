package com.jack.android.gson.extension.logger

/**
 * This interface responsible for output the debug error log.
 */
fun interface DebugLogger {
    fun e(throwable: Throwable?)
}