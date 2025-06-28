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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.ARITHMETIC_NEGATION;

public class PerlArithmeticNegationValue extends PerlOperationValue {
  PerlArithmeticNegationValue(@NotNull PerlValue baseValue) {
    super(baseValue);
    if (baseValue.isDeterministic()) {
      LOG.error("Deterministic values should be resolved in-place: " + baseValue);
    }
  }

  PerlArithmeticNegationValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedBaseValue, @NotNull PerlValueResolver resolver) {
    return doComputeStrictResolve(resolvedBaseValue);
  }

  private static @NotNull PerlValue doComputeStrictResolve(@NotNull PerlValue target) {
    return ObjectUtils.notNull(doComputeResolve(target), UNKNOWN_VALUE);
  }

  private static @Nullable PerlValue doComputeResolve(@NotNull PerlValue target) {
    if (target instanceof PerlArithmeticNegationValue arithmeticNegationValue) {
      return arithmeticNegationValue.getBaseValue();
    }
    if (!(target instanceof PerlScalarValue scalarValue) || !target.isDeterministic()) {
      return null;
    }
    String value = scalarValue.getValue();
    if (StringUtil.isEmpty(value)) {
      return UNKNOWN_VALUE;
    }
    char firstChar = value.charAt(0);
    if (firstChar == '-') {
      return PerlScalarValue.create("+" + value.substring(1));
    }
    if (firstChar == '+') {
      return PerlScalarValue.create("-" + value.substring(1));
    }
    return PerlScalarValue.create("-" + value);
  }

  @Override
  protected int getSerializationId() {
    return ARITHMETIC_NEGATION;
  }

  @Override
  public String toString() {
    return "-" + getBaseValue();
  }

  public static @NotNull PerlValue create(@NotNull PerlValue baseValue) {
    if (baseValue.isDeterministic()) {
      return PerlValuesBuilder.convert(baseValue, PerlArithmeticNegationValue::doComputeStrictResolve);
    }

    PerlValue resolvedValue = doComputeResolve(baseValue);
    return resolvedValue != null ? resolvedValue : new PerlArithmeticNegationValue(baseValue);
  }
}
