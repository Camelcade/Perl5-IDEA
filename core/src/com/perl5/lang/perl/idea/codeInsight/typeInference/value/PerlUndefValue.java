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

import com.perl5.lang.perl.lexer.PerlBaseLexer;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

final class PerlUndefValue extends PerlSpecialValue {
  static final PerlUndefValue INSTANCE = new PerlUndefValue();

  private PerlUndefValue() {
  }

  @Override
  protected boolean computeIsDeterministic() {
    return true;
  }

  @NotNull
  @Override
  public PerlValue createArrayElement(@NotNull PerlValue arrayIndex) {
    return UNKNOWN_VALUE;
  }

  @NotNull
  @Override
  protected PerlValue createArithmeticNegation() {
    return UNKNOWN_VALUE;
  }

  @NotNull
  @Override
  protected PerlValue createArraySlice(@NotNull PerlValue indexesValue) {
    return UNKNOWN_VALUE;
  }

  @NotNull
  @Override
  protected PerlValue createHashSlice(@NotNull PerlValue keysValue) {
    return UNKNOWN_VALUE;
  }

  @NotNull
  @Override
  protected PerlValue createHashElement(@NotNull PerlValue hashKey) {
    return UNKNOWN_VALUE;
  }

  @NotNull
  @Override
  protected PerlValue createScalarDereference() {
    return UNKNOWN_VALUE;
  }

  @NotNull
  @Override
  protected PerlContextType getContextType() {
    return PerlContextType.SCALAR;
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.UNDEF_ID;
  }

  @NotNull
  @Override
  public String getPresentableText() {
    return PerlBaseLexer.STRING_UNDEF;
  }

  @Override
  public String toString() {
    return "UNDEF_VALUE";
  }
}
