// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlSubSignatureContent;
import com.perl5.lang.perl.psi.PsiPerlSubSignatureElementIgnore;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsiPerlSubSignatureContentImpl extends ASTWrapperPsiElement implements PsiPerlSubSignatureContent
{

	public PsiPerlSubSignatureContentImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitSubSignatureContent(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public List<PsiPerlSubSignatureElementIgnore> getSubSignatureElementIgnoreList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlSubSignatureElementIgnore.class);
	}

}
