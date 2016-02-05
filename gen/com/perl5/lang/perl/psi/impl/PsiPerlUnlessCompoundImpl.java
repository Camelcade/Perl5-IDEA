// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlConditionalBlock;
import com.perl5.lang.perl.psi.PsiPerlUnconditionalBlock;
import com.perl5.lang.perl.psi.PsiPerlUnlessCompound;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlLexicalScopeMemberMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PsiPerlUnlessCompoundImpl extends PerlLexicalScopeMemberMixin implements PsiPerlUnlessCompound
{

	public PsiPerlUnlessCompoundImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitUnlessCompound(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public List<PsiPerlConditionalBlock> getConditionalBlockList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlConditionalBlock.class);
	}

	@Override
	@Nullable
	public PsiPerlUnconditionalBlock getUnconditionalBlock()
	{
		return findChildByClass(PsiPerlUnconditionalBlock.class);
	}

}
