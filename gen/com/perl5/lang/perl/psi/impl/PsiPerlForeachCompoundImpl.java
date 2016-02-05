// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mixins.PerlLexicalScopeMemberMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlForeachCompoundImpl extends PerlLexicalScopeMemberMixin implements PsiPerlForeachCompound
{

	public PsiPerlForeachCompoundImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitForeachCompound(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlBlock getBlock()
	{
		return findChildByClass(PsiPerlBlock.class);
	}

	@Override
	@Nullable
	public PsiPerlContinueBlock getContinueBlock()
	{
		return findChildByClass(PsiPerlContinueBlock.class);
	}

	@Override
	@Nullable
	public PsiPerlForIterator getForIterator()
	{
		return findChildByClass(PsiPerlForIterator.class);
	}

	@Override
	@Nullable
	public PsiPerlForListStatement getForListStatement()
	{
		return findChildByClass(PsiPerlForListStatement.class);
	}

}
