// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiPerlBlock extends PerlBlock {

  @NotNull
  List<PsiPerlAfterModifier> getAfterModifierList();

  @NotNull
  List<PsiPerlAnnotationAbstract> getAnnotationAbstractList();

  @NotNull
  List<PsiPerlAnnotationDeprecated> getAnnotationDeprecatedList();

  @NotNull
  List<PsiPerlAnnotationInject> getAnnotationInjectList();

  @NotNull
  List<PsiPerlAnnotationMethod> getAnnotationMethodList();

  @NotNull
  List<PsiPerlAnnotationNoInject> getAnnotationNoInjectList();

  @NotNull
  List<PsiPerlAnnotationNoinspection> getAnnotationNoinspectionList();

  @NotNull
  List<PsiPerlAnnotationOverride> getAnnotationOverrideList();

  @NotNull
  List<PsiPerlAnnotationReturns> getAnnotationReturnsList();

  @NotNull
  List<PsiPerlAnnotationType> getAnnotationTypeList();

  @NotNull
  List<PsiPerlAroundModifier> getAroundModifierList();

  @NotNull
  List<PsiPerlAugmentModifier> getAugmentModifierList();

  @NotNull
  List<PsiPerlBeforeModifier> getBeforeModifierList();

  @NotNull
  List<PsiPerlBlockCompound> getBlockCompoundList();

  @NotNull
  List<PsiPerlCaseCompound> getCaseCompoundList();

  @NotNull
  List<PsiPerlCaseDefault> getCaseDefaultList();

  @NotNull
  List<PsiPerlDefaultCompound> getDefaultCompoundList();

  @NotNull
  List<PsiPerlForCompound> getForCompoundList();

  @NotNull
  List<PsiPerlForeachCompound> getForeachCompoundList();

  @NotNull
  List<PsiPerlFormatDefinition> getFormatDefinitionList();

  @NotNull
  List<PsiPerlFuncDefinition> getFuncDefinitionList();

  @NotNull
  List<PsiPerlGivenCompound> getGivenCompoundList();

  @NotNull
  List<PsiPerlIfCompound> getIfCompoundList();

  @NotNull
  List<PsiPerlLabelDeclaration> getLabelDeclarationList();

  @NotNull
  List<PsiPerlMethodDefinition> getMethodDefinitionList();

  @NotNull
  List<PsiPerlNamedBlock> getNamedBlockList();

  @NotNull
  List<PsiPerlNamespaceDefinition> getNamespaceDefinitionList();

  @NotNull
  List<PsiPerlNyiStatement> getNyiStatementList();

  @NotNull
  List<PsiPerlStatement> getStatementList();

  @NotNull
  List<PsiPerlSubDefinition> getSubDefinitionList();

  @NotNull
  List<PsiPerlSwitchCompound> getSwitchCompoundList();

  @NotNull
  List<PsiPerlTrycatchCompound> getTrycatchCompoundList();

  @NotNull
  List<PsiPerlUnlessCompound> getUnlessCompoundList();

  @NotNull
  List<PsiPerlUntilCompound> getUntilCompoundList();

  @NotNull
  List<PsiPerlWhenCompound> getWhenCompoundList();

  @NotNull
  List<PsiPerlWhileCompound> getWhileCompoundList();

}
