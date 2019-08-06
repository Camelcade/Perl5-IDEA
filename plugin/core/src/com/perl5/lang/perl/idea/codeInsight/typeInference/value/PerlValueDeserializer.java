/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.psi.stubs.StubInputStream;
import gnu.trove.TIntObjectHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.*;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.*;

public class PerlValueDeserializer {

  @NotNull
  private final StubInputStream myInputStream;
  private final TIntObjectHashMap<PerlValue> myDryMap = new TIntObjectHashMap<>();

  public PerlValueDeserializer(@NotNull StubInputStream inputStream) {
    myInputStream = inputStream;
  }

  @NotNull
  PerlValue readValue() throws IOException {
    final int valueId = readVarInt();
    if (valueId == DUPLICATE_ID) {
      int duplicateId = readVarInt();
      PerlValue duplicate = myDryMap.get(duplicateId);
      if (duplicate == null) {
        throw new IOException("Missing duplicate with id: " + duplicateId);
      }
      return duplicate;
    }
    // Special values
    if (valueId == UNKNOWN_ID) {
      return UNKNOWN_VALUE;
    }
    else if (valueId == UNDEF_ID) {
      return UNDEF_VALUE;
    }
    else if (valueId == ARGUMENTS_ID) {
      return ARGUMENTS_VALUE;
    }

    PerlValue value = readValue(valueId);
    myDryMap.put(myDryMap.size() + 1, value);
    return PerlValuesManager.intern(value);
  }

  @NotNull
  private PerlValue readValue(int valueId) throws IOException {
    if (valueId == SCALAR_ID) {
      return new PerlScalarValue(this);
    }
    else if (valueId == SCALAR_CONTEXT_ID) {
      return new PerlScalarContextValue(this);
    }
    else if (valueId == ARRAY_ID) {
      return new PerlArrayValue(this);
    }
    else if (valueId == HASH_ID) {
      return new PerlHashValue(this);
    }
    else if (valueId == DEFERRED_HASH_ID) {
      return new PerlDeferredHashValue(this);
    }
    else if (valueId == REFERENCE_ID) {
      return new PerlReferenceValue(this);
    }
    else if (valueId == BLESSED_ID) {
      return new PerlBlessedValue(this);
    }
    else if (valueId == ONE_OF_ID) {
      return new PerlOneOfValue(this);
    }
    else if (valueId == CALL_OBJECT_ID) {
      return new PerlCallObjectValue(this);
    }
    else if (valueId == CALL_STATIC_ID) {
      return new PerlCallStaticValue(this);
    }
    else if (valueId == ARRAY_ELEMENT_ID) {
      return new PerlArrayElementValue(this);
    }
    else if (valueId == HASH_ELEMENT_VALUE) {
      return new PerlHashElementValue(this);
    }
    else if (valueId == ARITHMETIC_NEGATION) {
      return new PerlArithmeticNegationValue(this);
    }
    else if (valueId == ARRAY_SLICE_ID) {
      return new PerlArraySliceValue(this);
    }
    else if (valueId == HASH_SLICE_ID) {
      return new PerlHashSliceValue(this);
    }
    else if (valueId == SCALAR_DEREFERENCE_ID) {
      return new PerlScalarDereferenceValue(this);
    }
    else if (valueId == UNSHIFT_ID) {
      return new PerlUnshiftValue(this);
    }
    else if (valueId == PUSH_ID) {
      return new PerlPushValue(this);
    }
    else if (valueId == ARRAY_DEREFERENCE_ID) {
      return new PerlArrayDereferenceValue(this);
    }
    else if (valueId == HASH_DEREFERENCE_ID) {
      return new PerlHashDereferenceValue(this);
    }
    else if (valueId == SUBLIST_ID) {
      return new PerlSublistValue(this);
    }
    else if( valueId == SMART_GETTER_ID){
      return new PerlSmartGetterValue(this);
    }
    else if (valueId == DEFAULT_ARGUMENT_ID) {
      return new PerlDefaultArgumentValue(this);
    }
    throw new IOException("Don't know how to deserialize a value: " + valueId);
  }

  @NotNull
  List<PerlValue> readValuesList() throws IOException {
    int size = readVarInt();
    if (size == 0) {
      return Collections.emptyList();
    }
    List<PerlValue> elements = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      elements.add(readValue());
    }
    return Collections.unmodifiableList(elements);
  }

  @NotNull
  Set<PerlValue> readValuesSet() throws IOException {
    int size = readVarInt();
    if (size == 0) {
      return Collections.emptySet();
    }
    Set<PerlValue> elements = new HashSet<>(size);
    for (int i = 0; i < size; i++) {
      elements.add(readValue());
    }
    return Collections.unmodifiableSet(elements);
  }

  @Nullable
  public String readNameString() throws IOException {
    return myInputStream.readNameString();
  }

  public int readVarInt() throws IOException {
    return myInputStream.readVarInt();
  }

  public boolean readBoolean() throws IOException {
    return myInputStream.readBoolean();
  }
}
