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

import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.psi.PerlUnshiftPushExpr;
import com.perl5.lang.perl.psi.PsiPerlArrayUnshiftExpr;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlArrayValue.EMPTY_ARRAY;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public class PerlUnshiftValue extends PerlParametrizedOperationValue {
  private PerlUnshiftValue(@NotNull PerlValue arrayValue,
                           @NotNull PerlValue prefixValue) {
    super(arrayValue, prefixValue);
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedArrayValue,
                                              @NotNull PerlValue resolvedPrefixValue,
                                              @NotNull PerlValueResolver resolver) {
    return computeStrictResolve(resolvedArrayValue, resolvedPrefixValue);
  }

  @Override
  protected @Nullable PerlContextType getContextType() {
    return PerlContextType.LIST;
  }

  @Override
  public String toString() {
    return "Unshift: [" + getParameter() + ", " + getBaseValue() + "]";
  }

  private static @NotNull PerlValue computeStrictResolve(@NotNull PerlValue resolvedArrayValue,
                                                         @NotNull PerlValue resolvedPrefixValue) {
    return ObjectUtils.notNull(computeResolve(resolvedArrayValue, resolvedPrefixValue), UNKNOWN_VALUE);
  }

  private static @Nullable PerlValue computeResolve(@NotNull PerlValue resolvedArrayValue,
                                                    @NotNull PerlValue resolvedPrefixValue) {
    if (!(resolvedArrayValue instanceof PerlArrayValue)) {
      return null;
    }
    return PerlArrayValue.builder().addElement(resolvedPrefixValue).addElement(resolvedArrayValue).build();
  }

  public static @NotNull PerlValue create(@NotNull PsiPerlArrayUnshiftExpr unshiftExpr) {
    return create(unshiftExpr, PerlUnshiftValue::create);
  }

  static @NotNull PerlValue create(@NotNull PerlUnshiftPushExpr unshiftPushExpr,
                                   @NotNull BiFunction<? super PerlValue, ? super PerlValue, ? extends PerlValue> function) {
    PsiElement target = unshiftPushExpr.getTarget();
    if (target == null) {
      return UNKNOWN_VALUE;
    }
    List<PsiElement> modification = unshiftPushExpr.getModification();
    if (modification.isEmpty()) {
      return UNKNOWN_VALUE;
    }
    PerlValue targetValue = PerlValuesManager.from(target);
    PerlValue modificationValue = PerlArrayValue.builder().addPsiElements(modification).build();
    if (targetValue == EMPTY_ARRAY) {
      return modificationValue;
    }
    if (modificationValue == EMPTY_ARRAY) {
      return targetValue;
    }
    return function.apply(targetValue, modificationValue);
  }


  public static PerlValue create(@NotNull PerlValue arrayValue, @NotNull PerlValue prefixValue) {
    return new PerlUnshiftValue(arrayValue, prefixValue);
  }
}

