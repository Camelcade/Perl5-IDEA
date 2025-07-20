// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.mixins.PerlVariableDeclarationExprMixin;
import com.perl5.lang.perl.psi.*;

public class PsiPerlVariableDeclarationLexicalImpl extends PerlVariableDeclarationExprMixin implements PsiPerlVariableDeclarationLexical {

  public PsiPerlVariableDeclarationLexicalImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitVariableDeclarationLexical(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiPerlAttributes getAttributes() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAttributes.class);
  }

  @Override
  @NotNull
  public List<PsiPerlUndefExpr> getUndefExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlUndefExpr.class);
  }

  @Override
  @NotNull
  public List<PsiPerlVariableDeclarationElement> getVariableDeclarationElementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlVariableDeclarationElement.class);
  }

}
