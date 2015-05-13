// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlForeachCompound extends PsiElement {

  @Nullable
  PerlArrayVariable getArrayVariable();

  @Nullable
  PerlBlockCompound getBlockCompound();

  @NotNull
  List<PerlExpr> getExprList();

  @Nullable
  PerlHashVariable getHashVariable();

  @Nullable
  PerlScalarVariable getScalarVariable();

  @Nullable
  PerlString getString();

  @Nullable
  PerlUndefTerm getUndefTerm();

  @Nullable
  PerlVariableDeclarationGlobal getVariableDeclarationGlobal();

  @Nullable
  PerlVariableDeclarationLexical getVariableDeclarationLexical();

  @Nullable
  PerlVariableDeclarationLocal getVariableDeclarationLocal();

}
