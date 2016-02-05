// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PsiPerlNoStatement;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlNoStatementImplMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlNoStatementImpl extends PerlNoStatementImplMixin implements PsiPerlNoStatement
{

	public PsiPerlNoStatementImpl(ASTNode node)
	{
		super(node);
	}

	public PsiPerlNoStatementImpl(com.perl5.lang.perl.idea.stubs.imports.PerlUseStatementStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitNoStatement(this);
		else super.accept(visitor);
	}

}
