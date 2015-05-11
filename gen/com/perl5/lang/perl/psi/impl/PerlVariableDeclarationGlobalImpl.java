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

public class PerlVariableDeclarationGlobalImpl extends ASTWrapperPsiElement implements PerlVariableDeclarationGlobal {

  public PerlVariableDeclarationGlobalImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitVariableDeclarationGlobal(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PerlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlExpr.class);
  }

  @Override
  @NotNull
  public List<PerlPerlArray> getPerlArrayList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPerlArray.class);
  }

  @Override
  @NotNull
  public List<PerlPerlGlob> getPerlGlobList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPerlGlob.class);
  }

  @Override
  @NotNull
  public List<PerlPerlHash> getPerlHashList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPerlHash.class);
  }

  @Override
  @NotNull
  public List<PerlPerlScalar> getPerlScalarList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPerlScalar.class);
  }

}
