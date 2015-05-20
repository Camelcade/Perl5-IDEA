package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.05.2015.
 */
public class PerlCallableImplMixin extends ASTWrapperPsiElement
{
	PerlCallableImplMixin(@NotNull final ASTNode node){super(node);}


	public PsiElement getPrevPsiElement()
	{
		return getParent().getParent().getPrevSibling();
	}
}
