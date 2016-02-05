// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.PsiPerlForIteratorStatement;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;

public class PsiPerlForIteratorStatementImpl extends PsiPerlStatementImpl implements PsiPerlForIteratorStatement
{

	public PsiPerlForIteratorStatementImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitForIteratorStatement(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public PsiPerlExpr getExpr()
	{
		return findNotNullChildByClass(PsiPerlExpr.class);
	}

}
