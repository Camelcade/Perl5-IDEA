// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiPerlDerefExpr extends PsiPerlExpr, PerlDerefExpression {

  @NotNull
  List<PsiPerlArrayIndex> getArrayIndexList();

  @NotNull
  List<PsiPerlExpr> getExprList();

  @NotNull
  List<PsiPerlHashIndex> getHashIndexList();

  @NotNull
  List<PsiPerlParenthesisedCallArguments> getParenthesisedCallArgumentsList();

  @NotNull
  List<PsiPerlScalarCall> getScalarCallList();

}
