// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi.impl;

import java.util.List;
import com.intellij.psi.tree.IElementType;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.pod.parser.PodElementTypesGenerated.*;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterX;
import com.perl5.lang.pod.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;

public class PsiPodFormatIndexImpl extends PodFormatterX implements PsiPodFormatIndex {

  public PsiPodFormatIndexImpl(ASTNode node) {
    super(node);
  }

  public PsiPodFormatIndexImpl(PodSectionStub stub, IElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull PsiPodVisitorGenerated visitor) {
    visitor.visitPodFormatIndex(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPodVisitorGenerated) accept((PsiPodVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiFormattingSectionContent getFormattingSectionContent() {
    return PsiTreeUtil.getChildOfType(this, PsiFormattingSectionContent.class);
  }

}
