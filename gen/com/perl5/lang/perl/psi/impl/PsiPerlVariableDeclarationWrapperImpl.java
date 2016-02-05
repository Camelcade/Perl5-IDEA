// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlVariableDeclarationWrapperMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlVariableDeclarationWrapperImpl extends PerlVariableDeclarationWrapperMixin implements PsiPerlVariableDeclarationWrapper
{

	public PsiPerlVariableDeclarationWrapperImpl(ASTNode node)
	{
		super(node);
	}

	public PsiPerlVariableDeclarationWrapperImpl(com.perl5.lang.perl.idea.stubs.variables.PerlVariableStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitVariableDeclarationWrapper(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public PsiPerlExpr getExpr()
	{
		return findNotNullChildByClass(PsiPerlExpr.class);
	}

}
