// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlCodeLineInvalidElement extends PsiElement {

  @Nullable
  PerlFunctionCall getFunctionCall();

  @Nullable
  PerlMethodCall getMethodCall();

  @Nullable
  PerlPerlArrayValue getPerlArrayValue();

  @Nullable
  PerlPerlHashValue getPerlHashValue();

  @Nullable
  PerlPerlScalarValue getPerlScalarValue();

  @Nullable
  PsiElement getPerlFunctionBuiltIn();

  @Nullable
  PsiElement getPerlFunctionUser();

  @Nullable
  PsiElement getPerlMultilineDq();

  @Nullable
  PsiElement getPerlMultilineHtml();

  @Nullable
  PsiElement getPerlMultilineMarker();

  @Nullable
  PsiElement getPerlMultilineSq();

  @Nullable
  PsiElement getPerlMultilineXml();

  @Nullable
  PsiElement getPerlOperator();

  @Nullable
  PsiElement getPerlPackageBuiltIn();

  @Nullable
  PsiElement getPerlPackageBuiltInDeprecated();

  @Nullable
  PsiElement getPerlPackageBuiltInPragma();

  @Nullable
  PsiElement getPerlPackageUser();

  @Nullable
  PsiElement getPerlVariableGlob();

  @Nullable
  PsiElement getPerlVariableGlobBuiltIn();

  @Nullable
  PsiElement getPerlVersion();

}
