// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import com.perl5.lang.tt2.psi.PsiAnonBlockDirective;import com.perl5.lang.tt2.psi.PsiIdentifierExpr;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

public class PsiAnonBlockDirectiveImpl extends TemplateToolkitCompositeElementImpl implements PsiAnonBlockDirective {

  public PsiAnonBlockDirectiveImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitAnonBlockDirective(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiIdentifierExpr getIdentifierExpr() {
    return findNotNullChildByClass(PsiIdentifierExpr.class);
  }

}
