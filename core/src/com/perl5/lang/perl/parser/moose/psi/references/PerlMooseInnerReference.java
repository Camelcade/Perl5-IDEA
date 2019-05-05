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

package com.perl5.lang.perl.parser.moose.psi.references;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseAugmentStatement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.references.PerlCachingReference;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class PerlMooseInnerReference extends PerlCachingReference<PsiElement> {

  public PerlMooseInnerReference(PsiElement psiElement) {
    super(psiElement);
  }

  @Override
  public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
    return myElement;
  }

  @NotNull
  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    List<ResolveResult> result = new ArrayList<>();
    PsiElement element = getElement();

    String subName = null;
    PerlSubDefinitionElement subDefinitionBase = PsiTreeUtil.getParentOfType(element, PerlSubDefinitionElement.class);

    if (subDefinitionBase != null) {
      subName = subDefinitionBase.getSubName();
    }

    PerlMooseAugmentStatement augmentStatement = PsiTreeUtil.getParentOfType(element, PerlMooseAugmentStatement.class);

    if (augmentStatement != null) {
      subName = augmentStatement.getSubName();
    }

    if (subName != null) {
      PerlNamespaceDefinitionElement namespaceDefinition = PsiTreeUtil.getParentOfType(element, PerlNamespaceDefinitionElement.class);
      Set<PerlNamespaceDefinitionElement> recursionSet = new THashSet<>();

      if (StringUtil.isNotEmpty(subName) && namespaceDefinition != null) {
        collectNamespaceMethodsAugmentations(namespaceDefinition, subName, recursionSet, result);
      }
    }

    return result.toArray(new ResolveResult[result.size()]);
  }

  protected void collectNamespaceMethodsAugmentations(@NotNull PerlNamespaceDefinitionElement namespaceDefinition,
                                                      @NotNull String subName,
                                                      Set<PerlNamespaceDefinitionElement> recursionSet,
                                                      List<ResolveResult> result) {
    recursionSet.add(namespaceDefinition);

    for (PerlNamespaceDefinitionElement childNamespace : namespaceDefinition.getChildNamespaceDefinitions()) {
      if (!recursionSet.contains(childNamespace)) {
        boolean noSubclasses = false;

        for (PsiElement augmentStatement : getAugmentStatements(childNamespace)) {
          if (subName.equals(((PerlMooseAugmentStatement)augmentStatement).getSubName())) {
            result.add(new PsiElementResolveResult(augmentStatement));
            noSubclasses = true;
          }
        }

        if (!noSubclasses) {
          collectNamespaceMethodsAugmentations(childNamespace, subName, recursionSet, result);
        }
      }
    }
  }

  private static List<PsiElement> getAugmentStatements(@NotNull final PsiElement childNamespace) {
    return CachedValuesManager.getCachedValue(childNamespace,
                                              () -> CachedValueProvider.Result.create(PerlPsiUtil.collectNamespaceMembers(childNamespace,
                                                                                                                          PerlMooseAugmentStatement.class),
                                                                                      childNamespace));
  }
}
