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
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

public class PerlSimpleValueResolver extends PerlValueResolver {
  @NotNull
  private final PsiElement myContextElement;

  public PerlSimpleValueResolver(@NotNull PsiElement contextElement) {
    super(contextElement);
    myContextElement = contextElement;
  }

  @NotNull
  @Override
  protected PerlValue substitute(@NotNull PerlValue perlValue) {
    if (perlValue == PerlValues.ARGUMENTS_VALUE) {
      return PerlValuesManager.intern(PerlArrayValue.create(PerlPackageUtil.getExpectedSelfValue(myContextElement)));
    }
    return super.substitute(perlValue);
  }

  @Override
  public boolean equals(Object o) {
    return o.getClass().equals(getClass()) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode() * 31 + getClass().hashCode();
  }
}
