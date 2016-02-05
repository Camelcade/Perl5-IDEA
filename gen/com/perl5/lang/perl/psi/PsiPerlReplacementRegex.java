// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PsiPerlReplacementRegex extends PsiPerlExpr
{

	@NotNull
	List<PsiPerlAnnotation> getAnnotationList();

	@NotNull
	List<PsiPerlBlock> getBlockList();

	@NotNull
	List<PsiPerlContinueBlock> getContinueBlockList();

	@NotNull
	List<PsiPerlDefaultCompound> getDefaultCompoundList();

	@NotNull
	List<PsiPerlForCompound> getForCompoundList();

	@NotNull
	List<PsiPerlForeachCompound> getForeachCompoundList();

	@NotNull
	List<PsiPerlFormatDefinition> getFormatDefinitionList();

	@NotNull
	List<PsiPerlGivenCompound> getGivenCompoundList();

	@NotNull
	List<PsiPerlIfCompound> getIfCompoundList();

	@NotNull
	List<PsiPerlLabelDeclaration> getLabelDeclarationList();

	@NotNull
	List<PsiPerlNamedBlock> getNamedBlockList();

	@NotNull
	List<PsiPerlNamespaceDefinition> getNamespaceDefinitionList();

	@NotNull
	List<PsiPerlNyiStatement> getNyiStatementList();

	@NotNull
	List<PsiPerlPerlRegex> getPerlRegexList();

	@Nullable
	PsiPerlPerlRegexModifiers getPerlRegexModifiers();

	@NotNull
	List<PsiPerlStatement> getStatementList();

	@NotNull
	List<PsiPerlSubDefinition> getSubDefinitionList();

	@NotNull
	List<PsiPerlUntilCompound> getUntilCompoundList();

	@NotNull
	List<PsiPerlWhenCompound> getWhenCompoundList();

	@NotNull
	List<PsiPerlWhileCompound> getWhileCompoundList();

}
