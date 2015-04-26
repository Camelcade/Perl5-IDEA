package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlElementFactory;
import com.perl5.lang.perl.psi.PerlPackageBare;
import com.perl5.lang.perl.psi.PerlPackageNamespace;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlPsiImpUtil
{
	public static final TokenSet PERL_PACKAGES = TokenSet.create(
			PerlElementTypes.PERL_PACKAGE_USER
			, PerlElementTypes.PERL_PACKAGE_BUILT_IN
			, PerlElementTypes.PERL_PACKAGE_BUILT_IN_DEPRECATED
			, PerlElementTypes.PERL_PACKAGE_BUILT_IN_PRAGMA
	);

	public static String getName(PerlPackageBare element) {
		ASTNode keyNode = element.getNode().findChildByType(PERL_PACKAGES);
		if (keyNode != null) {
			return keyNode.getText();
		} else {
			return null;
		}
	}

	public static PsiElement setName(PerlPackageBare element, String newName) {
		ASTNode packageNode = element.getNode().findChildByType(PERL_PACKAGES);
		if (packageNode != null) {
			PerlPackageBare packageBare = PerlElementFactory.createPerlPackageBare(element.getProject(), newName);
			ASTNode newPackageNode = packageBare.getFirstChild().getNode();
			element.getNode().replaceChild(packageNode, newPackageNode);
		}
		return element;
	}

	public static PsiElement getNameIdentifier(PerlPackageBare element) {
		ASTNode packageNode = element.getNode().findChildByType(PERL_PACKAGES);
		if (packageNode != null) {
			return packageNode.getPsi();
		} else {
			return null;
		}
	}
}
