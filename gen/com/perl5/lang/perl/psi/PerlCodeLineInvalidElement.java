// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlCodeLineInvalidElement extends PsiElement {

  @Nullable
  PerlArrayValue getArrayValue();

  @Nullable
  PerlFunction getFunction();

  @Nullable
  PerlFunctionCall getFunctionCall();

  @Nullable
  PerlGlob getGlob();

  @Nullable
  PerlHashValue getHashValue();

  @Nullable
  PerlMethodCall getMethodCall();

  @Nullable
  PerlPackageBare getPackageBare();

  @Nullable
  PerlScalarValue getScalarValue();

}
