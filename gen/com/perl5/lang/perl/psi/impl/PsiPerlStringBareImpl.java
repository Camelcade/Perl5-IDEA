// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlStringBare;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlStringBareImplMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlStringBareImpl extends PerlStringBareImplMixin implements PsiPerlStringBare
{

	public PsiPerlStringBareImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitStringBare(this);
		else super.accept(visitor);
	}

}
