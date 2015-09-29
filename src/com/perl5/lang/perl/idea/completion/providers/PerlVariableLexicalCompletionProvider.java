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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionProviderUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by hurricup on 07.06.2015.
 */
public class PerlVariableLexicalCompletionProvider extends CompletionProvider<CompletionParameters>
{
	static void addScalarResults(@NotNull Collection<PerlVariableDeclarationWrapper> declaredVariables, @NotNull CompletionResultSet resultSet)
	{
		for (PerlVariableDeclarationWrapper variable : declaredVariables)
		{
			String variableName = variable.getName();
			if (variableName != null)
			{
				if (variable.getActualType() == PerlVariableType.SCALAR)
				{
					resultSet.addElement(PerlVariableCompletionProviderUtil.getScalarLookupElement(variableName));
				} else if (variable.getActualType() == PerlVariableType.ARRAY)
				{
					resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayElementLookupElement(variableName));
				} else if (variable.getActualType() == PerlVariableType.HASH)
				{
					resultSet.addElement(PerlVariableCompletionProviderUtil.getHashElementLookupElement(variableName));
				}
			}
		}

	}

	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull CompletionResultSet resultSet)
	{
		PsiElement variableNameElement = parameters.getOriginalPosition();

		if (variableNameElement == null)
			variableNameElement = parameters.getPosition();

		if (!(variableNameElement instanceof PerlVariableNameElement))
			return;
//		assert variableNameElement instanceof PerlVariableNameElement : "Got type " + variableNameElement.getClass();

		PsiElement perlVariable = variableNameElement.getParent();

		// fixme move this to pattern
		if (perlVariable instanceof PerlNamespaceElementContainer && ((PerlNamespaceElementContainer) perlVariable).getNamespaceElement() != null)
			return;

		PsiFile perlFile = variableNameElement.getContainingFile();
		assert perlFile instanceof PerlFileImpl;
		Collection<PerlVariableDeclarationWrapper> declaredVariables = ((PerlFileImpl) perlFile).getVisibleLexicalVariables(perlVariable);

		if (perlVariable instanceof PsiPerlScalarVariable)
			addScalarResults(declaredVariables, resultSet);
		else if (perlVariable instanceof PsiPerlArrayVariable)
		{
			for (PerlVariableDeclarationWrapper variable : declaredVariables)
			{
				String variableName = variable.getName();
				if (variableName != null)
				{
					if (variable.getActualType() == PerlVariableType.ARRAY)
					{
						resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayLookupElement(variableName));

					} else if (variable.getActualType() == PerlVariableType.HASH)
					{
						resultSet.addElement(PerlVariableCompletionProviderUtil.getHashSliceElementLookupElement(variableName));
					}
				}
			}
		} else if (perlVariable instanceof PsiPerlArrayIndexVariable)
		{
			for (PerlVariableDeclarationWrapper variable : declaredVariables)
			{
				String variableName = variable.getName();
				if (variableName != null)
				{
					if (variable.getActualType() == PerlVariableType.ARRAY)
					{
						resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayLookupElement(variableName));
					}
				}
			}
		} else if (perlVariable instanceof PsiPerlHashVariable)
		{
			for (PerlVariableDeclarationWrapper variable : declaredVariables)
			{
				String variableName = variable.getName();
				if (variableName != null)
				{
					if (variable.getActualType() == PerlVariableType.HASH)
					{
						resultSet.addElement(PerlVariableCompletionProviderUtil.getHashLookupElement(variableName));
					}
				}
			}
		}
	}

}
