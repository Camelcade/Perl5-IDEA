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

package com.perl5.lang.perl.psi.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlCallValue;
import com.perl5.lang.perl.psi.PerlMethodCall;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PerlSubReference extends PerlSubReferenceSimple {

  public PerlSubReference(PsiElement psiElement) {
    super(psiElement);
  }

  @NotNull
  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    PsiElement element = getElement();
    assert element instanceof PerlSubNameElement;

    PsiElement parent = element.getParent();
    if (!(parent instanceof PerlMethodCall)) {
      return ResolveResult.EMPTY_ARRAY;
    }

    PerlCallValue perlValue = PerlCallValue.from(parent);
    if (perlValue == null) {
      return ResolveResult.EMPTY_ARRAY;
    }

    List<PsiElement> relatedItems = new ArrayList<>();
    perlValue.processCallTargets(element.getProject(), element.getResolveScope(), element, (__, it) -> relatedItems.add(it));
    return getResolveResults(relatedItems).toArray(new ResolveResult[0]);
  }
}
