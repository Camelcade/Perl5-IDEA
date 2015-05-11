// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlBlock extends PsiElement {

  @NotNull
  List<PerlBlockCompound> getBlockCompoundList();

  @NotNull
  List<PerlExpr> getExprList();

  @NotNull
  List<PerlForCompound> getForCompoundList();

  @NotNull
  List<PerlForStatementModifier> getForStatementModifierList();

  @NotNull
  List<PerlForeachCompound> getForeachCompoundList();

  @NotNull
  List<PerlForeachStatementModifier> getForeachStatementModifierList();

  @NotNull
  List<PerlGivenCompound> getGivenCompoundList();

  @NotNull
  List<PerlIfCompound> getIfCompoundList();

  @NotNull
  List<PerlIfStatementModifier> getIfStatementModifierList();

  @NotNull
  List<PerlLabelDeclaration> getLabelDeclarationList();

  @NotNull
  List<PerlNamedBlock> getNamedBlockList();

  @NotNull
  List<PerlNamespace> getNamespaceList();

  @NotNull
  List<PerlNoStatement> getNoStatementList();

  @NotNull
  List<PerlSubDeclaration> getSubDeclarationList();

  @NotNull
  List<PerlSubDefinition> getSubDefinitionList();

  @NotNull
  List<PerlUnlessCompound> getUnlessCompoundList();

  @NotNull
  List<PerlUnlessStatementModifier> getUnlessStatementModifierList();

  @NotNull
  List<PerlUntilCompound> getUntilCompoundList();

  @NotNull
  List<PerlUntilStatementModifier> getUntilStatementModifierList();

  @NotNull
  List<PerlUseStatement> getUseStatementList();

  @NotNull
  List<PerlWhenStatementModifier> getWhenStatementModifierList();

  @NotNull
  List<PerlWhileCompound> getWhileCompoundList();

  @NotNull
  List<PerlWhileStatementModifier> getWhileStatementModifierList();

}
