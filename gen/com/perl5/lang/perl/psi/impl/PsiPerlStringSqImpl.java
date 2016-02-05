// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlStringSq;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlStringImplMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlStringSqImpl extends PerlStringImplMixin implements PsiPerlStringSq
{

	public PsiPerlStringSqImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitStringSq(this);
		else super.accept(visitor);
	}

}
