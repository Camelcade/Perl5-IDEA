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

/**
 * Pair of values, where second one is the fallback for the first one. If first can't be resolved to something sensible, second is used.
 */
public class PerlValueWithFallback extends PerlParametrizedOperationValue {
  private PerlValueWithFallback(@NotNull PerlValue baseValue,
                                @NotNull PerlValue parameter) {
    super(baseValue, parameter);
  }

  PerlValueWithFallback(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedBaseValue, @NotNull PerlValueResolver resolver) {
    if (resolvedBaseValue.isUnknown()) {
      return super.computeResolve(resolvedBaseValue, resolver);
    }
    if (resolvedBaseValue.isUndef()) {
      var fallBackValue = super.computeResolve(resolvedBaseValue, resolver);
      return fallBackValue.isUnknown() ? resolvedBaseValue : fallBackValue;
    }
    return resolvedBaseValue;
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedBaseValue,
                                              @NotNull PerlValue resolvedParameter,
                                              @NotNull PerlValueResolver resolver) {
    return resolvedParameter;
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.VALUE_WITH_FALLBACK;
  }

  @Override
  public String toString() {
    return "Value with fallback: " + getBaseValue() + " -> " + getParameter();
  }

  public static PerlValue create(@NotNull PerlValue baseValue, @NotNull PerlValue fallbackValue) {
    if (baseValue.isUnknown()) {
      return fallbackValue;
    }
    if (baseValue.isDeterministic()) {
      return baseValue;
    }
    if (fallbackValue.isUnknown()) {
      return baseValue;
    }
    return new PerlValueWithFallback(baseValue, fallbackValue);
  }
}
