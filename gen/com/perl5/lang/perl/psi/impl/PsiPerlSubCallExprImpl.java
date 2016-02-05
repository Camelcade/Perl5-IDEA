// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlSubCallExprImpl extends PsiPerlExprImpl implements PsiPerlSubCallExpr
{

	public PsiPerlSubCallExprImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitSubCallExpr(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlCallArguments getCallArguments()
	{
		return findChildByClass(PsiPerlCallArguments.class);
	}

	@Override
	@Nullable
	public PsiPerlExpr getExpr()
	{
		return findChildByClass(PsiPerlExpr.class);
	}

	@Override
	@Nullable
	public PsiPerlMethod getMethod()
	{
		return findChildByClass(PsiPerlMethod.class);
	}

}
