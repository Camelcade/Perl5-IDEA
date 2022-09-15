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

import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.ARRAY_SLICE_ID;

public class PerlArraySliceValue extends PerlParametrizedOperationValue {

  PerlArraySliceValue(@NotNull PerlValue arrayValue,
                      @NotNull PerlValue indexesValue) {
    super(arrayValue, indexesValue);
    if (arrayValue.isDeterministic() && indexesValue.isDeterministic()) {
      LOG.error("Bot array and indexes are deterministic and should be computed in-place: " +
                "array=" + arrayValue + "; " +
                "indexes=" + indexesValue);
    }
  }

  PerlArraySliceValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @Override
  protected @Nullable PerlContextType getContextType() {
    return PerlContextType.LIST;
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedArrayValue,
                                              @NotNull PerlValue resolvedIndexesValue,
                                              @NotNull PerlValueResolver resolver) {
    return computeStrictResolve(resolvedArrayValue, resolvedIndexesValue);
  }

  private static @NotNull PerlValue computeStrictResolve(@NotNull PerlValue resolvedArrayValue,
                                                         @NotNull PerlValue resolvedIndexesValue) {
    return ObjectUtils.notNull(computeResolve(resolvedArrayValue, resolvedIndexesValue), UNKNOWN_VALUE);
  }

  private static PerlValue computeResolve(@NotNull PerlValue arrayValue,
                                          @NotNull PerlValue indexValue) {
    if (!(arrayValue instanceof PerlArrayValue)) {
      return null;
    }
    PerlArrayValue.Builder builder = PerlArrayValue.builder();
    if (indexValue instanceof PerlArrayValue) {
      ((PerlArrayValue)indexValue).forEach(key -> builder.addElement(((PerlArrayValue)arrayValue).get(key)));
    }
    else {
      return null;
    }
    return builder.build();
  }

  @Override
  protected int getSerializationId() {
    return ARRAY_SLICE_ID;
  }

  @Override
  public String toString() {
    return "ArraySlice: " + getBaseValue() + "[" + getParameter() + "]";
  }

  public static @NotNull PerlValue create(@NotNull PerlValue arrayValue, @NotNull PerlValue indexValue) {
    if (arrayValue.isDeterministic() && indexValue.isDeterministic()) {
      return PerlValuesBuilder.convert(arrayValue, indexValue, PerlArraySliceValue::computeStrictResolve);
    }
    return new PerlArraySliceValue(arrayValue, indexValue);
  }
}
