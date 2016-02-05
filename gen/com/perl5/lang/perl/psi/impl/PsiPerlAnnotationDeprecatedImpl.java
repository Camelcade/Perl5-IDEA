// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlAnnotationDeprecated;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;

public class PsiPerlAnnotationDeprecatedImpl extends PsiPerlAnnotationImpl implements PsiPerlAnnotationDeprecated
{

	public PsiPerlAnnotationDeprecatedImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitAnnotationDeprecated(this);
		else super.accept(visitor);
	}

}
