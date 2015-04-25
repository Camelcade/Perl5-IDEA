// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.perl5.lang.perl.psi.*;

public class PerlPackageNamespaceImpl extends ASTWrapperPsiElement implements PerlPackageNamespace {

  public PerlPackageNamespaceImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitPackageNamespace(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PerlCodeLine> getCodeLineList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlCodeLine.class);
  }

  @Override
  @NotNull
  public List<PerlFunctionDefinition> getFunctionDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlFunctionDefinition.class);
  }

  @Override
  @NotNull
  public List<PerlIfBlock> getIfBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlIfBlock.class);
  }

  @Override
  @NotNull
  public PerlPackageDefinition getPackageDefinition() {
    return findNotNullChildByClass(PerlPackageDefinition.class);
  }

  @Override
  @NotNull
  public List<PerlPerlBlock> getPerlBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPerlBlock.class);
  }

}
