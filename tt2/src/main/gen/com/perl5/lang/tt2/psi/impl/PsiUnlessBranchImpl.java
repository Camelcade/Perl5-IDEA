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

public class PsiUnlessBranchImpl extends TemplateToolkitCompositeElementImpl implements PsiUnlessBranch {

  public PsiUnlessBranchImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitUnlessBranch(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiUnlessDirective getUnlessDirective() {
    return findNotNullChildByClass(PsiUnlessDirective.class);
  }

}
