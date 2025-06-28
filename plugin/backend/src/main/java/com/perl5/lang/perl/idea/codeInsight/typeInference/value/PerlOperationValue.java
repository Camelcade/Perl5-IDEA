/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Represents non-mutation operation value on some base value. E.g. item, slice, keys, scalar
 */
abstract class PerlOperationValue extends PerlValue {
  private final @NotNull PerlValue myBaseValue;

  public PerlOperationValue(@NotNull PerlValue baseValue) {
    myBaseValue = baseValue;
  }

  PerlOperationValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
    myBaseValue = deserializer.readValue();
  }

  @Override
  protected @Nullable PerlContextType getContextType() {
    return PerlContextType.SCALAR;
  }

  protected final @NotNull PerlValue getBaseValue() {
    return myBaseValue;
  }

  @Override
  protected void serializeData(@NotNull PerlValueSerializer serializer) throws IOException {
    serializer.writeValue(myBaseValue);
  }

  @Override
  final @NotNull PerlValue computeResolve(@NotNull PerlValueResolver resolver) {
    return resolver.resolve(myBaseValue, it -> computeResolve(it, resolver));
  }

  protected abstract @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedBaseValue,
                                                       @NotNull PerlValueResolver resolver);

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerlOperationValue value = (PerlOperationValue)o;

    return myBaseValue.equals(value.myBaseValue);
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + myBaseValue.hashCode();
    return result;
  }
}
