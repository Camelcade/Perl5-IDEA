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

public class PsiForSectionImpl extends PodSectionFormattedMixin implements PsiForSection {

  public PsiForSectionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPodVisitorGenerated visitor) {
    visitor.visitForSection(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPodVisitorGenerated) accept((PsiPodVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiForSectionContent getForSectionContent() {
    return PsiTreeUtil.getChildOfType(this, PsiForSectionContent.class);
  }

  @Override
  @Nullable
  public PsiPodSectionFormat getPodSectionFormat() {
    return PsiTreeUtil.getChildOfType(this, PsiPodSectionFormat.class);
  }

}
