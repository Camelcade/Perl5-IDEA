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

public class PerlPackageObjectCallImpl extends ASTWrapperPsiElement implements PerlPackageObjectCall {

  public PerlPackageObjectCallImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitPackageObjectCall(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PerlFunctionCallAny getFunctionCallAny() {
    return findNotNullChildByClass(PerlFunctionCallAny.class);
  }

  @Override
  @NotNull
  public PerlPackageBare getPackageBare() {
    return findNotNullChildByClass(PerlPackageBare.class);
  }

}
