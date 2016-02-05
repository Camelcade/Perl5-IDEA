// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.PsiPerlUnconditionalBlock;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;

public class PsiPerlUnconditionalBlockImpl extends ASTWrapperPsiElement implements PsiPerlUnconditionalBlock
{

	public PsiPerlUnconditionalBlockImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitUnconditionalBlock(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public PsiPerlBlock getBlock()
	{
		return findNotNullChildByClass(PsiPerlBlock.class);
	}

}
