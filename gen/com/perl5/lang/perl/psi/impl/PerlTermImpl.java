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

public class PerlTermImpl extends ASTWrapperPsiElement implements PerlTerm {

  public PerlTermImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitTerm(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlExpr getExpr() {
    return findChildByClass(PerlExpr.class);
  }

  @Override
  @Nullable
  public PerlPackageRequire getPackageRequire() {
    return findChildByClass(PerlPackageRequire.class);
  }

  @Override
  @Nullable
  public PerlRegex getRegex() {
    return findChildByClass(PerlRegex.class);
  }

  @Override
  @Nullable
  public PerlScalarAnonArray getScalarAnonArray() {
    return findChildByClass(PerlScalarAnonArray.class);
  }

  @Override
  @Nullable
  public PerlScalarAnonHash getScalarAnonHash() {
    return findChildByClass(PerlScalarAnonHash.class);
  }

  @Override
  @Nullable
  public PerlScalarArrayElement getScalarArrayElement() {
    return findChildByClass(PerlScalarArrayElement.class);
  }

  @Override
  @Nullable
  public PerlScalarGeneratedListItem getScalarGeneratedListItem() {
    return findChildByClass(PerlScalarGeneratedListItem.class);
  }

  @Override
  @Nullable
  public PerlScalarHashElement getScalarHashElement() {
    return findChildByClass(PerlScalarHashElement.class);
  }

  @Override
  @Nullable
  public PerlSubBlockAnon getSubBlockAnon() {
    return findChildByClass(PerlSubBlockAnon.class);
  }

}
