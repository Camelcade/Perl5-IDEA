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

package com.perl5.lang.pod.idea.ui.breadcrumbs;

import com.intellij.lang.Language;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.parser.psi.PodSectionItem;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public class PodBreadCrumbsProvider implements BreadcrumbsProvider {
  @Override
  public Language[] getLanguages() {
    return PodLanguage.ARRAY;
  }

  @Override
  public boolean acceptElement(@NotNull PsiElement element) {
    return element instanceof PodTitledSection && StringUtil.isNotEmpty(((PodTitledSection)element).getTitleText()) &&
           !(element instanceof PodSectionItem && ((PodSectionItem)element).isBulleted());
  }

  @Nullable
  @Override
  public Icon getElementIcon(@NotNull PsiElement element) {
    return element.getIcon(0);
  }

  @NotNull
  @Override
  public String getElementInfo(@NotNull PsiElement element) {
    if (element instanceof PodTitledSection) {
      return Objects.requireNonNull(((PodTitledSection)element).getTitleText());
    }

    throw new RuntimeException();
  }
}
