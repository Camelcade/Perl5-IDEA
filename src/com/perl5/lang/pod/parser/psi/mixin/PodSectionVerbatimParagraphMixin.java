/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.pod.parser.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.PodSectionVerbatimParagraph;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodSectionVerbatimParagraphMixin extends PodSectionMixin implements PodSectionVerbatimParagraph {
  public PodSectionVerbatimParagraphMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiElement getContentBlock() {
    return getFirstChild();
  }

  @Override
  public void renderElementContentAsHTML(StringBuilder builder, PodRenderingContext context) {
    builder.append("<div style=\"padding-bottom: 10px;\"><pre><code>"); //  style="padding-bottom: 10px;"
    super.renderElementContentAsHTML(builder, context);
    builder.append("</code></pre></div>");
  }
}
