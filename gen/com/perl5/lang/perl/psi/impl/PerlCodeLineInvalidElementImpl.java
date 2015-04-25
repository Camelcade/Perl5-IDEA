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

public class PerlCodeLineInvalidElementImpl extends ASTWrapperPsiElement implements PerlCodeLineInvalidElement {

  public PerlCodeLineInvalidElementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitCodeLineInvalidElement(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlFunctionCall getFunctionCall() {
    return findChildByClass(PerlFunctionCall.class);
  }

  @Override
  @Nullable
  public PerlMethodCall getMethodCall() {
    return findChildByClass(PerlMethodCall.class);
  }

  @Override
  @Nullable
  public PerlPerlArrayValue getPerlArrayValue() {
    return findChildByClass(PerlPerlArrayValue.class);
  }

  @Override
  @Nullable
  public PerlPerlHashValue getPerlHashValue() {
    return findChildByClass(PerlPerlHashValue.class);
  }

  @Override
  @Nullable
  public PerlPerlScalarValue getPerlScalarValue() {
    return findChildByClass(PerlPerlScalarValue.class);
  }

  @Override
  @Nullable
  public PsiElement getPerlFunctionBuiltIn() {
    return findChildByType(PERL_FUNCTION_BUILT_IN);
  }

  @Override
  @Nullable
  public PsiElement getPerlFunctionUser() {
    return findChildByType(PERL_FUNCTION_USER);
  }

  @Override
  @Nullable
  public PsiElement getPerlMultilineDq() {
    return findChildByType(PERL_MULTILINE_DQ);
  }

  @Override
  @Nullable
  public PsiElement getPerlMultilineDx() {
    return findChildByType(PERL_MULTILINE_DX);
  }

  @Override
  @Nullable
  public PsiElement getPerlMultilineHtml() {
    return findChildByType(PERL_MULTILINE_HTML);
  }

  @Override
  @Nullable
  public PsiElement getPerlMultilineMarker() {
    return findChildByType(PERL_MULTILINE_MARKER);
  }

  @Override
  @Nullable
  public PsiElement getPerlMultilineSq() {
    return findChildByType(PERL_MULTILINE_SQ);
  }

  @Override
  @Nullable
  public PsiElement getPerlMultilineXml() {
    return findChildByType(PERL_MULTILINE_XML);
  }

  @Override
  @Nullable
  public PsiElement getPerlOperator() {
    return findChildByType(PERL_OPERATOR);
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
  public PsiElement getPerlVariableGlob() {
    return findChildByType(PERL_VARIABLE_GLOB);
  }

  @Override
  @Nullable
  public PsiElement getPerlVariableGlobBuiltIn() {
    return findChildByType(PERL_VARIABLE_GLOB_BUILT_IN);
  }

  @Override
  @Nullable
  public PsiElement getPerlVersion() {
    return findChildByType(PERL_VERSION);
  }

}
