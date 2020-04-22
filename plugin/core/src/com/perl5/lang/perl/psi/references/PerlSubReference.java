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

package com.perl5.lang.perl.psi.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlCallObjectValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlCallValue;
import com.perl5.lang.perl.psi.PerlMethod;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlTokenSets.MODIFIER_DECLARATIONS_TOKENSET;

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
    PerlCallValue perlValue = null;
    if (parent instanceof PerlMethod) {
      perlValue = PerlCallValue.from(parent);
    }
    else if (MODIFIER_DECLARATIONS_TOKENSET.contains(PsiUtilCore.getElementType(parent))) {
      perlValue = PerlCallObjectValue.create(PerlPackageUtil.getContextNamespaceName(myElement),
                                             myElement.getText(),
                                             Collections.emptyList());
    }

    if (perlValue == null) {
      return ResolveResult.EMPTY_ARRAY;
    }

    List<PsiElement> relatedItems = new ArrayList<>();
    perlValue.processCallTargets(element, relatedItems::add);
    return getResolveResults(relatedItems).toArray(ResolveResult.EMPTY_ARRAY);
  }
}
