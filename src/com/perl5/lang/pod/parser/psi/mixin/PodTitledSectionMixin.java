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
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.pod.parser.psi.PodCompositeElement;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.PodSectionTitle;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodTitledSectionMixin extends PodSectionMixin implements PodTitledSection {
  public PodTitledSectionMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiElement getTitleBlock() {
    return findChildByClass(PodSectionTitle.class);
  }

  @Override
  public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    renderElementTitleAsHTML(builder, new PodRenderingContext());
    super.renderElementAsHTML(builder, context);
  }

  public void renderElementTitleAsHTML(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getTitleBlock();
    PodRenderUtil.renderPsiRangeAsHTML(content, content, builder, context);
  }

  @Override
  public void renderElementAsText(StringBuilder builder, PodRenderingContext context) {

    renderElementTitleAsText(builder, new PodRenderingContext());
    super.renderElementAsText(builder, context);
  }

  public void renderElementTitleAsText(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getTitleBlock();
    PodRenderUtil.renderPsiRangeAsText(content, content, builder, context);
  }

  @Override
  public boolean isIndexed() {
    PsiElement titleBlock = getTitleBlock();
    if (titleBlock instanceof PodCompositeElement && ((PodCompositeElement)titleBlock).isIndexed()) {
      return true;
    }

    return false;
  }

  @Nullable
  @Override
  public String getTitleText() {
    PsiElement titleElement = getTitleBlock();

    if (titleElement == null) {
      return null;
    }

    StringBuilder builder = new StringBuilder();
    renderElementTitleAsText(builder, new PodRenderingContext());
    return builder.toString().trim();
  }

  @Nullable
  @Override
  public String getPresentableText() {
    return getTitleText();
  }


  @Nullable
  @Override
  public String getPodLink() {
    return PodRenderUtil.getPodLinkForElement(this);
  }

  @Nullable
  @Override
  public String getPodLinkText() {
    return getTitleText();
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return getTitleBlock();
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier != null) {
      ElementManipulators.getManipulator(nameIdentifier).handleContentChange(nameIdentifier, name);
    }
    return this;
  }

  @Override
  public int getTextOffset() {
    PsiElement nameIdentifier = getNameIdentifier();

    return nameIdentifier == null
           ? super.getTextOffset()
           : nameIdentifier.getTextOffset();
  }

  @Override
  public String getName() {
    PsiElement nameIdentifier = getNameIdentifier();
    return nameIdentifier == null ? null : nameIdentifier.getText();
  }

  @Nullable
  @Override
  public String getUsageViewLongNameLocation() {
    return getTitleText();
  }

  @Nullable
  @Override
  public String getUsageViewShortNameLocation() {
    return getName();
  }
}
