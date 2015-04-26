// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlSubexpression extends PsiElement {

  @NotNull
  List<PerlArrayValue> getArrayValueList();

  @NotNull
  List<PerlFunctionCall> getFunctionCallList();

  @NotNull
  List<PerlGlob> getGlobList();

  @NotNull
  List<PerlHashValue> getHashValueList();

  @NotNull
  List<PerlMethodCall> getMethodCallList();

  @NotNull
  List<PerlScalarValue> getScalarValueList();

}
