// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PsiPerlAnnotationDeprecated;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlNamespaceDefinitionImplMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlNamespaceDefinitionImpl extends PerlNamespaceDefinitionImplMixin implements PsiPerlNamespaceDefinition
{

	public PsiPerlNamespaceDefinitionImpl(ASTNode node)
	{
		super(node);
	}

	public PsiPerlNamespaceDefinitionImpl(com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitNamespaceDefinition(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlAnnotationDeprecated getAnnotationDeprecated()
	{
		return findChildByClass(PsiPerlAnnotationDeprecated.class);
	}

	@Override
	@Nullable
	public PsiPerlBlock getBlock()
	{
		return findChildByClass(PsiPerlBlock.class);
	}

}
