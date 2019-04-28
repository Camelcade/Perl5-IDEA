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

package com.perl5.lang.pod.parser.psi;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageViewLongNameLocation;
import com.intellij.usageView.UsageViewShortNameLocation;
import com.intellij.usageView.UsageViewTypeLocation;
import com.perl5.lang.pod.PodLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PodElementDescriptionProvider implements ElementDescriptionProvider {
  private static final Logger LOG = Logger.getInstance(PodElementDescriptionProvider.class);
  @Nullable
  @Override
  public String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location) {
    if (!element.getLanguage().isKindOf(PodLanguage.INSTANCE)) {
      return null;
    }
    if (element instanceof PodCompositeElement) {
      if (location == UsageViewShortNameLocation.INSTANCE) {
        return ((PodCompositeElement)element).getUsageViewShortNameLocation();
      }
      else if (location == UsageViewLongNameLocation.INSTANCE) {
        return ((PodCompositeElement)element).getUsageViewLongNameLocation();
      }
      else if (location == UsageViewTypeLocation.INSTANCE) {
        return ((PodCompositeElement)element).getUsageViewTypeLocation();
      }
    }
    LOG.warn("Unresolved " + element + " in " + location);
    return null;
  }
}
