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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public final class PerlArrayElementValue extends PerlParametrizedOperationValue {
  private PerlArrayElementValue(@NotNull PerlValue arrayValue, @NotNull PerlValue indexValue) {
    super(arrayValue, indexValue);
    if (arrayValue.isDeterministic() && indexValue.isDeterministic()) {
      LOG.error("Bot array and index are deterministic and should be computed in-place: " +
                "array=" + arrayValue + "; " +
                "index=" + indexValue);
    }
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedArrayValue,
                                              @NotNull PerlValue resolvedIndexValue,
                                              @NotNull PerlValueResolver resolver) {
    return computeStrictResolve(resolvedArrayValue, resolvedIndexValue);
  }

  private static @NotNull PerlValue computeStrictResolve(@NotNull PerlValue resolvedArrayValue,
                                                         @NotNull PerlValue resolvedIndexValue) {
    return ObjectUtils.notNull(computeResolve(resolvedArrayValue, resolvedIndexValue), UNKNOWN_VALUE);
  }

  private static @Nullable PerlValue computeResolve(@NotNull PerlValue resolvedArrayValue,
                                                    @NotNull PerlValue resolvedIndexValue) {
    return resolvedArrayValue instanceof PerlArrayValue arrayValue ? arrayValue.get(resolvedIndexValue) : null;
  }

  @Override
  public String toString() {
    return "ArrayItem: " + getBaseValue() + "[" + getParameter() + "]";
  }

  public static @NotNull PerlValue create(@NotNull PerlValue arrayValue, @NotNull PerlValue indexValue) {
    if (arrayValue.isDeterministic() && indexValue.isDeterministic()) {
      return PerlValuesBuilder.convert(arrayValue, indexValue, PerlArrayElementValue::computeStrictResolve);
    }
    PerlValue resolvedValue = computeResolve(arrayValue, indexValue);
    return !PerlValue.isUnknown(resolvedValue) ? resolvedValue : new PerlArrayElementValue(arrayValue, indexValue);
  }
}
