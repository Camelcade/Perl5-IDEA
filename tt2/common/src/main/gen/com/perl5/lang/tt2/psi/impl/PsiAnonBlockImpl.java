// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import com.perl5.lang.tt2.psi.PsiAnonBlock;import com.perl5.lang.tt2.psi.PsiAnonBlockDirective;import com.perl5.lang.tt2.psi.PsiEndDirective;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

public class PsiAnonBlockImpl extends TemplateToolkitCompositeElementImpl implements PsiAnonBlock {

  public PsiAnonBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitAnonBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiAnonBlockDirective getAnonBlockDirective() {
    return findNotNullChildByClass(PsiAnonBlockDirective.class);
  }

  @Override
  @NotNull
  public PsiEndDirective getEndDirective() {
    return findNotNullChildByClass(PsiEndDirective.class);
  }

}
