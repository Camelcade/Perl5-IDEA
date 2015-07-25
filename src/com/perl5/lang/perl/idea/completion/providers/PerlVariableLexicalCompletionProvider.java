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
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionProviderUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by hurricup on 07.06.2015.
 */
public class PerlVariableLexicalCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public void addCompletions(@NotNull final CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull final CompletionResultSet resultSet)
	{

		final PsiElement variableNameElement = parameters.getPosition();
		final PsiElement perlVariable = variableNameElement.getParent();
		PsiFile perlFile = variableNameElement.getContainingFile();
		assert perlFile instanceof PerlFileElement;
		final Collection<PerlVariable> declaredVariables = ((PerlFileElement) perlFile).getVisibleLexicalVariables(perlVariable);

		if (perlVariable instanceof PsiPerlScalarVariable)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					addScalarResults(declaredVariables, resultSet);
				}
			});
		else if (perlVariable instanceof PsiPerlArrayVariable)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					if (((PerlVariable) perlVariable).getScalarSigils() != null)
						addScalarResults(declaredVariables, resultSet);
					else
						for (PerlVariable variable : declaredVariables)
							if (variable instanceof PsiPerlArrayVariable)
							{
								PerlVariableNameElement variableName = variable.getVariableNameElement();
								if (variableName != null && variableName.getName() != null)
									resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayLookupElement(variableName.getName()));

							} else if (variable instanceof PsiPerlHashVariable)
							{
								PerlVariableNameElement variableName = variable.getVariableNameElement();
								if (variableName != null && variableName.getName() != null)
									resultSet.addElement(PerlVariableCompletionProviderUtil.getHashSliceElementLookupElement(variableName.getName()));
							}
				}
			});
		else if (perlVariable instanceof PsiPerlArrayIndexVariable)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					if (((PerlVariable) perlVariable).getScalarSigils() != null)
						addScalarResults(declaredVariables, resultSet);
					else
						for (PerlVariable variable : declaredVariables)
							if (variable instanceof PsiPerlArrayVariable)
							{
								PerlVariableNameElement variableName = variable.getVariableNameElement();
								if (variableName != null && variableName.getName() != null)
									resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayLookupElement(variableName.getName()));
							}
				}
			});
		else if (perlVariable instanceof PsiPerlHashVariable)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					if (((PerlVariable) perlVariable).getScalarSigils() != null)
						addScalarResults(declaredVariables, resultSet);
					else
						for (PerlVariable variable : declaredVariables)
							if (variable instanceof PsiPerlHashVariable)
							{
								PerlVariableNameElement variableName = variable.getVariableNameElement();
								if (variableName != null && variableName.getName() != null)
									resultSet.addElement(PerlVariableCompletionProviderUtil.getHashLookupElement(variableName.getName()));
							}
				}
			});
	}

	static void addScalarResults(@NotNull Collection<PerlVariable> declaredVariables, @NotNull CompletionResultSet resultSet)
	{
		for (PerlVariable variable : declaredVariables)
		{
			if (variable instanceof PsiPerlScalarVariable)
			{
				PerlVariableNameElement variableName = variable.getVariableNameElement();
				if (variableName != null && variableName.getName() != null)
					resultSet.addElement(PerlVariableCompletionProviderUtil.getScalarLookupElement(variableName.getName()));
			} else if (variable instanceof PsiPerlArrayVariable)
			{
				PerlVariableNameElement variableName = variable.getVariableNameElement();
				if (variableName != null && variableName.getName() != null)
					resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayElementLookupElement(variableName.getName()));
			} else if (variable instanceof PsiPerlHashVariable)
			{
				PerlVariableNameElement variableName = variable.getVariableNameElement();
				if (variableName != null && variableName.getName() != null)
					resultSet.addElement(PerlVariableCompletionProviderUtil.getHashElementLookupElement(variableName.getName()));
			}
		}

	}

}
