// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import com.perl5.lang.tt2.psi.PsiEndDirective;import com.perl5.lang.tt2.psi.PsiFilterBlock;import com.perl5.lang.tt2.psi.PsiFilterDirective;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

public class PsiFilterBlockImpl extends TemplateToolkitCompositeElementImpl implements PsiFilterBlock {

  public PsiFilterBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitFilterBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiEndDirective getEndDirective() {
    return findNotNullChildByClass(PsiEndDirective.class);
  }

  @Override
  @NotNull
  public PsiFilterDirective getFilterDirective() {
    return findNotNullChildByClass(PsiFilterDirective.class);
  }

}
