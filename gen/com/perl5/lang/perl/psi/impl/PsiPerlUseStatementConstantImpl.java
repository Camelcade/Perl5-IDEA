// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlUseStatementConstantImpl extends PsiPerlStatementImpl implements PsiPerlUseStatementConstant
{

	public PsiPerlUseStatementConstantImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitUseStatementConstant(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlConstantDefinition getConstantDefinition()
	{
		return findChildByClass(PsiPerlConstantDefinition.class);
	}

	@Override
	@Nullable
	public PsiPerlConstantsBlock getConstantsBlock()
	{
		return findChildByClass(PsiPerlConstantsBlock.class);
	}

	@Override
	@Nullable
	public PsiPerlExpr getExpr()
	{
		return findChildByClass(PsiPerlExpr.class);
	}

}
