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

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.Nullable;

public interface PodSection extends PodCompositeElement, PodRenderableElement {
  default boolean hasContent() {
    return getContentBlock() != null;
  }

  @Nullable
  default PsiElement getContentBlock() {
    return PsiTreeUtil.getChildOfType(this, PodSectionContent.class);
  }

  default void renderElementContentAsHTML(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getContentBlock();
    PodRenderUtil.renderPsiRangeAsHTML(content, content, builder, context);
  }

  default void renderElementContentAsText(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getContentBlock();
    PodRenderUtil.renderPsiRangeAsText(content, content, builder, context);
  }

  @Override
  default void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    renderElementContentAsHTML(builder, new PodRenderingContext());
  }

  @Override
  default void renderElementAsText(StringBuilder builder, PodRenderingContext context) {
    renderElementContentAsText(builder, new PodRenderingContext());
  }
}
