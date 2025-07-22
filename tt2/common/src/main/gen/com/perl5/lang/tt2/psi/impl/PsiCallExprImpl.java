// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import com.perl5.lang.tt2.psi.PsiCallArguments;import com.perl5.lang.tt2.psi.PsiCallExpr;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

public class PsiCallExprImpl extends PsiExprImpl implements PsiCallExpr {

  public PsiCallExprImpl(ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitCallExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiCallArguments getCallArguments() {
    return findNotNullChildByClass(PsiCallArguments.class);
  }

}
