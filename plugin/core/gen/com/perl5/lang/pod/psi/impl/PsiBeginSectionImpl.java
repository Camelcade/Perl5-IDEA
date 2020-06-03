// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.*;
import com.perl5.lang.pod.parser.psi.mixin.PodSectionFormattedMixin;
import com.perl5.lang.pod.psi.*;

public class PsiBeginSectionImpl extends PodSectionFormattedMixin implements PsiBeginSection {

  public PsiBeginSectionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPodVisitorGenerated visitor) {
    visitor.visitBeginSection(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPodVisitorGenerated) accept((PsiPodVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiBeginSectionContent getBeginSectionContent() {
    return PsiTreeUtil.getChildOfType(this, PsiBeginSectionContent.class);
  }

  @Override
  @NotNull
  public List<PsiPodSectionFormat> getPodSectionFormatList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodSectionFormat.class);
  }

}
