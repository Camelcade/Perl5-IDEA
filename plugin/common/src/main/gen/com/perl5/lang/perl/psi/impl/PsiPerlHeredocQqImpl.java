// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.*;

public class PsiPerlHeredocQqImpl extends PerlCompositeElementImpl implements PsiPerlHeredocQq {

  public PsiPerlHeredocQqImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitHeredocQq(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiPerlDerefExpr> getDerefExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlDerefExpr.class);
  }

  @Override
  @NotNull
  public List<PsiPerlEscChar> getEscCharList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlEscChar.class);
  }

  @Override
  @NotNull
  public List<PsiPerlHexChar> getHexCharList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlHexChar.class);
  }

  @Override
  @NotNull
  public List<PsiPerlOctChar> getOctCharList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlOctChar.class);
  }

  @Override
  @NotNull
  public List<PsiPerlUnicodeChar> getUnicodeCharList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlUnicodeChar.class);
  }

}
