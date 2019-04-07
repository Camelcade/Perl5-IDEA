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
import com.intellij.util.containers.WeakInterner;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUndefValue.UNDEF_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUnknownValue.UNKNOWN_VALUE;

/**
 * Manages {@link PerlValue} serialization and deserialization, manages serialization ID
 * We could implement something like PerlValueElementType but this thing is not supported to be extendable, so good for now
 */
public final class PerlValuesManager {
  public static final int VERSION = 2;

  static final int UNKNOWN_ID = 0;
  static final int UNDEF_ID = 1;
  static final int STATIC_ID = 2;
  static final int SCALAR_ID = 3;
  static final int REFERENCE_ID = 4;
  static final int ARRAY_ID = 5;
  static final int HASH_ID = 6;
  static final int ONE_OF_ID = 7;
  static final int CALL_STATIC_ID = 8;
  static final int CALL_OBJECT_ID = 9;

  private static final WeakInterner<PerlValue> INTERNER = new WeakInterner<>();

  public static PerlValue deserialize(@NotNull StubInputStream dataStream) throws IOException {
    return intern(doDeserialize(dataStream));
  }

  public static PerlValue intern(PerlValue value) {
    return INTERNER.intern(value);
  }

  @NotNull
  private static PerlValue doDeserialize(@NotNull StubInputStream dataStream) throws IOException {
    int valueId = dataStream.readInt();
    switch (valueId) {
      case UNKNOWN_ID:
        dataStream.readBoolean();
        return UNKNOWN_VALUE;
      case UNDEF_ID:
        dataStream.readBoolean();
        return UNDEF_VALUE;
      case STATIC_ID:
        return new PerlStaticValue(dataStream);
      case SCALAR_ID:
        return new PerlScalarValue(dataStream);
      case REFERENCE_ID:
        return new PerlReferenceValue(dataStream);
      case ARRAY_ID:
        return new PerlArrayValue(dataStream);
      case HASH_ID:
        return new PerlHashValue(dataStream);
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
}
