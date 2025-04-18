// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.mixins.PerlDerefExpressionMixin;
import com.perl5.lang.perl.psi.*;

public class PsiPerlDerefExprImpl extends PerlDerefExpressionMixin implements PsiPerlDerefExpr {

  public PsiPerlDerefExprImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitDerefExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiPerlArrayIndex> getArrayIndexList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlArrayIndex.class);
  }

  @Override
  @NotNull
  public List<PsiPerlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlExpr.class);
  }

  @Override
  @NotNull
  public List<PsiPerlHashIndex> getHashIndexList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlHashIndex.class);
  }

  @Override
  @NotNull
  public List<PsiPerlParenthesisedCallArguments> getParenthesisedCallArgumentsList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlParenthesisedCallArguments.class);
  }

  @Override
  @NotNull
  public List<PsiPerlScalarCall> getScalarCallList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlScalarCall.class);
  }

}
