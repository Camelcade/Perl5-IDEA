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
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionProviderUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
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


	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull CompletionResultSet resultSet)
	{

		PsiElement variableNameElement = parameters.getPosition();
		PsiElement perlVariable = variableNameElement.getParent();
		Project project = variableNameElement.getProject();

		// fixme refactor smart variables selection by *package
		if (perlVariable instanceof PerlNamespaceElementContainer
				&& ((PerlNamespaceElementContainer) perlVariable).getNamespaceElement() != null
				&& ((PerlNamespaceElementContainer) perlVariable).getNamespaceElement().getCanonicalName() != null
				)
			resultSet = resultSet.withPrefixMatcher(
					((PerlNamespaceElementContainer) perlVariable).getNamespaceElement().getCanonicalName()
							+ "::"
							+ resultSet.getPrefixMatcher().getPrefix()
			);


		if (perlVariable instanceof PsiPerlScalarVariable)
		{
			// global scalars
			for (String name : PerlScalarUtil.getDefinedGlobalScalarNames(project))
				resultSet.addElement(PerlVariableCompletionProviderUtil.getScalarLookupElement(name));

			// global arrays
			for (String name : PerlArrayUtil.getDefinedGlobalArrayNames(project))
				resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayElementLookupElement(name));

			// global hashes
			for (String name : PerlHashUtil.getDefinedGlobalHashNames(project))
				resultSet.addElement(PerlVariableCompletionProviderUtil.getHashElementLookupElement(name));
		} else if (perlVariable instanceof PerlGlobVariable)
		{
			// global scalars
			for (String name : PerlScalarUtil.getDefinedGlobalScalarNames(project))
				resultSet.addElement(PerlVariableCompletionProviderUtil.getScalarLookupElement(name));

			// global arrays
			for (String name : PerlArrayUtil.getDefinedGlobalArrayNames(project))
				resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayElementLookupElement(name));

			// global hashes
			for (String name : PerlHashUtil.getDefinedGlobalHashNames(project))
				resultSet.addElement(PerlVariableCompletionProviderUtil.getHashElementLookupElement(name));

			// globs
			for (String name : PerlGlobUtil.getDefinedGlobsNames(project))
				resultSet.addElement(PerlVariableCompletionProviderUtil.getGlobLookupElement(name));
		} else if (perlVariable instanceof PsiPerlArrayVariable)
		{
			// global arrays
			for (String name : PerlArrayUtil.getDefinedGlobalArrayNames(project))
				resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayLookupElement(name));

			// global hashes
			for (String name : PerlHashUtil.getDefinedGlobalHashNames(project))
				resultSet.addElement(PerlVariableCompletionProviderUtil.getHashSliceElementLookupElement(name));
		} else if (perlVariable instanceof PsiPerlArrayIndexVariable)
		{
			// global arrays
			for (String name : PerlArrayUtil.getDefinedGlobalArrayNames(project))
				resultSet.addElement(PerlVariableCompletionProviderUtil.getArrayLookupElement(name));
		} else if (perlVariable instanceof PsiPerlHashVariable)
		{
			// global hashes
			for (String name : PerlHashUtil.getDefinedGlobalHashNames(project))
				resultSet.addElement(PerlVariableCompletionProviderUtil.getHashLookupElement(name));
		}
	}

}