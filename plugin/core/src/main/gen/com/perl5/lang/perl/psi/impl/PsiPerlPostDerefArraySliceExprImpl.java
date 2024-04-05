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

public class PsiPerlPostDerefArraySliceExprImpl extends PsiPerlExprImpl implements PsiPerlPostDerefArraySliceExpr {

  public PsiPerlPostDerefArraySliceExprImpl(ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitPostDerefArraySliceExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiPerlArrayIndex getArrayIndex() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlArrayIndex.class);
  }

  @Override
  @Nullable
  public PsiPerlHashIndex getHashIndex() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlHashIndex.class);
  }

}
