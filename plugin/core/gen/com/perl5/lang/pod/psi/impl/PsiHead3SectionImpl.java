// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.*;
import com.perl5.lang.pod.parser.psi.mixin.PodSectionH3;
import com.perl5.lang.pod.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;

public class PsiHead3SectionImpl extends PodSectionH3 implements PsiHead3Section {

  public PsiHead3SectionImpl(ASTNode node) {
    super(node);
  }

  public PsiHead3SectionImpl(PodSectionStub stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull PsiPodVisitorGenerated visitor) {
    visitor.visitHead3Section(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPodVisitorGenerated) accept((PsiPodVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiHead3SectionContent getHead3SectionContent() {
    return PsiTreeUtil.getChildOfType(this, PsiHead3SectionContent.class);
  }

  @Override
  @Nullable
  public PsiSectionTitle getSectionTitle() {
    return PsiTreeUtil.getChildOfType(this, PsiSectionTitle.class);
  }

}
