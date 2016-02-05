// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PsiPerlConstantName;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlConstantImplMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlConstantNameImpl extends PerlConstantImplMixin implements PsiPerlConstantName
{

	public PsiPerlConstantNameImpl(ASTNode node)
	{
		super(node);
	}

	public PsiPerlConstantNameImpl(com.perl5.lang.perl.idea.stubs.subsdefinitions.constants.PerlConstantStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitConstantName(this);
		else super.accept(visitor);
	}

}
