// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.pod.parser.PodElementTypesGenerated.*;
import com.perl5.lang.pod.parser.psi.mixin.PodCompositeElementMixin;
import com.perl5.lang.pod.psi.*;

public class PsiHead4SectionContentImpl extends PodCompositeElementMixin implements PsiHead4SectionContent {

  public PsiHead4SectionContentImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPodVisitorGenerated visitor) {
    visitor.visitHead4SectionContent(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPodVisitorGenerated) accept((PsiPodVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiCutSection> getCutSectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiCutSection.class);
  }

  @Override
  @NotNull
  public List<PsiEncodingSection> getEncodingSectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiEncodingSection.class);
  }

  @Override
  @NotNull
  public List<PsiForSection> getForSectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiForSection.class);
  }

  @Override
  @NotNull
  public List<PsiOverSection> getOverSectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiOverSection.class);
  }

  @Override
  @NotNull
  public List<PsiPodFormatIndex> getPodFormatIndexList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodFormatIndex.class);
  }

  @Override
  @NotNull
  public List<PsiPodParagraph> getPodParagraphList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodParagraph.class);
  }

  @Override
  @NotNull
  public List<PsiPodSection> getPodSectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodSection.class);
  }

  @Override
  @NotNull
  public List<PsiPodVerbatimParagraph> getPodVerbatimParagraphList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodVerbatimParagraph.class);
  }

  @Override
  @NotNull
  public List<PsiUnknownSection> getUnknownSectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiUnknownSection.class);
  }

}
