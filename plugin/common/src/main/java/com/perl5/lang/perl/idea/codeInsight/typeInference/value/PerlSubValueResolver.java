/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import org.jetbrains.annotations.NotNull;

public class PerlSubValueResolver extends PerlValueResolver {
  private final @NotNull PerlValue myArguments;

  public PerlSubValueResolver(@NotNull PsiElement contextElement,
                              @NotNull PerlValue arguments) {
    super(contextElement);
    myArguments = arguments;
  }

  @Override
  protected @NotNull PerlValue substitute(@NotNull PerlValue perlValue) {
    return perlValue == PerlValues.ARGUMENTS_VALUE ? myArguments : perlValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlSubValueResolver resolver = (PerlSubValueResolver)o;

    return myArguments.equals(resolver.myArguments);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + myArguments.hashCode();
    return result;
  }
}
