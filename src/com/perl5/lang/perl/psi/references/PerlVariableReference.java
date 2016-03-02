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
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.psi.references.resolvers.PerlVariableReferenceResolver;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.05.2015.
 */
public class PerlVariableReference extends PerlPolyVariantReference<PerlVariableNameElement>
{
	protected static final ResolveCache.PolyVariantResolver<PerlVariableReference> RESOLVER = new PerlVariableReferenceResolver();

	private PerlVariable myVariable;

	public PerlVariableReference(@NotNull PerlVariableNameElement element, TextRange textRange)
	{
		super(element, textRange);
	}

	public PerlVariable getVariable()
	{
		if (myVariable == null)
		{
			assert myElement.getParent() instanceof PerlVariable;
			myVariable = (PerlVariable) myElement.getParent();
		}
		return myVariable;
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, incompleteCode);
	}

}
