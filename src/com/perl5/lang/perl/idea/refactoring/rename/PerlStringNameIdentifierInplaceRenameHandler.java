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

package com.perl5.lang.perl.idea.refactoring.rename;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.refactoring.rename.inplace.VariableInplaceRenameHandler;
import com.intellij.refactoring.rename.inplace.VariableInplaceRenamer;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.extensions.PerlRenameUsagesHelper;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 20.09.2015.
 */
public class PerlStringNameIdentifierInplaceRenameHandler extends VariableInplaceRenameHandler {
  @Nullable
  @Override
  protected VariableInplaceRenamer createRenamer(@NotNull PsiElement elementToRename, Editor editor) {
    return new PerlVariableInplaceRenamer((PsiNamedElement)elementToRename, editor);
  }

  @Override
  protected boolean isAvailable(PsiElement element, Editor editor, PsiFile file) {

    return
      editor.getSettings().isVariableInplaceRenameEnabled()
      && element instanceof PsiNameIdentifierOwner
      && !(element instanceof PerlRenameUsagesHelper)
      && element.getUseScope() instanceof LocalSearchScope
      && element.getLanguage() == PerlLanguage.INSTANCE
      && ((PsiNameIdentifierOwner)element).getNameIdentifier() instanceof PerlStringContentElement
      && element.getContainingFile().getViewProvider().getAllFiles().size() < 2
      ;
  }
}