// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlConditionalBlockWhile;
import com.perl5.lang.perl.psi.PsiPerlContinueBlock;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlWhileCompound;
import com.perl5.lang.perl.psi.mixins.PerlLexicalScopeMemberMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlWhileCompoundImpl extends PerlLexicalScopeMemberMixin implements PsiPerlWhileCompound
{

	public PsiPerlWhileCompoundImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitWhileCompound(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlConditionalBlockWhile getConditionalBlockWhile()
	{
		return findChildByClass(PsiPerlConditionalBlockWhile.class);
	}

	@Override
	@Nullable
	public PsiPerlContinueBlock getContinueBlock()
	{
		return findChildByClass(PsiPerlContinueBlock.class);
	}

}
