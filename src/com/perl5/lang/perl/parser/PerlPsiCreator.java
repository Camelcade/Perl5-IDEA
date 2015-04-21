package com.perl5.lang.perl.parser;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

/**
 * Created by hurricup on 12.04.2015.
 * Creates PSI element by given AST node
 */
public class PerlPsiCreator implements PerlElementTypes
{
	public static PsiElement createElement(ASTNode node)
	{
		IElementType elem = node.getElementType();

		// custom elements here

		return new ASTWrapperPsiElement(node);
	}
}
