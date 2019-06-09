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
import com.perl5.lang.perl.psi.PsiPerlArrayPushExpr;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.PUSH_ID;

public class PerlPushValue extends PerlParametrizedOperationValue {
  private PerlPushValue(@NotNull PerlValue arrayValue,
                        @NotNull PerlValue suffixValue) {
    super(arrayValue, suffixValue);
  }

  PerlPushValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedArrayValue,
                                     @NotNull PerlValue resolvedSuffixValue,
                                     @NotNull PerlValueResolver resolver) {
    return computeStrictResolve(resolvedArrayValue, resolvedSuffixValue);
  }

  @Override
  protected int getSerializationId() {
    return PUSH_ID;
  }

  @Override
  public String toString() {
    return "Push: [" + getBaseValue() + ", " + getParameter() + "]";
  }

  @Nullable
  @Override
  protected PerlContextType getContextType() {
    return PerlContextType.LIST;
  }

  @NotNull
  private static PerlValue computeStrictResolve(@NotNull PerlValue resolvedArrayValue,
                                                @NotNull PerlValue resolvedSuffixValue) {
    return ObjectUtils.notNull(computeResolve(resolvedArrayValue, resolvedSuffixValue), UNKNOWN_VALUE);
  }

  @Nullable
  private static PerlValue computeResolve(@NotNull PerlValue resolvedArrayValue,
                                          @NotNull PerlValue resolvedSuffixValue) {
    if (!(resolvedArrayValue instanceof PerlArrayValue)) {
      return null;
    }
    return PerlArrayValue.builder().addElement(resolvedArrayValue).addElement(resolvedSuffixValue).build();
  }

  @NotNull
  public static PerlValue create(@NotNull PsiPerlArrayPushExpr pushExpr) {
    return PerlUnshiftValue.create(pushExpr, PerlPushValue::create);
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue arrayValue, @NotNull PerlValue suffixValue) {
    return new PerlPushValue(arrayValue, suffixValue);
  }
}

