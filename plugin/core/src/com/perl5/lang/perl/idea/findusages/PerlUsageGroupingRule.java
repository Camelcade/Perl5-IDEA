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

package com.perl5.lang.perl.idea.findusages;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.usages.*;
import com.intellij.usages.rules.PsiElementUsage;
import com.intellij.usages.rules.SingleParentUsageGroupingRule;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.ui.breadcrumbs.PerlBreadcrumbsProvider;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class PerlUsageGroupingRule extends SingleParentUsageGroupingRule {
  @Nullable
  @Override
  protected UsageGroup getParentGroupFor(@NotNull Usage usage, @NotNull UsageTarget[] targets) {
    if (!(usage instanceof PsiElementUsage)) {
      return null;
    }
    PsiElement element = ((PsiElementUsage)usage).getElement();
    if (element == null || !element.getLanguage().isKindOf(PerlLanguage.INSTANCE)) {
      return null;
    }
    return computeParentGroupFor(element);
  }

  @Nullable
  private UsageGroup computeParentGroupFor(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    PsiElement structuralParentElement = PerlBreadcrumbsProvider.getStructuralParentElement(element);

    if (structuralParentElement instanceof PerlFile) {
      structuralParentElement = ((PerlFile)structuralParentElement).getNamespaceDefinitionElement();
    }

    if (structuralParentElement == null) {
      return null;
    }

    if (structuralParentElement instanceof PerlSubDefinitionElement) {
      String name = StringUtil.notNullize(((PerlSubDefinitionElement)structuralParentElement).getCanonicalName());
      return new PsiNamedElementUsageGroupBase<PerlSubDefinitionElement>((PerlSubDefinitionElement)structuralParentElement) {
        @NotNull
        @Override
        public String getText(UsageView view) {
          return name;
        }
      };
    }

    if (structuralParentElement instanceof PerlNamespaceDefinitionElement) {
      return new PsiNamedElementUsageGroupBase<>((PerlNamespaceDefinitionElement)structuralParentElement);
    }

    return computeParentGroupFor(structuralParentElement.getParent());
  }
}
