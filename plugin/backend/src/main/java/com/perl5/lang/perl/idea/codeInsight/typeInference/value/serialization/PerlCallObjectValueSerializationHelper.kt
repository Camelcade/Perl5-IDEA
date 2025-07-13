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

import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlCallObjectValue
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValueDeserializer
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValueSerializer

class PerlCallObjectValueSerializationHelper : PerlCallValueSerializationHelper<PerlCallObjectValue>() {
  override val serializationId: Int
    get() = PerlValueSerializationHelper.CALL_OBJECT_ID

  override fun serializeData(value: PerlCallObjectValue, serializer: PerlValueSerializer) {
    super.serializeData(value, serializer)
    serializer.writeName(value.superContext)
  }

  override fun deserialize(
    deserializer: PerlValueDeserializer,
    baseValue: PerlValue,
    parameter: PerlValue,
    arguments: List<PerlValue>
  ): PerlValue = PerlCallObjectValue.create(baseValue, parameter, arguments, deserializer.readNameString())
}