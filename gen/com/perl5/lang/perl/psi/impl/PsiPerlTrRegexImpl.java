// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlTrRegexImpl extends PsiPerlExprImpl implements PsiPerlTrRegex
{

	public PsiPerlTrRegexImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitTrRegex(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlTrModifiers getTrModifiers()
	{
		return findChildByClass(PsiPerlTrModifiers.class);
	}

	@Override
	@Nullable
	public PsiPerlTrReplacementlist getTrReplacementlist()
	{
		return findChildByClass(PsiPerlTrReplacementlist.class);
	}

	@Override
	@Nullable
	public PsiPerlTrSearchlist getTrSearchlist()
	{
		return findChildByClass(PsiPerlTrSearchlist.class);
	}

}
