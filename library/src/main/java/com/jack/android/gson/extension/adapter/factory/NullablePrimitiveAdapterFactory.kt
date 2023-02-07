package com.jack.android.gson.extension.adapter.factory

import com.google.gson.TypeAdapterFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.TypeAdapter
import com.jack.android.gson.extension.adapter.type.NullableBooleanTypeAdapter
import com.jack.android.gson.extension.adapter.type.NullableFloatTypeAdapter
import com.jack.android.gson.extension.adapter.type.NullableIntegerTypeAdapter
import com.jack.android.gson.extension.adapter.type.NullableDoubleTypeAdapter
import com.jack.android.gson.extension.adapter.type.NullableLongTypeAdapter

/**
 * The nullable primitive [TypeAdapterFactory] helps us handler all the primitive types.
 * Since the Json only support [Boolean], [Double] and [Int], We usually don't need to mapping all the primitive types.
 * For deserialize:
 * <p>
 *     TestClass(val value1:Float, val value2: Double)
 *     {"float":"0.0","double","0.0"} will be parse to TestClass(0f,0.0)
 *     TestClass(val value1:Int, val value2: Long)
 *     {"int":0,"long",0} will be parse to TestClass(0,0L)
 * </p>
 * For serialize
 * <p>
 *     TestClass(val value1:Float, val value2: Double)
 *     TestClass(0f,0.0) -> {"float":"0.0","double","0.0"}
 *     TestClass(val value1:Int, val value2: Long)
 *     TestClass(0,0L) -> {"int":0,"long",0}
 * </p>
 */
class NullablePrimitiveAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        return when (type.rawType) {
            Float::class.java,
            Float::class.javaPrimitiveType,
            java.lang.Float::class.java -> {
                NullableFloatTypeAdapter() as? TypeAdapter<T>
            }
            Int::class.java,
            Int::class.javaPrimitiveType,
            Integer::class.java -> {
                NullableIntegerTypeAdapter() as? TypeAdapter<T>
            }
            Double::class.java,
            Double::class.javaPrimitiveType,
            java.lang.Double::class.java -> {
                NullableDoubleTypeAdapter() as? TypeAdapter<T>
            }
            Long::class.java,
            Long::class.javaPrimitiveType,
            java.lang.Long::class.java -> {
                NullableLongTypeAdapter() as? TypeAdapter<T>
            }
            Boolean::class.java,
            Boolean::class.javaPrimitiveType,
            java.lang.Boolean::class.java -> {
                NullableBooleanTypeAdapter() as? TypeAdapter<T>
            }
            else -> null
        }
    }
}