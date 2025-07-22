// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import com.perl5.lang.tt2.psi.PsiExpr;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import com.perl5.lang.tt2.psi.PsiUnlessDirective;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

public class PsiUnlessDirectiveImpl extends TemplateToolkitCompositeElementImpl implements PsiUnlessDirective {

  public PsiUnlessDirectiveImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitUnlessDirective(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiExpr getExpr() {
    return findChildByClass(PsiExpr.class);
  }

}
