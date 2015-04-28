// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlArrayElement extends PsiElement {

  @Nullable
  PerlArray getArray();

  @Nullable
  PerlArrayDereference getArrayDereference();

  @Nullable
  PerlArraySlice getArraySlice();

  @Nullable
  PerlHashSlice getHashSlice();

  @Nullable
  PerlHashValue getHashValue();

  @Nullable
  PerlScalarValue getScalarValue();

}
