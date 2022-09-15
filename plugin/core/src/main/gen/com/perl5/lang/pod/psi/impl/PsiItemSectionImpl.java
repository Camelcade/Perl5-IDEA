// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.*;
import com.perl5.lang.pod.parser.psi.mixin.PodSectionItem;
import com.perl5.lang.pod.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;

public class PsiItemSectionImpl extends PodSectionItem implements PsiItemSection {

  public PsiItemSectionImpl(ASTNode node) {
    super(node);
  }

  public PsiItemSectionImpl(PodSectionStub stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull PsiPodVisitorGenerated visitor) {
    visitor.visitItemSection(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPodVisitorGenerated) accept((PsiPodVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiItemSectionContent getItemSectionContent() {
    return PsiTreeUtil.getChildOfType(this, PsiItemSectionContent.class);
  }

  @Override
  @Nullable
  public PsiItemSectionTitle getItemSectionTitle() {
    return PsiTreeUtil.getChildOfType(this, PsiItemSectionTitle.class);
  }

  @Override
  @NotNull
  public List<PsiPodFormatIndex> getPodFormatIndexList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, PsiPodFormatIndex.class);
  }

}
