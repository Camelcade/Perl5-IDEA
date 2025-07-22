// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import java.util.List;
import com.perl5.lang.tt2.psi.PsiDirectivePostfix;import com.perl5.lang.tt2.psi.PsiExpr;import com.perl5.lang.tt2.psi.PsiProcessDirective;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

public class PsiProcessDirectiveImpl extends TemplateToolkitCompositeElementImpl implements PsiProcessDirective {

  public PsiProcessDirectiveImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitProcessDirective(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiDirectivePostfix getDirectivePostfix() {
    return findChildByClass(PsiDirectivePostfix.class);
  }

  @Override
  @NotNull
  public List<PsiExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiExpr.class);
  }

}
