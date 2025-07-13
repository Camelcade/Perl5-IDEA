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

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public final class PerlHashElementValue extends PerlParametrizedOperationValue {
  PerlHashElementValue(@NotNull PerlValue hashValue, @NotNull PerlValue keyValue) {
    super(hashValue, keyValue);
    if (hashValue.isDeterministic() && keyValue.isDeterministic()) {
      LOG.error("Bot hash and key are deterministic and should be computed in-place: " +
                "hash=" + hashValue + "; " +
                "key=" + keyValue);
    }
  }

  PerlHashElementValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedHashValue,
                                              @NotNull PerlValue resolvedKeyValue,
                                              @NotNull PerlValueResolver resolver) {
    return computeStrictResolve(resolvedHashValue, resolvedKeyValue);
  }

  private static @NotNull PerlValue computeStrictResolve(@NotNull PerlValue hashValue,
                                                         @NotNull PerlValue keyValue) {
    return ObjectUtils.notNull(computeResolve(hashValue, keyValue), UNKNOWN_VALUE);
  }

  private static @Nullable PerlValue computeResolve(@NotNull PerlValue hashValue,
                                                    @NotNull PerlValue keyValue) {
    if (keyValue.isUnknown() || keyValue.isUndef()) {
      return null;
    }
    if (hashValue instanceof PerlHashValue perlHashValue) {
      return perlHashValue.get(keyValue);
    }
    else if (hashValue instanceof PerlDeferredHashValue perlDeferredHashValue) {
      return perlDeferredHashValue.tryGet(keyValue);
    }
    return null;
  }

  public PerlValue getHash() {
    return getBaseValue();
  }

  public @NotNull PerlValue getKey() {
    return getParameter();
  }

  @Override
  public String toString() {
    return "HashItem: " + getBaseValue() + "{" + getParameter() + "}";
  }

  public static @NotNull PerlValue create(@NotNull PerlValue hashValue, @NotNull PerlValue keyValue) {
    if (hashValue.isDeterministic() && keyValue.isDeterministic()) {
      return PerlValuesBuilder.convert(hashValue, keyValue, PerlHashElementValue::computeStrictResolve);
    }
    PerlValue resolvedValue = computeResolve(hashValue, keyValue);
    return PerlValue.isUnknown(resolvedValue) ? new PerlHashElementValue(hashValue, keyValue) : resolvedValue;
  }
}
