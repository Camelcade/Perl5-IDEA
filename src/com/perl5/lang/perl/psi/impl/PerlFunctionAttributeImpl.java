package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

/**
 * Created by hurricup on 14.05.2015.
 */
public class PerlFunctionAttributeImpl extends ASTWrapperPsiElement
{
		public PerlFunctionAttributeImpl(ASTNode node) {
			super(node);
		}

		// @todo what for?
//	public void accept(@NotNull PsiElementVisitor visitor) {
//		if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitBlock(this);
//		else super.accept(visitor);
//	}

}
