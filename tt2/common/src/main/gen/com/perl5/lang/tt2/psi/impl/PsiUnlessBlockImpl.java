// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.tt2.parser.TemplateToolkitElementTypesGenerated.*;
import com.perl5.lang.tt2.psi.*;

public class PsiUnlessBlockImpl extends TemplateToolkitCompositeElementImpl implements PsiUnlessBlock {

  public PsiUnlessBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitUnlessBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElseBranch getElseBranch() {
    return findChildByClass(PsiElseBranch.class);
  }

  @Override
  @NotNull
  public List<PsiElsifBranch> getElsifBranchList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiElsifBranch.class);
  }

  @Override
  @NotNull
  public PsiEndDirective getEndDirective() {
    return findNotNullChildByClass(PsiEndDirective.class);
  }

  @Override
  @NotNull
  public PsiUnlessBranch getUnlessBranch() {
    return findNotNullChildByClass(PsiUnlessBranch.class);
  }

}
