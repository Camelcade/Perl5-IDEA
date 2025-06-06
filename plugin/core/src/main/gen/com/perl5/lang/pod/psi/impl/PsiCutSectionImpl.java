// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.pod.parser.PodElementTypesGenerated.*;
import com.perl5.lang.pod.parser.psi.mixin.PodSectionMixin;
import com.perl5.lang.pod.psi.*;

public class PsiCutSectionImpl extends PodSectionMixin implements PsiCutSection {

  public PsiCutSectionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPodVisitorGenerated visitor) {
    visitor.visitCutSection(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPodVisitorGenerated) accept((PsiPodVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiSectionTitle getSectionTitle() {
    return PsiTreeUtil.getChildOfType(this, PsiSectionTitle.class);
  }

}
