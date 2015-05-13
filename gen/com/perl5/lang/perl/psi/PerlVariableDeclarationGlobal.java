// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlVariableDeclarationGlobal extends PsiElement {

  @NotNull
  List<PerlArrayVariable> getArrayVariableList();

  @NotNull
  List<PerlExpr> getExprList();

  @NotNull
  List<PerlHashVariable> getHashVariableList();

  @NotNull
  List<PerlScalarVariable> getScalarVariableList();

  @NotNull
  List<PerlString> getStringList();

  @NotNull
  List<PerlUndefTerm> getUndefTermList();

}
