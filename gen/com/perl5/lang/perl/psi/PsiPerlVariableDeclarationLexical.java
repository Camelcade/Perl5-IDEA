// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PsiPerlVariableDeclarationLexical extends PsiPerlExpr, PerlVariableDeclaration
{

	@NotNull
	List<PsiPerlAttribute> getAttributeList();

	@NotNull
	List<PsiPerlVariableDeclarationWrapper> getVariableDeclarationWrapperList();

}
