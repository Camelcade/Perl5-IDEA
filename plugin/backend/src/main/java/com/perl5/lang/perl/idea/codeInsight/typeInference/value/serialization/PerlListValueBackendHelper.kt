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

import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlListValue
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValueDeserializer
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValueSerializer

abstract class PerlListValueBackendHelper<Val : PerlListValue> : PerlValueBackendHelper<Val> {
  override fun serializeData(value: Val, serializer: PerlValueSerializer): Unit = serializer.writeValuesList(value.elements)

  final override fun deserialize(deserializer: PerlValueDeserializer): PerlValue = deserialize(deserializer, deserializer.readValuesList())

  abstract fun deserialize(deserializer: PerlValueDeserializer, valuesList: List<PerlValue>): PerlValue
}