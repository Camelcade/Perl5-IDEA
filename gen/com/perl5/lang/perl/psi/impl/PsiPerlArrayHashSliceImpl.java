// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlArrayHashSlice;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.PsiPerlHashIndex;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;

public class PsiPerlArrayHashSliceImpl extends PsiPerlExprImpl implements PsiPerlArrayHashSlice
{

	public PsiPerlArrayHashSliceImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitArrayHashSlice(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public PsiPerlExpr getExpr()
	{
		return findNotNullChildByClass(PsiPerlExpr.class);
	}

	@Override
	@NotNull
	public PsiPerlHashIndex getHashIndex()
	{
		return findNotNullChildByClass(PsiPerlHashIndex.class);
	}

}
