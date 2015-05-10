// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlOp1Expr extends PerlExpr {

  @Nullable
  PerlCallable getCallable();

  @Nullable
  PerlCompileRegex getCompileRegex();

  @Nullable
  PerlDoTerm getDoTerm();

  @Nullable
  PerlEvalTerm getEvalTerm();

  @Nullable
  PerlExpr getExpr();

  @Nullable
  PerlFileReadTerm getFileReadTerm();

  @Nullable
  PerlGrepTerm getGrepTerm();

  @Nullable
  PerlMapTerm getMapTerm();

  @Nullable
  PerlMatchRegex getMatchRegex();

  @Nullable
  PerlOpenFile getOpenFile();

  @Nullable
  PerlOpenHandle getOpenHandle();

  @Nullable
  PerlOpenMode getOpenMode();

  @Nullable
  PerlOpenRef getOpenRef();

  @Nullable
  PerlReferenceValue getReferenceValue();

  @Nullable
  PerlReplacementRegex getReplacementRegex();

  @Nullable
  PerlSortTerm getSortTerm();

  @Nullable
  PerlTrRegex getTrRegex();

  @Nullable
  PerlVariableDeclarationGlobal getVariableDeclarationGlobal();

  @Nullable
  PerlVariableDeclarationLexical getVariableDeclarationLexical();

  @Nullable
  PerlVariableDeclarationLocal getVariableDeclarationLocal();

}
