package com.jack.android.gson.extension.logger

/**
 * Internal debug logger.
 */
class DefaultInternalDebugLogger : DebugLogger {
    override fun e(throwable: Throwable?) {
        throwable?.printStackTrace()
    }
}