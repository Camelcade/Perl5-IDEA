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

public class PerlIfBranchImpl extends ASTWrapperPsiElement implements PerlIfBranch {

  public PerlIfBranchImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitIfBranch(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlCodeLine getCodeLine() {
    return findChildByClass(PerlCodeLine.class);
  }

  @Override
  @Nullable
  public PerlPerlBlock getPerlBlock() {
    return findChildByClass(PerlPerlBlock.class);
  }

}
