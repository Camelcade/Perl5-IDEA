// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlAnonHash;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.PsiPerlCallArguments;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlCallArgumentsImpl extends ASTWrapperPsiElement implements PsiPerlCallArguments
{

	public PsiPerlCallArgumentsImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitCallArguments(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlAnonHash getAnonHash()
	{
		return findChildByClass(PsiPerlAnonHash.class);
	}

	@Override
	@Nullable
	public PsiPerlBlock getBlock()
	{
		return findChildByClass(PsiPerlBlock.class);
	}

}
