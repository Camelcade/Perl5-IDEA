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

package com.perl5.lang.perl.idea.refactoring.rename;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.rename.PsiElementRenameHandler;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.parser.Class.Accessor.psi.impl.PerlClassAccessorMethod;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.util.PerlSubUtil;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;


public abstract class PerlRenamePolyReferencedElementProcessor extends RenamePsiElementProcessor {
  @Override
  public void prepareRenaming(@NotNull PsiElement element,
                              @NotNull String newName,
                              @NotNull Map<PsiElement, String> allRenames,
                              @NotNull SearchScope scope) {
    final String currentBaseName = ((PsiNameIdentifierOwner)element).getName();

    if (currentBaseName != null && StringUtil.isNotEmpty(newName)) {
      boolean globScanned = element instanceof PerlGlobVariable;

      for (PsiReference reference : ReferencesSearch.search(element, element.getUseScope()).findAll()) {
        if (reference instanceof PsiPolyVariantReference) {
          for (ResolveResult resolveResult : ((PsiPolyVariantReference)reference).multiResolve(false)) {
            PsiElement resolveResultElement = resolveResult.getElement();
            if (!allRenames.containsKey(resolveResultElement)) {
              allRenames.put(resolveResultElement, newName);
              if (!globScanned && resolveResultElement instanceof PerlGlobVariable) {
                globScanned = true;
                prepareRenaming(resolveResultElement, newName, allRenames, scope);
              }
            }
          }
        }
        processDocReference(currentBaseName, newName, reference, allRenames);
      }

      if (element instanceof PerlSubElement && ((PerlSubElement)element).isMethod()) {
        for (PerlSubElement overridingSub : PerlSubUtil.collectOverridingSubs((PerlSubElement)element)) {
          allRenames.put(overridingSub, newName);
        }
      }

      // following is the hack until #1730 is fixed
      Set<PsiElement> allElements = new THashSet<>(allRenames.keySet());
      allElements.stream()
        .filter(e -> e instanceof PerlClassAccessorMethod)
        .forEach(e -> {
          PerlClassAccessorMethod pairedMethod = ((PerlClassAccessorMethod)e).getPairedMethod();
          if (pairedMethod != null && allRenames.containsKey(e)) {
            allRenames.remove(pairedMethod);
          }
        });

    }
  }

  private void processDocReference(String currentBaseName, String newName, PsiReference reference, Map<PsiElement, String> allRenames) {
    PsiElement sourceElement = reference.getElement();
    if (!sourceElement.getLanguage().isKindOf(PodLanguage.INSTANCE)) {
      return;
    }
    PodTitledSection titledSection = PsiTreeUtil.getParentOfType(sourceElement, PodTitledSection.class);
    if (titledSection == null) {
      return;
    }
    PsiElement titleElement = titledSection.getTitleElement();
    if (titleElement == null || !titleElement.getTextRange().contains(sourceElement.getTextRange())) {
      return;
    }

    String titleText = titledSection.getTitleText();
    if (titleText == null || !titleText.startsWith(currentBaseName)) {
      return;
    }

    allRenames.put(titledSection, titleText.replace(currentBaseName, newName));
  }

  @Nullable
  @Override
  public PsiElement substituteElementToRename(@NotNull PsiElement element, @Nullable Editor editor) {
    if (PsiElementRenameHandler.isVetoed(element)) {
      return null;
    }

    if (element instanceof PerlSubElement && ((PerlSubElement)element).isMethod()) {
      return suggestSuperMethod((PerlSubElement)element);
    }

    return super.substituteElementToRename(element, editor);
  }

  @NotNull
  private PsiElement suggestSuperMethod(@NotNull PerlSubElement subBase) {
    PerlSubElement topLevelSuperMethod = subBase.getTopmostSuperMethod();
    String canonicalName = topLevelSuperMethod.getCanonicalName();

    if (topLevelSuperMethod == subBase || canonicalName == null) {
      return subBase;
    }

    int dialogResult = Messages.showOkCancelDialog(
      "This method overrides SUPER method: " + canonicalName + ".",
      "Method Rename",
      "Rename SUPER method",
      "Rename this one",
      PerlIcons.PERL_LANGUAGE_ICON);

    return dialogResult == Messages.OK ? topLevelSuperMethod : subBase;
  }
}
