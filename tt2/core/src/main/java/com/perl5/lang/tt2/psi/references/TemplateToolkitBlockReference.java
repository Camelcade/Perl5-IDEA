/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.tt2.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.references.PerlCachingReference;
import com.perl5.lang.tt2.psi.TemplateToolkitNamedBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class TemplateToolkitBlockReference extends PerlCachingReference<PsiElement> {
  public TemplateToolkitBlockReference(PsiElement psiElement) {
    super(psiElement);
  }

  @Override
  protected @NotNull ResolveResult[] resolveInner(boolean incompleteCode) {
    PsiElement element = getElement();

    TextRange range = ElementManipulators.getValueTextRange(element);

    final CharSequence targetName = range.subSequence(element.getText());
    if (StringUtil.isEmpty(targetName)) {
      return ResolveResult.EMPTY_ARRAY;
    }

    final List<ResolveResult> result = new ArrayList<>();

    PsiTreeUtil.processElements(element.getContainingFile(), element1 ->
    {
      if (element1 instanceof TemplateToolkitNamedBlock && StringUtil.equals(((TemplateToolkitNamedBlock)element1).getName(), targetName)) {
        result.add(new PsiElementResolveResult(element1));
      }
      return true;
    });

    return result.toArray(ResolveResult.EMPTY_ARRAY);
  }
}
