// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PsiPerlSubExprImpl extends PsiPerlExprImpl implements PsiPerlSubExpr
{

	public PsiPerlSubExprImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitSubExpr(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public List<PsiPerlAttribute> getAttributeList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAttribute.class);
	}

	@Override
	@NotNull
	public PsiPerlBlock getBlock()
	{
		return findNotNullChildByClass(PsiPerlBlock.class);
	}

	@Override
	@Nullable
	public PsiPerlSubSignatureContent getSubSignatureContent()
	{
		return findChildByClass(PsiPerlSubSignatureContent.class);
	}

}
