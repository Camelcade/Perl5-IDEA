// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PsiPerlUseStatement;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlUseStatementImplMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlUseStatementImpl extends PerlUseStatementImplMixin implements PsiPerlUseStatement
{

	public PsiPerlUseStatementImpl(ASTNode node)
	{
		super(node);
	}

	public PsiPerlUseStatementImpl(com.perl5.lang.perl.idea.stubs.imports.PerlUseStatementStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitUseStatement(this);
		else super.accept(visitor);
	}

}
