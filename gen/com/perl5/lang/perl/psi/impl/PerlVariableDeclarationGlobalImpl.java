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
  public List<PerlArrayVariable> getArrayVariableList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlArrayVariable.class);
  }

  @Override
  @NotNull
  public List<PerlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlExpr.class);
  }

  @Override
  @NotNull
  public List<PerlHashVariable> getHashVariableList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlHashVariable.class);
  }

  @Override
  @NotNull
  public List<PerlScalarVariable> getScalarVariableList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlScalarVariable.class);
  }

  @Override
  @NotNull
  public List<PerlString> getStringList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlString.class);
  }

}
