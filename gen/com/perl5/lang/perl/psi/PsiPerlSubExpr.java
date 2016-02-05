// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PsiPerlSubExpr extends PsiPerlExpr
{

	@NotNull
	List<PsiPerlAttribute> getAttributeList();

	@NotNull
	PsiPerlBlock getBlock();

	@Nullable
	PsiPerlSubSignatureContent getSubSignatureContent();

}
