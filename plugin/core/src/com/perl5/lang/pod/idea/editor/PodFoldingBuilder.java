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

package com.perl5.lang.pod.idea.editor;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.templateLanguages.OuterLanguageElementImpl;
import com.perl5.lang.perl.idea.folding.PerlFoldingBuilderBase;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.PodRecursiveVisitor;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PodFoldingBuilder extends PerlFoldingBuilderBase implements PodElementTypes, DumbAware {
  @NotNull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
    // @todo handle this
    if (root instanceof OuterLanguageElementImpl) {
      return FoldingDescriptor.EMPTY;
    }

    FoldingRegionsCollector collector = new FoldingRegionsCollector(document);
    root.accept(collector);
    List<FoldingDescriptor> descriptors = collector.getDescriptors();

    return descriptors.toArray(FoldingDescriptor.EMPTY);
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    return false;
  }

  @Nullable
  @Override
  public String getPlaceholderText(@NotNull ASTNode node) {
    PsiElement psi = node.getPsi();
    if (!(psi instanceof PodTitledSection)) {
      return null;
    }
    String titleText = ((PodTitledSection)psi).getTitleText();
    if (StringUtil.isEmpty(titleText)) {
      return null;
    }
    return " " + StringUtil.shortenTextWithEllipsis(titleText, 80, 0);
  }


  public static class FoldingRegionsCollector extends PodRecursiveVisitor {
    protected final Document myDocument;
    protected List<FoldingDescriptor> myDescriptors = new ArrayList<>();

    public FoldingRegionsCollector(Document document) {
      myDocument = document;
    }

    public List<FoldingDescriptor> getDescriptors() {
      return myDescriptors;
    }

    @Override
    public void visitBeginSection(@NotNull PsiBeginSection o) {
      addDescriptorFor(o);
      super.visitBeginSection(o);
    }

    @Override
    public void visitForSection(@NotNull PsiForSection o) {
      addDescriptorFor(o);
      super.visitForSection(o);
    }

    @Override
    public void visitHead1Section(@NotNull PsiHead1Section o) {
      addDescriptorFor(o);
      super.visitHead1Section(o);
    }

    @Override
    public void visitHead2Section(@NotNull PsiHead2Section o) {
      addDescriptorFor(o);
      super.visitHead2Section(o);
    }

    @Override
    public void visitHead3Section(@NotNull PsiHead3Section o) {
      addDescriptorFor(o);
      super.visitHead3Section(o);
    }

    @Override
    public void visitHead4Section(@NotNull PsiHead4Section o) {
      addDescriptorFor(o);
      super.visitHead4Section(o);
    }

    @Override
    public void visitItemSection(@NotNull PsiItemSection o) {
      if (o.getItemSectionContent() != null) {
        addDescriptorFor(o);
      }
      super.visitItemSection(o);
    }

    @Override
    public void visitOverSection(@NotNull PsiOverSection o) {
      addDescriptorFor(o);
      super.visitOverSection(o);
    }

    @Override
    public void visitUnknownSection(@NotNull PsiUnknownSection o) {
      addDescriptorFor(o);
      super.visitUnknownSection(o);
    }

    @Override
    public void visitPodSection(@NotNull PsiPodSection o) {
      addDescriptorFor(o);
      super.visitPodSection(o);
    }

    protected void addDescriptorFor(PsiElement element) {
      PsiElement firstChild = element.getFirstChild();
      if (firstChild != null) {
        int endMargin = StringUtil.endsWith(element.getNode().getChars(), "\n") ? 1 : 0;
        PerlFoldingBuilderBase.addDescriptorFor(myDescriptors, myDocument, element, firstChild.getTextLength(), endMargin, 1);
      }
    }
  }
}
