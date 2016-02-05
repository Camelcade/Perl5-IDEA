// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlFileReadExpr;
import com.perl5.lang.perl.psi.PsiPerlReadHandle;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlFileReadExprImpl extends PsiPerlExprImpl implements PsiPerlFileReadExpr
{

	public PsiPerlFileReadExprImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitFileReadExpr(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlReadHandle getReadHandle()
	{
		return findChildByClass(PsiPerlReadHandle.class);
	}

}
