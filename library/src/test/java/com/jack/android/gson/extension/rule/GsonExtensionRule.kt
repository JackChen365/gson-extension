package com.jack.android.gson.extension.rule

import com.jack.android.gson.extension.factory.DefaultGsonFactory
import com.jack.android.gson.extension.initialGsonExtension
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class GsonExtensionRule : TestRule {

    override fun apply(
        base: Statement,
        description: Description
    ): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                val forceCheckEmptyConstructor = description.annotations.find {
                    it is ForceCheckEmptyConstructor
                }
                if (null != forceCheckEmptyConstructor) {
                    initialGsonExtension(object : DefaultGsonFactory() {
                        override fun forceUseEmptyConstructor(): Boolean = true
                    })
                } else {
                    initialGsonExtension(null)
                }
                base.evaluate()
            }
        }
    }
}
