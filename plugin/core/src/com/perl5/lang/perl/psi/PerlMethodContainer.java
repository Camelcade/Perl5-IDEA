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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.psi.mixins.PerlCallArguments;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * fixme find a better name. This is a basically PerlCallExpression
 */
public interface PerlMethodContainer extends PsiElement, PerlValuableEntity {
  @Nullable
  PsiPerlMethod getMethod();

  @Override
  default @NotNull PerlValue computePerlValue() {
    return PerlValuesManager.from(getMethod());
  }

  default @Nullable PsiPerlCallArguments getCallArguments() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlCallArguments.class);
  }

  default @NotNull List<PsiElement> getCallArgumentsList() {
    PsiPerlCallArguments callArguments = getCallArguments();
    if (callArguments == null) {
      return Collections.emptyList();
    }
    return ((PerlCallArguments)callArguments).getArgumentsList();
  }
}
