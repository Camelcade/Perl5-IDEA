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

public class PerlOp1ExprImpl extends PerlExprImpl implements PerlOp1Expr {

  public PerlOp1ExprImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitOp1Expr(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlCallLeftward getCallLeftward() {
    return findChildByClass(PerlCallLeftward.class);
  }

  @Override
  @Nullable
  public PerlTerm getTerm() {
    return findChildByClass(PerlTerm.class);
  }

  @Override
  @Nullable
  public PerlVariableDefinition getVariableDefinition() {
    return findChildByClass(PerlVariableDefinition.class);
  }

}
