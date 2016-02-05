// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlConstantDefinition;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import com.perl5.lang.perl.psi.mixins.PerlConstantDefinitionMixin;
import org.jetbrains.annotations.NotNull;

public class PsiPerlConstantDefinitionImpl extends PerlConstantDefinitionMixin implements PsiPerlConstantDefinition
{

	public PsiPerlConstantDefinitionImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitConstantDefinition(this);
		else super.accept(visitor);
	}

}
