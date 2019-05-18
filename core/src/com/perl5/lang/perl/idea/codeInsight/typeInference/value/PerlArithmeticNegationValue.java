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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.StubInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.ARITHMETIC_NEGATION;

public class PerlArithmeticNegationValue extends PerlOperationValue {
  PerlArithmeticNegationValue(@NotNull PerlValue baseValue) {
    super(baseValue);
  }

  PerlArithmeticNegationValue(@NotNull StubInputStream dataStream)
    throws IOException {
    super(dataStream);
  }

  @NotNull
  @Override
  protected PerlValue createArithmeticNegation() {
    return getBaseValue();
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedBaseValue, @NotNull PerlValueResolver resolver) {
    if (!(resolvedBaseValue instanceof PerlScalarValue)) {
      return UNKNOWN_VALUE;
    }
    String value = ((PerlScalarValue)resolvedBaseValue).getValue();
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
}
