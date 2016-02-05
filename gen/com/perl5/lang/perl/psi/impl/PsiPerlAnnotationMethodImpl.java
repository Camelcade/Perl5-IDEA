// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PsiPerlAnnotationMethod;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;

public class PsiPerlAnnotationMethodImpl extends PsiPerlAnnotationImpl implements PsiPerlAnnotationMethod
{

	public PsiPerlAnnotationMethodImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PsiPerlVisitor) ((PsiPerlVisitor) visitor).visitAnnotationMethod(this);
		else super.accept(visitor);
	}

}
