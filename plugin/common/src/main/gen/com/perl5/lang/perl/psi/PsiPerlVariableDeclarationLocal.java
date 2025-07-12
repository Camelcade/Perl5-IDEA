// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiPerlVariableDeclarationLocal extends PsiPerlExpr, PerlVariableDeclarationExpr {

  @NotNull
  List<PsiPerlUndefExpr> getUndefExprList();

  @NotNull
  List<PsiPerlVariableDeclarationElement> getVariableDeclarationElementList();

}
