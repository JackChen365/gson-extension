package com.jack.android.gson.extension.value

import com.google.gson.internal.Primitives

class DefaultInitialValueProvider : InitialValueProvider {

    override fun <T> getInitialValue(rawType: Class<T>): T? {
        val isPrimitive = Primitives.isPrimitive(rawType) || Primitives.isWrapperType(rawType)
        return if (isPrimitive) {
            getInitialValueFromPrimitiveTypes(rawType) as T
        } else {
            getInitialValueFromTypes(rawType) as T?
        }
    }

    private fun <T> getInitialValueFromPrimitiveTypes(rawType: Class<T>): Any {
        return when (rawType) {
            Boolean::class.javaObjectType, Boolean::class.java -> false
            Character::class.javaObjectType, Char::class.java -> ' '
            Byte::class.javaObjectType, Byte::class.java -> 0
            Short::class.javaObjectType, Short::class.java -> 0
            Integer::class.javaObjectType, Int::class.java -> 0
            Long::class.javaObjectType, Long::class.java -> 0L
            Float::class.javaObjectType, Float::class.java -> 0f
            Double::class.javaObjectType, Double::class.java -> 0.0
            else -> error("This is not a primitive type:$rawType")
        }
    }

    private fun <T> getInitialValueFromTypes(rawType: Class<T>): Any? {
        if (null != rawType.componentType) {
            return when (rawType.componentType) {
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
        return when (rawType) {
            Array::class.java,
            Array::class.javaObjectType -> emptyArray<Any>()
            List::class.java -> emptyList<Any>()
            Map::class.java -> emptyMap<String, Any?>()
            String::class.java -> ""
            else -> null
        }
    }

}