/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.idea.navigation;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.htmlmason.HtmlMasonBundle;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonMethodDefinition;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;


public class HTMLMasonLineMarkerProvider extends RelatedItemLineMarkerProvider {
  @Override
  protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
    if (element instanceof HTMLMasonFileImpl htmlMasonFile) {
      HTMLMasonFileImpl parentComponent = htmlMasonFile.getParentComponent();
      if (parentComponent != null) {
        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
          .create(AllIcons.Gutter.ImplementingMethod)
          .setTargets(parentComponent)
          .setTooltipText(HtmlMasonBundle.message("tooltip.parent.component"));

        result.add(builder.createLineMarkerInfo(element));
      }

      List<HTMLMasonFileImpl> childComponents = htmlMasonFile.getChildComponents();
      if (!childComponents.isEmpty()) {
        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
          .create(AllIcons.Gutter.ImplementedMethod)
          .setTargets(childComponents)
          .setTooltipText(HtmlMasonBundle.message("tooltip.child.components"));

        result.add(builder.createLineMarkerInfo(element));
      }
    }
    else if (element instanceof HTMLMasonMethodDefinition masonMethodDefinition) {
      String methodName = masonMethodDefinition.getName();
      PsiFile component = element.getContainingFile();
      var identifier = masonMethodDefinition.getNameIdentifier();
      var markerTarget = identifier == null ? element.getFirstChild() : identifier;

      if (StringUtil.isNotEmpty(methodName) && component instanceof HTMLMasonFileImpl htmlMasonFile) {
        // method in parent components
        HTMLMasonMethodDefinition methodDefinition = htmlMasonFile.findMethodDefinitionByNameInParents(methodName);
        if (methodDefinition != null) {
          NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
            .create(AllIcons.Gutter.OverridingMethod)
            .setTargets(methodDefinition)
            .setTooltipText(HtmlMasonBundle.message("tooltip.overriding.method"));

          result.add(builder.createLineMarkerInfo(markerTarget));
        }

        // method in subcomponents
        List<HTMLMasonMethodDefinition> methodDefinitions =
          htmlMasonFile.findMethodDefinitionByNameInChildComponents(methodName);
        if (!methodDefinitions.isEmpty()) {
          NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
            .create(AllIcons.Gutter.OverridenMethod)
            .setTargets(methodDefinitions)
            .setTooltipText(HtmlMasonBundle.message("tooltip.overridden.methods"));

          result.add(builder.createLineMarkerInfo(markerTarget));
        }
      }
    }
  }
}
