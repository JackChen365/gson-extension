package com.jack.android.gson.extension.adapter

import com.google.gson.FieldNamingPolicy
import com.google.gson.FieldNamingStrategy
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.internal.`$Gson$Types`
import com.google.gson.internal.ConstructorConstructor
import com.google.gson.internal.Excluder
import com.google.gson.internal.ObjectConstructor
import com.google.gson.internal.Primitives
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory
import com.google.gson.internal.reflect.ReflectionAccessor
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.jack.android.gson.extension.value.InitialValueProvider
import java.io.IOException
import java.lang.reflect.Field
import java.lang.reflect.Type

/**
 * Type adapter that reflects over the fields and methods of a class.
 */
class ReflectiveTypeAdapterFactory @JvmOverloads constructor(
    private val initialValueProvider: InitialValueProvider,
    private val constructorConstructor: ConstructorConstructor,
    private val fieldNamingPolicy: FieldNamingStrategy = FieldNamingPolicy.IDENTITY,
    private val excluder: Excluder = Excluder.DEFAULT,
    private val jsonAdapterFactory: JsonAdapterAnnotationTypeAdapterFactory =
        JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
) : TypeAdapterFactory {
    companion object {
        fun excludeField(f: Field, serialize: Boolean, excluder: Excluder): Boolean {
            return !excluder.excludeClass(f.type, serialize) && !excluder.excludeField(f, serialize)
        }
    }

    private val internalGson = Gson()
    private val accessor = ReflectionAccessor.getInstance()
    fun excludeField(f: Field, serialize: Boolean): Boolean {
        return excludeField(f, serialize, excluder)
    }

    /**
     * first element holds the default name
     */
    private fun getFieldNames(f: Field): List<String> {
        val annotation = f.getAnnotation(SerializedName::class.java)
        if (annotation == null) {
            val name = fieldNamingPolicy.translateName(f)
            return listOf(name)
        }
        val serializedName: String = annotation.value
        val alternates: Array<String> = annotation.alternate
        if (alternates.isEmpty()) {
            return listOf(serializedName)
        }
        val fieldNames: MutableList<String> = ArrayList(alternates.size + 1)
        fieldNames.add(serializedName)
        for (alternate in alternates) {
            fieldNames.add(alternate)
        }
        return fieldNames
    }

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val raw = type.rawType
        val typeAdapter = internalGson.getAdapter(type)
        if (typeAdapter !is ReflectiveTypeAdapterFactory.Adapter<*>) {
            // It means this is a object and all the default factory can not handle it.
            return null
        }
        val constructor = constructorConstructor.get(type)
        return Adapter(constructor, getBoundFields(gson, type, raw))
    }

    private fun createBoundField(
        context: Gson, field: Field, name: String,
        fieldType: TypeToken<*>, serialize: Boolean, deserialize: Boolean
    ): BoundField {
        val isPrimitive = Primitives.isPrimitive(fieldType.rawType)
        // special casing primitives here saves ~5% on Android...
        val annotation = field.getAnnotation(JsonAdapter::class.java)
        var mapped: TypeAdapter<*>? = null
        if (annotation != null) {
            mapped = jsonAdapterFactory.getTypeAdapter(
                constructorConstructor, context, fieldType, annotation
            )
        }
        val jsonAdapterPresent = mapped != null
        val typeAdapter: TypeAdapter<*> = mapped ?: context.getAdapter(fieldType)
        return object : BoundField(name, serialize, deserialize) {
            @Throws(IOException::class, IllegalAccessException::class)
            override fun write(writer: JsonWriter?, value: Any?) {
                var fieldValue = field[value]
                val t: TypeAdapter<Any?> = if (jsonAdapterPresent) {
                    typeAdapter as TypeAdapter<Any?>
                } else {
                    RuntimeAdapterTypeWrapper(
                        context,
                        typeAdapter as TypeAdapter<Any?>,
                        fieldType.type
                    )
                }
                if (null == fieldValue) {
                    // if the field value is null, we initial it with the initialValueProvider
                    val initialValue = initialValueProvider.getInitialValue(fieldType.rawType)
                    if (null != initialValue) {
                        fieldValue = initialValue
                    }
                }
                t?.write(writer, fieldValue)
            }

            @Throws(IOException::class, IllegalAccessException::class)
            override fun read(reader: JsonReader?, value: Any?) {
                val fieldValue = typeAdapter?.read(reader)
                if (fieldValue != null || !isPrimitive) {
                    if (null != fieldValue) {
                        field[value] = fieldValue
                        return
                    }
                }
                if (null == fieldValue) {
                    // the json is null, but the object has its default value.
                    if (null == field[value]) {
                        // the class didn't has a default value. use the value from initialValueProvider
                        val initialValue = initialValueProvider.getInitialValue(fieldType.rawType)
                        if (null != initialValue) {
                            field[value] = initialValue
                        }
                    }
                }
            }

            @Throws(IOException::class, IllegalAccessException::class)
            override fun writeField(value: Any): Boolean {
                if (!serialized) return false
                val fieldValue = field[value]
                // avoid recursion for example for Throwable.cause
                return fieldValue !== value
            }
        }
    }

    private fun getBoundFields(context: Gson, type: TypeToken<*>, raw: Class<*>): Map<String?, BoundField> {
        var type = type
        var raw = raw
        val result: MutableMap<String?, BoundField> = LinkedHashMap()
        if (raw.isInterface) {
            return result
        }
        val declaredType = type.type
        while (raw != Any::class.java) {
            val fields = raw.declaredFields
            for (field in fields) {
                var serialize = excludeField(field, true)
                val deserialize = excludeField(field, false)
                if (!serialize && !deserialize) {
                    continue
                }
                accessor.makeAccessible(field)
                val fieldType: Type = `$Gson$Types`.resolve(type.type, raw, field.genericType)
                val fieldNames = getFieldNames(field)
                var previous: BoundField? = null
                var i = 0
                val size = fieldNames.size
                while (i < size) {
                    val name = fieldNames[i]
                    if (i != 0) serialize = false // only serialize the default name
                    val boundField = createBoundField(
                        context, field, name,
                        TypeToken.get(fieldType), serialize, deserialize
                    )
                    val replaced = result.put(name, boundField)
                    if (previous == null) previous = replaced
                    ++i
                }
                require(previous == null) {
                    declaredType
                        .toString() + " declares multiple JSON fields named " + previous?.name
                }
            }
            type = TypeToken.get(`$Gson$Types`.resolve(type.type, raw, raw.genericSuperclass))
            raw = type.rawType
        }
        return result
    }

    internal abstract class BoundField protected constructor(
        val name: String,
        val serialized: Boolean,
        val deserialized: Boolean
    ) {
        @Throws(IOException::class, IllegalAccessException::class)
        abstract fun writeField(value: Any): Boolean

        @Throws(IOException::class, IllegalAccessException::class)
        abstract fun write(writer: JsonWriter?, value: Any?)

        @Throws(IOException::class, IllegalAccessException::class)
        abstract fun read(reader: JsonReader?, value: Any?)
    }

    class Adapter<T> internal constructor(
        private val constructor: ObjectConstructor<T>,
        private val boundFields: Map<String?, BoundField>
    ) : TypeAdapter<T>() {
        @Throws(IOException::class)
        override fun read(reader: JsonReader): T? {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return null
            }
            val instance = constructor.construct()
            try {
                reader.beginObject()
                while (reader.hasNext()) {
                    val name = reader.nextName()
                    val field = boundFields[name]
                    if (field == null || !field.deserialized) {
                        reader.skipValue()
                    } else {
                        field.read(reader, instance)
                    }
                }
            } catch (e: IllegalStateException) {
                throw JsonSyntaxException(e)
            } catch (e: IllegalAccessException) {
                throw AssertionError(e)
            }
            reader.endObject()
            return instance
        }

        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: T?) {
            if (value == null) {
                out.nullValue()
                return
            }
            out.beginObject()
            try {
                for (boundField in boundFields.values) {
                    if (boundField.writeField(value)) {
                        out.name(boundField.name)
                        boundField.write(out, value)
                    }
                }
            } catch (e: IllegalAccessException) {
                throw AssertionError(e)
            }
            out.endObject()
        }
    }
}