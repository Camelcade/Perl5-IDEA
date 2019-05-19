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

  PerlBlessedValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
  }

  @NotNull
  public PerlValue getBless() {
    return getParameter();
  }

  @NotNull
  public PerlValue getTarget() {
    return getBaseValue();
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedValue,
                                     @NotNull PerlValue resolvedBless,
                                     @NotNull PerlValueResolver resolver) {
    return computeStrictResolve(resolvedValue, resolvedBless);
  }

  @NotNull
  @Override
  protected PerlContextType getContextType() {
    return PerlContextType.SCALAR;
  }

  @NotNull
  @Override
  public String getPresentableText() {
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

  @NotNull
  private static PerlValue computeStrictResolve(@NotNull PerlValue resolvedValue,
                                                @NotNull PerlValue resolvedBless) {
    return ObjectUtils.notNull(computeResolve(resolvedValue, resolvedBless), UNKNOWN_VALUE);
  }

  @Nullable
  private static PerlValue computeResolve(@NotNull PerlValue resolvedValue,
                                          @NotNull PerlValue resolvedBless) {
    if (resolvedValue instanceof PerlReferenceValue) {
      return PerlReferenceValue.create(((PerlReferenceValue)resolvedValue).getTarget(), resolvedBless);
    }
    return null;
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue targetValue, @NotNull PerlValue blessValue) {
    if (targetValue.isDeterministic()) {
      return PerlValuesBuilder.convert(targetValue, blessValue, PerlBlessedValue::computeStrictResolve);
    }
    PerlValue resolvedValue = computeResolve(targetValue, blessValue);
    return PerlValue.isUnknown(resolvedValue) ? new PerlBlessedValue(targetValue, blessValue) : resolvedValue;
  }

}
