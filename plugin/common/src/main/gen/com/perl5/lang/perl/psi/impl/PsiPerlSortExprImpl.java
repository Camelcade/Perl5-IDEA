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

public class PsiPerlSortExprImpl extends PsiPerlExprImpl implements PsiPerlSortExpr {

  public PsiPerlSortExprImpl(ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitSortExpr(this);
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
  @NotNull
  public List<PsiPerlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlExpr.class);
  }

  @Override
  @Nullable
  public PsiPerlMethod getMethod() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlMethod.class);
  }

}
