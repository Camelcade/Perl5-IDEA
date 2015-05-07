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
  public PerlCallable getCallable() {
    return findChildByClass(PerlCallable.class);
  }

  @Override
  @Nullable
  public PerlCompileRegex getCompileRegex() {
    return findChildByClass(PerlCompileRegex.class);
  }

  @Override
  @Nullable
  public PerlDoTerm getDoTerm() {
    return findChildByClass(PerlDoTerm.class);
  }

  @Override
  @Nullable
  public PerlEvalTerm getEvalTerm() {
    return findChildByClass(PerlEvalTerm.class);
  }

  @Override
  @Nullable
  public PerlExpr getExpr() {
    return findChildByClass(PerlExpr.class);
  }

  @Override
  @Nullable
  public PerlFileReadTerm getFileReadTerm() {
    return findChildByClass(PerlFileReadTerm.class);
  }

  @Override
  @Nullable
  public PerlMatchRegex getMatchRegex() {
    return findChildByClass(PerlMatchRegex.class);
  }

  @Override
  @Nullable
  public PerlReferenceValue getReferenceValue() {
    return findChildByClass(PerlReferenceValue.class);
  }

  @Override
  @Nullable
  public PerlReplacementRegex getReplacementRegex() {
    return findChildByClass(PerlReplacementRegex.class);
  }

  @Override
  @Nullable
  public PerlTrRegex getTrRegex() {
    return findChildByClass(PerlTrRegex.class);
  }

  @Override
  @Nullable
  public PerlVariableDeclarationGlobal getVariableDeclarationGlobal() {
    return findChildByClass(PerlVariableDeclarationGlobal.class);
  }

  @Override
  @Nullable
  public PerlVariableDeclarationLexical getVariableDeclarationLexical() {
    return findChildByClass(PerlVariableDeclarationLexical.class);
  }

  @Override
  @Nullable
  public PerlVariableDeclarationLocal getVariableDeclarationLocal() {
    return findChildByClass(PerlVariableDeclarationLocal.class);
  }

}
