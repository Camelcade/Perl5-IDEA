// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import com.perl5.lang.tt2.psi.PsiEndDirective;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import com.perl5.lang.tt2.psi.PsiWrapperBlock;import com.perl5.lang.tt2.psi.PsiWrapperDirective;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

public class PsiWrapperBlockImpl extends TemplateToolkitCompositeElementImpl implements PsiWrapperBlock {

  public PsiWrapperBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitWrapperBlock(this);
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
  public PsiWrapperDirective getWrapperDirective() {
    return findNotNullChildByClass(PsiWrapperDirective.class);
  }

}
