/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.refactoring.rename.inplace.MemberInplaceRenamer;
import com.perl5.lang.perl.parser.PerlIdentifierRangeProvider;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * fixme shouldn't we extend PerlVariableInplaceRenamer?
 */
public class PerlMemberInplaceRenamer extends MemberInplaceRenamer {
  public PerlMemberInplaceRenamer(@NotNull PsiNamedElement elementToRename, PsiElement substituted, Editor editor) {
    super(elementToRename, substituted, editor);
  }

  @Override
  protected boolean notSameFile(@Nullable VirtualFile file, @NotNull PsiFile containingFile) {
    final PsiFile currentFile = PsiDocumentManager.getInstance(myProject).getPsiFile(myEditor.getDocument());
    if (currentFile == null) {
      return true;
    }
    InjectedLanguageManager manager = InjectedLanguageManager.getInstance(containingFile.getProject());
    PsiFile topLevelFile = manager.getTopLevelFile(containingFile);
    PsiFile topLevelFile1 = manager.getTopLevelFile(currentFile);
    return topLevelFile == null || topLevelFile1 == null || topLevelFile.getViewProvider() != topLevelFile1.getViewProvider();
  }

  @Override
  protected @NotNull TextRange getRangeToRename(@NotNull PsiElement element) {
    PsiElement namedElement = TargetElementUtil.getInstance().getNamedElement(element, 0);
    return namedElement instanceof PerlIdentifierRangeProvider rangeProvider
      ? rangeProvider.getRangeInIdentifier()
           : ElementManipulators.getValueTextRange(element);
  }

  @Override
  protected Collection<PsiReference> collectRefs(SearchScope referencesSearchScope) {
    Set<PsiReference> references = new HashSet<>(super.collectRefs(referencesSearchScope));

    if (myElementToRename instanceof PerlSubElement subElement) {
      for (PsiElement relatedItem : PerlRenameSubProcessor.computeRelatedItems(subElement)) {
        if (!relatedItem.equals(myElementToRename)) {
          references.addAll(ReferencesSearch.search(relatedItem, referencesSearchScope).findAll());
        }
      }
    }

    return references;
  }

  @Override
  protected @Nullable PsiNamedElement getVariable() {
    PsiNamedElement variable = super.getVariable();
    if (variable != null) {
      return variable;
    }

    if (!(myElementToRename instanceof PerlDelegatingLightNamedElement)) {
      return null;
    }

    PsiFile psiFile = PsiDocumentManager.getInstance(myProject).getPsiFile(myEditor.getDocument());
    if (psiFile == null) {
      return null;
    }

    PsiElement psiLeaf = psiFile.getViewProvider().findElementAt(myRenameOffset.getStartOffset(), myElementToRename.getLanguage());
    if (psiLeaf == null) {
      return null;
    }

    PsiElement namedElement = TargetElementUtil.getInstance().getNamedElement(psiLeaf, 0);
    if (namedElement instanceof PerlDelegatingLightNamedElement) {
      return (PsiNamedElement)namedElement;
    }

    return null;
  }
}
