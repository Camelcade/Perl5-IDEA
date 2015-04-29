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

public class PerlIfBranchConditionalImpl extends ASTWrapperPsiElement implements PerlIfBranchConditional {

  public PerlIfBranchConditionalImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitIfBranchConditional(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PerlBlock getBlock() {
    return findNotNullChildByClass(PerlBlock.class);
  }

  @Override
  @NotNull
  public PerlExpr getExpr() {
    return findNotNullChildByClass(PerlExpr.class);
  }

}
