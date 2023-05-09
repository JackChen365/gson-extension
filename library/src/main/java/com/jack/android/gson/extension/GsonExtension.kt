package com.jack.android.gson.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * This method use a closure to help you use the internal [Gson].
 * Such as [Gson.toJsonTree].
 * <p>
 *  val jsonTree: JsonElement? = useGson {
 *      toJsonTree(gson)
 *  }
 * </p>
 */
fun <T> useGson(block: Gson.() -> T): T? {
    val result = runCatching {
        block(internalGson())
    }
    result.outputDebugLogIfNecessary()
    return result.getOrNull()
}

/**
 * Convert the specific class instance to json string.
 */
fun <T> toJson(item: T): String {
    val result = runCatching {
        internalGson().toJson(item)
    }
    result.outputDebugLogIfNecessary()
    return result.getOrNull() ?: String()
}

/**
 * Convert the json string to a specific class instance by using the given class.
 * Tips:
 * We only use this method to convert the string to a single class, not [List] or [Map]
 */
fun <T> fromJson(json: String, classOfT: Class<T>): T? {
    val result = runCatching {
        internalGson().fromJson(json, classOfT)
    }
    result.outputDebugLogIfNecessary()
    return result.getOrNull()
}

/**
 * Convert the json string to a specific class instance by using the given type.
 * Tips:
 * We only use this method to convert the string to a single class, not [List] or [Map]
 */
fun <T> fromJson(json: String, typeOfT: Type): T? {
    val result = runCatching {
        internalGson().fromJson<T>(json, typeOfT)
    }
    result.outputDebugLogIfNecessary()
    return result.getOrNull()
}

/**
 * Convert the json string to a specific class instance by using the given typeToken.
 * Tips:
 * We only use this method to convert the string to a single class, not [List] or [Map]
 */
fun <T> fromJson(json: String, token: TypeToken<T>): T? {
    return fromJson(json, token.type)
}

/**
 * Convert the JSON string to a specific class instance by using the given class.
 * The result will deliver to a Kotlin closure, you can safely use it there
 */
fun <T> fromJson(json: String, classOfT: Class<T>, block: (T) -> Unit) {
    runCatching {
        internalGson().fromJson(json, classOfT)
    }.fold(
        onSuccess = { item ->
            block(item)
        },
        onFailure = { error ->
            internalDebugger().e(error)
        }
    )
}

/**
 * Convert the json string to a [List]
 */
fun <T> toList(json: String, clazzOfT: Class<T>): List<T> {
    val type = kotlinTypeToJavaType(clazzOfT)
    return fromJson<List<T>>(json, ListTypeWrapper(type)) ?: emptyList()
}

/**
 * Convert the json string to a [List]
 * The result will deliver to a Kotlin closure, you can safely use it there
 */
fun <T> toList(json: String, clazzOfT: Class<T>, block: (List<T>) -> Unit) {
    val type = kotlinTypeToJavaType(clazzOfT)
    val result = fromJson<List<T>>(json, ListTypeWrapper(type))
    if (null != result) {
        block(result)
    }
}

/**
 * Kotlin primitive type be java primitive type if possible.
 * This will cause parse fails.
 * <p>
 *     $Gson$Types.java
 *     456: static void checkNotPrimitive(Type type) {
 *     457:  checkArgument(!(type instanceof Class<?>) || !((Class<?>) type).isPrimitive());
 *     458: }
 * </p>
 * This method convert the Kotlin primitive type to Java Primitive object type (not primitive type)
 */
fun <T> kotlinTypeToJavaType(clazzOfT: Class<T>): Class<T> {
    return when (clazzOfT) {
        Int::class.java -> java.lang.Integer::class.java
        Long::class.java -> java.lang.Long::class.java
        Float::class.java -> java.lang.Float::class.java
        Double::class.java -> java.lang.Double::class.java
        Boolean::class.java -> java.lang.Boolean::class.java
        else -> clazzOfT
    } as Class<T>
}

/**
 * Convert the json string to a [Map]
 */
private fun <K, V> toMap(json: String, keyClazz: Class<K>, valueClazz: Class<V>): Map<K, V> {
    return fromJson<Map<K, V>>(
        json, TypeToken.getParameterized(MutableMap::class.java, keyClazz, valueClazz).type
    ) ?: emptyMap()
}

/**
 * Convert the json string to a [Map]
 * The result will deliver to a Kotlin closure, you can safely use it there
 */
fun <V> toMap(json: String, valueClazz: Class<V>, block: (Map<String, V>) -> Unit) {
    val type = kotlinTypeToJavaType(valueClazz)
    val result = fromJson<Map<String, V>>(
        json, TypeToken.getParameterized(
            MutableMap::class.java, String::class.java, type
        ).type
    )
    if (null != result) {
        block(result)
    }
}

fun <V> toMap(json: String, valueClazz: Class<V>): Map<String, V> {
    val type = kotlinTypeToJavaType(valueClazz)
    return fromJson<Map<String, V>>(
        json, TypeToken.getParameterized(
            MutableMap::class.java, String::class.java, type
        ).type
    ) ?: emptyMap()
}

/**
 * UseGson instance to do any thing by using a Kotlin coroutines IO context.
 * for example:
 * <p>
 *  lifecycleScope.launch{
 *      val result = asyncUseGson {
 *          toJsonTree(jsonString)
 *      }
 *      // Use the result in main-thread
 *  }
 * </p>
 */
suspend fun <T> asyncUseGson(block: Gson.() -> T): T? = withContext(Dispatchers.IO) {
    useGson(block)
}

/**
 * Serializer the given item to a JSON string using a Kotlin coroutines IO context.
 * for example:
 * <p>
 *  lifecycleScope.launch{
 *      class TestClass(val value:String)
 *      val result = asyncToJson(TestClass("Hello world"))
 *      // Use the result in main-thread
 *  }
 * </p>
 */
suspend fun <T> asyncToJson(item: T): String = withContext(Dispatchers.IO) {
    toJson(item)
}

/**
 * Deserializer the given JSON string to a specific class using a Kotlin coroutines IO context.
 * for example:
 * <p>
 *  lifecycleScope.launch{
 *      class TestClass(val value:String)
 *      val result = asyncToJson("{"value":"Hello world"}",TestClass::class.java)
 *      // Use the result in main-thread
 *  }
 * </p>
 */
suspend fun <T> asyncFromJson(json: String, classOfT: Class<T>): T? = withContext(Dispatchers.IO) {
    fromJson(json, classOfT)
}

/**
 * Deserializer the given JSON string to a specific class using a Kotlin coroutines IO context.
 * for example:
 * <p>
 *  lifecycleScope.launch{
 *      class TestClass(val value:String)
 *      val result = asyncToJson("{"value":"Hello world"}",TestClass::class.java)
 *      // Use the result in main-thread
 *  }
 * </p>
 */
suspend fun <T> asyncFromJson(json: String, typeOfT: Type): T? = withContext(Dispatchers.IO) {
    fromJson(json, typeOfT)
}

suspend fun <T> asyncFromJson(json: String, token: TypeToken<T>): T? = withContext(Dispatchers.IO) {
    fromJson(json, token)
}

/**
 * Deserializer the given JSON string to a list using a Kotlin coroutines IO context.
 * for example:
 * <p>
 *  lifecycleScope.launch{
 *      val list:List<Int> = asyncToJson("[1,2,3,4]",Int::class.java)
 *      // Use the list in main-thread
 *  }
 * </p>
 * If it parse failed. we will return an empty list.
 */
suspend fun <T> asyncToList(json: String, clazzOfT: Class<T>): List<T> =
    withContext(Dispatchers.IO) {
        toList(json, clazzOfT) ?: emptyList()
    }

/**
 * Deserializer the given JSON string to a map using a Kotlin coroutines IO context.
 * for example:
 * <p>
 *  lifecycleScope.launch{
 *      class TestClass(val value:String)
 *      val map:Map<String,String> = asyncToMap("{"key1":"value1","key2":"value2"}",String::class.java)
 *      // Use the map here.
 *  }
 * </p>
 * If it parse failed. we will return an empty list.
 */
suspend fun <V> asyncToMap(json: String, valueClazz: Class<V>): Map<String, V> =
    withContext(Dispatchers.IO) {
        toMap(json, valueClazz) ?: emptyMap()
    }

private fun <T> Result<T>.outputDebugLogIfNecessary() {
    if (isFailure) {
        val throwable = exceptionOrNull()
        internalDebugger().e(throwable)
    }
}

/**
 * The List generic type wrapper.
 * We use this [ParameterizedType] to support us deserializer the JSON string to List<T>
 */
private class ListTypeWrapper<T>(private val wrapped: Class<T>) : ParameterizedType {
    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf(wrapped)
    }

    override fun getRawType(): Type {
        return MutableList::class.java
    }

    override fun getOwnerType(): Type? {
        return null
    }
}