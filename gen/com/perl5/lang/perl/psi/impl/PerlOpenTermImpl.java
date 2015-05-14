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

public class PerlOpenTermImpl extends ASTWrapperPsiElement implements PerlOpenTerm {

  public PerlOpenTermImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitOpenTerm(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlExpr getExpr() {
    return findChildByClass(PerlExpr.class);
  }

  @Override
  @NotNull
  public PerlOpenHandle getOpenHandle() {
    return findNotNullChildByClass(PerlOpenHandle.class);
  }

  @Override
  @Nullable
  public PerlOpenMode getOpenMode() {
    return findChildByClass(PerlOpenMode.class);
  }

  @Override
  @Nullable
  public PerlOpenRef getOpenRef() {
    return findChildByClass(PerlOpenRef.class);
  }

}
