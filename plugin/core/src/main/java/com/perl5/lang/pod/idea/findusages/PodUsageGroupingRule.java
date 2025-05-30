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

package com.perl5.lang.pod.idea.findusages;

import com.intellij.psi.PsiElement;
import com.intellij.usages.PsiNamedElementUsageGroupBase;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageGroup;
import com.intellij.usages.UsageTarget;
import com.intellij.usages.rules.PsiElementUsage;
import com.intellij.usages.rules.SingleParentUsageGroupingRule;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.idea.ui.breadcrumbs.PodBreadCrumbsProvider;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PodUsageGroupingRule extends SingleParentUsageGroupingRule {
  @Override
  protected @Nullable UsageGroup getParentGroupFor(@NotNull Usage usage, UsageTarget @NotNull [] targets) {
    if (!(usage instanceof PsiElementUsage psiElementUsage)) {
      return null;
    }
    PsiElement element = psiElementUsage.getElement();
    if (element == null || !element.getLanguage().isKindOf(PodLanguage.INSTANCE)) {
      return null;
    }
    PsiElement structuralParentElement = PodBreadCrumbsProvider.getStructuralParentElement(element);
    if (structuralParentElement instanceof PodTitledSection podTitledSection) {
      return new PsiNamedElementUsageGroupBase<>(podTitledSection);
    }
    return null;
  }
}
