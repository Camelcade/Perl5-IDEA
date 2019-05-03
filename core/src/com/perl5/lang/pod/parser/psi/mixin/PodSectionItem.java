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
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.pod.parser.psi.PodOverSectionContent;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.*;

public class PodSectionItem extends PodTitledSectionMixin {
  public PodSectionItem(@NotNull ASTNode node) {
    super(node);
  }

  public PodSectionItem(@NotNull PodSectionStub stub,
                        @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Nullable
  @Override
  public String getPresentableText() {
    if (!isNumbered() && !isBulleted()) {
      return super.getPresentableText();
    }
    PsiElement contentBlock = getContentBlock();
    if (contentBlock == null) {
      return null;
    }
    PsiElement firstChild = contentBlock.getFirstChild();
    if (firstChild == null) {
      return null;
    }
    return PodRenderUtil.renderPsiElementAsText(firstChild);
  }

  /**
   * Checks if this item defines bullteted list, as described in http://perldoc.perl.org/perlpodspec.html#About-%3dover...%3dback-Regions
   *
   * @return true if list should be bulleted
   */
  public boolean isBulleted() {
    IElementType elementType = getFirstTitleTokenElementType();
    return elementType == POD_NEWLINE || elementType == POD_ASTERISK;
  }

  @Nullable
  private IElementType getFirstTitleTokenElementType() {
    PsiElement titleBlock = getTitleElement();

    if (titleBlock == null) {
      return null;
    }
    return PsiUtilCore.getElementType(titleBlock.getFirstChild());
  }

  /**
   * @return true iff this item is numbered one
   */
  public boolean isNumbered() {
    IElementType elementType = getFirstTitleTokenElementType();
    return elementType == POD_NUMBER;
  }


  /**
   * Check if this item container is bulleted
   *
   * @return true if yep
   */
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
