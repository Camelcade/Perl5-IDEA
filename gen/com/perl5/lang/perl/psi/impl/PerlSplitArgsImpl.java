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

public class PerlSplitArgsImpl extends ASTWrapperPsiElement implements PerlSplitArgs {

  public PerlSplitArgsImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitSplitArgs(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PerlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlExpr.class);
  }

  @Override
  @NotNull
  public List<PerlPackageRequire> getPackageRequireList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPackageRequire.class);
  }

  @Override
  @NotNull
  public List<PerlRegex> getRegexList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlRegex.class);
  }

  @Override
  @NotNull
  public List<PerlScalarAnonArray> getScalarAnonArrayList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlScalarAnonArray.class);
  }

  @Override
  @NotNull
  public List<PerlScalarAnonHash> getScalarAnonHashList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlScalarAnonHash.class);
  }

  @Override
  @NotNull
  public List<PerlScalarArrayElement> getScalarArrayElementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlScalarArrayElement.class);
  }

  @Override
  @NotNull
  public List<PerlScalarGeneratedListItem> getScalarGeneratedListItemList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlScalarGeneratedListItem.class);
  }

  @Override
  @NotNull
  public List<PerlScalarHashElement> getScalarHashElementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlScalarHashElement.class);
  }

  @Override
  @NotNull
  public List<PerlSubBlockAnon> getSubBlockAnonList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlSubBlockAnon.class);
  }

}
