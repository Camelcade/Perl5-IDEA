// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiPerlVariableDeclarationGlobal extends PsiPerlExpr, PerlVariableDeclarationExpr {

  @Nullable
  PsiPerlAttributes getAttributes();

  @NotNull
  List<PsiPerlUndefExpr> getUndefExprList();

  @NotNull
  List<PsiPerlVariableDeclarationElement> getVariableDeclarationElementList();

}
