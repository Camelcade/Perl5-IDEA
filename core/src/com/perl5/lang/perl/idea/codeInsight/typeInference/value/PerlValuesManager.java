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
  public static final int VERSION = 4;
  // special values
  static final int UNKNOWN_ID = 0;
  static final int UNDEF_ID = 1;
  static final int ARGUMENTS_ID = 2;
  // primitives
  static final int SCALAR_ID = 3;
  static final int ARRAY_ID = 4;
  static final int HASH_ID = 5;
  static final int REFERENCE_ID = 6;
  // synthetic values
  static final int ONE_OF_ID = 7;
  static final int CALL_STATIC_ID = 8;
  static final int CALL_OBJECT_ID = 9;

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
    switch (valueId) {
      case UNKNOWN_ID:
        return UNKNOWN_VALUE;
      case UNDEF_ID:
        return UNDEF_VALUE;
      case ARGUMENTS_ID:
        return ARGUMENTS_VALUE;
      case SCALAR_ID:
        return new PerlScalarValue(dataStream);
      case ARRAY_ID:
        return new PerlArrayValue(dataStream);
      case HASH_ID:
        return new PerlHashValue(dataStream);
      case REFERENCE_ID:
        return new PerlReferenceValue(dataStream);
      case ONE_OF_ID:
        return new PerlOneOfValue(dataStream);
      case CALL_OBJECT_ID:
        return new PerlCallObjectValue(dataStream);
      case CALL_STATIC_ID:
        return new PerlCallStaticValue(dataStream);
      default:
        throw new RuntimeException("Don't know how to deserialize a value: " + valueId);
    }
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
