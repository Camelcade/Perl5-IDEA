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

package com.perl5.lang.pod.parser.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.parser.psi.PodOverSectionContent;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.PodSectionItem;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodSectionItemMixin extends PodTitledSectionMixin implements PodSectionItem {
  public PodSectionItemMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public boolean isBulleted() {
    PsiElement titleBlock = getTitleElement();

    if (titleBlock != null) {
      PsiElement firstTitleToken = titleBlock.getFirstChild();
      if (firstTitleToken != null) {
        IElementType elementType = firstTitleToken.getNode().getElementType();

        return elementType == POD_NEWLINE || elementType == POD_ASTERISK;
      }
    }

    return true;
  }

  public boolean isContainerBulleted() {
    PsiElement container = getParent();
    return !(container instanceof PodOverSectionContent) || ((PodOverSectionContent)container).isBulleted();
  }

  @Override
  public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    boolean isBulleted = isContainerBulleted();

    if (isBulleted) {
      builder.append("<li>");
      super.renderElementAsHTML(builder, context);
      builder.append("</li>");
    }
    else {
      builder.append("<dt style=\"padding-bottom:4px;font-weight:bold;\">");
      super.renderElementTitleAsHTML(builder, context);
      builder.append("</dt>");

      StringBuilder elementBuilder = new StringBuilder();
      super.renderElementContentAsHTML(elementBuilder, context);
      if (elementBuilder.length() > 0) {
        builder.append("<dd style=\"padding-top:6px;\">");
        builder.append(elementBuilder);
        builder.append("</dd>");
      }
    }
  }
}
