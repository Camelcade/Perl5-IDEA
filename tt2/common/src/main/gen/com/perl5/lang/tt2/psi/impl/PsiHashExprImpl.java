// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import java.util.List;
import com.perl5.lang.tt2.psi.PsiExpr;import com.perl5.lang.tt2.psi.PsiHashExpr;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

public class PsiHashExprImpl extends PsiExprImpl implements PsiHashExpr {

  public PsiHashExprImpl(ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitHashExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiExpr.class);
  }

}
