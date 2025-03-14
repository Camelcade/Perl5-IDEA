// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.mixins.Perl5RegexpMixin;
import com.perl5.lang.perl.psi.*;

public class PsiPerlPerlRegexImpl extends Perl5RegexpMixin implements PsiPerlPerlRegex {

  public PsiPerlPerlRegexImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitPerlRegex(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiPerlBlockCompound> getBlockCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlBlockCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlDerefExpr> getDerefExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlDerefExpr.class);
  }

}
