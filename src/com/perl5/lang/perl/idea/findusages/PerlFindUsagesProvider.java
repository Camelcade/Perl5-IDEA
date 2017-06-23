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

package com.perl5.lang.perl.idea.findusages;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 24.05.2015.
 */
public class PerlFindUsagesProvider implements FindUsagesProvider {

  public PerlFindUsagesProvider() {
    super();
  }

  @Nullable
  @Override
  public WordsScanner getWordsScanner() {
    return new PerlWordsScanner();
  }

  @Override
  public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
    return psiElement instanceof PsiNamedElement;
  }

  @Nullable
  @Override
  public String getHelpId(@NotNull PsiElement psiElement) {
    return null;
  }

  @NotNull
  @Override
  public String getType(@NotNull PsiElement element) {
    if (element instanceof PerlSubNameElement) {
      return "sub";
    }
    else if (element instanceof PerlNamespaceElement) {
      return "package";
    }
    else {
      return "Unknown element type: " + element.toString();
    }
  }

  @NotNull
  @Override
  public String getDescriptiveName(@NotNull PsiElement element) {
    if (element instanceof PsiNamedElement) {
      String name = ((PsiNamedElement)element).getName();

      if (name != null) {
        return name;
      }
    }

    return "Unknown descriptive name: " + element.toString();
  }

  @NotNull
  @Override
  public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
    if (element instanceof PsiNamedElement) {
      String name = ((PsiNamedElement)element).getName();

      if (name != null) {
        return name;
      }
    }

    return "Unknown node text: " + element.toString();
  }
}
