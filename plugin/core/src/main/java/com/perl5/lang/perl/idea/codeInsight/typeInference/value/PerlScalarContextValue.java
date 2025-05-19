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
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.SCALAR_CONTEXT_ID;

public class PerlScalarContextValue extends PerlOperationValue {

  PerlScalarContextValue(@NotNull PerlValue baseValue) {
    super(baseValue);
    if (baseValue.getContextType() == PerlContextType.SCALAR) {
      LOG.error("Value is already a scalar: " + baseValue);
    }
    else if (baseValue.isDeterministic()) {
      LOG.error("Deterministic values should be resolved in place: " + baseValue);
    }
  }

  PerlScalarContextValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedTarget, @NotNull PerlValueResolver resolver) {
    return doComputeStrictResolve(resolvedTarget);
  }

  private static @NotNull PerlValue doComputeStrictResolve(@NotNull PerlValue resolvedTarget) {
    return ObjectUtils.notNull(doComputeResolve(resolvedTarget), UNKNOWN_VALUE);
  }

  private static @Nullable PerlValue doComputeResolve(@NotNull PerlValue targetValue) {
    if (targetValue.isUnknown()) {
      return UNKNOWN_VALUE;
    }
    if (targetValue.getContextType() == PerlContextType.SCALAR) {
      return targetValue;
    }
    if (!targetValue.isDeterministic()) {
      return null;
    }
    if (targetValue instanceof PerlArrayValue perlArrayValue) {
      List<PerlValue> arrayElements = perlArrayValue.getElements();
      return !arrayElements.contains(UNKNOWN_VALUE) ? PerlScalarValue.create(arrayElements.size()) : UNKNOWN_VALUE;
    }
    else if (targetValue instanceof PerlHashValue hashValue) {
      return !hashValue.getElements().contains(UNKNOWN_VALUE) ?
             PerlScalarValue.create(hashValue.getMap().size()) : UNKNOWN_VALUE;
    }
    return null;
  }

  @Override
  protected int getSerializationId() {
    return SCALAR_CONTEXT_ID;
  }

  @Override
  public String toString() {
    return "scalar " + getBaseValue();
  }

  @Override
  public @NotNull String getPresentableText() {
    return "scalar " + getBaseValue().getPresentableText();
  }

  public static @NotNull PerlValue create(@NotNull PerlValue baseValue) {
    if (baseValue.isDeterministic()) {
      return PerlValuesBuilder.convert(baseValue, PerlScalarContextValue::doComputeStrictResolve);
    }

    PerlValue resolvedValue = doComputeResolve(baseValue);
    return resolvedValue != null ? resolvedValue : new PerlScalarContextValue(baseValue);
  }
}
