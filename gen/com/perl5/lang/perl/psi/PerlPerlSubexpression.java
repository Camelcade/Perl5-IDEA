// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlPerlSubexpression extends PsiElement {

  @NotNull
  List<PerlFunctionCall> getFunctionCallList();

  @NotNull
  List<PerlMethodCall> getMethodCallList();

  @NotNull
  List<PerlPerlArrayValue> getPerlArrayValueList();

  @NotNull
  List<PerlPerlGlob> getPerlGlobList();

  @NotNull
  List<PerlPerlHashValue> getPerlHashValueList();

  @NotNull
  List<PerlPerlScalarValue> getPerlScalarValueList();

}
