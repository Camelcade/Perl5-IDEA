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
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PerlSubReference extends PerlReferencePoly
{
	PerlSubNameElement mySubNameElement;

	public PerlSubReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
		assert element instanceof PerlSubNameElement;
		mySubNameElement = (PerlSubNameElement) element;
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		List<PsiElement> relatedItems = new ArrayList<PsiElement>();
		relatedItems.addAll(mySubNameElement.getSubDeclarations());
		relatedItems.addAll(mySubNameElement.getSubDefinitions());
		relatedItems.addAll(mySubNameElement.getRelatedGlobs());
		relatedItems.addAll(mySubNameElement.getConstantDefinitions());

		List<ResolveResult> result = new ArrayList<ResolveResult>();

		for (PsiElement element : relatedItems)
			result.add(new PsiElementResolveResult(element));

		return result.toArray(new ResolveResult[result.size()]);
	}


	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		if( element instanceof PerlGlobVariable || element instanceof PerlString || element instanceof PerlSubDeclaration || element instanceof PerlSubDefinition )
			return super.isReferenceTo(element);

		PsiElement parent = element.getParent();
		if( parent instanceof PerlGlobVariable || parent instanceof PerlString || parent instanceof PerlSubDeclaration || parent instanceof PerlSubDefinition )
			return isReferenceTo(parent);

		return false;
	}


	@Nullable
	@Override
	public PsiElement resolve()
	{
		ResolveResult[] resolveResults = multiResolve(false);
		if (resolveResults.length == 0)
			return null;

		for (ResolveResult resolveResult : resolveResults)
			if (resolveResult.getElement() instanceof PerlGlobVariable)
				return resolveResult.getElement();

		return resolveResults[0].getElement();
	}

}
