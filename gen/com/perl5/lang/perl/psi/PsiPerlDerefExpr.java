// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PsiPerlDerefExpr extends PsiPerlExpr, PerlDerefExpression
{

	@NotNull
	List<PsiPerlArrayIndex> getArrayIndexList();

	@NotNull
	List<PsiPerlExpr> getExprList();

	@NotNull
	List<PsiPerlHashIndex> getHashIndexList();

	@NotNull
	List<PsiPerlNestedCallArguments> getNestedCallArgumentsList();

	@NotNull
	List<PsiPerlScalarCall> getScalarCallList();

}
