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

public class PerlForBlockArgumentsImpl extends ASTWrapperPsiElement implements PerlForBlockArguments {

  public PerlForBlockArgumentsImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitForBlockArguments(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PerlBlock> getBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlBlock.class);
  }

  @Override
  @NotNull
  public List<PerlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlExpr.class);
  }

  @Override
  @Nullable
  public PerlList getList() {
    return findChildByClass(PerlList.class);
  }

  @Override
  @Nullable
  public PerlVariableDefinition getVariableDefinition() {
    return findChildByClass(PerlVariableDefinition.class);
  }

}
