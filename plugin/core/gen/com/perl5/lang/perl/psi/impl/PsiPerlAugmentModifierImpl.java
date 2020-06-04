// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.mixins.PerlMethodModifierMixin;
import com.perl5.lang.perl.psi.*;

public class PsiPerlAugmentModifierImpl extends PerlMethodModifierMixin implements PsiPerlAugmentModifier {

  public PsiPerlAugmentModifierImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitAugmentModifier(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiPerlAttributes getAttributes() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAttributes.class);
  }

  @Override
  @Nullable
  public PsiPerlBlock getBlock() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlBlock.class);
  }

  @Override
  @Nullable
  public PsiPerlSignatureContent getSignatureContent() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlSignatureContent.class);
  }

}