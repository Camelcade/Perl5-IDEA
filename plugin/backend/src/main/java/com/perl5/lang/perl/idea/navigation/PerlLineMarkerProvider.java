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

package com.perl5.lang.perl.idea.navigation;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class PerlLineMarkerProvider extends RelatedItemLineMarkerProvider implements PerlElementTypes {
  @Override
  protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
    switch (element) {
      case PerlNamespaceDefinitionWithIdentifier namespaceDefinition -> addNamespaceMarkers(namespaceDefinition, result);
      case PerlSubDefinitionElement subDefinition when subDefinition.isMethod() -> addSubDefinitionsMarkers(subDefinition, result);
      case PerlPolyNamedElement<?> polyNamedElement -> {
        for (PerlDelegatingLightNamedElement<?> lightNamedElement : polyNamedElement.getLightElements()) {
          if (lightNamedElement instanceof PerlNamespaceDefinitionWithIdentifier namespaceDefinition) {
            addNamespaceMarkers(namespaceDefinition, result);
          }
          else if (lightNamedElement instanceof PerlSubDefinitionElement subDefinition && subDefinition.isMethod()) {
            addSubDefinitionsMarkers(subDefinition, result);
          }
        }
      }
      default -> {
      }
    }
  }

  private void addNamespaceMarkers(@NotNull PerlNamespaceDefinitionWithIdentifier element,
                                   Collection<? super RelatedItemLineMarkerInfo<?>> result) {
    PsiElement nameIdentifier = element.getNameIdentifier();
    if (nameIdentifier == null) {
      nameIdentifier = element;
    }

    List<PerlNamespaceDefinitionElement> parentNamespaces =
      PerlNamespaceDefinitionHandler.instance(element).getParentNamespaceDefinitions(element);
    if (!parentNamespaces.isEmpty()) {
      NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
        .create(AllIcons.Gutter.ImplementingMethod)
        .setTargets(parentNamespaces)
        .setTooltipText(PerlBundle.message("tooltip.parent.classes"));

      result.add(getMarkerInfo(builder, nameIdentifier));
    }

    Collection<PerlNamespaceDefinitionElement> childNamespaces =
      PerlNamespaceDefinitionHandler.instance(element).getChildNamespaceDefinitions(element);
    if (!childNamespaces.isEmpty()) {
      NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
        .create(AllIcons.Gutter.ImplementedMethod)
        .setTargets(childNamespaces)
        .setTooltipText(PerlBundle.message("tooltip.subclasses"));

      result.add(getMarkerInfo(builder, nameIdentifier));
    }
  }

  private RelatedItemLineMarkerInfo<?> getMarkerInfo(@NotNull NavigationGutterIconBuilder<PsiElement> builder,
                                                     @NotNull PsiElement element) {
    while (element.getFirstChild() != null) {
      element = element.getFirstChild();
    }
    return builder.createLineMarkerInfo(element);
  }

  private void addSubDefinitionsMarkers(@NotNull PerlSubDefinitionElement subElement,
                                        Collection<? super RelatedItemLineMarkerInfo<?>> result) {
    PerlNamespaceDefinitionElement containingNamespace = PsiTreeUtil.getParentOfType(subElement, PerlNamespaceDefinitionElement.class);
    if (containingNamespace != null) {
      PsiElement nameIdentifier = subElement.getNameIdentifier();
      if (nameIdentifier == null) {
        nameIdentifier = subElement;
      }

      @Nullable PerlSubElement parentSub = PerlSubUtil.getDirectSuperMethod(subElement);

      if (parentSub != null) {
        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
          .create(AllIcons.Gutter.OverridingMethod)
          .setTarget(parentSub)
          .setTooltipText(PerlBundle.message("tooltip.overriding.method"));

        result.add(getMarkerInfo(builder, nameIdentifier));
      }

      List<PerlSubElement> overridingSubs = new ArrayList<>();
      PerlSubUtil.processDirectOverridingSubs(subElement, (Processor<? super PerlSubDefinitionElement>)overridingSubs::add);
      if (!overridingSubs.isEmpty()) {
        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
          .create(AllIcons.Gutter.OverridenMethod)
          .setTargets(overridingSubs)
          .setTooltipText(PerlBundle.message("tooltip.overridden.methods"));

        result.add(getMarkerInfo(builder, nameIdentifier));
      }
    }
  }
}
