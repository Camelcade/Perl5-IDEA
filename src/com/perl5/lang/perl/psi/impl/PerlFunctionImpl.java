package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PerlVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 01.05.2015.
 */
public class PerlFunctionImpl extends ASTWrapperPsiElement
{

	public PerlFunctionImpl(ASTNode node) {
		super(node);
	}

	// @todo what for?
//	public void accept(@NotNull PsiElementVisitor visitor) {
//		if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitBlock(this);
//		else super.accept(visitor);
//	}

}
