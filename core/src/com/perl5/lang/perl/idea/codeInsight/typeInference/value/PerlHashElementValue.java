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

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.HASH_ELEMENT_VALUE;

public final class PerlHashElementValue extends PerlParametrizedOperationValue {
  PerlHashElementValue(@NotNull PerlValue hashValue, @NotNull PerlValue keyValue) {
    super(hashValue, keyValue);
    if (hashValue.isDeterministic() && keyValue.isDeterministic()) {
      LOG.error("Bot hash and key are deterministic and should be computed in-place: " +
                "hash=" + hashValue + "; " +
                "key=" + keyValue);
    }
  }

  PerlHashElementValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
  }

  @Override
  protected int getSerializationId() {
    return HASH_ELEMENT_VALUE;
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedHashValue,
                                     @NotNull PerlValue resolvedKeyValue,
                                     @NotNull PerlValueResolver resolver) {
    return computeStrictResolve(resolvedHashValue, resolvedKeyValue);
  }

  @NotNull
  private static PerlValue computeStrictResolve(@NotNull PerlValue hashValue,
                                                @NotNull PerlValue keyValue) {
    return ObjectUtils.notNull(computeResolve(hashValue, keyValue), UNKNOWN_VALUE);
  }

  @Nullable
  private static PerlValue computeResolve(@NotNull PerlValue hashValue,
                                          @NotNull PerlValue keyValue) {
    if (keyValue.isUnknown() || keyValue.isUndef()) {
      return null;
    }
    if (hashValue instanceof PerlHashValue) {
      return ((PerlHashValue)hashValue).get(keyValue);
    }
    else if (hashValue instanceof PerlDeferredHashValue) {
      return ((PerlDeferredHashValue)hashValue).tryGet(keyValue);
    }
    return null;
  }

  public PerlValue getHash() {
    return getBaseValue();
  }

  @NotNull
  public PerlValue getKey() {
    return getParameter();
  }

  @Override
  public String toString() {
    return "HashItem: " + getBaseValue() + "{" + getParameter() + "}";
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue hashValue, @NotNull PerlValue keyValue) {
    if (hashValue.isDeterministic() && keyValue.isDeterministic()) {
      return PerlValuesBuilder.convert(hashValue, keyValue, PerlHashElementValue::computeStrictResolve);
    }
    PerlValue resolvedValue = computeResolve(hashValue, keyValue);
    return PerlValue.isUnknown(resolvedValue) ? new PerlHashElementValue(hashValue, keyValue) : resolvedValue;
  }
}
