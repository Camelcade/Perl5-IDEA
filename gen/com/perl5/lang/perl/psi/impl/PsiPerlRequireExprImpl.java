// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PsiPerlRequireExpr;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlRequireExprImplMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlRequireExprImpl extends PerlRequireExprImplMixin implements PsiPerlRequireExpr
{

	public PsiPerlRequireExprImpl(ASTNode node)
	{
		super(node);
	}

	public PsiPerlRequireExprImpl(com.perl5.lang.perl.idea.stubs.imports.runtime.PerlRuntimeImportStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitRequireExpr(this);
		else super.accept(visitor);
	}

}
