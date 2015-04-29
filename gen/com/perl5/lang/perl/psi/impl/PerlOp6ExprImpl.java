// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypes.*;
import com.perl5.lang.perl.psi.*;

public class PerlOp6ExprImpl extends PerlExprImpl implements PerlOp6Expr {

  public PerlOp6ExprImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitOp6Expr(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PerlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlExpr.class);
  }

}
