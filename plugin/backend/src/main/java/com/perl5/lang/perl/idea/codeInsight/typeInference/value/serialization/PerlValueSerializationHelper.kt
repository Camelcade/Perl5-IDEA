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
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlDuckValue
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValueSerializer
import java.io.IOException


private val EP = ClassExtension<PerlValueSerializationHelper<*>>("com.perl5.valueSerializationHelper")
var id = 0

interface PerlValueSerializationHelper<Val : PerlValue> {

  /**
   * @return a serialization id unique for this value.
   * @see PerlValuesManager
   */
  val serializationId: Int

  @Throws(IOException::class)
  fun serializeData(value: Val, serializer: PerlValueSerializer) = Unit

  companion object {
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    operator fun <Val : PerlValue> get(value: Val): PerlValueSerializationHelper<Val> =
      EP.findSingle(value.javaClass) as? PerlValueSerializationHelper<Val> ?: throw RuntimeException("No seraizliationhelper for $value")

    @JvmField
    val DUPLICATE_ID = id++

    // special values
    @JvmField
    val UNKNOWN_ID = id++

    @JvmField
    val UNDEF_ID = id++

    @JvmField
    val ARGUMENTS_ID = id++

    // primitives
    @JvmField
    val SCALAR_ID = id++

    @JvmField
    val SCALAR_DEREFERENCE_ID = id++

    @JvmField
    val SCALAR_CONTEXT_ID = id++

    @JvmField
    val ARRAY_ID = id++

    @JvmField
    val ARRAY_ELEMENT_ID = id++

    @JvmField
    val ARRAY_SLICE_ID = id++

    @JvmField
    val ARRAY_DEREFERENCE_ID = id++

    @JvmField
    val UNSHIFT_ID = id++

    @JvmField
    val PUSH_ID = id++

    @JvmField
    val SUBLIST_ID = id++

    @JvmField
    val HASH_ID = id++

    @JvmField
    val HASH_ELEMENT_VALUE = id++

    @JvmField
    val DEFERRED_HASH_ID = id++

    @JvmField
    val HASH_SLICE_ID = id++

    @JvmField
    val HASH_DEREFERENCE_ID = id++

    @JvmField
    val ARITHMETIC_NEGATION = id++

    @JvmField
    val REFERENCE_ID = id++

    @JvmField
    val BLESSED_ID = id++

    // synthetic values
    @JvmField
    val CALL_STATIC_ID = id++

    @JvmField
    val CALL_OBJECT_ID = id++

    @JvmField
    val ONE_OF_ID = id++

    @JvmField
    val DEFAULT_ARGUMENT_ID = id++

    @JvmField
    val SMART_GETTER_ID = id++

    @JvmField
    val DUCK_TYPE_ID = id++

    @JvmField
    val VALUE_WITH_FALLBACK = id++

    @JvmStatic
    fun getVersion(): Int = id + (if (PerlDuckValue.isDuckTypingEnabled()) 100 else 0)
  }
}