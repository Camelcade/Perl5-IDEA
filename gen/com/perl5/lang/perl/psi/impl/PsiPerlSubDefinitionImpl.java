// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mixins.PerlSubDefinitionImplMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PsiPerlSubDefinitionImpl extends PerlSubDefinitionImplMixin implements PsiPerlSubDefinition
{

	public PsiPerlSubDefinitionImpl(ASTNode node)
	{
		super(node);
	}

	public PsiPerlSubDefinitionImpl(com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitSubDefinition(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public List<PsiPerlAnnotation> getAnnotationList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlAnnotation.class);
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
