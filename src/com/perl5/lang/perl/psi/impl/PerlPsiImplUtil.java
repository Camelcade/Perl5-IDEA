/*
 * Copyright 2015 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
