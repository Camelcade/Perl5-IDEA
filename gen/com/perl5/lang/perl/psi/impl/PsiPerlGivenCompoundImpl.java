// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlConditionalBlock;
import com.perl5.lang.perl.psi.PsiPerlGivenCompound;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlLexicalScopeMemberMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlGivenCompoundImpl extends PerlLexicalScopeMemberMixin implements PsiPerlGivenCompound
{

	public PsiPerlGivenCompoundImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitGivenCompound(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlConditionalBlock getConditionalBlock()
	{
		return findChildByClass(PsiPerlConditionalBlock.class);
	}

}
