// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PsiPerlIfCompound extends PerlLexicalScope
{

	@NotNull
	List<PsiPerlConditionalBlock> getConditionalBlockList();

	@Nullable
	PsiPerlUnconditionalBlock getUnconditionalBlock();

}
