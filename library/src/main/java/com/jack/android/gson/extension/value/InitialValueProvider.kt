package com.jack.android.gson.extension.value

import com.google.gson.internal.ConstructorConstructor
import com.google.gson.reflect.TypeToken

interface InitialValueProvider {
    fun <T> ConstructorConstructor.getInitialValue(typeToken: TypeToken<T>): T?
}