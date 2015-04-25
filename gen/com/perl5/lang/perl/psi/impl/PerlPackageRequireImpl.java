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

public class PerlPackageRequireImpl extends ASTWrapperPsiElement implements PerlPackageRequire {

  public PerlPackageRequireImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitPackageRequire(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getPerlDqString() {
    return findChildByType(PERL_DQ_STRING);
  }

  @Override
  @Nullable
  public PsiElement getPerlDxString() {
    return findChildByType(PERL_DX_STRING);
  }

  @Override
  @Nullable
  public PsiElement getPerlPackageBuiltIn() {
    return findChildByType(PERL_PACKAGE_BUILT_IN);
  }

  @Override
  @Nullable
  public PsiElement getPerlPackageBuiltInDeprecated() {
    return findChildByType(PERL_PACKAGE_BUILT_IN_DEPRECATED);
  }

  @Override
  @Nullable
  public PsiElement getPerlPackageBuiltInPragma() {
    return findChildByType(PERL_PACKAGE_BUILT_IN_PRAGMA);
  }

  @Override
  @Nullable
  public PsiElement getPerlPackageUser() {
    return findChildByType(PERL_PACKAGE_USER);
  }

  @Override
  @Nullable
  public PsiElement getPerlSqString() {
    return findChildByType(PERL_SQ_STRING);
  }

  @Override
  @Nullable
  public PsiElement getPerlVersion() {
    return findChildByType(PERL_VERSION);
  }

}
