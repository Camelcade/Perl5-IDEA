// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlAnnotation;
import com.perl5.lang.perl.psi.PsiPerlAttribute;
import com.perl5.lang.perl.psi.PsiPerlSubDeclaration;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlSubDeclarationImplMixin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsiPerlSubDeclarationImpl extends PerlSubDeclarationImplMixin implements PsiPerlSubDeclaration
{

	public PsiPerlSubDeclarationImpl(ASTNode node)
	{
		super(node);
	}

	public PsiPerlSubDeclarationImpl(com.perl5.lang.perl.idea.stubs.subsdeclarations.PerlSubDeclarationStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitSubDeclaration(this);
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

}
