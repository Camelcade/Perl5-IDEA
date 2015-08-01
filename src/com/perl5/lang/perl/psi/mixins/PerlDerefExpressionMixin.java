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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlExprImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;

/**
 * Created by hurricup on 07.06.2015.
 */
public abstract class PerlDerefExpressionMixin extends PsiPerlExprImpl implements PsiPerlDerefExpr
{
	public PerlDerefExpressionMixin(ASTNode node)
	{
		super(node);
	}

	@Override
	public String getPackageNameForElement(PsiElement methodElement)
	{
		// todo add some caching here
		if (methodElement == getFirstChild())    // first element
			return PerlPackageUtil.getContextPackageName(this);

		PsiElement currentElement = methodElement.getPrevSibling();
		if (currentElement.getNode().getElementType() == PerlElementTypes.OPERATOR_DEREFERENCE)
			currentElement = currentElement.getPrevSibling();

		assert currentElement != null;
		boolean isFirst = currentElement == getFirstChild();

		if (currentElement instanceof PsiPerlNamespaceExpr)
			return ((PerlNamespaceElement) currentElement.getFirstChild()).getCanonicalName();
		else if (isFirst && currentElement instanceof PsiPerlVariableExpr && currentElement.getFirstChild() instanceof PerlVariable)
			return ((PerlVariable) currentElement.getFirstChild()).guessVariableType();
		else if (
				currentElement instanceof PerlMethodContainer
						&& ((PerlMethodContainer) currentElement).getMethod() != null
						&& ((PerlMethodContainer) currentElement).getMethod().getSubNameElement() != null
				)
		{
			// fixme this should be moved to a method
			PerlSubNameElement subNameElement = ((PerlMethodContainer) currentElement).getMethod().getSubNameElement();

			if (subNameElement != null)
			{
				if ("new".equals(subNameElement.getName()))
					return ((PerlMethodContainer) currentElement).getMethod().getPackageName();
				for (PerlSubDefinition subDefinition : subNameElement.getSubDefinitions())
					if (subDefinition.getSubAnnotations().getReturns() != null)
						return subDefinition.getSubAnnotations().getReturns();
				for (PerlSubDeclaration subDeclaration : subNameElement.getSubDeclarations())
					if (subDeclaration.getSubAnnotations().getReturns() != null)
						return subDeclaration.getSubAnnotations().getReturns();
			}
		}


		return null;
	}
}
