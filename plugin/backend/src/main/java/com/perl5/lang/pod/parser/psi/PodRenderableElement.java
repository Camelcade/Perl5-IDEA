/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;

public interface PodRenderableElement extends PsiElement {
  /**
   * Appends HTML representation of the section to the {@code builder}
   */
  default void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    PodRenderUtil.renderPsiRangeAsHTML(getFirstChild(), null, builder, context);
  }

  /**
   * Appends text representation of the section to the {@code builder}
   */
  default void renderElementAsText(StringBuilder builder, PodRenderingContext context) {
    PodRenderUtil.renderPsiRangeAsText(getFirstChild(), null, builder, context);
  }
}
