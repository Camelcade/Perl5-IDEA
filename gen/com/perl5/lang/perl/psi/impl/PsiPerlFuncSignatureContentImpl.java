// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlFuncSignatureContent;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsiPerlFuncSignatureContentImpl extends PsiPerlSubSignatureContentImpl implements PsiPerlFuncSignatureContent
{

	public PsiPerlFuncSignatureContentImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitFuncSignatureContent(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public List<PsiPerlVariableDeclarationWrapper> getVariableDeclarationWrapperList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlVariableDeclarationWrapper.class);
	}

}
