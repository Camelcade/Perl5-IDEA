// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import com.perl5.lang.tt2.psi.PsiEndDirective;import com.perl5.lang.tt2.psi.PsiForeachBlock;import com.perl5.lang.tt2.psi.PsiForeachDirective;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

public class PsiForeachBlockImpl extends TemplateToolkitCompositeElementImpl implements PsiForeachBlock {

  public PsiForeachBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitForeachBlock(this);
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
  public PsiForeachDirective getForeachDirective() {
    return findNotNullChildByClass(PsiForeachDirective.class);
  }

}
