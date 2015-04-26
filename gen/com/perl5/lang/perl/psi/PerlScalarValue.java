// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlScalarValue extends PsiElement {

  @Nullable
  PerlArrayValue getArrayValue();

  @Nullable
  PerlFunctionCall getFunctionCall();

  @Nullable
  PerlScalar getScalar();

  @Nullable
  PerlScalarValue getScalarValue();

  @Nullable
  PerlString getString();

}
