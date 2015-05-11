// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlTermExpr extends PerlExpr {

  @Nullable
  PerlCallable getCallable();

  @Nullable
  PerlCloseTerm getCloseTerm();

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
  PerlOpenTerm getOpenTerm();

  @Nullable
  PerlPerlArray getPerlArray();

  @Nullable
  PerlPerlGlob getPerlGlob();

  @Nullable
  PerlPerlHash getPerlHash();

  @Nullable
  PerlPerlScalar getPerlScalar();

  @Nullable
  PerlPrintTerm getPrintTerm();

  @Nullable
  PerlReferenceValue getReferenceValue();

  @Nullable
  PerlReplacementRegex getReplacementRegex();

  @Nullable
  PerlRequireTerm getRequireTerm();

  @Nullable
  PerlSortTerm getSortTerm();

  @Nullable
  PerlString getString();

  @Nullable
  PerlSubTerm getSubTerm();

  @Nullable
  PerlTrRegex getTrRegex();

  @Nullable
  PerlVariableDeclarationGlobal getVariableDeclarationGlobal();

  @Nullable
  PerlVariableDeclarationLexical getVariableDeclarationLexical();

  @Nullable
  PerlVariableDeclarationLocal getVariableDeclarationLocal();

}
