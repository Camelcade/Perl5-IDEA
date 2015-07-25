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
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionProviderUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 01.06.2015.
 */
public class PerlVariableGlobalCompletionProvider extends CompletionProvider<CompletionParameters>
{


	public void addCompletions(@NotNull final CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull final CompletionResultSet resultSet)
	{

		final PsiElement variableNameElement = parameters.getPosition();
		final PsiElement perlVariable = variableNameElement.getParent();

		if (perlVariable instanceof PerlGlobVariable)
		{
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					// global scalars
					for (String name : PerlScalarUtil.listDefinedGlobalScalars(variableNameElement.getProject()))
						resultSet.addElement(PerlVariableCompletionProviderUtil.getScalarLookupElement(name));

					// global arrays
					for (String name : PerlArrayUtil.listDefinedGlobalArrays(variableNameElement.getProject()))
						resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayElementLookupElement(name));

					// global hashes
					for (String name : PerlHashUtil.listDefinedGlobalHahses(variableNameElement.getProject()))
						resultSet.addElement(PerlVariableCompletionProviderUtil.getHashElementLookupElement(name));

					// globs
					for (String name : PerlGlobUtil.getDefinedGlobsNames(variableNameElement.getProject()))
						resultSet.addElement(PerlVariableCompletionProviderUtil.getGlobLookupElement(name));
				}
			});
		} else if (perlVariable instanceof PsiPerlScalarVariable)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					// global scalars
					for (String name : PerlScalarUtil.listDefinedGlobalScalars(variableNameElement.getProject()))
						resultSet.addElement(PerlVariableCompletionProviderUtil.getScalarLookupElement(name));

					// global arrays
					for (String name : PerlArrayUtil.listDefinedGlobalArrays(variableNameElement.getProject()))
						resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayElementLookupElement(name));

					// global hashes
					for (String name : PerlHashUtil.listDefinedGlobalHahses(variableNameElement.getProject()))
						resultSet.addElement(PerlVariableCompletionProviderUtil.getHashElementLookupElement(name));
				}
			});
		else if (perlVariable instanceof PsiPerlArrayVariable)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					boolean isScalarCast = ((PsiPerlArrayVariable) perlVariable).getScalarSigils() != null;

					// global scalars for casts
					if (isScalarCast)
						for (String name : PerlScalarUtil.listDefinedGlobalScalars(variableNameElement.getProject()))
							resultSet.addElement(PerlVariableCompletionProviderUtil.getScalarLookupElement(name));

					// global arrays
					for (String name : PerlArrayUtil.listDefinedGlobalArrays(variableNameElement.getProject()))
						resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayLookupElement(name));

					// global hashes
					for (String name : PerlHashUtil.listDefinedGlobalHahses(variableNameElement.getProject()))
						resultSet.addElement(PerlVariableCompletionProviderUtil.getHashSliceElementLookupElement(name));
				}
			});
		else if (perlVariable instanceof PsiPerlArrayIndexVariable)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					boolean isScalarCast = ((PsiPerlArrayIndexVariable) perlVariable).getScalarSigils() != null;

					// global scalars
					if (isScalarCast)
						for (String name : PerlScalarUtil.listDefinedGlobalScalars(variableNameElement.getProject()))
							resultSet.addElement(PerlVariableCompletionProviderUtil.getScalarLookupElement(name));

					// global arrays
					for (String name : PerlArrayUtil.listDefinedGlobalArrays(variableNameElement.getProject()))
						resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayLookupElement(name));
				}
			});
		else if (perlVariable instanceof PsiPerlHashVariable)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{

					boolean isScalarCast = ((PsiPerlHashVariable) perlVariable).getScalarSigils() != null;

					// global scalars
					if (isScalarCast)
						for (String name : PerlScalarUtil.listDefinedGlobalScalars(variableNameElement.getProject()))
							resultSet.addElement(PerlVariableCompletionProviderUtil.getScalarLookupElement(name));

					// global hashes
					for (String name : PerlHashUtil.listDefinedGlobalHahses(variableNameElement.getProject()))
						resultSet.addElement(PerlVariableCompletionProviderUtil.getHashLookupElement(name));

				}
			});
	}
}