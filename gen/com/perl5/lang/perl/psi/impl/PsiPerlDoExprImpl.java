// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.PsiPerlDoExpr;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlDoExprImplMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPerlDoExprImpl extends PerlDoExprImplMixin implements PsiPerlDoExpr
{

	public PsiPerlDoExprImpl(ASTNode node)
	{
		super(node);
	}

	public PsiPerlDoExprImpl(com.perl5.lang.perl.idea.stubs.imports.runtime.PerlRuntimeImportStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitDoExpr(this);
		else super.accept(visitor);
	}

	@Override
	@Nullable
	public PsiPerlBlock getBlock()
	{
		return findChildByClass(PsiPerlBlock.class);
	}

	@Override
	@Nullable
	public PsiPerlExpr getExpr()
	{
		return findChildByClass(PsiPerlExpr.class);
	}

}
