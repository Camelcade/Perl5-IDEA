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

import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

/**
 * Represents a plain value - string or number
 */
public final class PerlScalarValue extends PerlValue {

  @NotNull
  private final String myValue;

  private PerlScalarValue(@NotNull String value) {
    myValue = value;
  }

  PerlScalarValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
    myValue = Objects.requireNonNull(deserializer.readNameString());
  }

  @NotNull
  @Override
  PerlValue computeResolve(@NotNull PerlValueResolver resolver) {
    return this;
  }

  @NotNull
  @Override
  protected final PerlContextType getContextType() {
    return PerlContextType.SCALAR;
  }

  @Override
  protected void serializeData(@NotNull PerlValueSerializer serializer) throws IOException {
    serializer.writeName(myValue);
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.SCALAR_ID;
  }

  @NotNull
  public String getValue() {
    return myValue;
  }

  @Override
  protected boolean computeIsDeterministic() {
    return true;
  }

  @NotNull
  @Override
  public Set<String> getSubNames() {
    return Collections.singleton(myValue);
  }

  @NotNull
  @Override
  public Set<String> getNamespaceNames() {
    return Collections.singleton(myValue);
  }

  @Override
  public boolean canRepresentNamespace(@Nullable String namespaceName) {
    return myValue.equals(namespaceName);
  }

  @Override
  public boolean canRepresentSubName(@Nullable String subName) {
    return myValue.equals(subName);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerlScalarValue value = (PerlScalarValue)o;

    return myValue.equals(value.myValue);
  }

  @Override
  public int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + myValue.hashCode();
    return result;
  }


  @NotNull
  public static AtomicNotNullLazyValue<PerlValue> createLazy(@Nullable String value) {
    return PerlValuesManager.lazy(create(value));
  }

  @NotNull
  public static PerlValue create(@Nullable String value) {
    return value == null ? UNKNOWN_VALUE : PerlValuesManager.intern(new PerlScalarValue(value));
  }

  @NotNull
  public static PerlValue create(int value) {
    return new PerlScalarValue(String.valueOf(value));
  }

  @Override
  public String toString() {
    return myValue;
  }
}
