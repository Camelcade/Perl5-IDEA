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

public class PerlPerlHashValueImpl extends ASTWrapperPsiElement implements PerlPerlHashValue {

  public PerlPerlHashValueImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitPerlHashValue(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlPerlScalarValue getPerlScalarValue() {
    return findChildByClass(PerlPerlScalarValue.class);
  }

  @Override
  @Nullable
  public PsiElement getPerlSigilHash() {
    return findChildByType(PERL_SIGIL_HASH);
  }

  @Override
  @Nullable
  public PsiElement getPerlVariableHash() {
    return findChildByType(PERL_VARIABLE_HASH);
  }

  @Override
  @Nullable
  public PsiElement getPerlVariableHashBuiltIn() {
    return findChildByType(PERL_VARIABLE_HASH_BUILT_IN);
  }

}
