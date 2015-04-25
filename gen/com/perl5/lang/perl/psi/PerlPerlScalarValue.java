// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlPerlScalarValue extends PsiElement {

  @Nullable
  PerlPerlArrayValue getPerlArrayValue();

  @Nullable
  PerlPerlScalar getPerlScalar();

  @Nullable
  PerlPerlScalarFunctionResult getPerlScalarFunctionResult();

  @Nullable
  PerlPerlScalarValue getPerlScalarValue();

  @Nullable
  PsiElement getPerlDqString();

  @Nullable
  PsiElement getPerlDxString();

  @Nullable
  PsiElement getPerlMultilineMarker();

  @Nullable
  PsiElement getPerlNumber();

  @Nullable
  PsiElement getPerlSigilScalar();

  @Nullable
  PsiElement getPerlSqString();

  @Nullable
  PsiElement getPerlVersion();

}
