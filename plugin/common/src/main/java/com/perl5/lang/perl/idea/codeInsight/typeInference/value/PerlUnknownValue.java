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

import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PerlUnknownValue extends PerlSpecialValue {
  static final PerlUnknownValue INSTANCE = new PerlUnknownValue();

  private PerlUnknownValue() {
  }

  @NotNull
  @Override
  PerlValue computeResolve(@NotNull PerlValueResolver resolver) {
    return this;
  }

  @Override
  protected @Nullable PerlContextType getContextType() {
    return null;
  }

  @Override
  protected boolean computeIsDeterministic() {
    return true;
  }

  @Override
  public @NotNull String getPresentableText() {
    return PerlBundle.message("perl.value.unknown.presentable");
  }

  @Override
  public String toString() {
    return "UNKNOWN_VALUE";
  }
}
