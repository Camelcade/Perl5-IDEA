// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.*;

public class PsiPerlWhileCompoundImpl extends PerlCompositeElementImpl implements PsiPerlWhileCompound {

  public PsiPerlWhileCompoundImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitWhileCompound(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiPerlBlock getBlock() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlBlock.class);
  }

  @Override
  @Nullable
  public PsiPerlConditionExpr getConditionExpr() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlConditionExpr.class);
  }

  @Override
  @Nullable
  public PsiPerlContinueBlock getContinueBlock() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlContinueBlock.class);
  }

}
