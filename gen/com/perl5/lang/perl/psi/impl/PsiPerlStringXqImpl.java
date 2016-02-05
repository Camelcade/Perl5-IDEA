// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlStringXq;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlStringImplMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlStringXqImpl extends PerlStringImplMixin implements PsiPerlStringXq
{

	public PsiPerlStringXqImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitStringXq(this);
		else super.accept(visitor);
	}

}
