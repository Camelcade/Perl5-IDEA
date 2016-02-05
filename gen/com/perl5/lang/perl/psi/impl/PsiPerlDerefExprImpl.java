// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mixins.PerlDerefExpressionMixin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsiPerlDerefExprImpl extends PerlDerefExpressionMixin implements PsiPerlDerefExpr
{

	public PsiPerlDerefExprImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitDerefExpr(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public List<PsiPerlArrayIndex> getArrayIndexList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlArrayIndex.class);
	}

	@Override
	@NotNull
	public List<PsiPerlExpr> getExprList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlExpr.class);
	}

	@Override
	@NotNull
	public List<PsiPerlHashIndex> getHashIndexList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlHashIndex.class);
	}

	@Override
	@NotNull
	public List<PsiPerlNestedCallArguments> getNestedCallArgumentsList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlNestedCallArguments.class);
	}

	@Override
	@NotNull
	public List<PsiPerlScalarCall> getScalarCallList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlScalarCall.class);
	}

}
