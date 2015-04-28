// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlCodeLineElement extends PsiElement {

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
  PerlMultilineMarker getMultilineMarker();

  @Nullable
  PerlMultilineString getMultilineString();

  @Nullable
  PerlScalarValue getScalarValue();

  @Nullable
  PerlVariableDefinition getVariableDefinition();

}
