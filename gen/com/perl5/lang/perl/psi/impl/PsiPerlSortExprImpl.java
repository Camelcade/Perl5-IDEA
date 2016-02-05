// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PsiPerlSortExprImpl extends PsiPerlExprImpl implements PsiPerlSortExpr
{

	public PsiPerlSortExprImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitSortExpr(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlBlock getBlock()
	{
		return findChildByClass(PsiPerlBlock.class);
	}

	@Override
	@NotNull
	public List<PsiPerlExpr> getExprList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlExpr.class);
	}

	@Override
	@Nullable
	public PsiPerlMethod getMethod()
	{
		return findChildByClass(PsiPerlMethod.class);
	}

}
