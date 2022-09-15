// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.*;
import com.perl5.lang.pod.parser.psi.mixin.PodSectionH2;
import com.perl5.lang.pod.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;

public class PsiHead2SectionImpl extends PodSectionH2 implements PsiHead2Section {

  public PsiHead2SectionImpl(ASTNode node) {
    super(node);
  }

  public PsiHead2SectionImpl(PodSectionStub stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull PsiPodVisitorGenerated visitor) {
    visitor.visitHead2Section(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPodVisitorGenerated) accept((PsiPodVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiHead2SectionContent getHead2SectionContent() {
    return PsiTreeUtil.getChildOfType(this, PsiHead2SectionContent.class);
  }

  @Override
  @Nullable
  public PsiSectionTitle getSectionTitle() {
    return PsiTreeUtil.getChildOfType(this, PsiSectionTitle.class);
  }

}
