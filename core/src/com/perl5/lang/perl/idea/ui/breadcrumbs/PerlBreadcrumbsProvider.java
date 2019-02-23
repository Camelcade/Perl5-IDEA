/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.ui.breadcrumbs;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.properties.PerlSubOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PerlBreadcrumbsProvider implements BreadcrumbsProvider {

  @Override
  public Language[] getLanguages() {
    return PerlLanguage.ARRAY;
  }

  @Override
  public boolean acceptElement(@NotNull PsiElement element) {
    return element instanceof PerlFile ||
           element instanceof PerlSubDefinitionElement && ((PerlSubDefinitionElement)element).getSubName() != null ||
           element instanceof PerlSubExpr && (!(element.getParent() instanceof PerlSubOwner)) ||
           element instanceof PerlNamespaceDefinitionElement && ((PerlNamespaceDefinitionElement)element).getPackageName() != null;
  }

  @Nullable
  @Override
  public PsiElement getParent(@NotNull PsiElement element) {
    PsiElement structuralParent = getStructuralParentElement(element);
    return structuralParent instanceof PsiFile ? null : structuralParent;
  }

  @Nullable
  public static PsiElement getStructuralParentElement(@NotNull PsiElement element) {
    if (element instanceof PerlFile) {
      return null;
    }
    if (element instanceof PerlNamespaceDefinitionElement) {
      return element.getContainingFile();
    }
    else if (element instanceof PerlSubDefinitionElement) {
      return PsiTreeUtil.getParentOfType(element, PerlNamespaceDefinitionElement.class);
    }

    PsiElement nearestParent =
      PsiTreeUtil.getParentOfType(element, PerlSubDefinitionElement.class, PerlNamespaceDefinitionElement.class, PerlSubExpr.class);
    if (nearestParent == null ||
        nearestParent instanceof PerlSubDefinitionElement ||
        nearestParent instanceof PerlNamespaceDefinitionElement) {
      return nearestParent;
    }

    if (nearestParent.getParent() instanceof PerlSubOwner) {
      return getStructuralParentElement(nearestParent.getParent());
    }

    PsiPerlBlock exprBlock = ((PerlSubExpr)nearestParent).getBlock();
    if (exprBlock == null) {
      return getStructuralParentElement(nearestParent.getParent());
    }

    PerlPolyNamedElement polyNamedElement = PsiTreeUtil.getParentOfType(nearestParent, PerlPolyNamedElement.class);
    if (polyNamedElement != null) {
      for (PerlDelegatingLightNamedElement lightNamedElement : polyNamedElement.getLightElements()) {
        if (lightNamedElement instanceof PerlSubDefinitionElement &&
            exprBlock.equals(((PerlSubDefinitionElement)lightNamedElement).getSubDefinitionBody())) {
          return lightNamedElement;
        }
      }
    }
    return nearestParent;
  }

  @Nullable
  @Override
  public Icon getElementIcon(@NotNull PsiElement element) {
    return element.getIcon(0);
  }

  @NotNull
  @Override
  public String getElementInfo(@NotNull PsiElement element) {
    if (element instanceof PerlFile) {
      return ((PerlFile)element).getName();
    }
    else if (element instanceof PerlSubDefinitionElement) {
      return ((PerlSubDefinition)element).getSubName() + "()";
    }
    else if (element instanceof PerlNamespaceDefinitionElement) {
      return ((PerlNamespaceDefinition)element).getPackageName();
    }
    else if (element instanceof PerlSubExpr) {
      return "sub()";
    }

    throw new RuntimeException("Can't happen: " + element);
  }
}
