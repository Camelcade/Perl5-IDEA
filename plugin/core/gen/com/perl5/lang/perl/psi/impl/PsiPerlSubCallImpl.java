// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementStub;

public class PsiPerlSubCallImpl extends PerlSubCallElement implements PsiPerlSubCall {

  public PsiPerlSubCallImpl(ASTNode node) {
    super(node);
  }

  public PsiPerlSubCallImpl(PerlSubCallElementStub stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitSubCall(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiPerlCallArguments getCallArguments() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlCallArguments.class);
  }

  @Override
  @Nullable
  public PsiPerlExpr getExpr() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlExpr.class);
  }

  @Override
  @Nullable
  public PsiPerlMethod getMethod() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlMethod.class);
  }

  @Override
  @Nullable
  public PsiPerlParenthesisedCallArguments getParenthesisedCallArguments() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlParenthesisedCallArguments.class);
  }

}
