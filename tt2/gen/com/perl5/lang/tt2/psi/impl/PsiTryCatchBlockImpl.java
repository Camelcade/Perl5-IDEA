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

public class PsiTryCatchBlockImpl extends TemplateToolkitCompositeElementImpl implements PsiTryCatchBlock {

  public PsiTryCatchBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitTryCatchBlock(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiCatchBranch> getCatchBranchList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiCatchBranch.class);
  }

  @Override
  @NotNull
  public PsiEndDirective getEndDirective() {
    return findNotNullChildByClass(PsiEndDirective.class);
  }

  @Override
  @Nullable
  public PsiFinalBranch getFinalBranch() {
    return findChildByClass(PsiFinalBranch.class);
  }

  @Override
  @NotNull
  public PsiTryBranch getTryBranch() {
    return findNotNullChildByClass(PsiTryBranch.class);
  }

}
