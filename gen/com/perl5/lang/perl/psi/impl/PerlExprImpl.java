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

public class PerlExprImpl extends ASTWrapperPsiElement implements PerlExpr {

  public PerlExprImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitExpr(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlArrayValue getArrayValue() {
    return findChildByClass(PerlArrayValue.class);
  }

  @Override
  @Nullable
  public PerlOp10 getOp10() {
    return findChildByClass(PerlOp10.class);
  }

  @Override
  @Nullable
  public PerlOp11 getOp11() {
    return findChildByClass(PerlOp11.class);
  }

  @Override
  @Nullable
  public PerlOp12 getOp12() {
    return findChildByClass(PerlOp12.class);
  }

  @Override
  @Nullable
  public PerlOp13 getOp13() {
    return findChildByClass(PerlOp13.class);
  }

  @Override
  @Nullable
  public PerlOp14 getOp14() {
    return findChildByClass(PerlOp14.class);
  }

  @Override
  @Nullable
  public PerlOp15 getOp15() {
    return findChildByClass(PerlOp15.class);
  }

  @Override
  @Nullable
  public PerlOp16 getOp16() {
    return findChildByClass(PerlOp16.class);
  }

  @Override
  @Nullable
  public PerlOp17 getOp17() {
    return findChildByClass(PerlOp17.class);
  }

  @Override
  @Nullable
  public PerlOp18 getOp18() {
    return findChildByClass(PerlOp18.class);
  }

  @Override
  @Nullable
  public PerlOp19 getOp19() {
    return findChildByClass(PerlOp19.class);
  }

  @Override
  @Nullable
  public PerlOp21 getOp21() {
    return findChildByClass(PerlOp21.class);
  }

  @Override
  @Nullable
  public PerlOp22 getOp22() {
    return findChildByClass(PerlOp22.class);
  }

  @Override
  @Nullable
  public PerlOp23 getOp23() {
    return findChildByClass(PerlOp23.class);
  }

  @Override
  @Nullable
  public PerlOp24 getOp24() {
    return findChildByClass(PerlOp24.class);
  }

  @Override
  @Nullable
  public PerlOp3 getOp3() {
    return findChildByClass(PerlOp3.class);
  }

  @Override
  @Nullable
  public PerlOp4 getOp4() {
    return findChildByClass(PerlOp4.class);
  }

  @Override
  @Nullable
  public PerlOp5 getOp5() {
    return findChildByClass(PerlOp5.class);
  }

  @Override
  @Nullable
  public PerlOp6 getOp6() {
    return findChildByClass(PerlOp6.class);
  }

  @Override
  @Nullable
  public PerlOp7 getOp7() {
    return findChildByClass(PerlOp7.class);
  }

  @Override
  @Nullable
  public PerlOp8 getOp8() {
    return findChildByClass(PerlOp8.class);
  }

  @Override
  @Nullable
  public PerlOp9 getOp9() {
    return findChildByClass(PerlOp9.class);
  }

  @Override
  @Nullable
  public PerlScalarValue getScalarValue() {
    return findChildByClass(PerlScalarValue.class);
  }

}
