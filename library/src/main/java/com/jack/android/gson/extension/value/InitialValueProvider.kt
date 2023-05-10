package com.jack.android.gson.extension.value

interface InitialValueProvider {
    fun <T> getInitialValue(rawType: Class<T>): T?
}

inline fun<reified T> InitialValueProvider.getInitialValue() = getInitialValue(T::class.java)