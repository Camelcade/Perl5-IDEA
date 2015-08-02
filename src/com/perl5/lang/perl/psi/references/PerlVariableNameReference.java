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

/**
 * Created by hurricup on 27.05.2015.
 */
public class PerlVariableNameReference extends PerlReferencePoly
{

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

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		List<ResolveResult> result = new ArrayList<ResolveResult>();
		;

		PsiElement variableContainer = myVariable.getParent();
		PerlVariable lexicalDeclaration = null;

		if (variableContainer instanceof PsiPerlVariableDeclarationLexical)
			return new ResolveResult[0];
		else if (!(variableContainer instanceof PsiPerlVariableDeclarationGlobal))
			lexicalDeclaration = myVariable.getLexicalDeclaration();

		if (lexicalDeclaration == null || lexicalDeclaration.getParent() instanceof PsiPerlVariableDeclarationGlobal)
		{
			// not found lexical declaration or our is closes to us

			// our variable declaration
			for (PerlGlobVariable glob : myVariable.getRelatedGlobs())
				result.add(new PsiElementResolveResult(glob));

			// globs
			for (PerlVariable globalDeclaration : myVariable.getGlobalDeclarations())
				result.add(new PsiElementResolveResult(globalDeclaration));
		} else
			result.add(new PsiElementResolveResult(lexicalDeclaration));

		return result.toArray(new ResolveResult[result.size()]);
	}

	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		PsiElement parent = element.getParent();
		if (parent instanceof PerlVariable || parent instanceof PerlGlobVariable)
			return super.isReferenceTo(parent) || super.isReferenceTo(element);

		return super.isReferenceTo(element);
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

		for (ResolveResult resolveResult : resolveResults)
			if (resolveResult.getElement() instanceof PerlGlobVariable)
				return resolveResult.getElement();

		return resolveResults[0].getElement();
	}
}
