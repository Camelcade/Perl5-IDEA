// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PsiPerlUnlessCompound extends PsiPerlIfCompound
{

	@NotNull
	List<PsiPerlConditionalBlock> getConditionalBlockList();

	@Nullable
	PsiPerlUnconditionalBlock getUnconditionalBlock();

}
