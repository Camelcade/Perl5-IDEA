// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlArray extends PsiElement {

  @NotNull
  List<PerlExpr> getExprList();

  @Nullable
  PerlPackageRequire getPackageRequire();

  @Nullable
  PerlRegex getRegex();

  @Nullable
  PerlScalarAnonArray getScalarAnonArray();

  @Nullable
  PerlScalarAnonHash getScalarAnonHash();

  @Nullable
  PerlScalarArrayElement getScalarArrayElement();

  @Nullable
  PerlScalarGeneratedListItem getScalarGeneratedListItem();

  @Nullable
  PerlScalarHashElement getScalarHashElement();

  @Nullable
  PerlSubBlockAnon getSubBlockAnon();

}
