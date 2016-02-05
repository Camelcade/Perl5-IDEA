// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import org.jetbrains.annotations.Nullable;

public interface PsiPerlForCompound extends PerlLexicalScope
{

	@Nullable
	PsiPerlBlock getBlock();

	@Nullable
	PsiPerlContinueBlock getContinueBlock();

	@Nullable
	PsiPerlForIterator getForIterator();

	@Nullable
	PsiPerlForListStatement getForListStatement();

}
