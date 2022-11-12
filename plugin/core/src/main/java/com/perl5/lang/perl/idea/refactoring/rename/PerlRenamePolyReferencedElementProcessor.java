/*
 * Copyright 2015-2022 Alexandr Evstigneev
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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.refactoring.rename.PsiElementRenameHandler;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.perl5.lang.perl.psi.PerlGlobVariableElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


public abstract class PerlRenamePolyReferencedElementProcessor extends RenamePsiElementProcessor {
  @Override
  public void prepareRenaming(@NotNull PsiElement element,
                              @NotNull String newName,
                              @NotNull Map<PsiElement, String> allRenames,
                              @NotNull SearchScope scope) {
    final String currentBaseName = ((PsiNameIdentifierOwner)element).getName();

    if (currentBaseName != null && StringUtil.isNotEmpty(newName)) {
      boolean globScanned = element instanceof PerlGlobVariableElement;

      for (PsiReference reference : ReferencesSearch.search(element, element.getUseScope()).findAll()) {
        if (reference instanceof PsiPolyVariantReference) {
          for (ResolveResult resolveResult : ((PsiPolyVariantReference)reference).multiResolve(false)) {
            PsiElement resolveResultElement = resolveResult.getElement();
            if (!allRenames.containsKey(resolveResultElement)) {
              allRenames.put(resolveResultElement, newName);
              if (!globScanned && resolveResultElement instanceof PerlGlobVariableElement) {
                globScanned = true;
                prepareRenaming(resolveResultElement, newName, allRenames, scope);
              }
            }
          }
        }
      }
    }
  }

  @Override
  public @Nullable PsiElement substituteElementToRename(@NotNull PsiElement element, @Nullable Editor editor) {
    if (PsiElementRenameHandler.isVetoed(element)) {
      return null;
    }
    return super.substituteElementToRename(element, editor);
  }
}
