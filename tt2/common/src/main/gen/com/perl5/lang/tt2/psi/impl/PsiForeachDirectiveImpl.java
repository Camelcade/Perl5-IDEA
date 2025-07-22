// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import com.perl5.lang.tt2.psi.PsiForeachDirective;import com.perl5.lang.tt2.psi.PsiForeachIterable;import com.perl5.lang.tt2.psi.PsiForeachIterator;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

public class PsiForeachDirectiveImpl extends TemplateToolkitCompositeElementImpl implements PsiForeachDirective {

  public PsiForeachDirectiveImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitForeachDirective(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiForeachIterable getForeachIterable() {
    return findChildByClass(PsiForeachIterable.class);
  }

  @Override
  @Nullable
  public PsiForeachIterator getForeachIterator() {
    return findChildByClass(PsiForeachIterator.class);
  }

}
