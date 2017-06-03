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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.perl5.lang.pod.parser.psi.PodCompositeElement;
import com.perl5.lang.pod.parser.psi.PodFormatterX;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodCompositeElementMixin extends ASTWrapperPsiElement implements PodCompositeElement {
  public PodCompositeElementMixin(@NotNull ASTNode node) {
    super(node);
  }

  // fixme this is debugging method
  public String getAsHTML() {
    StringBuilder builder = new StringBuilder();
    renderElementAsHTML(builder, new PodRenderingContext());
    return builder.toString();
  }

  // fixme this is debugging method
  public String getAsText() {
    StringBuilder builder = new StringBuilder();
    renderElementAsText(builder, new PodRenderingContext());
    return builder.toString();
  }

  @NotNull
  @Override
  public final PsiReference[] getReferences() {
    return getReferencesWithCache();
  }

  @Override
  public final PsiReference getReference() {
    PsiReference[] references = getReferences();
    return references.length == 0 ? null : references[0];
  }

  @Override
  public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    PodRenderUtil.renderPsiRangeAsHTML(getFirstChild(), null, builder, context);
  }

  @Override
  public void renderElementAsText(StringBuilder builder, PodRenderingContext context) {
    PodRenderUtil.renderPsiRangeAsText(getFirstChild(), null, builder, context);
  }

  @Override
  public boolean isIndexed() {
    return findChildByClass(PodFormatterX.class) != null;
  }

  @Override
  public int getListLevel() {
    PsiElement parent = getParent();
    return parent instanceof PodCompositeElement ? ((PodCompositeElement)parent).getListLevel() : 0;
  }

  @Override
  public boolean isHeading() {
    return false;
  }

  @Override
  public ItemPresentation getPresentation() {
    return this;
  }

  @Nullable
  @Override
  public String getPresentableText() {
    return null;
  }

  @Nullable
  @Override
  public String getLocationString() {
    PsiFile file = getContainingFile();
    if (file != null) {
      ItemPresentation presentation = file.getPresentation();
      if (presentation != null) {
        String filePresentableText = presentation.getPresentableText();
        if (StringUtil.isNotEmpty(filePresentableText)) {
          return filePresentableText;
        }
      }
    }
    return null;
  }

  @Nullable
  @Override
  public Icon getIcon(boolean unused) {
    PsiFile file = getContainingFile();
    return file == null ? null : file.getIcon(0);
  }

  @Override
  public int getHeadingLevel() {
    return 0;
  }

  @Nullable
  @Override
  public String getUsageViewTypeLocation() {
    return "NYI Type location string for " + this;
  }

  @Nullable
  @Override
  public String getUsageViewLongNameLocation() {
    return "NYI Long name location string for " + this;
  }

  @Nullable
  @Override
  public String getUsageViewShortNameLocation() {
    return "NYI Short name location string for " + this;
  }
}
