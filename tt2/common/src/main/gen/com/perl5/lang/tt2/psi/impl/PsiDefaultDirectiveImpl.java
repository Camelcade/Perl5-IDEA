// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import java.util.List;
import com.perl5.lang.tt2.psi.PsiAssignExpr;import com.perl5.lang.tt2.psi.PsiDefaultDirective;import com.perl5.lang.tt2.psi.PsiDirectivePostfix;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

public class PsiDefaultDirectiveImpl extends TemplateToolkitCompositeElementImpl implements PsiDefaultDirective {

  public PsiDefaultDirectiveImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitDefaultDirective(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiAssignExpr> getAssignExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiAssignExpr.class);
  }

  @Override
  @Nullable
  public PsiDirectivePostfix getDirectivePostfix() {
    return findChildByClass(PsiDirectivePostfix.class);
  }

}
