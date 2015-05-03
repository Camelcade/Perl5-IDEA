// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlCalee extends PsiElement {

  @Nullable
  PerlExpr getExpr();

  @Nullable
  PerlObjectMethod getObjectMethod();

  @Nullable
  PerlObjectMethodObject getObjectMethodObject();

  @Nullable
  PerlPackageMethod getPackageMethod();

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
