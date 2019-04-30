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

package com.perl5.lang.perl.idea.findusages;

import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.usageView.UsageViewNodeTextLocation;
import com.intellij.usageView.UsageViewUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlFindUsagesProviderBase implements FindUsagesProvider {
  private static final Logger LOG = Logger.getInstance(PerlFindUsagesProviderBase.class);

  @Override
  public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
    return psiElement instanceof PsiNamedElement;
  }

  @Nullable
  @Override
  public String getHelpId(@NotNull PsiElement psiElement) {
    return null;
  }

  /**
   * @implNote invoked if {@link ElementDescriptionProvider} failed to resolve {@link com.intellij.usageView.UsageViewTypeLocation#INSTANCE}
   * @see ElementDescriptionProvider#getElementDescription(com.intellij.psi.PsiElement, com.intellij.psi.ElementDescriptionLocation)
   */
  @NotNull
  @Override
  public String getType(@NotNull PsiElement element) {
    return "";
  }

  @NotNull
  @Override
  public String getDescriptiveName(@NotNull PsiElement element) {
    return StringUtil.notNullize(UsageViewUtil.getLongName(element));
  }

  /**
   * @implNote invoked if {@link ElementDescriptionProvider} failed to resolve {@link UsageViewNodeTextLocation#INSTANCE}
   * @see UsageViewNodeTextLocation#DEFAULT_PROVIDER
   */
  @NotNull
  @Override
  public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
    LOG.warn("Unhandled element " + element);
    return "Unhandled element " + element;
  }
}
