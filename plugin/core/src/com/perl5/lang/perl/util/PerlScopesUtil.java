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

package com.perl5.lang.perl.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.PsiFileEx;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

public final class PerlScopesUtil {
  private PerlScopesUtil() {
  }

  /**
   * @return a global search scope for the psi element excluding current file, if we can detect that current file has AST
   */
  public static @NotNull GlobalSearchScope allScopeWithoutCurrentWithAst(@NotNull PsiElement psiElement) {
    GlobalSearchScope allScope = GlobalSearchScope.allScope(psiElement.getProject());
    PsiFile originalFile = psiElement.getContainingFile().getOriginalFile();
    if (originalFile instanceof PsiFileEx && !((PsiFileEx)originalFile).isContentsLoaded()) {
      return allScope;
    }
    return allScope.intersectWith(GlobalSearchScope.notScope(GlobalSearchScope.fileScope(psiElement.getContainingFile())));
  }
}
