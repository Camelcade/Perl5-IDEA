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

public class PerlSubexpressionImpl extends ASTWrapperPsiElement implements PerlSubexpression {

  public PerlSubexpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitSubexpression(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PerlArrayValue> getArrayValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlArrayValue.class);
  }

  @Override
  @NotNull
  public List<PerlFunctionCall> getFunctionCallList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlFunctionCall.class);
  }

  @Override
  @NotNull
  public List<PerlGlob> getGlobList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlGlob.class);
  }

  @Override
  @NotNull
  public List<PerlHashValue> getHashValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlHashValue.class);
  }

  @Override
  @NotNull
  public List<PerlMethodCall> getMethodCallList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlMethodCall.class);
  }

  @Override
  @NotNull
  public List<PerlScalarValue> getScalarValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlScalarValue.class);
  }

}
