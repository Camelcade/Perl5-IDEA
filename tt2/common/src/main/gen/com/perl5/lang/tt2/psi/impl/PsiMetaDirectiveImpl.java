// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import java.util.List;
import com.perl5.lang.tt2.psi.PsiExpr;import com.perl5.lang.tt2.psi.PsiMetaDirective;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

public class PsiMetaDirectiveImpl extends TemplateToolkitCompositeElementImpl implements PsiMetaDirective {

  public PsiMetaDirectiveImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitMetaDirective(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiExpr.class);
  }

}
