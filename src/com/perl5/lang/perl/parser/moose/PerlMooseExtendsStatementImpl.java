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

package com.perl5.lang.perl.parser.moose;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;
import com.perl5.lang.perl.psi.references.PerlNamespaceReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 25.11.2015.
 */
public class PerlMooseExtendsStatementImpl extends PsiPerlStatementImpl implements PerlMooseExtendsStatement
{
	public PerlMooseExtendsStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void changeParentsList(@NotNull List<String> currentList)
	{
		currentList.clear();
		currentList.addAll(getParentsList());
	}

	@NotNull
	protected List<String> getParentsList()
	{
		List<String> result = new ArrayList<String>();

		PsiElement expr = getExpr();
		if (expr != null)
		{
			if (expr instanceof PerlString)
			{
				String content = ((PerlString) expr).getStringContent();
				if (!content.isEmpty())
				{
					result.add(content);
				}
			}
			else if (expr instanceof PsiPerlCommaSequenceExpr)
			{
				PsiElement element = expr.getFirstChild();
				while (element != null)
				{
					if (element instanceof PerlString)
					{
						String content = ((PerlString) element).getStringContent();
						if (!content.isEmpty())
						{
							result.add(content);
						}
					}
					element = element.getNextSibling();
				}
			}
			else
			{
				// todo we need to somehow mark statement as bad
			}
		}

		return result;
	}

	@Nullable
	@Override
	public PsiReference[] getReferences(PsiElement element)
	{
		PsiElement string = element.getParent();
		if (string != null)
		{
			PsiElement meOrCommaSequence = string.getParent();
			if (meOrCommaSequence == this || meOrCommaSequence != null && meOrCommaSequence.getParent() == this)
			{
				return new PsiReference[]{new PerlNamespaceReference(element, null)};
			}
		}
		return null;
	}
}
