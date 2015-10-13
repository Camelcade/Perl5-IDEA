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
import com.perl5.lang.perl.idea.completion.util.PerlCompletionUtil;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionProviderUtil;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.processors.PerlInternalIndexKeysProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 24.07.2015.
 */
public class PerlPackageSubCompletionProvider extends CompletionProvider<CompletionParameters>
{
	@Override
	protected void addCompletions(
			@NotNull CompletionParameters parameters,
			ProcessingContext context,
			final @NotNull CompletionResultSet result)
	{
		PsiElement method = parameters.getPosition().getParent();
		assert method instanceof PsiPerlMethod;

		PerlNamespaceElement explicitNamespace = ((PsiPerlMethod) method).getNamespaceElement();

		boolean isObjectMethod = ((PsiPerlMethod) method).isObjectMethod();

		String nameFilter = null;
		final Project project = parameters.getPosition().getProject();

		if (explicitNamespace != null)
		{
			nameFilter = explicitNamespace.getCanonicalName() + "::";
		}
		else
		{
			result.addElement(PerlPackageCompletionProviderUtil.getPackageLookupElementWithAutocomplete(project, "SUPER::"));
		}

		final String finalNameFilter = nameFilter;

		if (!isObjectMethod)
		{
			// fixme not dry with PerlPackageNamesCompletionProvider
			PerlPackageUtil.processDefinedPackageNames(PerlCompletionUtil.getCompletionScope(parameters), new PerlInternalIndexKeysProcessor()
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						if (finalNameFilter == null)
						{
							result.addElement(PerlPackageCompletionProviderUtil.getPackageLookupElementWithAutocomplete(project, s));
						}
						else if (s.startsWith(finalNameFilter))
						{
							result.addElement(PerlPackageCompletionProviderUtil.getPackageLookupElementWithAutocomplete(project, s.substring(finalNameFilter.length())));
						}
					}
					return true;
				}
			});
		}
	}
}
