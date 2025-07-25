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

package com.perl5.lang.perl.idea;

import com.intellij.ide.util.DeleteNameDescriptionLocation;
import com.intellij.ide.util.DeleteTypeDescriptionLocation;
import com.intellij.lang.Language;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.util.NonCodeSearchDescriptionLocation;
import com.intellij.usageView.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlElementDescriptionProviderBase implements ElementDescriptionProvider {
  private static final Logger LOG = Logger.getInstance(PerlElementDescriptionProviderBase.class);

  private final @NotNull Language myLanguage;

  protected PerlElementDescriptionProviderBase(@NotNull Language language) {
    myLanguage = language;
  }

  @Override
  public final @Nullable String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location) {
    if (!isMyElement(element)) {
      return null;
    }
    if (location == DeleteNameDescriptionLocation.INSTANCE) {
      return UsageViewUtil.getShortName(element);
    }
    else if (location == DeleteTypeDescriptionLocation.SINGULAR) {
      return UsageViewUtil.getType(element);
    }
    else if (location == DeleteTypeDescriptionLocation.PLURAL) {
      return StringUtil.pluralize(UsageViewUtil.getType(element));
    }
    else if (location == UsageViewShortNameLocation.INSTANCE) {
      return getShortName(element);
    }
    else if (location == UsageViewLongNameLocation.INSTANCE) {
      return getLongName(element);
    }
    else if (location == UsageViewNodeTextLocation.INSTANCE) {
      return UsageViewUtil.getShortName(element);
    }
    else if (location == UsageViewTypeLocation.INSTANCE) {
      return getTypeName(element);
    }
    else if (location == NonCodeSearchDescriptionLocation.STRINGS_AND_COMMENTS) {
      return getShortName(element);
    }
    LOG.warn("Unhandled location: " + location);
    return "Unknown location: " + location;
  }

  /**
   * @return true iff element is handled by this provider
   */
  protected boolean isMyElement(@NotNull PsiElement element) {
    return element.getLanguage().isKindOf(myLanguage);
  }

  protected abstract @Nullable String getShortName(@NotNull PsiElement element);

  protected abstract @Nullable String getTypeName(@NotNull PsiElement element);

  protected abstract @Nullable String getLongName(@NotNull PsiElement element);
}
