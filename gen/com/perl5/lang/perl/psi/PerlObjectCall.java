// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlObjectCall extends PsiElement {

  @NotNull
  PerlFunctionCall getFunctionCall();

  @Nullable
  PerlPerlScalarFunctionResult getPerlScalarFunctionResult();

  @Nullable
  PerlPerlScalarValue getPerlScalarValue();

  @Nullable
  PsiElement getPerlSigilScalar();

  @Nullable
  PsiElement getPerlVariableScalar();

  @Nullable
  PsiElement getPerlVariableScalarBuiltIn();

}
