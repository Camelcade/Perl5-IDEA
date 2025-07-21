/*
 * Copyright 2015-2025 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.idea.codeInsight.typeInference.value.serialization

import com.intellij.openapi.util.ClassExtension
import com.jetbrains.rd.util.concurrentMapOf
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValueDeserializer
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValueSerializer
import java.io.IOException


private val EP = ClassExtension<PerlValueBackendHelper<*>>("com.perl5.valueBackendHelper")
private var id = 0
private val idMap = concurrentMapOf<Int, PerlValueBackendHelper<*>>()
private val instanceCache = concurrentMapOf<Class<*>, PerlValueBackendHelper<*>>()

interface PerlValueBackendHelper<Val : PerlValue> {

  /**
   * @return a serialization id unique for this value.
   */
  val serializationId: Int

  @Throws(IOException::class)
  fun serializeData(value: Val, serializer: PerlValueSerializer): Unit = Unit

  fun deserialize(deserializer: PerlValueDeserializer): PerlValue =
    throw UnsupportedOperationException("This method is not implemented for $this")

  companion object {

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    operator fun <Val : PerlValue> get(value: Val): PerlValueBackendHelper<Val> =
      EP.findSingle(value.javaClass) as? PerlValueBackendHelper<Val> ?: throw RuntimeException("No serialization helper for $value")

    fun instance(clazz: Class<*>): PerlValueBackendHelper<*> = instanceCache.computeIfAbsent(clazz) {
      EP.point!!.extensionList.map { it.instance }.find { it.serializationId == id }
        ?: throw RuntimeException("No serialization helper for id $id")
    }

    inline fun <reified T : PerlValueBackendHelper<*>> instance(): T = instance(T::class.java) as T

    @JvmStatic
    operator fun get(id: Int): PerlValueBackendHelper<*> = idMap.computeIfAbsent(id) {
      EP.point!!.extensionList.map { it.instance }.find { it.serializationId == id }
        ?: throw RuntimeException("No serialization helper for id $id")
    }

    @JvmField
    val DUPLICATE_ID: Int = id++

    // special values
    @JvmField
    val UNKNOWN_ID: Int = id++

    @JvmField
    val UNDEF_ID: Int = id++

    @JvmField
    val ARGUMENTS_ID: Int = id++

    // primitives
    @JvmField
    val SCALAR_ID: Int = id++

    @JvmField
    val SCALAR_DEREFERENCE_ID: Int = id++

    @JvmField
    val SCALAR_CONTEXT_ID: Int = id++

    @JvmField
    val ARRAY_ID: Int = id++

    @JvmField
    val ARRAY_ELEMENT_ID: Int = id++

    @JvmField
    val ARRAY_SLICE_ID: Int = id++

    @JvmField
    val ARRAY_DEREFERENCE_ID: Int = id++

    @JvmField
    val UNSHIFT_ID: Int = id++

    @JvmField
    val PUSH_ID: Int = id++

    @JvmField
    val SUBLIST_ID: Int = id++

    @JvmField
    val HASH_ID: Int = id++

    @JvmField
    val HASH_ELEMENT_VALUE: Int = id++

    @JvmField
    val DEFERRED_HASH_ID: Int = id++

    @JvmField
    val HASH_SLICE_ID: Int = id++

    @JvmField
    val HASH_DEREFERENCE_ID: Int = id++

    @JvmField
    val ARITHMETIC_NEGATION: Int = id++

    @JvmField
    val REFERENCE_ID: Int = id++

    @JvmField
    val BLESSED_ID: Int = id++

    // synthetic values
    @JvmField
    val CALL_STATIC_ID: Int = id++

    @JvmField
    val CALL_OBJECT_ID: Int = id++

    @JvmField
    val ONE_OF_ID: Int = id++

    @JvmField
    val DEFAULT_ARGUMENT_ID: Int = id++

    @JvmField
    val SMART_GETTER_ID: Int = id++

    @JvmField
    val DUCK_TYPE_ID: Int = id++

    @JvmField
    val VALUE_WITH_FALLBACK: Int = id++

    @JvmStatic
    fun getVersion(): Int = id + (if (PerlValue.isDuckTypingEnabled()) 100 else 0)
  }
}