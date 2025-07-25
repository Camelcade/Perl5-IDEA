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

package com.perl5.lang.pod.parser.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.pod.parser.PodElementTypesGenerated.POD_NUMBER;

public class PodSectionItem extends PodTitledSectionMixin {
  public PodSectionItem(@NotNull ASTNode node) {
    super(node);
  }

  public PodSectionItem(@NotNull PodSectionStub stub,
                        @NotNull IElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public @Nullable String getPresentableText() {
    PodSectionStub greenStub = getGreenStub();
    if (greenStub != null) {
      String content = greenStub.getContent();
      return content.isEmpty() ? "" : content.substring(1);
    }

    if (isTargetable()) {
      return super.getPresentableText();
    }
    PsiElement contentBlock = getContentBlock();
    if (contentBlock == null) {
      return null;
    }
    return PodRenderUtil.trimItemText(PodRenderUtil.renderPsiElementAsText(contentBlock));
  }

  @Override
  public @Nullable String getTitleText() {
    PodSectionStub greenStub = getGreenStub();
    if (greenStub != null) {
      String content = greenStub.getContent();
      return content.isEmpty() ? "" : content.substring(1);
    }
    return super.getTitleText();
  }

  /**
   * @return true iff this item may be targeted from link (has title)
   */
  public boolean isTargetable() {
    PodSectionStub stub = getGreenStub();
    if (stub != null) {
      return stub.getContent().startsWith("+");
    }
    return getTitleElement() != null;
  }

  /**
   * Checks if this item defines bullteted list, as described in http://perldoc.perl.org/perlpodspec.html#About-%3dover...%3dback-Regions
   *
   * @return true if list should be bulleted
   */
  public boolean isBulleted() {
    return !isTargetable() && !isNumbered();
  }

  /**
   * @return true iff this item is numbered one
   */
  public boolean isNumbered() {
    if (isTargetable()) {
      return false;
    }
    PsiElement run = getFirstChild();
    while (run != null) {
      if (PsiUtilCore.getElementType(run) == POD_NUMBER) {
        return true;
      }
      run = run.getNextSibling();
    }
    return false;
  }

  /**
   * @return true iff item should be rendered with {@code <li>..</li>} tags
   */
  private boolean isRenderAsListItem() {
    PsiElement container = getParent();
    return container instanceof PodOverSectionContent overSectionContent &&
           (overSectionContent.isBulleted() || overSectionContent.isNumbered());
  }

  @Override
  public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    boolean isListItem = isRenderAsListItem();

    if (isListItem) {
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
      if (!elementBuilder.isEmpty()) {
        builder.append("<dd style=\"padding-top:6px;\">");
        builder.append(elementBuilder);
        builder.append("</dd>");
      }
    }
  }
}
