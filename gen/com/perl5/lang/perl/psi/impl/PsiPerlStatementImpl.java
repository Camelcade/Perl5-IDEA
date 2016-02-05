// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlStatementImpl extends ASTWrapperPsiElement implements PsiPerlStatement
{

	public PsiPerlStatementImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitStatement(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlExpr getExpr()
	{
		return findChildByClass(PsiPerlExpr.class);
	}

	@Override
	@Nullable
	public PsiPerlNoStatement getNoStatement()
	{
		return findChildByClass(PsiPerlNoStatement.class);
	}

	@Override
	@Nullable
	public PsiPerlStatement getStatement()
	{
		return findChildByClass(PsiPerlStatement.class);
	}

	@Override
	@Nullable
	public PsiPerlSubDeclaration getSubDeclaration()
	{
		return findChildByClass(PsiPerlSubDeclaration.class);
	}

}
