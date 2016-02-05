// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.psi.PsiPerlNestedCall;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;

public class PsiPerlNestedCallImpl extends PsiPerlSubCallExprImpl implements PsiPerlNestedCall
{

	public PsiPerlNestedCallImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitNestedCall(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public PsiPerlMethod getMethod()
	{
		return findNotNullChildByClass(PsiPerlMethod.class);
	}

}
