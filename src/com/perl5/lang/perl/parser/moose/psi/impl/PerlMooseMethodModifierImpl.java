/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.moose.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseMethodModifier;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;
import com.perl5.lang.perl.psi.PsiPerlParenthesisedExpr;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;
import com.perl5.lang.perl.psi.references.PerlSubReferenceSimple;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.11.2015.
 */
public class PerlMooseMethodModifierImpl extends PsiPerlStatementImpl implements PerlMooseMethodModifier
{
	public PerlMooseMethodModifierImpl(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PsiReference[] getReferences(PsiElement element)
	{
		PsiElement expr = getExpr();

		if (expr instanceof PsiPerlParenthesisedExpr)
		{
			expr = expr.getFirstChild();
			if (expr != null)
			{
				expr = expr.getNextSibling();
			}
		}

		if (expr instanceof PsiPerlCommaSequenceExpr)
		{
			PsiElement lastElement = expr.getLastChild();

			if (PsiTreeUtil.isAncestor(expr, element, true) && !PsiTreeUtil.isAncestor(lastElement, element, true))
			{
				return new PsiReference[]{new PerlSubReferenceSimple(element, null)};
			}
		}

		return null;
	}

}
