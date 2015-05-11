// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlVariableDeclarationLocal extends PsiElement {

  @NotNull
  List<PerlExpr> getExprList();

  @NotNull
  List<PerlPerlArray> getPerlArrayList();

  @NotNull
  List<PerlPerlGlob> getPerlGlobList();

  @NotNull
  List<PerlPerlHash> getPerlHashList();

  @NotNull
  List<PerlPerlScalar> getPerlScalarList();

}
