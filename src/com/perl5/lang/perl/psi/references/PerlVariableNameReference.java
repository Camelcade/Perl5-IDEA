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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.psi.references.resolvers.PerlVariableReferenceResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 27.05.2015.
 */
public class PerlVariableNameReference extends PerlReferencePoly
{
	protected static final ResolveCache.PolyVariantResolver<PerlVariableNameReference> RESOLVER = new PerlVariableReferenceResolver();

	private PerlVariable myVariable;

	public PerlVariableNameReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
		assert element instanceof PerlVariableNameElement;

		if (element.getParent() instanceof PerlVariable)
			myVariable = (PerlVariable) element.getParent();
		else
			throw new RuntimeException("Can't be: got myVariable name without a myVariable");

	}

	public PerlVariable getVariable()
	{
		return myVariable;
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, false);
	}

	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		if (element instanceof PerlVariable || element instanceof PerlGlobVariable)
			return super.isReferenceTo(element);
		else if (element instanceof PerlVariableNameElement)
			return isReferenceTo(element.getParent());

		return false;
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		ResolveResult[] resolveResults = multiResolve(false);
		if (resolveResults.length == 0)
			return null;
		else if (resolveResults.length == 1)
			return resolveResults[0].getElement();


		PerlGlobVariable lastGlob = null;
		for (ResolveResult resolveResult : resolveResults)
			if (resolveResult.getElement() instanceof PerlGlobVariable)
			{
				lastGlob = (PerlGlobVariable) resolveResult.getElement();
				if (lastGlob.isLeftSideOfAssignment())
					return lastGlob;
			}

		if (lastGlob != null)
			return lastGlob;

		return resolveResults[0].getElement();
	}

	@Override
	public TextRange getRangeInElement()
	{
		return new TextRange(0, myElement.getTextLength());
	}
}
