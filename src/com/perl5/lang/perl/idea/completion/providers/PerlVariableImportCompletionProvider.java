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
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.util.PerlUtil;
import com.perl5.lang.perl.util.processors.PerlNamespaceEntityProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 24.08.2015.
 */
public class PerlVariableImportCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull final CompletionResultSet resultSet)
	{
		PsiElement variableNameElement = parameters.getPosition();
		PsiElement perlVariable = variableNameElement.getParent();

		// fixme move this to pattern
		if (perlVariable instanceof PerlNamespaceElementContainer && ((PerlNamespaceElementContainer) perlVariable).getNamespaceElement() != null)
		{
			return;
		}

		PerlNamespaceContainer namespaceContainer = PsiTreeUtil.getParentOfType(perlVariable, PerlNamespaceContainer.class);

		if (namespaceContainer == null)
		{
			return;
		}

		String packageName = namespaceContainer.getPackageName();

		if (packageName == null) // incomplete package definition
		{
			return;
		}

		PerlNamespaceEntityProcessor<PerlExportDescriptor> processor = null;

		if (perlVariable instanceof PsiPerlScalarVariable)
		{
			processor = new PerlNamespaceEntityProcessor<PerlExportDescriptor>()
			{
				@Override
				public boolean process(String namespaceName, PerlExportDescriptor entity)
				{
					LookupElementBuilder lookupElement = null;
					if (entity.isScalar())
					{
						lookupElement = PerlVariableCompletionUtil.getScalarLookupElement(entity.getTargetName());
					}
					else if (entity.isArray())
					{
						lookupElement = PerlVariableCompletionUtil.getArrayElementLookupElement(entity.getTargetName());
					}
					else if (entity.isHash())
					{
						lookupElement = PerlVariableCompletionUtil.getHashElementLookupElement(entity.getTargetName());
					}

					if (lookupElement != null)
					{
						resultSet.addElement(lookupElement.withTypeText(entity.getTargetPackage(), true));
					}
					return true;
				}
			};
		}
		else if (perlVariable instanceof PsiPerlArrayVariable || perlVariable instanceof PsiPerlArrayIndexVariable)
		{
			processor = new PerlNamespaceEntityProcessor<PerlExportDescriptor>()
			{
				@Override
				public boolean process(String namespaceName, PerlExportDescriptor entity)
				{
					LookupElementBuilder lookupElement = null;
					if (entity.isArray())
					{
						lookupElement = PerlVariableCompletionUtil.getArrayLookupElement(entity.getTargetName());
					}
					else if (entity.isHash())
					{
						lookupElement = PerlVariableCompletionUtil.getHashSliceLookupElement(entity.getTargetName());
					}

					if (lookupElement != null)
					{
						resultSet.addElement(lookupElement.withTypeText(entity.getTargetPackage(), true));
					}
					return true;
				}
			};
		}
		else if (perlVariable instanceof PsiPerlHashVariable)
		{
			processor = new PerlNamespaceEntityProcessor<PerlExportDescriptor>()
			{
				@Override
				public boolean process(String namespaceName, PerlExportDescriptor entity)
				{
					LookupElementBuilder lookupElement = null;
					if (entity.isHash())
					{
						lookupElement = PerlVariableCompletionUtil.getHashLookupElement(entity.getTargetName());
					}

					if (lookupElement != null)
					{
						resultSet.addElement(lookupElement.withTypeText(entity.getTargetPackage(), true));
					}
					return true;
				}
			};
		}

		if (processor != null)
		{
			PerlUtil.processImportedEntities(namespaceContainer, processor);
		}
	}

}
