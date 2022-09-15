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

public class PsiPerlCatchConditionImpl extends PerlCompositeElementImpl implements PsiPerlCatchCondition {

  public PsiPerlCatchConditionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitCatchCondition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiPerlExpr getExpr() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlExpr.class);
  }

  @Override
  @Nullable
  public PsiPerlTypeConstraints getTypeConstraints() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlTypeConstraints.class);
  }

  @Override
  @Nullable
  public PsiPerlVariableDeclarationElement getVariableDeclarationElement() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlVariableDeclarationElement.class);
  }

}
