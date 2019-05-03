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

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PerlReferenceOwner;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterX;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public interface PodCompositeElement extends PsiElement, PodRenderableElement, ItemPresentation, PerlReferenceOwner {
  /**
   * @return true iff element contains direct child X<>
   */
  default boolean isIndexed() {
    return PsiTreeUtil.getChildOfType(this, PodFormatterX.class) != null;
  }

  /**
   * @return zero-based list level for current item
   */
  default int getListLevel() {
    PsiElement parent = getParent();
    return parent instanceof PodCompositeElement ? ((PodCompositeElement)parent).getListLevel() : 0;
  }

  /**
   * @return true iff element is a heading
   */
  default boolean isHeading() {
    return false;
  }

  /**
   * @return heading level
   */
  default int getHeadingLevel() {
    return 0;
  }

  @Nullable
  @Override
  default String getPresentableText() {
    return null;
  }

  @Nullable
  @Override
  default String getLocationString() {
    PsiFile file = getContainingFile();
    if (file != null) {
      ItemPresentation presentation = file.getPresentation();
      if (presentation != null) {
        String filePresentableText = presentation.getPresentableText();
        if (StringUtil.isNotEmpty(filePresentableText)) {
          return filePresentableText;
        }
      }
    }
    return null;
  }

  @Nullable
  @Override
  default Icon getIcon(boolean unused) {
    PsiFile file = getContainingFile();
    return file == null ? null : file.getIcon(0);
  }

}
