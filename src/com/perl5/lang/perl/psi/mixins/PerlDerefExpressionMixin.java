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
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlExprImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;

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
	public String getPreviousElementType(PsiElement methodElement)
	{
		// todo add some caching here
		if (methodElement == getFirstChild())    // first element
			return PerlPackageUtil.getContextPackageName(this);

		PsiElement currentElement = methodElement.getPrevSibling();
		if (currentElement.getNode().getElementType() == PerlElementTypes.OPERATOR_DEREFERENCE)
			currentElement = currentElement.getPrevSibling();

		return getCurrentElementType(currentElement);
	}

	public String getCurrentElementType(PsiElement currentElement)
	{
		while (currentElement != null && (currentElement instanceof PsiWhiteSpace || currentElement instanceof PsiComment))
		{
			currentElement = currentElement.getPrevSibling();
		}

		if (currentElement != null)
		{
			boolean isFirst = currentElement == getFirstChild();

			if (currentElement instanceof PsiPerlNamespaceExpr)
				return ((PerlNamespaceElement) currentElement.getFirstChild()).getCanonicalName();
			else if (isFirst && currentElement instanceof PerlVariable)
				return ((PerlVariable) currentElement).guessVariableType();
			else if (currentElement instanceof PerlMethodContainer)
				return PerlSubUtil.getMethodReturnValue((PerlMethodContainer) currentElement);
			else if (currentElement instanceof PsiPerlTagScalar && "__PACKAGE__".equals(currentElement.getText()))
				return PerlPackageUtil.getContextPackageName(this);
		}
		return null;

	}

	public String guessType()
	{
		PsiElement[] children = getChildren();
		if (children.length > 0)
			return getCurrentElementType(children[children.length - 1]);
		return null;
	}


}
