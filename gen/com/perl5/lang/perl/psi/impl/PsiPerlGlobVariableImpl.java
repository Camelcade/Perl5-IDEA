// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlGlobVariableImplMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlGlobVariableImpl extends PerlGlobVariableImplMixin implements PsiPerlGlobVariable
{

	public PsiPerlGlobVariableImpl(ASTNode node)
	{
		super(node);
	}

	public PsiPerlGlobVariableImpl(com.perl5.lang.perl.idea.stubs.globs.PerlGlobStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitGlobVariable(this);
		else super.accept(visitor);
	}

}
