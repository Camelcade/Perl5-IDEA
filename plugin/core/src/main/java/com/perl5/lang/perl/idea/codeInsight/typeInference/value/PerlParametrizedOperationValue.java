/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

abstract class PerlParametrizedOperationValue extends PerlOperationValue {
  private final @NotNull PerlValue myParameter;

  public PerlParametrizedOperationValue(@NotNull PerlValue baseValue,
                                        @NotNull PerlValue parameter) {
    super(baseValue);
    myParameter = parameter;
  }

  PerlParametrizedOperationValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
    myParameter = deserializer.readValue();
  }

  @Override
  protected void serializeData(@NotNull PerlValueSerializer serializer) throws IOException {
    super.serializeData(serializer);
    serializer.writeValue(myParameter);
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedBaseValue, @NotNull PerlValueResolver resolver) {
    return resolver.resolve(myParameter, it -> computeResolve(resolvedBaseValue, it, resolver));
  }

  protected abstract @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedBaseValue,
                                                       @NotNull PerlValue resolvedParameter,
                                                       @NotNull PerlValueResolver resolver);

  protected final @NotNull PerlValue getParameter() {
    return myParameter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlParametrizedOperationValue value = (PerlParametrizedOperationValue)o;

    return myParameter.equals(value.myParameter);
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + myParameter.hashCode();
    return result;
  }
}
