// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlCodeVariable;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlVariableImplMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlCodeVariableImpl extends PerlVariableImplMixin implements PsiPerlCodeVariable
{

	public PsiPerlCodeVariableImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitCodeVariable(this);
		else super.accept(visitor);
	}

}
