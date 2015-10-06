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

package com.perl5.lang.perl.idea.refactoring.rename;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.perl5.lang.perl.psi.PerlGlobVariable;

import java.util.Map;

/**
 * Created by hurricup on 03.10.2015.
 */
public abstract class PerlRenamePolyReferencedElementProcessor extends RenamePsiElementProcessor
{
	@Override
	public void prepareRenaming(PsiElement element, String newName, Map<PsiElement, String> allRenames, SearchScope scope)
	{
		boolean globScanned = element instanceof PerlGlobVariable;

		for (PsiReference reference : ReferencesSearch.search(element, element.getUseScope()).findAll())
		{
			if (reference instanceof PsiPolyVariantReference)
			{
				for (ResolveResult resolveResult : ((PsiPolyVariantReference) reference).multiResolve(false))
				{
					PsiElement resolveResultElement = resolveResult.getElement();
					if (!allRenames.containsKey(resolveResultElement))
					{
						preparePsiElementRenaming(resolveResultElement, newName, allRenames);
						if (!globScanned && resolveResultElement instanceof PerlGlobVariable)
						{
							globScanned = true;
							prepareRenaming(resolveResultElement, newName, allRenames, scope);
						}
					}
				}
			}
		}
	}

	public void preparePsiElementRenaming(PsiElement element, String newBaseName, Map<PsiElement, String> allRenames)
	{
		allRenames.put(element, newBaseName);
	}
}
