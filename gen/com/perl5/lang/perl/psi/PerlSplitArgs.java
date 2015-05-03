// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlSplitArgs extends PsiElement {

  @NotNull
  List<PerlExpr> getExprList();

  @NotNull
  List<PerlPackageRequire> getPackageRequireList();

  @NotNull
  List<PerlRegex> getRegexList();

  @NotNull
  List<PerlScalarAnonArray> getScalarAnonArrayList();

  @NotNull
  List<PerlScalarAnonHash> getScalarAnonHashList();

  @NotNull
  List<PerlScalarArrayElement> getScalarArrayElementList();

  @NotNull
  List<PerlScalarGeneratedListItem> getScalarGeneratedListItemList();

  @NotNull
  List<PerlScalarHashElement> getScalarHashElementList();

  @NotNull
  List<PerlSubBlockAnon> getSubBlockAnonList();

}
