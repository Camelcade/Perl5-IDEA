package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.PerlSub;
import com.perl5.lang.perl.psi.PerlElementFactory;

import java.util.List;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlPsiImplUtil implements PerlElementTypes
{
//	public static List<PerlSub>


/*
	public static String getName(PerlPackageBare element) {
		ASTNode keyNode = element.getNode().findChildByType(PERL_PACKAGE);
		if (keyNode != null) {
			return keyNode.getText();
		} else {
			return null;
		}
	}

	public static PsiElement setName(PerlPackageBare element, String newName) {
		ASTNode packageNode = element.getNode().findChildByType(PERL_PACKAGE);
		if (packageNode != null) {
			PerlPackageBare packageBare = PerlElementFactory.createPerlPackageBare(element.getProject(), newName);
			ASTNode newPackageNode = packageBare.getFirstChild().getNode();
			element.getNode().replaceChild(packageNode, newPackageNode);
		}
		return element;
	}

	public static PsiElement getNameIdentifier(PerlPackageBare element) {
		ASTNode packageNode = element.getNode().findChildByType(PERL_PACKAGE);
		if (packageNode != null) {
			return packageNode.getPsi();
		} else {
			return null;
		}
	}
*/
}
