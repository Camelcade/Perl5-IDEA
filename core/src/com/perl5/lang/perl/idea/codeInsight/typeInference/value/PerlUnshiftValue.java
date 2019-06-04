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

import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.psi.PerlUnshiftPushExpr;
import com.perl5.lang.perl.psi.PsiPerlArrayUnshiftExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.UNSHIFT_ID;

public class PerlUnshiftValue extends PerlParametrizedOperationValue {
  private PerlUnshiftValue(@NotNull PerlValue arrayValue,
                           @NotNull PerlValue prefixValue) {
    super(arrayValue, prefixValue);
    if (arrayValue.isDeterministic() && prefixValue.isDeterministic()) {
      LOG.error("Bot array and prefix are deterministic and should be computed in-place: " +
                "array=" + arrayValue + "; " +
                "prefix=" + prefixValue);
    }
  }

  public PerlUnshiftValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedArrayValue,
                                     @NotNull PerlValue resolvedPrefixValue,
                                     @NotNull PerlValueResolver resolver) {
    return computeStrictResolve(resolvedArrayValue, resolvedPrefixValue);
  }

  @Override
  protected int getSerializationId() {
    return UNSHIFT_ID;
  }

  @Override
  public String toString() {
    return "Unshift: [" + getParameter() + ", " + getBaseValue() + "]";
  }

  @NotNull
  private static PerlValue computeStrictResolve(@NotNull PerlValue resolvedArrayValue,
                                                @NotNull PerlValue resolvedPrefixValue) {
    return ObjectUtils.notNull(computeResolve(resolvedArrayValue, resolvedPrefixValue), UNKNOWN_VALUE);
  }

  @Nullable
  private static PerlValue computeResolve(@NotNull PerlValue resolvedArrayValue,
                                          @NotNull PerlValue resolvedPrefixValue) {
    if (!(resolvedArrayValue instanceof PerlArrayValue)) {
      return null;
    }
    return PerlArrayValue.builder().addElement(resolvedPrefixValue).addElement(resolvedArrayValue).build();
  }

  @NotNull
  public static PerlValue create(@NotNull PsiPerlArrayUnshiftExpr unshiftExpr) {
    return create(unshiftExpr, PerlUnshiftValue::create);
  }

  @NotNull
  static PerlValue create(@NotNull PerlUnshiftPushExpr unshiftPushExpr, @NotNull BiFunction<PerlValue, PerlValue, PerlValue> function) {
    PsiElement target = unshiftPushExpr.getTarget();
    if (target == null) {
      return UNKNOWN_VALUE;
    }
    List<PsiElement> modification = unshiftPushExpr.getModification();
    if (modification.isEmpty()) {
      return UNKNOWN_VALUE;
    }
    return function.apply(PerlValuesManager.from(target), PerlArrayValue.builder().addPsiElements(modification).build());
  }


  public static PerlValue create(@NotNull PerlValue arrayValue, @NotNull PerlValue prefixValue) {
    if (arrayValue.isDeterministic() && prefixValue.isDeterministic()) {
      return PerlValuesBuilder.convert(arrayValue, prefixValue, PerlUnshiftValue::computeStrictResolve);
    }
    PerlValue resolvedValue = computeResolve(arrayValue, prefixValue);
    return !PerlValue.isUnknown(resolvedValue) ? resolvedValue : new PerlUnshiftValue(arrayValue, prefixValue);
  }
}

