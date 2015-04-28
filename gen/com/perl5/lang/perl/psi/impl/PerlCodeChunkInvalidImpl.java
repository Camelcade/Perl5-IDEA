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

public class PerlCodeChunkInvalidImpl extends ASTWrapperPsiElement implements PerlCodeChunkInvalid {

  public PerlCodeChunkInvalidImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitCodeChunkInvalid(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlEvalInvalid getEvalInvalid() {
    return findChildByClass(PerlEvalInvalid.class);
  }

  @Override
  @Nullable
  public PerlPackageNoInvalid getPackageNoInvalid() {
    return findChildByClass(PerlPackageNoInvalid.class);
  }

  @Override
  @Nullable
  public PerlPackageRequireInvalid getPackageRequireInvalid() {
    return findChildByClass(PerlPackageRequireInvalid.class);
  }

  @Override
  @Nullable
  public PerlPackageUseInvalid getPackageUseInvalid() {
    return findChildByClass(PerlPackageUseInvalid.class);
  }

}
