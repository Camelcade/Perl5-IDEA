/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.perl5.lang.perl.idea.codeInsight.typeInferrence.value.PerlValue;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import com.perl5.lang.perl.util.PerlArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInferrence.value.PerlValueUnknown.UNKNOWN_VALUE;

/**
 * Created by hurricup on 25.07.2015.
 * fixme find a better name. This is a basically PerlCallExpression
 */
public interface PerlMethodContainer extends PsiElement, PerlValuableEntity {
  @Nullable
  PsiPerlMethod getMethod();

  @NotNull
  @Override
  default PerlValue getPerlValue() {
    PsiPerlMethod perlMethod = getMethod();
    return perlMethod == null ? UNKNOWN_VALUE : perlMethod.getPerlValue();
  }

  @Nullable
  default PsiPerlCallArguments getCallArguments() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlCallArguments.class);
  }

  @NotNull
  default List<PsiElement> getCallArgumentsList() {
    PsiPerlCallArguments callArguments = getCallArguments();
    if (callArguments == null) {
      return Collections.emptyList();
    }
    PsiPerlExpr expression = PsiTreeUtil.getChildOfType(callArguments, PsiPerlExpr.class);
    return expression == null ? Collections.emptyList() : PerlArrayUtil.collectListElements(expression);
  }
}
