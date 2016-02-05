// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlCodeCastExpr;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlCastExpressionMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlCodeCastExprImpl extends PerlCastExpressionMixin implements PsiPerlCodeCastExpr
{

	public PsiPerlCodeCastExprImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitCodeCastExpr(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlExpr getExpr()
	{
		return findChildByClass(PsiPerlExpr.class);
	}

}
