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

public class PerlPackageUseArgumentsImpl extends ASTWrapperPsiElement implements PerlPackageUseArguments {

  public PerlPackageUseArgumentsImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitPackageUseArguments(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlArrayValue getArrayValue() {
    return findChildByClass(PerlArrayValue.class);
  }

  @Override
  @Nullable
  public PerlPackageBare getPackageBare() {
    return findChildByClass(PerlPackageBare.class);
  }

}
