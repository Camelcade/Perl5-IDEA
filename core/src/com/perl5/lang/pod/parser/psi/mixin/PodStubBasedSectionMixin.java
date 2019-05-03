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

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.pod.parser.psi.*;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class PodStubBasedSectionMixin extends StubBasedPsiElementBase<PodSectionStub> implements PodStubBasedSection {
  public PodStubBasedSectionMixin(@NotNull PodSectionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PodStubBasedSectionMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public PsiElement getTitleElement() {
    return findChildByClass(PodSectionTitle.class);
  }

  @Override
  public boolean hasContent() {
    return getContentBlock() != null;
  }

  @Override
  public PsiElement getContentBlock() {
    return findChildByClass(PodSectionContent.class);
  }

  @Override
  public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    renderElementTitleAsHTML(builder, new PodRenderingContext());
    renderElementContentAsHTML(builder, context);
  }

  public void renderElementTitleAsHTML(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getTitleElement();
    PodRenderUtil.renderPsiRangeAsHTML(content, content, builder, context);
  }

  public void renderElementContentAsHTML(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getContentBlock();
    PodRenderUtil.renderPsiRangeAsHTML(content, content, builder, context);
  }

  @Override
  public void renderElementAsText(StringBuilder builder, PodRenderingContext context) {
    renderElementTitleAsText(builder, new PodRenderingContext());
    renderElementContentAsText(builder, context);
  }

  public void renderElementTitleAsText(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getTitleElement();
    PodRenderUtil.renderPsiRangeAsText(content, content, builder, context);
  }

  public void renderElementContentAsText(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getContentBlock();
    PodRenderUtil.renderPsiRangeAsText(content, content, builder, context);
  }

  @Override
  @Nullable
  public String getTitleText() {
    PodSectionStub stub = getGreenStub();
    if (stub != null) {
      return stub.getTitleText();
    }

    StringBuilder builder = new StringBuilder();
    renderElementTitleAsText(builder, new PodRenderingContext());
    return builder.toString();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" + getElementType().toString() + ")";
  }

  @Override
  public boolean isIndexed() {
    return findChildByClass(PodFormatterX.class) != null;
  }

  @Override
  public boolean isHeading() {
    return false;
  }

  @Override
  public int getHeadingLevel() {
    return 0;
  }

  @Override
  public int getListLevel() {
    PsiElement parent = getParent();
    return parent instanceof PodCompositeElement ? ((PodCompositeElement)parent).getListLevel() : 0;
  }

  @Override
  public ItemPresentation getPresentation() {
    return this;
  }

  @Nullable
  @Override
  public String getPresentableText() {
    return getTitleText();
  }

  @Nullable
  @Override
  public String getLocationString() {
    return "Element location";
  }

  @Nullable
  @Override
  public Icon getIcon(boolean unused) {
    return getContainingFile().getIcon(0);
  }

  @Nullable
  @Override
  public String getPodLinkText() {
    return getTitleText();
  }
}
