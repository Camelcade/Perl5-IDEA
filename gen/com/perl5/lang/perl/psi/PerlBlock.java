// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlBlock extends PsiElement {

  @NotNull
  List<PerlCompoundStatement> getCompoundStatementList();

  @NotNull
  List<PerlExpr> getExprList();

  @NotNull
  List<PerlForStatementModifier> getForStatementModifierList();

  @NotNull
  List<PerlForeachStatementModifier> getForeachStatementModifierList();

  @NotNull
  List<PerlIfStatementModifier> getIfStatementModifierList();

  @NotNull
  List<PerlLastStatement> getLastStatementList();

  @NotNull
  List<PerlNamespace> getNamespaceList();

  @NotNull
  List<PerlNextStatement> getNextStatementList();

  @NotNull
  List<PerlNoStatement> getNoStatementList();

  @NotNull
  List<PerlRedoStatement> getRedoStatementList();

  @NotNull
  List<PerlSubDeclaration> getSubDeclarationList();

  @NotNull
  List<PerlSubDefinition> getSubDefinitionList();

  @NotNull
  List<PerlUndefStatement> getUndefStatementList();

  @NotNull
  List<PerlUnlessStatementModifier> getUnlessStatementModifierList();

  @NotNull
  List<PerlUntilStatementModifier> getUntilStatementModifierList();

  @NotNull
  List<PerlUseStatement> getUseStatementList();

  @NotNull
  List<PerlWhenStatementModifier> getWhenStatementModifierList();

  @NotNull
  List<PerlWhileStatementModifier> getWhileStatementModifierList();

}
