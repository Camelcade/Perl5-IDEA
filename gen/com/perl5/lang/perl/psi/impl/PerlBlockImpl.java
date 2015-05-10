// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.perl5.lang.perl.psi.*;

public class PerlBlockImpl extends ASTWrapperPsiElement implements PerlBlock {

  public PerlBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitBlock(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PerlBlockCompound> getBlockCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlBlockCompound.class);
  }

  @Override
  @NotNull
  public List<PerlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlExpr.class);
  }

  @Override
  @NotNull
  public List<PerlForCompound> getForCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlForCompound.class);
  }

  @Override
  @NotNull
  public List<PerlForStatementModifier> getForStatementModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlForStatementModifier.class);
  }

  @Override
  @NotNull
  public List<PerlForeachCompound> getForeachCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlForeachCompound.class);
  }

  @Override
  @NotNull
  public List<PerlForeachStatementModifier> getForeachStatementModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlForeachStatementModifier.class);
  }

  @Override
  @NotNull
  public List<PerlGivenCompound> getGivenCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlGivenCompound.class);
  }

  @Override
  @NotNull
  public List<PerlIfCompound> getIfCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlIfCompound.class);
  }

  @Override
  @NotNull
  public List<PerlIfStatementModifier> getIfStatementModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlIfStatementModifier.class);
  }

  @Override
  @NotNull
  public List<PerlLabelDeclaration> getLabelDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlLabelDeclaration.class);
  }

  @Override
  @NotNull
  public List<PerlLastStatement> getLastStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlLastStatement.class);
  }

  @Override
  @NotNull
  public List<PerlNamedBlock> getNamedBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlNamedBlock.class);
  }

  @Override
  @NotNull
  public List<PerlNamespace> getNamespaceList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlNamespace.class);
  }

  @Override
  @NotNull
  public List<PerlNextStatement> getNextStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlNextStatement.class);
  }

  @Override
  @NotNull
  public List<PerlNoStatement> getNoStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlNoStatement.class);
  }

  @Override
  @NotNull
  public List<PerlRedoStatement> getRedoStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlRedoStatement.class);
  }

  @Override
  @NotNull
  public List<PerlSubDeclaration> getSubDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlSubDeclaration.class);
  }

  @Override
  @NotNull
  public List<PerlSubDefinition> getSubDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlSubDefinition.class);
  }

  @Override
  @NotNull
  public List<PerlUndefStatement> getUndefStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlUndefStatement.class);
  }

  @Override
  @NotNull
  public List<PerlUnlessCompound> getUnlessCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlUnlessCompound.class);
  }

  @Override
  @NotNull
  public List<PerlUnlessStatementModifier> getUnlessStatementModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlUnlessStatementModifier.class);
  }

  @Override
  @NotNull
  public List<PerlUntilCompound> getUntilCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlUntilCompound.class);
  }

  @Override
  @NotNull
  public List<PerlUntilStatementModifier> getUntilStatementModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlUntilStatementModifier.class);
  }

  @Override
  @NotNull
  public List<PerlUseStatement> getUseStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlUseStatement.class);
  }

  @Override
  @NotNull
  public List<PerlWhenStatementModifier> getWhenStatementModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlWhenStatementModifier.class);
  }

  @Override
  @NotNull
  public List<PerlWhileCompound> getWhileCompoundList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlWhileCompound.class);
  }

  @Override
  @NotNull
  public List<PerlWhileStatementModifier> getWhileStatementModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlWhileStatementModifier.class);
  }

}
