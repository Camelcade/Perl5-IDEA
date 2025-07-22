// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi.impl;

import com.perl5.lang.tt2.psi.PsiMacroContent;import com.perl5.lang.tt2.psi.PsiMacroDirective;import com.perl5.lang.tt2.psi.PsiMacroName;import com.perl5.lang.tt2.psi.PsiTemplateToolkitVisitorGenerated;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

public class PsiMacroDirectiveImpl extends TemplateToolkitCompositeElementImpl implements PsiMacroDirective {

  public PsiMacroDirectiveImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTemplateToolkitVisitorGenerated visitor) {
    visitor.visitMacroDirective(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTemplateToolkitVisitorGenerated) accept((PsiTemplateToolkitVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiMacroContent getMacroContent() {
    return findChildByClass(PsiMacroContent.class);
  }

  @Override
  @Nullable
  public PsiMacroName getMacroName() {
    return findChildByClass(PsiMacroName.class);
  }

}
