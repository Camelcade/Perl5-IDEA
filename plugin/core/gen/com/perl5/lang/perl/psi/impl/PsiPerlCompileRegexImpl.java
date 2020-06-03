// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.*;

public class PsiPerlCompileRegexImpl extends PsiPerlExprImpl implements PsiPerlCompileRegex {

  public PsiPerlCompileRegexImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitCompileRegex(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiPerlPerlRegex getPerlRegex() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlPerlRegex.class);
  }

  @Override
  @Nullable
  public PsiPerlPerlRegexModifiers getPerlRegexModifiers() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlPerlRegexModifiers.class);
  }

}
