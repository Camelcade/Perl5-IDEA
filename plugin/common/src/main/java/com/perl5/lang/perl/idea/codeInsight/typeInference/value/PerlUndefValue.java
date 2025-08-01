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

import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;

public final class PerlUndefValue extends PerlSpecialValue {
  public static final String STRING_UNDEF = "undef";

  static final PerlUndefValue INSTANCE = new PerlUndefValue();

  private PerlUndefValue() {
  }

  @Override
  protected boolean computeIsDeterministic() {
    return true;
  }

  @NotNull
  @Override
  PerlValue computeResolve(@NotNull PerlValueResolver resolver) {
    return this;
  }

  @Override
  protected @NotNull PerlContextType getContextType() {
    return PerlContextType.SCALAR;
  }

  @Override
  public @NotNull String getPresentableText() {
    return STRING_UNDEF;
  }

  @Override
  public String toString() {
    return "UNDEF_VALUE";
  }
}
