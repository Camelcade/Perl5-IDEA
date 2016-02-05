// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.psi.PsiPerlNamedUnaryExpr;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlNamedUnaryExprImpl extends PsiPerlExprImpl implements PsiPerlNamedUnaryExpr
{

	public PsiPerlNamedUnaryExprImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitNamedUnaryExpr(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlMethod getMethod()
	{
		return findChildByClass(PsiPerlMethod.class);
	}

}
