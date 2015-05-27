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

package com.perl5.lang.perl.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hurricup on 27.05.2015.
 * Misc helper methods
 */
public class PerlUtil
{
	/**
	 * Traverses PSI tree up from current element and finds all lexical variables definition (state, my)
	 * @param currentElement current Psi element to traverse from
	 * @return ArrayList of variables in declarations
	 */
	public static Collection<PerlVariable> findDeclaredLexicalVariables(PsiElement currentElement)
	{
		HashMap<String,PerlVariable> declarationsHash = new HashMap<>();

		assert currentElement instanceof PerlLexicalScopeElement;

		PerlLexicalScope currentScope = ((PerlLexicalScopeElement) currentElement).getLexicalScope();

		Collection<PerlVariableDeclaration> declarations = PsiTreeUtil.findChildrenOfType(currentElement.getContainingFile(), PerlVariableDeclaration.class);

		for(PerlVariableDeclaration declaration: declarations)
		{
			if( declaration.getTextOffset() < currentElement.getTextOffset())
			{
				// lexically ok
				PerlLexicalScope declarationScope = declaration.getLexicalScope();
				if( declarationScope == null 	// file level
					|| currentScope != null && PsiTreeUtil.isAncestor(declarationScope, currentScope, false)	// declaration is an ancestor
						)
				{
					for(PsiElement var: declaration.getPerlScalarList())
					{
						assert var instanceof PerlVariable;
						declarationsHash.put(var.getText(),(PerlVariable)var);
					}
					for(PsiElement var: declaration.getPerlArrayList())
					{
						assert var instanceof PerlVariable;
						declarationsHash.put(var.getText(),(PerlVariable)var);
					}
					for(PsiElement var: declaration.getPerlHashList())
					{
						assert var instanceof PerlVariable;
						declarationsHash.put(var.getText(),(PerlVariable)var);
					}
				}
			}
		}

		return declarationsHash.values();
	}




}
