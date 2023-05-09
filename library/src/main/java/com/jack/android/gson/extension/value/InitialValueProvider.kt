package com.jack.android.gson.extension.value

interface InitialValueProvider {
    fun <T> getInitialValue(rawType: Class<T>): T?
}

inline fun InitialValueProvider.getBooleanInitialValue() = getInitialValue(Boolean::class.java)

inline fun InitialValueProvider.getByteInitialValue() = getInitialValue(Byte::class.java)

inline fun InitialValueProvider.getCharInitialValue() = getInitialValue(Char::class.java)

inline fun InitialValueProvider.getShortInitialValue() = getInitialValue(Short::class.java)

inline fun InitialValueProvider.getIntInitialValue() = getInitialValue(Int::class.java)

inline fun InitialValueProvider.getLongInitialValue() = getInitialValue(Long::class.java)

inline fun InitialValueProvider.getFloatInitialValue() = getInitialValue(Float::class.java)

inline fun InitialValueProvider.getDoubleInitialValue() = getInitialValue(Double::class.java)

inline fun InitialValueProvider.getStringInitialValue() = getInitialValue(String::class.java)

inline fun InitialValueProvider.getArrayInitialValue() = getInitialValue(Array::class.java)