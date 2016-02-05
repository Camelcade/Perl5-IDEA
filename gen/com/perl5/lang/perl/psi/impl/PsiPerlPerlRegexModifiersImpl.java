// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlPerlRegexModifiers;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;

public class PsiPerlPerlRegexModifiersImpl extends ASTWrapperPsiElement implements PsiPerlPerlRegexModifiers
{

	public PsiPerlPerlRegexModifiersImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitPerlRegexModifiers(this);
		else super.accept(visitor);
	}

}
