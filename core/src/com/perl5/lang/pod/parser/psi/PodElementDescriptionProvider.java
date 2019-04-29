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
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.PerlElementDescriptionProviderBase;
import com.perl5.lang.pod.PodLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.*;

public class PodElementDescriptionProvider extends PerlElementDescriptionProviderBase {
  private static final Logger LOG = Logger.getInstance(PodElementDescriptionProvider.class);

  public PodElementDescriptionProvider() {
    super(PodLanguage.INSTANCE);
  }

  @Override
  protected boolean isMyElement(@NotNull PsiElement element) {
    return super.isMyElement(element) && element instanceof PodCompositeElement;
  }

  @Nullable
  @Override
  protected String getShortName(@NotNull PsiElement element) {
    return getLongName(element);
  }

  @Nullable
  @Override
  protected String getTypeName(@NotNull PsiElement element) {
    if (element instanceof PodFile) {
      return "POD file";
    }
    IElementType elementType = PsiUtilCore.getElementType(element);
    if (elementType == POD_FORMAT_INDEX) {
      return PerlBundle.message("pod.type.index");
    }
    else if (elementType == HEAD_1_SECTION) {
      return PerlBundle.message("pod.type.heading1");
    }
    else if (elementType == HEAD_2_SECTION) {
      return PerlBundle.message("pod.type.heading2");
    }
    else if (elementType == HEAD_3_SECTION) {
      return PerlBundle.message("pod.type.heading3");
    }
    else if (elementType == HEAD_4_SECTION) {
      return PerlBundle.message("pod.type.heading4");
    }
    else if (elementType == ITEM_SECTION) {
      return PerlBundle.message("pod.type.list.item");
    }
    else if (elementType == UNKNOWN_SECTION) {
      return PerlBundle.message("pod.type.unknown");
    }

    LOG.warn("Unhandled type for " + element);
    return "Unhandled type for " + element;
  }

  @Nullable
  @Override
  protected String getLongName(@NotNull PsiElement element) {
    if (element instanceof PodTitledSection) {
      return ((PodTitledSection)element).getTitleTextWithFormatting();
    }
    LOG.warn("Unhandled long name for " + element);
    return "Unhandled long name for " + element;
  }
}
