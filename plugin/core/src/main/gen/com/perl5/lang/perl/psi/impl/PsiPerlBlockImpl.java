// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.*;

public class PsiPerlBlockImpl extends PerlCompositeElementImpl implements PsiPerlBlock {

  public PsiPerlBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiPerlAfterModifier> getAfterModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAfterModifier.class);
  }

  @Override
  @NotNull
  public List<PsiPerlAnnotationAbstract> getAnnotationAbstractList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAnnotationAbstract.class);
  }

  @Override
  @NotNull
  public List<PsiPerlAnnotationDeprecated> getAnnotationDeprecatedList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAnnotationDeprecated.class);
  }

  @Override
  @NotNull
  public List<PsiPerlAnnotationInject> getAnnotationInjectList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAnnotationInject.class);
  }

  @Override
  @NotNull
  public List<PsiPerlAnnotationMethod> getAnnotationMethodList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAnnotationMethod.class);
  }

  @Override
  @NotNull
  public List<PsiPerlAnnotationNoInject> getAnnotationNoInjectList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAnnotationNoInject.class);
  }

  @Override
  @NotNull
  public List<PsiPerlAnnotationNoinspection> getAnnotationNoinspectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAnnotationNoinspection.class);
  }

  @Override
  @NotNull
  public List<PsiPerlAnnotationOverride> getAnnotationOverrideList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAnnotationOverride.class);
  }

  @Override
  @NotNull
  public List<PsiPerlAnnotationReturns> getAnnotationReturnsList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAnnotationReturns.class);
  }

  @Override
  @NotNull
  public List<PsiPerlAnnotationType> getAnnotationTypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAnnotationType.class);
  }

  @Override
  @NotNull
  public List<PsiPerlAroundModifier> getAroundModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAroundModifier.class);
  }

  @Override
  @NotNull
  public List<PsiPerlAugmentModifier> getAugmentModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAugmentModifier.class);
  }

  @Override
  @NotNull
  public List<PsiPerlBeforeModifier> getBeforeModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlBeforeModifier.class);
  }

  @Override
  @NotNull
  public List<PsiPerlBlockCompound> getBlockCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlBlockCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlCaseCompound> getCaseCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlCaseCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlCaseDefault> getCaseDefaultList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlCaseDefault.class);
  }

  @Override
  @NotNull
  public List<PsiPerlDefaultCompound> getDefaultCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlDefaultCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlForCompound> getForCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlForCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlForeachCompound> getForeachCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlForeachCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlFormatDefinition> getFormatDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlFormatDefinition.class);
  }

  @Override
  @NotNull
  public List<PsiPerlFuncDefinition> getFuncDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlFuncDefinition.class);
  }

  @Override
  @NotNull
  public List<PsiPerlGivenCompound> getGivenCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlGivenCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlIfCompound> getIfCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlIfCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlLabelDeclaration> getLabelDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlLabelDeclaration.class);
  }

  @Override
  @NotNull
  public List<PsiPerlMethodDefinition> getMethodDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlMethodDefinition.class);
  }

  @Override
  @NotNull
  public List<PsiPerlNamedBlock> getNamedBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlNamedBlock.class);
  }

  @Override
  @NotNull
  public List<PsiPerlNamespaceDefinition> getNamespaceDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlNamespaceDefinition.class);
  }

  @Override
  @NotNull
  public List<PsiPerlNyiStatement> getNyiStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlNyiStatement.class);
  }

  @Override
  @NotNull
  public List<PsiPerlStatement> getStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlStatement.class);
  }

  @Override
  @NotNull
  public List<PsiPerlSubDefinition> getSubDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlSubDefinition.class);
  }

  @Override
  @NotNull
  public List<PsiPerlSwitchCompound> getSwitchCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlSwitchCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlTrycatchCompound> getTrycatchCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlTrycatchCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlUnlessCompound> getUnlessCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlUnlessCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlUntilCompound> getUntilCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlUntilCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlWhenCompound> getWhenCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlWhenCompound.class);
  }

  @Override
  @NotNull
  public List<PsiPerlWhileCompound> getWhileCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlWhileCompound.class);
  }

}
