// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.PsiPerlForListEpxr;
import com.perl5.lang.perl.psi.PsiPerlForListStatement;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlForListStatementImpl extends PsiPerlStatementImpl implements PsiPerlForListStatement
{

	public PsiPerlForListStatementImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitForListStatement(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlExpr getExpr()
	{
		return findChildByClass(PsiPerlExpr.class);
	}

	@Override
	@NotNull
	public PsiPerlForListEpxr getForListEpxr()
	{
		return findNotNullChildByClass(PsiPerlForListEpxr.class);
	}

}
