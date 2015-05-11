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

public class PerlForeachCompoundImpl extends ASTWrapperPsiElement implements PerlForeachCompound {

  public PerlForeachCompoundImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitForeachCompound(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlBlockCompound getBlockCompound() {
    return findChildByClass(PerlBlockCompound.class);
  }

  @Override
  @NotNull
  public List<PerlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlExpr.class);
  }

  @Override
  @Nullable
  public PerlPerlArray getPerlArray() {
    return findChildByClass(PerlPerlArray.class);
  }

  @Override
  @Nullable
  public PerlPerlGlob getPerlGlob() {
    return findChildByClass(PerlPerlGlob.class);
  }

  @Override
  @Nullable
  public PerlPerlHash getPerlHash() {
    return findChildByClass(PerlPerlHash.class);
  }

  @Override
  @Nullable
  public PerlPerlScalar getPerlScalar() {
    return findChildByClass(PerlPerlScalar.class);
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
