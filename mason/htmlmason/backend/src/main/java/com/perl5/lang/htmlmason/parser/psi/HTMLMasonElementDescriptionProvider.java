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

package com.perl5.lang.htmlmason.parser.psi;

import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.usageView.UsageViewLongNameLocation;
import com.intellij.usageView.UsageViewNodeTextLocation;
import com.intellij.usageView.UsageViewShortNameLocation;
import com.intellij.usageView.UsageViewTypeLocation;
import com.perl5.lang.htmlmason.HTMLMasonUtil;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class HTMLMasonElementDescriptionProvider implements ElementDescriptionProvider {
  @Override
  public @Nullable String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location) {
    if (!(element instanceof HTMLMasonNamedElement namedElement)) {
      return null;
    }
    if (location == UsageViewLongNameLocation.INSTANCE) {
      return HTMLMasonUtil.getAbsoluteComponentPath(((HTMLMasonFileImpl)element.getContainingFile())) +
             ":" +
             ((PsiNamedElement)element).getName();
    }
    else if (location == UsageViewNodeTextLocation.INSTANCE) {
      return namedElement.getName() + HTMLMasonUtil.getArgumentsListAsString((HTMLMasonParametrizedEntity)element);
    }
    else if (location == UsageViewShortNameLocation.INSTANCE) {
      return namedElement.getName() + HTMLMasonUtil.getArgumentsListAsString((HTMLMasonParametrizedEntity)element);
    }
    else if (element instanceof HTMLMasonMethodDefinition) {
      if (location == UsageViewTypeLocation.INSTANCE) {
        return "HTML::Mason method";
      }
      return "HTML::Mason method for " + location.getClass().getSimpleName();
    }
    else if (element instanceof HTMLMasonSubcomponentDefitnition) {
      if (location == UsageViewTypeLocation.INSTANCE) {
        return "HTML::Mason subcomponent";
      }
      return "HTML::Mason subcomponent for " + location.getClass().getSimpleName();
    }
    return null;
  }
}
