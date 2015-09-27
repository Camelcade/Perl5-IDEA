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
import com.perl5.lang.perl.psi.references.resolvers.PerlGlobVariableReferenceResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlGlobVariableReference extends PerlPolyVariantReference
{
	protected static final ResolveCache.PolyVariantResolver<PerlGlobVariableReference> RESOLVER = new PerlGlobVariableReferenceResolver();
	PerlGlobVariable myVariable;

	public PerlGlobVariableReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
		PsiElement parent = element.getParent();
		assert parent instanceof PerlGlobVariable;
		myVariable = (PerlGlobVariable) parent;
	}

	public PerlGlobVariable getVariable()
	{
		return myVariable;
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, false);
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		ResolveResult[] resolveResults = multiResolve(false);

		// got nothing
		if (resolveResults.length == 0)
			return null;

		// first left-side of assignment element
		for (ResolveResult result : resolveResults)
		{
			PsiElement element = result.getElement();
			if (element instanceof PerlGlobVariable && ((PerlGlobVariable) element).isLeftSideOfAssignment())
				return element;
		}

		// any element
		return resolveResults[0].getElement();
	}
}
