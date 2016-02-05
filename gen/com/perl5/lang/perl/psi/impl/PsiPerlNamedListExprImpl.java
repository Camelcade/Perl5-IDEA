// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlNamedListExpr;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlMethodContainerMixIn;
import org.jetbrains.annotations.NotNull;

public class PsiPerlNamedListExprImpl extends PerlMethodContainerMixIn implements PsiPerlNamedListExpr
{

	public PsiPerlNamedListExprImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitNamedListExpr(this);
		else super.accept(visitor);
	}

}
