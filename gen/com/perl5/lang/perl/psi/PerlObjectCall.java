// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlObjectCall extends PsiElement {

  @Nullable
  PerlFunctionCall getFunctionCall();

  @NotNull
  PerlFunctionCallAny getFunctionCallAny();

  @Nullable
  PerlScalar getScalar();

  @Nullable
  PerlScalarValue getScalarValue();

  @Nullable
  PsiElement getPerlSigilScalar();

}
