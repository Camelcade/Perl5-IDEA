// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.PsiPerlPrintHandle;
import com.perl5.lang.perl.psi.PsiPerlScalarVariable;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlPrintHandleImpl extends ASTWrapperPsiElement implements PsiPerlPrintHandle
{

	public PsiPerlPrintHandleImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitPrintHandle(this);
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
	public PsiPerlScalarVariable getScalarVariable()
	{
		return findChildByClass(PsiPerlScalarVariable.class);
	}

}
