// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PsiPerlReplacementRegexImpl extends PsiPerlExprImpl implements PsiPerlReplacementRegex
{

	public PsiPerlReplacementRegexImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitReplacementRegex(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public List<PsiPerlAnnotation> getAnnotationList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAnnotation.class);
	}

	@Override
	@NotNull
	public List<PsiPerlBlock> getBlockList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlBlock.class);
	}

	@Override
	@NotNull
	public List<PsiPerlContinueBlock> getContinueBlockList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlContinueBlock.class);
	}

	@Override
	@NotNull
	public List<PsiPerlDefaultCompound> getDefaultCompoundList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlDefaultCompound.class);
	}

	@Override
	@NotNull
	public List<PsiPerlForCompound> getForCompoundList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlForCompound.class);
	}

	@Override
	@NotNull
	public List<PsiPerlForeachCompound> getForeachCompoundList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlForeachCompound.class);
	}

	@Override
	@NotNull
	public List<PsiPerlFormatDefinition> getFormatDefinitionList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlFormatDefinition.class);
	}

	@Override
	@NotNull
	public List<PsiPerlGivenCompound> getGivenCompoundList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlGivenCompound.class);
	}

	@Override
	@NotNull
	public List<PsiPerlIfCompound> getIfCompoundList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlIfCompound.class);
	}

	@Override
	@NotNull
	public List<PsiPerlLabelDeclaration> getLabelDeclarationList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlLabelDeclaration.class);
	}

	@Override
	@NotNull
	public List<PsiPerlNamedBlock> getNamedBlockList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlNamedBlock.class);
	}

	@Override
	@NotNull
	public List<PsiPerlNamespaceDefinition> getNamespaceDefinitionList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlNamespaceDefinition.class);
	}

	@Override
	@NotNull
	public List<PsiPerlNyiStatement> getNyiStatementList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlNyiStatement.class);
	}

	@Override
	@NotNull
	public List<PsiPerlPerlRegex> getPerlRegexList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlPerlRegex.class);
	}

	@Override
	@Nullable
	public PsiPerlPerlRegexModifiers getPerlRegexModifiers()
	{
		return findChildByClass(PsiPerlPerlRegexModifiers.class);
	}

	@Override
	@NotNull
	public List<PsiPerlStatement> getStatementList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlStatement.class);
	}

	@Override
	@NotNull
	public List<PsiPerlSubDefinition> getSubDefinitionList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlSubDefinition.class);
	}

	@Override
	@NotNull
	public List<PsiPerlUntilCompound> getUntilCompoundList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlUntilCompound.class);
	}

	@Override
	@NotNull
	public List<PsiPerlWhenCompound> getWhenCompoundList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlWhenCompound.class);
	}

	@Override
	@NotNull
	public List<PsiPerlWhileCompound> getWhileCompoundList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlWhileCompound.class);
	}

}
