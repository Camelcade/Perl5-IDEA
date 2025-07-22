// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import com.perl5.lang.tt2.psi.PsiRawperlDirective;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

public class PsiRawperlDirectiveImpl extends TemplateToolkitCompositeElementImpl implements PsiRawperlDirective {

  public PsiRawperlDirectiveImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitRawperlDirective(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

}
