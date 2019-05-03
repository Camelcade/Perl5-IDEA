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

import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import com.perl5.lang.pod.psi.PsiPodFormatIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PodTitledSection extends PodSection,
                                          PodLinkTarget,
                                          PodStructureElement,
                                          PsiNameIdentifierOwner,
                                          NavigationItem {
  /**
   * @return text representation of section
   */
  @Nullable
  default String getTitleText() {
    PsiElement titleElement = getTitleElement();

    if (titleElement == null) {
      return null;
    }

    StringBuilder builder = new StringBuilder();
    renderElementTitleAsText(builder, new PodRenderingContext());
    return builder.toString().trim();
  }

  default void renderElementTitleAsHTML(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getTitleElement();
    PodRenderUtil.renderPsiRangeAsHTML(content, content, builder, context);
  }

  default void renderElementTitleAsText(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getTitleElement();
    PodRenderUtil.renderPsiRangeAsText(content, content, builder, context);
  }

  /**
   * @return a text of the section title with all formatting codes but indexes, or null if there is no title section
   */
  @Nullable
  default String getTitleTextWithoutIndexes() {
    PsiElement titleElement = getTitleElement();
    if (titleElement == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    titleElement.accept(new PodRecursiveVisitor() {
      @Override
      public void visitElement(@NotNull PsiElement element) {
        if (element.getChildren().length > 0) {
          super.visitElement(element);
        }
        else {
          sb.append(element.getNode().getChars());
        }
      }

      @Override
      public void visitPodFormatIndex(@NotNull PsiPodFormatIndex o) {
      }
    });
    return sb.toString().trim();
  }

  /**
   * @return an element containing title of this section, probably with formatting codes
   */
  @Nullable
  default PsiElement getTitleElement() {
    return PsiTreeUtil.getChildOfType(this, PodSectionTitle.class);
  }

  @Override
  default void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    renderElementTitleAsHTML(builder, new PodRenderingContext());
    PodSection.super.renderElementAsHTML(builder, context);
  }

  @Override
  default void renderElementAsText(StringBuilder builder, PodRenderingContext context) {
    renderElementTitleAsText(builder, new PodRenderingContext());
    PodSection.super.renderElementAsText(builder, context);
  }

  @Override
  default boolean isIndexed() {
    PsiElement titleBlock = getTitleElement();
    return titleBlock instanceof PodCompositeElement && ((PodCompositeElement)titleBlock).isIndexed();
  }
}
