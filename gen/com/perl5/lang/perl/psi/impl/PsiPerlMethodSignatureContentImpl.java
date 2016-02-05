// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlMethodSignatureContent;
import com.perl5.lang.perl.psi.PsiPerlMethodSignatureInvocant;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PsiPerlMethodSignatureContentImpl extends PsiPerlSubSignatureContentImpl implements PsiPerlMethodSignatureContent
{

	public PsiPerlMethodSignatureContentImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitMethodSignatureContent(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlMethodSignatureInvocant getMethodSignatureInvocant()
	{
		return findChildByClass(PsiPerlMethodSignatureInvocant.class);
	}

	@Override
	@NotNull
	public List<PsiPerlVariableDeclarationWrapper> getVariableDeclarationWrapperList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlVariableDeclarationWrapper.class);
	}

}
