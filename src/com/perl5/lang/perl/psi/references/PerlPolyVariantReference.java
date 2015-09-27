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
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 24.05.2015.
 */
public abstract class PerlPolyVariantReference extends PerlReference implements PsiPolyVariantReference
{
	public PerlPolyVariantReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
	}

	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		ResolveResult[] results = multiResolve(false);
		PsiManager psiManager = getElement().getManager();

		for (ResolveResult result : results)
		{
			if (psiManager.areElementsEquivalent(result.getElement(), element))
				return true;
		}

		return false;
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		ResolveResult[] resolveResults = multiResolve(false);
		return resolveResults.length > 0 ? resolveResults[0].getElement() : null;
	}

}
