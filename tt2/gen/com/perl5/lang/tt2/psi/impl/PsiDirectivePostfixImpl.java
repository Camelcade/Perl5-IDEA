// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.tt2.lexer.TemplateToolkitElementTypesGenerated.*;
import com.perl5.lang.tt2.psi.*;

public class PsiDirectivePostfixImpl extends TemplateToolkitCompositeElementImpl implements PsiDirectivePostfix {

  public PsiDirectivePostfixImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitDirectivePostfix(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiFilterElementExpr> getFilterElementExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiFilterElementExpr.class);
  }

  @Override
  @Nullable
  public PsiForeachDirective getForeachDirective() {
    return findChildByClass(PsiForeachDirective.class);
  }

  @Override
  @Nullable
  public PsiIfDirective getIfDirective() {
    return findChildByClass(PsiIfDirective.class);
  }

  @Override
  @Nullable
  public PsiUnlessDirective getUnlessDirective() {
    return findChildByClass(PsiUnlessDirective.class);
  }

  @Override
  @Nullable
  public PsiWhileDirective getWhileDirective() {
    return findChildByClass(PsiWhileDirective.class);
  }

  @Override
  @Nullable
  public PsiWrapperDirective getWrapperDirective() {
    return findChildByClass(PsiWrapperDirective.class);
  }

}
