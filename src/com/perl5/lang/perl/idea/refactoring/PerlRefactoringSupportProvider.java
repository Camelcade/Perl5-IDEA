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

package com.perl5.lang.perl.idea.refactoring;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.extensions.PerlRenameUsagesSubstitutor;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 23.05.2015.
 * This class is responsible for controlling refactoring process
 */
public class PerlRefactoringSupportProvider extends RefactoringSupportProvider {
  // todo RenameInputValidator
  @Override
  public boolean isInplaceRenameAvailable(@NotNull PsiElement element, PsiElement context) {
    SearchScope useScope = element.getUseScope();
    return useScope instanceof LocalSearchScope
           && element instanceof PsiNameIdentifierOwner
           && !(element instanceof PerlRenameUsagesSubstitutor)
           && !(((PsiNameIdentifierOwner)element).getNameIdentifier() instanceof PerlStringContentElement)
           && element.getContainingFile().getLanguage() == PerlLanguage.INSTANCE
           && !(((LocalSearchScope)useScope).getScope()[0] instanceof PsiFile)
      ;
  }

  @Override
  public boolean isMemberInplaceRenameAvailable(@NotNull PsiElement element, PsiElement context) {
    return false;
  }

  @Override
  public boolean isSafeDeleteAvailable(@NotNull PsiElement element) {
    return false;
  }
}
