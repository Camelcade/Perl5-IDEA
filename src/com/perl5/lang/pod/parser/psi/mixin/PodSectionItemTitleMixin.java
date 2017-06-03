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
import com.perl5.lang.pod.parser.psi.PodSectionItem;
import com.perl5.lang.pod.parser.psi.PodSectionItemTitle;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodSectionItemTitleMixin extends PodCompositeElementMixin implements PodSectionItemTitle {
  public PodSectionItemTitleMixin(@NotNull ASTNode node) {
    super(node);
  }

  protected boolean isItemBulleted() {
    PsiElement parent = getParent();
    return !(parent instanceof PodSectionItem) || ((PodSectionItem)parent).isContainerBulleted();
  }

  @Override
  public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    if (isItemBulleted()) {
      PsiElement firstChild = getFirstChild();
      if (firstChild != null && firstChild.getNode().getElementType() == POD_ASTERISK) {
        firstChild = firstChild.getNextSibling();
      }
      PodRenderUtil.renderPsiRangeAsHTML(firstChild, null, builder, context);
    }
    else {
      super.renderElementAsHTML(builder, context);
    }
  }
}
