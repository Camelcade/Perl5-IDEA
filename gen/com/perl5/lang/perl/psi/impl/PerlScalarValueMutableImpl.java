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

public class PerlScalarValueMutableImpl extends ASTWrapperPsiElement implements PerlScalarValueMutable {

  public PerlScalarValueMutableImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitScalarValueMutable(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlScalar getScalar() {
    return findChildByClass(PerlScalar.class);
  }

  @Override
  @Nullable
  public PerlScalarArrayElement getScalarArrayElement() {
    return findChildByClass(PerlScalarArrayElement.class);
  }

  @Override
  @Nullable
  public PerlScalarDereference getScalarDereference() {
    return findChildByClass(PerlScalarDereference.class);
  }

  @Override
  @Nullable
  public PerlScalarFunctionResult getScalarFunctionResult() {
    return findChildByClass(PerlScalarFunctionResult.class);
  }

  @Override
  @Nullable
  public PerlScalarHashElement getScalarHashElement() {
    return findChildByClass(PerlScalarHashElement.class);
  }

}
