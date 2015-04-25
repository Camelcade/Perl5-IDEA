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

public class PerlObjectCallImpl extends ASTWrapperPsiElement implements PerlObjectCall {

  public PerlObjectCallImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitObjectCall(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PerlFunctionCallAny getFunctionCallAny() {
    return findNotNullChildByClass(PerlFunctionCallAny.class);
  }

  @Override
  @Nullable
  public PerlPerlScalar getPerlScalar() {
    return findChildByClass(PerlPerlScalar.class);
  }

  @Override
  @Nullable
  public PerlPerlScalarFunctionResult getPerlScalarFunctionResult() {
    return findChildByClass(PerlPerlScalarFunctionResult.class);
  }

  @Override
  @Nullable
  public PerlPerlScalarValue getPerlScalarValue() {
    return findChildByClass(PerlPerlScalarValue.class);
  }

  @Override
  @Nullable
  public PsiElement getPerlSigilScalar() {
    return findChildByType(PERL_SIGIL_SCALAR);
  }

}
