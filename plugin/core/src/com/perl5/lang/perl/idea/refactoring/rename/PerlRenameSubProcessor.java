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
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiSearchScopeUtil;
import com.intellij.psi.search.SearchScope;
import com.intellij.refactoring.rename.PsiElementRenameHandler;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.parser.Class.Accessor.psi.impl.PerlClassAccessorMethod;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PerlRenameSubProcessor extends RenamePsiElementProcessor {
  @Override
  public boolean canProcessElement(@NotNull PsiElement element) {
    return element instanceof PerlSubElement;
  }

  @Override
  public void prepareRenaming(@NotNull PsiElement element,
                              @NotNull String newName,
                              @NotNull Map<PsiElement, String> allRenames,
                              @NotNull SearchScope scope) {
    assert element instanceof PerlSubElement;
    PerlSubElement subElement = (PerlSubElement)element;
    for (PsiElement relatedItem : computeRelatedItems(subElement)) {
      if (PsiSearchScopeUtil.isInScope(scope, relatedItem)) {
        allRenames.put(relatedItem, newName);
      }
    }
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
      PerlBundle.message("perl.sub.rename.dialog.message", canonicalName),
      PerlBundle.message("perl.sub.rename.dialog.title"),
      PerlBundle.message("perl.sub.rename.dialog.rename.super"),
      PerlBundle.message("perl.sub.rename.dialog.rename.current"),
      PerlIcons.PERL_LANGUAGE_ICON);

    return dialogResult == Messages.OK ? topLevelSuperMethod : subBase;
  }

  /**
   * @return related items for sub declaration or definition. All synonymous definitions, declarations and typeglobs from
   * the {@link GlobalSearchScope#allScope(com.intellij.openapi.project.Project) all scope}
   */
  @NotNull
  public static Set<PsiElement> computeRelatedItems(@NotNull PerlSubElement subElement) {
    String canonicalName = subElement.getCanonicalName();
    if (StringUtil.isEmpty(canonicalName)) {
      return Collections.emptySet();
    }
    Set<PsiElement> relatedItems = new HashSet<>();
    PerlSubUtil
      .processRelatedItems(subElement.getProject(), GlobalSearchScope.allScope(subElement.getProject()), canonicalName, relatedItems::add);

    for (PsiElement relatedItem : new ArrayList<>(relatedItems)) {
      if (relatedItem instanceof PerlSubElement && ((PerlSubElement)relatedItem).isMethod()) {
        relatedItems.addAll(PerlSubUtil.collectOverridingSubs((PerlSubElement)relatedItem));
      }
      // following is the hack until #1730 is fixed
      if (!(relatedItem instanceof PerlClassAccessorMethod) ||
          !relatedItems.contains(((PerlClassAccessorMethod)relatedItem).getPairedMethod())) {
        relatedItems.add(relatedItem);
      }
    }
    return relatedItems;
  }
}
