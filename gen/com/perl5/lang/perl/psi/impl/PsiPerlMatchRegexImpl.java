// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlMatchRegex;
import com.perl5.lang.perl.psi.PsiPerlPerlRegex;
import com.perl5.lang.perl.psi.PsiPerlPerlRegexModifiers;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlMatchRegexImpl extends PsiPerlExprImpl implements PsiPerlMatchRegex
{

	public PsiPerlMatchRegexImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitMatchRegex(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlPerlRegex getPerlRegex()
	{
		return findChildByClass(PsiPerlPerlRegex.class);
	}

	@Override
	@Nullable
	public PsiPerlPerlRegexModifiers getPerlRegexModifiers()
	{
		return findChildByClass(PsiPerlPerlRegexModifiers.class);
	}

}
