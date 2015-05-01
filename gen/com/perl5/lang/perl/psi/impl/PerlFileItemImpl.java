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

public class PerlFileItemImpl extends ASTWrapperPsiElement implements PerlFileItem {

  public PerlFileItemImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitFileItem(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlBlockBlock getBlockBlock() {
    return findChildByClass(PerlBlockBlock.class);
  }

  @Override
  @Nullable
  public PerlCodeLine getCodeLine() {
    return findChildByClass(PerlCodeLine.class);
  }

  @Override
  @Nullable
  public PerlEval getEval() {
    return findChildByClass(PerlEval.class);
  }

  @Override
  @Nullable
  public PerlExpr getExpr() {
    return findChildByClass(PerlExpr.class);
  }

  @Override
  @Nullable
  public PerlForBlock getForBlock() {
    return findChildByClass(PerlForBlock.class);
  }

  @Override
  @Nullable
  public PerlGivenBlock getGivenBlock() {
    return findChildByClass(PerlGivenBlock.class);
  }

  @Override
  @Nullable
  public PerlIfBlock getIfBlock() {
    return findChildByClass(PerlIfBlock.class);
  }

  @Override
  @Nullable
  public PerlPackageDefinition getPackageDefinition() {
    return findChildByClass(PerlPackageDefinition.class);
  }

  @Override
  @Nullable
  public PerlPackageNo getPackageNo() {
    return findChildByClass(PerlPackageNo.class);
  }

  @Override
  @Nullable
  public PerlPackageRequire getPackageRequire() {
    return findChildByClass(PerlPackageRequire.class);
  }

  @Override
  @Nullable
  public PerlPackageUse getPackageUse() {
    return findChildByClass(PerlPackageUse.class);
  }

  @Override
  @Nullable
  public PerlSubBlockNamed getSubBlockNamed() {
    return findChildByClass(PerlSubBlockNamed.class);
  }

  @Override
  @Nullable
  public PerlWhileBlock getWhileBlock() {
    return findChildByClass(PerlWhileBlock.class);
  }

}
