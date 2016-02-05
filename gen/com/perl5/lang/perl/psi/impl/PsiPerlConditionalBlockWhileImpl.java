// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.PsiPerlConditionStatementWhile;
import com.perl5.lang.perl.psi.PsiPerlConditionalBlockWhile;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlLexicalScopeMemberMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlConditionalBlockWhileImpl extends PerlLexicalScopeMemberMixin implements PsiPerlConditionalBlockWhile
{

	public PsiPerlConditionalBlockWhileImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitConditionalBlockWhile(this);
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
	public PsiPerlConditionStatementWhile getConditionStatementWhile()
	{
		return findNotNullChildByClass(PsiPerlConditionStatementWhile.class);
	}

}
