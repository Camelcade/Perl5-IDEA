/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.BLESSED_ID;

public final class PerlBlessedValue extends PerlParametrizedOperationValue {
  PerlBlessedValue(@NotNull PerlValue targetValue, @NotNull PerlValue blessValue) {
    super(targetValue, blessValue);
  }

  PerlBlessedValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  public @NotNull PerlValue getBless() {
    return getParameter();
  }

  public @NotNull PerlValue getTarget() {
    return getBaseValue();
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedValue,
                                              @NotNull PerlValue resolvedBless,
                                              @NotNull PerlValueResolver resolver) {
    return computeStrictResolve(resolvedValue, resolvedBless);
  }

  @Override
  protected @NotNull PerlContextType getContextType() {
    return PerlContextType.SCALAR;
  }

  @Override
  public @NotNull String getPresentableText() {
    return PerlBundle.message("perl.value.presentable.blessed", getTarget(), getParameter());
  }

  @Override
  public String toString() {
    return "Bless " + getTarget() + " with " + getBless();
  }

  @Override
  protected int getSerializationId() {
    return BLESSED_ID;
  }

  private static @NotNull PerlValue computeStrictResolve(@NotNull PerlValue resolvedValue,
                                                         @NotNull PerlValue resolvedBless) {
    return ObjectUtils.notNull(computeResolve(resolvedValue, resolvedBless), UNKNOWN_VALUE);
  }

  private static @Nullable PerlValue computeResolve(@NotNull PerlValue resolvedValue,
                                                    @NotNull PerlValue resolvedBless) {
    if (resolvedValue instanceof PerlReferenceValue referenceValue) {
      return PerlReferenceValue.create(referenceValue.getTarget(), resolvedBless);
    }
    return null;
  }

  public static @NotNull PerlValue create(@NotNull PerlValue targetValue, @NotNull PerlValue blessValue) {
    if (targetValue.isDeterministic()) {
      return PerlValuesBuilder.convert(targetValue, blessValue, PerlBlessedValue::computeStrictResolve);
    }
    PerlValue resolvedValue = computeResolve(targetValue, blessValue);
    return PerlValue.isUnknown(resolvedValue) ? new PerlBlessedValue(targetValue, blessValue) : resolvedValue;
  }

}
