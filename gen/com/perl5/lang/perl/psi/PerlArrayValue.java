// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlArrayValue extends PsiElement {

  @NotNull
  List<PerlArray> getArrayList();

  @NotNull
  List<PerlArrayValue> getArrayValueList();

  @NotNull
  List<PerlHashValue> getHashValueList();

  @NotNull
  List<PerlScalarValue> getScalarValueList();

}
