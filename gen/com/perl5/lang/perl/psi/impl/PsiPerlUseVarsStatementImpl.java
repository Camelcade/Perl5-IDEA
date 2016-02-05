// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlUseVarsStatement;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlUseVarsMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlUseVarsStatementImpl extends PerlUseVarsMixin implements PsiPerlUseVarsStatement
{

	public PsiPerlUseVarsStatementImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitUseVarsStatement(this);
		else super.accept(visitor);
	}

}
