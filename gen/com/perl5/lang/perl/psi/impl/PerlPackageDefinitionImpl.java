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

public class PerlPackageDefinitionImpl extends ASTWrapperPsiElement implements PerlPackageDefinition {

  public PerlPackageDefinitionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitPackageDefinition(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlBlock getBlock() {
    return findChildByClass(PerlBlock.class);
  }

  @Override
  @NotNull
  public List<PerlBlockBlock> getBlockBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlBlockBlock.class);
  }

  @Override
  @NotNull
  public List<PerlCodeLine> getCodeLineList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlCodeLine.class);
  }

  @Override
  @NotNull
  public List<PerlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlExpr.class);
  }

  @Override
  @NotNull
  public List<PerlForBlock> getForBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlForBlock.class);
  }

  @Override
  @NotNull
  public List<PerlGivenBlock> getGivenBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlGivenBlock.class);
  }

  @Override
  @NotNull
  public List<PerlIfBlock> getIfBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlIfBlock.class);
  }

  @Override
  @NotNull
  public List<PerlIfPostfix> getIfPostfixList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlIfPostfix.class);
  }

  @Override
  @NotNull
  public List<PerlPackageNo> getPackageNoList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPackageNo.class);
  }

  @Override
  @NotNull
  public List<PerlPackageRequire> getPackageRequireList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPackageRequire.class);
  }

  @Override
  @NotNull
  public List<PerlPackageUse> getPackageUseList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPackageUse.class);
  }

  @Override
  @Nullable
  public PerlPerlVersion getPerlVersion() {
    return findChildByClass(PerlPerlVersion.class);
  }

  @Override
  @NotNull
  public List<PerlSubBlockNamed> getSubBlockNamedList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlSubBlockNamed.class);
  }

  @Override
  @NotNull
  public List<PerlWhileBlock> getWhileBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlWhileBlock.class);
  }

}
