/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.extensions.parser;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 22.02.2016.
 */
public class PerlRuntimeParentsProviderFromArray implements PerlRuntimeParentsProvider {
  private final PsiElement myStringListContainer;

  public PerlRuntimeParentsProviderFromArray(@NotNull PsiElement stringListContainer) {
    this.myStringListContainer = stringListContainer;
  }

  @Override
  public void changeParentsList(@NotNull List<String> currentList) {
    PsiElement firstChild = myStringListContainer.getFirstChild();
    if (firstChild != null) {
      currentList.clear();
      for (PsiElement psiElement : PerlPsiUtil.collectStringElements(firstChild)) {
        currentList.add(psiElement.getText());
      }
    }
  }
}
