## Readme

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![jCenter](https://img.shields.io/badge/Kotlin-1.6.21-green.svg
)](https://github.com/Foso/Sheasy/blob/master/LICENSE)

# Gson extensions

**Provides the function to safely using Gson to serialize or deserialize the class**


## Table of content

- [How to use](#how-to-use)
    - [Compile](#Compile)
    - [Initial the lib](#Initial-the-Gson-extension)
    - [Customized the lib](#Customize-the-Gson-extension)
    - [Serialize synchronously](#Serialize-synchronously)
    - [Serialize with Kotlin Coroutines](#Serialize-with-Kotlin-Coroutines)
    - [Deserialize with default value](#Deserialize-with-default-value)
- [Samples](#samples)
- [FAQ](#faq)

## How to use

#### Compile

<details>
<summary>Kotlin</summary>

```kotlin
repositories {
    mavenCentral()
}
dependencies {
    todo
}
```

</details>

#### Initial the Gson extension

<details>
<summary>Kotlin</summary>

```
initialGsonExtension() or do nothing.
```

</details>

#### Customize the Gson extension

<details>
<summary>Kotlin</summary>

Customize the Gson factory

```kotlin
val debugLogger = DebugLogger { error-> 
    Log.e("tag",error)
}
```

* Customize the debug logger and Gson

```kotlin
open class DefaultGsonFactory : GsonFactory {
  override fun createGson(): Gson {
    val constructorConstructor = ConstructorConstructor(
      Collections.emptyMap()
    )
    val initialValueProvider = createInitialValueProvider()
    return GsonBuilder()
      .setLenient()
      .enableComplexMapKeySerialization()
      .registerTypeAdapterFactory(
        ReflectiveTypeAdapterFactory(
          initialValueProvider,
          constructorConstructor
        )
      )
      .disableHtmlEscaping()
      .create()
  }

  override fun createLogger(): DebugLogger {
      return DefaultInternalDebugLogger()
  }
}
```

* If you only want to override either the `DebugLogger` or `Gson`

```
val androidLogger = DebugLogger{ e->
    // Log.e(TAG,e)
}
class MyOwnGsonFactory(val gsonFactory:GsonFactory = DefaultGsonFactory()): GsonFactory by gsonFactory{
    override fun createLogger(): DebugLogger = androidLogger 
}
```

</details>

#### Serialize synchronously

<details>
<summary>Kotlin</summary>

```kotlin
//1. For class
private class StringClass(val str: String?)
val jsonString = """
            {"str":"hello world"}
        """.trimIndent()
val result = fromJson(jsonString, StringClass::class.java)

//2. For Array
private class ArrayClass(val value: Array<Int>?)
val jsonString = """
            {"value":[1,2,3]}
        """.trimIndent()
val result = fromJson(jsonString, ArrayClass::class.java)

//3. For list
val jsonString = """
            [1,2,3,4]
        """.trimIndent()
val intList = toList(jsonString, Int::class.java)

//4. For map
val jsonString = """
            {"key1":1,"key2":2}
        """.trimIndent()
val map = toMap(jsonString, Int::class.java)
```

</details>

#### Serialize with Kotlin Coroutines

<details>
<summary>Kotlin</summary>

```kotlin
lifecycleScope.launch {
    val jsonElement: JsonElement? = asyncUseGson {
        // Scope in Gson instance. So you can do anything with the internal gson instance.
        toJsonTree("{\"value\":false}")
    }
    // use jsonElement
}

lifecycleScope.launch {
    val jsonString = """
        {"value":[1,2,3]}
    """.trimIndent()
    val result = asyncFromJson(jsonString, ArrayClass::class.java)
    // do something
}

lifecycleScope.launch {
    val jsonString = """
            {"key1":"value1","key2":"value2"}
        """.trimIndent()
    val map = asyncToMap(jsonString, String::class.java)
    // do something
}
```

</details>

#### Deserialize with default value

<details>
<summary>Kotlin</summary>

```kotlin
    private class DataClass(val name: String = "Jack", val id: Int = 2)

    @Test
    fun testDeserializeWithDefaultValue() {
        val jsonString = """
            {"name":null,id:null}
        """.trimIndent()
        val dataClass = fromJson(jsonString, DataClass::class.java)
        Assert.assertNotNull(dataClass)
        Assert.assertEquals("Jack", dataClass?.name)
        Assert.assertEquals(2, dataClass?.id)
    }

    @Test
    fun testDeserializeWithDefaultValueAndResponse() {
        val jsonString = """
            {"name":"Tom",id:null}
        """.trimIndent()
        val dataClass = fromJson(jsonString, DataClass::class.java)
        Assert.assertNotNull(dataClass)
        Assert.assertEquals("Tom", dataClass?.name)
        Assert.assertEquals(2, dataClass?.id)
    }

    private class DataClass2(val name: String, val id: Int)

    @Test
    fun testDeserializeWithoutDefaultValueAndResponse() {
        val jsonString = """
            {"name":null,id:null}
        """.trimIndent()
        val dataClass = fromJson(jsonString, DataClass2::class.java)
        Assert.assertNotNull(dataClass)
        Assert.assertEquals("", dataClass?.name)
        Assert.assertEquals(0, dataClass?.id)
    }
```

</details>

#### Samples

<details>

<summary>Kotlin</summary>

```kotlin
// Primitive types
private class BooleanClass(val number: Boolean?)
@Test
fun testNullableStringDeserialize() {
    val jsonString = """
            {"str":null}
        """.trimIndent()
    val stringClass = fromJson(jsonString, StringClass::class.java)
    Assert.assertNotNull(stringClass)
    Assert.assertEquals("", stringClass!!.str)
}
@Test
fun testNullableBooleanSerialize() {
    Assert.assertEquals("{\"number\":false}", toJson(BooleanClass(null)))
}
// String
private class StringClass(val str: String?)
@Test
fun testNullableStringDeserialize() {
    val jsonString = """
            {"str":null}
        """.trimIndent()
    val stringClass = fromJson(jsonString, StringClass::class.java)
    Assert.assertNotNull(stringClass)
    Assert.assertEquals("", stringClass!!.str)
}
@Test
fun testNullableStringSerialize() {
    Assert.assertEquals("{\"str\":\"\"}", toJson(StringClass(null)))
}
// Array
@Test
fun testNullableArrayDeserialize() {
    val jsonString = """
            {"value":[null,null,null]}
        """.trimIndent()
    val result = fromJson(jsonString, ArrayClass::class.java)
    Assert.assertNotNull(result)
    Assert.assertNotNull(result!!.value)
    Assert.assertEquals(arrayOf(null,null,null), result!!.value)
}
@Test
fun testNullableArraySerialize() {
    Assert.assertEquals("{}", toJson(ArrayClass(value = null)))
}
// List
@Test
fun testNullableIntListDeserialize() {
    val jsonString = """
            [null,null,null,null]
        """.trimIndent()
    val intList = toList(jsonString, Int::class.java)
    Assert.assertNotNull(intList)
    Assert.assertEquals(4, intList!!.size)
    Assert.assertEquals(listOf(null,null,null,null), intList)
}
@Test
fun testNullableIntListSerialize() {
    Assert.assertEquals("[]", toJson(listOfNotNull<Int>(null, null, null, null)))
}
// Map
@Test
fun testNullableIntMapDeserialize() {
    val jsonString = """
            {"key1":null,"key2":null}
        """.trimIndent()
    val map = toMap(jsonString, Int::class.java)
    Assert.assertNotNull(map)
    Assert.assertEquals(2, map!!.size)
    Assert.assertEquals(mapOf("key1" to 0, "key2" to 0), map)
}
@Test
fun testNullableValueMapSerialize() {
    Assert.assertEquals(
        "{}",
        toJson(mapOf("key1" to null, "key2" to null))
    )
}
```

</details>

## faq

* Why we need this lib to safely using Gson
Besides there are some traps about the Kotlin type-safe feature.


```kotlin
data class Foo(val name: String = "")
@Test
fun testWithoutNullableAdapter() {
    val jsonString = """
            {"name": null}
        """.trimIndent()
    val foo = Gson().fromJson(jsonString, Foo::class.java)
    Assert.assertThrows(AssertionError::class.java) {
        // The foo's name will be null
        Assert.assertNotNull(foo?.name)
    }
}
```

If the backend gives us a null value without checking and if we declare the field with the safe type. It will crash the app.
Probably they think it is your fault because your app crashed...

# License
```
Copyright 2022 JackChen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```