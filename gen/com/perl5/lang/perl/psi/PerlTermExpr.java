// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlTermExpr extends PerlExpr {

  @Nullable
  PerlArrayVariable getArrayVariable();

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
  PerlHashVariable getHashVariable();

  @Nullable
  PerlLastTerm getLastTerm();

  @Nullable
  PerlMapTerm getMapTerm();

  @Nullable
  PerlMatchRegex getMatchRegex();

  @Nullable
  PerlNextTerm getNextTerm();

  @Nullable
  PerlOpenTerm getOpenTerm();

  @Nullable
  PerlPrintTerm getPrintTerm();

  @Nullable
  PerlRedoTerm getRedoTerm();

  @Nullable
  PerlReferenceValue getReferenceValue();

  @Nullable
  PerlReplacementRegex getReplacementRegex();

  @Nullable
  PerlRequireTerm getRequireTerm();

  @Nullable
  PerlRightwardCall getRightwardCall();

  @Nullable
  PerlScalarVariable getScalarVariable();

  @Nullable
  PerlSortTerm getSortTerm();

  @Nullable
  PerlString getString();

  @Nullable
  PerlSubTerm getSubTerm();

  @Nullable
  PerlTrRegex getTrRegex();

  @Nullable
  PerlUndefTerm getUndefTerm();

  @Nullable
  PerlVariableDeclarationGlobal getVariableDeclarationGlobal();

  @Nullable
  PerlVariableDeclarationLexical getVariableDeclarationLexical();

  @Nullable
  PerlVariableDeclarationLocal getVariableDeclarationLocal();

}
