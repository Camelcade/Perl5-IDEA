// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.*;

public class PsiPerlTrRegexImpl extends PsiPerlExprImpl implements PsiPerlTrRegex {

  public PsiPerlTrRegexImpl(ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitTrRegex(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiPerlTrModifiers getTrModifiers() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlTrModifiers.class);
  }

  @Override
  @Nullable
  public PsiPerlTrReplacementlist getTrReplacementlist() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlTrReplacementlist.class);
  }

  @Override
  @Nullable
  public PsiPerlTrSearchlist getTrSearchlist() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlTrSearchlist.class);
  }

}
