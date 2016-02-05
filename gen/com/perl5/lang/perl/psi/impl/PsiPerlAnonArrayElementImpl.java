// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlAnonArrayElement;
import com.perl5.lang.perl.psi.PsiPerlArrayIndex;
import com.perl5.lang.perl.psi.PsiPerlParenthesisedExpr;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;

public class PsiPerlAnonArrayElementImpl extends PsiPerlExprImpl implements PsiPerlAnonArrayElement
{

	public PsiPerlAnonArrayElementImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitAnonArrayElement(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public PsiPerlArrayIndex getArrayIndex()
	{
		return findNotNullChildByClass(PsiPerlArrayIndex.class);
	}

	@Override
	@NotNull
	public PsiPerlParenthesisedExpr getParenthesisedExpr()
	{
		return findNotNullChildByClass(PsiPerlParenthesisedExpr.class);
	}

}
