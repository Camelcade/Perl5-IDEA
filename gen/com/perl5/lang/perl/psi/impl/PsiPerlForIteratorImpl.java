// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlForIterator;
import com.perl5.lang.perl.psi.PsiPerlForIteratorStatement;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsiPerlForIteratorImpl extends ASTWrapperPsiElement implements PsiPerlForIterator
{

	public PsiPerlForIteratorImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitForIterator(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public List<PsiPerlForIteratorStatement> getForIteratorStatementList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlForIteratorStatement.class);
	}

}
