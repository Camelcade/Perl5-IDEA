// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PsiPerlConditionalBlock extends PsiElement
{

	@Nullable
	PsiPerlBlock getBlock();

	@NotNull
	PsiPerlConditionStatement getConditionStatement();

}
