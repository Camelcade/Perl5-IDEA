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

import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.HASH_SLICE_ID;

public class PerlHashSliceValue extends PerlParametrizedOperationValue {
  public PerlHashSliceValue(@NotNull PerlValue hashValue,
                            @NotNull PerlValue keysValue) {
    super(hashValue, keysValue);
    if (hashValue.isDeterministic() && keysValue.isDeterministic()) {
      LOG.error("Both hash and keys are deterministic and should be computed in-place: " +
                "hash=" + hashValue + "; " +
                "keys=" + keysValue);
    }
  }

  PerlHashSliceValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @Nullable
  @Override
  protected PerlContextType getContextType() {
    return PerlContextType.LIST;
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedHashValue,
                                     @NotNull PerlValue resolvedKeysValue,
                                     @NotNull PerlValueResolver resolver) {
    return computeStrictResolve(resolvedHashValue, resolvedKeysValue);
  }

  @NotNull
  private static PerlValue computeStrictResolve(@NotNull PerlValue resolvedHashValue,
                                                @NotNull PerlValue resolvedKeysValue) {
    return ObjectUtils.notNull(computeResolve(resolvedHashValue, resolvedKeysValue), UNKNOWN_VALUE);
  }

  @Nullable
  private static PerlValue computeResolve(@NotNull PerlValue resolvedHashValue,
                                          @NotNull PerlValue resolvedKeysValue) {
    if (!(resolvedHashValue instanceof PerlHashValue)) {
      return null;
    }

    PerlArrayValue.Builder builder = PerlArrayValue.builder();
    if (resolvedKeysValue instanceof PerlArrayValue) {
      ((PerlArrayValue)resolvedKeysValue).forEach(key -> builder.addElement(((PerlHashValue)resolvedHashValue).get(key)));
    }
    else {
      return null;
    }
    return builder.build();
  }

  @Override
  protected int getSerializationId() {
    return HASH_SLICE_ID;
  }

  @Override
  public String toString() {
    return "HashSlice: " + getBaseValue() + "{" + getParameter() + "}";
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue hashValue, @NotNull PerlValue keysValue) {
    if (hashValue.isDeterministic() && keysValue.isDeterministic()) {
      return PerlValuesBuilder.convert(hashValue, keysValue, PerlHashSliceValue::computeStrictResolve);
    }
    return new PerlHashSliceValue(hashValue, keysValue);
  }
}
