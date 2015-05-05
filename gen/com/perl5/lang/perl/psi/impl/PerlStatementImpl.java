// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.perl5.lang.perl.psi.*;

public class PerlStatementImpl extends ASTWrapperPsiElement implements PerlStatement {

  public PerlStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitStatement(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlExpr getExpr() {
    return findChildByClass(PerlExpr.class);
  }

  @Override
  @Nullable
  public PerlForStatementModifier getForStatementModifier() {
    return findChildByClass(PerlForStatementModifier.class);
  }

  @Override
  @Nullable
  public PerlForeachStatementModifier getForeachStatementModifier() {
    return findChildByClass(PerlForeachStatementModifier.class);
  }

  @Override
  @Nullable
  public PerlIfStatementModifier getIfStatementModifier() {
    return findChildByClass(PerlIfStatementModifier.class);
  }

  @Override
  @Nullable
  public PerlLastStatement getLastStatement() {
    return findChildByClass(PerlLastStatement.class);
  }

  @Override
  @Nullable
  public PerlNextStatement getNextStatement() {
    return findChildByClass(PerlNextStatement.class);
  }

  @Override
  @Nullable
  public PerlNoStatement getNoStatement() {
    return findChildByClass(PerlNoStatement.class);
  }

  @Override
  @Nullable
  public PerlRedoStatement getRedoStatement() {
    return findChildByClass(PerlRedoStatement.class);
  }

  @Override
  @Nullable
  public PerlSubDeclarationStatement getSubDeclarationStatement() {
    return findChildByClass(PerlSubDeclarationStatement.class);
  }

  @Override
  @Nullable
  public PerlUndefStatement getUndefStatement() {
    return findChildByClass(PerlUndefStatement.class);
  }

  @Override
  @Nullable
  public PerlUnlessStatementModifier getUnlessStatementModifier() {
    return findChildByClass(PerlUnlessStatementModifier.class);
  }

  @Override
  @Nullable
  public PerlUntilStatementModifier getUntilStatementModifier() {
    return findChildByClass(PerlUntilStatementModifier.class);
  }

  @Override
  @Nullable
  public PerlUseStatement getUseStatement() {
    return findChildByClass(PerlUseStatement.class);
  }

  @Override
  @Nullable
  public PerlWhenStatementModifier getWhenStatementModifier() {
    return findChildByClass(PerlWhenStatementModifier.class);
  }

  @Override
  @Nullable
  public PerlWhileStatementModifier getWhileStatementModifier() {
    return findChildByClass(PerlWhileStatementModifier.class);
  }

}
