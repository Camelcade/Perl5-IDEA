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
import com.perl5.lang.perl.util.*;
import com.perl5.lang.perl.util.processors.PerlInternalIndexKeysProcessor;
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
		PerlNamespaceElement namespaceElement = null;

		if (perlVariable instanceof PerlNamespaceElementContainer)
		{
			namespaceElement = ((PerlNamespaceElementContainer) perlVariable).getNamespaceElement();
		}


		boolean forceShortMain = false;

		// fixme refactor smart variables selection by *package
		if (namespaceElement != null)
		{
			String namespaceName = namespaceElement.getText();
			String namespaceCanonicalName = namespaceElement.getCanonicalName();

			if (PerlPackageUtil.PACKAGE_SEPARATOR.equals(namespaceName))
			{
				resultSet = resultSet.withPrefixMatcher(
						PerlPackageUtil.PACKAGE_SEPARATOR
								+ resultSet.getPrefixMatcher().getPrefix()
				);
				forceShortMain = true;
			}
			else if (namespaceCanonicalName != null)
			{
				resultSet = resultSet.withPrefixMatcher(
						((PerlNamespaceElementContainer) perlVariable).getNamespaceElement().getCanonicalName()
								+ PerlPackageUtil.PACKAGE_SEPARATOR
								+ resultSet.getPrefixMatcher().getPrefix()
				);
			}
		}

		final CompletionResultSet finalResultSet = resultSet;

		if (perlVariable instanceof PsiPerlScalarVariable)
		{
			// global scalars
			PerlScalarUtil.processDefinedGlobalScalarNames(project, new PerlInternalIndexKeysProcessor(forceShortMain)
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						finalResultSet.addElement(PerlVariableCompletionProviderUtil.getScalarLookupElement(adjustName(s)));
					}
					return true;
				}
			});

			// global arrays
			PerlArrayUtil.processDefinedGlobalArrayNames(project, new PerlInternalIndexKeysProcessor(forceShortMain)
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						finalResultSet.addElement(PerlVariableCompletionProviderUtil.getArrayElementLookupElement(adjustName(s)));
					}
					return true;
				}
			});

			// global hashes
			PerlHashUtil.processDefinedGlobalHashNames(project, new PerlInternalIndexKeysProcessor(forceShortMain)
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						finalResultSet.addElement(PerlVariableCompletionProviderUtil.getHashElementLookupElement(adjustName(s)));
					}
					return true;
				}
			});

		}
		else if (perlVariable instanceof PerlGlobVariable)
		{
			// global scalars
			PerlScalarUtil.processDefinedGlobalScalarNames(project, new PerlInternalIndexKeysProcessor(forceShortMain)
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						finalResultSet.addElement(PerlVariableCompletionProviderUtil.getScalarLookupElement(adjustName(s)));
					}
					return true;
				}
			});

			// global arrays
			PerlArrayUtil.processDefinedGlobalArrayNames(project, new PerlInternalIndexKeysProcessor(forceShortMain)
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						finalResultSet.addElement(PerlVariableCompletionProviderUtil.getArrayLookupElement(adjustName(s)));
					}
					return true;
				}
			});

			// global hashes
			PerlHashUtil.processDefinedGlobalHashNames(project, new PerlInternalIndexKeysProcessor(forceShortMain)
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						finalResultSet.addElement(PerlVariableCompletionProviderUtil.getHashLookupElement(adjustName(s)));
					}
					return true;
				}
			});

			// globs
			PerlGlobUtil.processDefinedGlobsNames(project, new PerlInternalIndexKeysProcessor(forceShortMain)
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						finalResultSet.addElement(PerlVariableCompletionProviderUtil.getGlobLookupElement(adjustName(s)));
					}
					return true;
				}
			});
		}
		else if (perlVariable instanceof PsiPerlArrayVariable)
		{
			// global arrays
			PerlArrayUtil.processDefinedGlobalArrayNames(project, new PerlInternalIndexKeysProcessor(forceShortMain)
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						finalResultSet.addElement(PerlVariableCompletionProviderUtil.getArrayLookupElement(adjustName(s)));
					}
					return true;
				}
			});

			// global hashes
			PerlHashUtil.processDefinedGlobalHashNames(project, new PerlInternalIndexKeysProcessor(forceShortMain)
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						finalResultSet.addElement(PerlVariableCompletionProviderUtil.getHashSliceElementLookupElement(adjustName(s)));
					}
					return true;
				}
			});
		}
		else if (perlVariable instanceof PsiPerlArrayIndexVariable)
		{
			// global arrays
			PerlArrayUtil.processDefinedGlobalArrayNames(project, new PerlInternalIndexKeysProcessor(forceShortMain)
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						finalResultSet.addElement(PerlVariableCompletionProviderUtil.getArrayLookupElement(adjustName(s)));
					}
					return true;
				}
			});
		}
		else if (perlVariable instanceof PsiPerlHashVariable)
		{
			// global hashes
			PerlHashUtil.processDefinedGlobalHashNames(project, new PerlInternalIndexKeysProcessor(forceShortMain)
			{
				@Override
				public boolean process(String s)
				{
					if (super.process(s))
					{
						finalResultSet.addElement(PerlVariableCompletionProviderUtil.getHashLookupElement(adjustName(s)));
					}
					return true;
				}
			});
		}
	}

}