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

public class PerlCompoundStatementImpl extends ASTWrapperPsiElement implements PerlCompoundStatement {

  public PerlCompoundStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitCompoundStatement(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlBlockCompound getBlockCompound() {
    return findChildByClass(PerlBlockCompound.class);
  }

  @Override
  @Nullable
  public PerlForCompound getForCompound() {
    return findChildByClass(PerlForCompound.class);
  }

  @Override
  @Nullable
  public PerlForeachCompound getForeachCompound() {
    return findChildByClass(PerlForeachCompound.class);
  }

  @Override
  @Nullable
  public PerlGivenCompound getGivenCompound() {
    return findChildByClass(PerlGivenCompound.class);
  }

  @Override
  @Nullable
  public PerlIfCompound getIfCompound() {
    return findChildByClass(PerlIfCompound.class);
  }

  @Override
  @Nullable
  public PerlLabelDeclaration getLabelDeclaration() {
    return findChildByClass(PerlLabelDeclaration.class);
  }

  @Override
  @Nullable
  public PerlUnlessCompound getUnlessCompound() {
    return findChildByClass(PerlUnlessCompound.class);
  }

  @Override
  @Nullable
  public PerlUntilCompound getUntilCompound() {
    return findChildByClass(PerlUntilCompound.class);
  }

  @Override
  @Nullable
  public PerlWhileCompound getWhileCompound() {
    return findChildByClass(PerlWhileCompound.class);
  }

}
