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

public class PerlDerefExprImpl extends PerlExprImpl implements PerlDerefExpr {

  public PerlDerefExprImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitDerefExpr(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PerlCallable> getCallableList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlCallable.class);
  }

  @Override
  @NotNull
  public List<PerlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlExpr.class);
  }

  @Override
  @NotNull
  public List<PerlRightwardCall> getRightwardCallList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlRightwardCall.class);
  }

  @Override
  @NotNull
  public List<PerlScalarCall> getScalarCallList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlScalarCall.class);
  }

}
