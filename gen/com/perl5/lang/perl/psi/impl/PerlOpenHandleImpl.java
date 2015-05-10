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

public class PerlOpenHandleImpl extends ASTWrapperPsiElement implements PerlOpenHandle {

  public PerlOpenHandleImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitOpenHandle(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlExpr getExpr() {
    return findChildByClass(PerlExpr.class);
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
