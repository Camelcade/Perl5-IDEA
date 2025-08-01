// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import com.perl5.lang.tt2.psi.*;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.tt2.psi.*;

public class PsiUseDirectiveImpl extends TemplateToolkitCompositeElementImpl implements PsiUseDirective {

  public PsiUseDirectiveImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitUseDirective(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiModuleName getModuleName() {
    return findChildByClass(PsiModuleName.class);
  }

  @Override
  @Nullable
  public PsiModuleParams getModuleParams() {
    return findChildByClass(PsiModuleParams.class);
  }

  @Override
  @Nullable
  public PsiUseInstance getUseInstance() {
    return findChildByClass(PsiUseInstance.class);
  }

}
