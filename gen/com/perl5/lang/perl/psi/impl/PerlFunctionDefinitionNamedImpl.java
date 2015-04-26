// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypes.*;
import com.perl5.lang.perl.psi.*;

public class PerlFunctionDefinitionNamedImpl extends PerlFunctionDefinitionNamedUtil implements PerlFunctionDefinitionNamed {

  public PerlFunctionDefinitionNamedImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitFunctionDefinitionNamed(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PerlPerlBlock getPerlBlock() {
    return findNotNullChildByClass(PerlPerlBlock.class);
  }

  @Override
  @Nullable
  public PsiElement getPerlFunctionBuiltIn() {
    return findChildByType(PERL_FUNCTION_BUILT_IN);
  }

  @Override
  @Nullable
  public PsiElement getPerlFunctionUser() {
    return findChildByType(PERL_FUNCTION_USER);
  }

}
