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

package com.perl5.lang.htmlmason.parser.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.psi.PerlString;
import org.jetbrains.annotations.NotNull;


public class HTMLMasonComponentParentReference extends HTMLMasonStringReference {

  public HTMLMasonComponentParentReference(@NotNull PerlString element, TextRange textRange) {
    super(element, textRange);
  }


  @Override
  public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
    return myElement;
  }

  @Override
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    ResolveCache.getInstance(element.getProject()).clearCache(true);
    return myElement;
  }

  @NotNull
  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    PsiFile psiFile = getElement().getContainingFile();

    if (psiFile instanceof HTMLMasonFileImpl) {
      PsiFile parentComponent = ((HTMLMasonFileImpl)psiFile).getParentComponent();
      if (parentComponent != null) {
        return new ResolveResult[]{new PsiElementResolveResult(parentComponent)};
      }
    }

    return ResolveResult.EMPTY_ARRAY;
  }
}