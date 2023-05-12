package com.jack.android.gson.extension.value

import com.google.gson.internal.ConstructorConstructor
import com.google.gson.reflect.TypeToken

class DefaultInitialValueProvider : InitialValueProvider {

    override fun <T> ConstructorConstructor.getInitialValue(typeToken: TypeToken<T>): T? {
        val componentType = typeToken.rawType.componentType
        return if (null != componentType) {
            getInitialValueFromComponentType(componentType) as T
        } else {
            val constructor = get(typeToken)
            constructor.construct()
        }
    }

    private fun <T> getInitialValueFromComponentType(componentType: Class<T>): Any {
        return when (componentType) {
            Int::class.javaObjectType, Int::class.javaPrimitiveType -> emptyArray<Int>()
            Boolean::class.javaObjectType, Boolean::class.javaPrimitiveType -> emptyArray<Boolean>()
            Char::class.javaObjectType, Char::class.javaPrimitiveType -> emptyArray<Char>()
            Byte::class.javaObjectType, Byte::class.javaPrimitiveType -> emptyArray<Byte>()
            Short::class.javaObjectType, Short::class.javaPrimitiveType -> emptyArray<Short>()
            Long::class.javaObjectType, Long::class.javaPrimitiveType -> emptyArray<Long>()
            Float::class.javaObjectType, Float::class.javaPrimitiveType -> emptyArray<Float>()
            Double::class.javaObjectType, Double::class.javaPrimitiveType -> emptyArray<Double>()
            else -> emptyArray<Any>()
        }
    }

}