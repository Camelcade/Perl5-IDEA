/*
 * Copyright 2015-2018 Alexandr Evstigneev
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
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.containers.WeakInterner;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlArgumentsValue.ARGUMENTS_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUndefValue.UNDEF_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUnknownValue.UNKNOWN_VALUE;

/**
 * Manages {@link PerlValue} serialization and deserialization, manages serialization ID
 * We could implement something like PerlValueElementType but this thing is not supported to be extendable, so good for now
 */
public final class PerlValuesManager {
  private static int id = 0;
  // special values
  static final int UNKNOWN_ID = id++;
  static final int UNDEF_ID = id++;
  static final int ARGUMENTS_ID = id++;
  // primitives
  static final int SCALAR_ID = id++;
  static final int SCALAR_CAST_ID = id++;
  static final int SCALAR_CONTEXT_ID = id++;
  static final int STRINGIFY_ID = id++;
  static final int NUMIFY_ID = id++;

  static final int ARRAY_ID = id++;
  static final int ARRAY_ELEMENT_ID = id++;
  static final int ARRAY_SLICE_ID = id++;
  static final int ARRAY_CAST_ID = id++;

  static final int HASH_ID = id++;
  static final int HASH_ELEMENT_VALUE = id++;
  static final int DEFERRED_HASH_ID = id++;
  static final int HASH_SLICE_ID = id++;
  static final int HASH_KEYS_ID = id++;
  static final int HASH_VALUES_ID = id++;
  static final int HASH_CAST_ID = id++;

  static final int REFERENCE_ID = id++;
  static final int BLESSED_ID = id++;

  // synthetic values
  static final int CALL_STATIC_ID = id++;
  static final int CALL_OBJECT_ID = id++;
  static final int ONE_OF_ID = id++;
  static final int CONDITION_ID = id++;

  // MUST stay here. Automatically changes on new element creation
  public static final int VERSION = id;

  private static final WeakInterner<PerlValue> INTERNER = new WeakInterner<>();

  public static PerlValue readValue(@NotNull StubInputStream dataStream) throws IOException {
    return intern(deserialize(dataStream));
  }

  public static PerlValue intern(PerlValue value) {
    return INTERNER.intern(value);
  }

  @NotNull
  private static PerlValue deserialize(@NotNull StubInputStream dataStream) throws IOException {
    int valueId = dataStream.readVarInt();
    if (valueId == UNKNOWN_ID) {
      return UNKNOWN_VALUE;
    }
    else if (valueId == UNDEF_ID) {
      return UNDEF_VALUE;
    }
    else if (valueId == ARGUMENTS_ID) {
      return ARGUMENTS_VALUE;
    }
    else if (valueId == SCALAR_ID) {
      return new PerlScalarValue(dataStream);
    }
    else if (valueId == SCALAR_CONTEXT_ID) {
      return new PerlScalarContextValue(dataStream);
    }
    else if (valueId == ARRAY_ID) {
      return new PerlArrayValue(dataStream);
    }
    else if (valueId == HASH_ID) {
      return new PerlHashValue(dataStream);
    }
    else if (valueId == DEFERRED_HASH_ID) {
      return new PerlDeferredHashValue(dataStream);
    }
    else if (valueId == REFERENCE_ID) {
      return new PerlReferenceValue(dataStream);
    }
    else if (valueId == BLESSED_ID) {
      return new PerlBlessedValue(dataStream);
    }
    else if (valueId == ONE_OF_ID) {
      return new PerlOneOfValue(dataStream);
    }
    else if (valueId == CALL_OBJECT_ID) {
      return new PerlCallObjectValue(dataStream);
    }
    else if (valueId == CALL_STATIC_ID) {
      return new PerlCallStaticValue(dataStream);
    }
    else if (valueId == ARRAY_ELEMENT_ID) {
      return new PerlArrayElementValue(dataStream);
    }
    else if (valueId == HASH_ELEMENT_VALUE) {
      return new PerlHashElementValue(dataStream);
    }
    throw new RuntimeException("Don't know how to deserialize a value: " + valueId);
  }

  @NotNull
  static List<PerlValue> readList(@NotNull StubInputStream dataStream) throws IOException {
    int size = dataStream.readVarInt();
    if (size == 0) {
      return Collections.emptyList();
    }
    List<PerlValue> elements = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      elements.add(readValue(dataStream));
    }
    return Collections.unmodifiableList(elements);
  }

  static void writeList(@NotNull StubOutputStream dataStream, @NotNull List<PerlValue> elements) throws IOException {
    dataStream.writeVarInt(elements.size());
    for (PerlValue element : elements) {
      element.serialize(dataStream);
    }
  }
}
