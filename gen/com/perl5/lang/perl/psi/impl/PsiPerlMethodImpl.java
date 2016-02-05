// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlMethodImplMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlMethodImpl extends PerlMethodImplMixin implements PsiPerlMethod
{

	public PsiPerlMethodImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitMethod(this);
		else super.accept(visitor);
	}

}
