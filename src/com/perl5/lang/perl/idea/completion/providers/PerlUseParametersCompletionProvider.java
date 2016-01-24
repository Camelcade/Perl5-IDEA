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
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.Processor;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageOptionsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageParentsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.PsiPerlUseStatement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;

/**
 * Created by hurricup on 31.05.2015.
 */
public class PerlUseParametersCompletionProvider extends CompletionProvider<CompletionParameters>
{
	@Override
	protected void addCompletions(@NotNull final CompletionParameters parameters, ProcessingContext context, @NotNull final CompletionResultSet resultSet)
	{
		PsiElement stringContentElement = parameters.getPosition();
		final Project project = stringContentElement.getProject();

		@SuppressWarnings("unchecked")
		PsiPerlUseStatement useStatement = PsiTreeUtil.getParentOfType(stringContentElement, PsiPerlUseStatement.class, true, PsiPerlStatement.class);

		if (useStatement != null)
		{
			PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();
			if (packageProcessor != null)
			{
				// fixme we should allow lookup elements customization by package processor
				if (packageProcessor instanceof PerlPackageOptionsProvider)
				{
					Map<String, String> options = ((PerlPackageOptionsProvider) packageProcessor).getOptions();

					for (Map.Entry<String, String> option : options.entrySet())
						resultSet.addElement(LookupElementBuilder
								.create(option.getKey())
								.withTypeText(option.getValue(), true)
								.withIcon(PerlIcons.PERL_OPTION)
						);

					options = ((PerlPackageOptionsProvider) packageProcessor).getOptionsBundles();

					for (Map.Entry<String, String> option : options.entrySet())
						resultSet.addElement(LookupElementBuilder
								.create(option.getKey())
								.withTypeText(option.getValue(), true)
								.withIcon(PerlIcons.PERL_OPTIONS)
						);
				}

				if (packageProcessor instanceof PerlPackageParentsProvider && ((PerlPackageParentsProvider) packageProcessor).hasPackageFilesOptions())
					PerlPackageUtil.processPackageFilesForPsiElement(parameters.getPosition(), new Processor<String>()
					{
						@Override
						public boolean process(String s)
						{
							resultSet.addElement(PerlPackageCompletionUtil.getPackageLookupElement(project, s));
							return true;
						}
					});

				String packageName = useStatement.getPackageName();
				if (packageName != null)
					for (PerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, packageName))
					{
						HashSet<String> export = new HashSet<String>(namespaceDefinition.getEXPORT());
						HashSet<String> exportOk = new HashSet<String>(namespaceDefinition.getEXPORT_OK());
						// fixme we should resove subs here and put them in with signatures
						for (String subName : export)
							resultSet.addElement(LookupElementBuilder
									.create(subName)
									.withIcon(PerlIcons.SUB_GUTTER_ICON)
									.withTypeText("default", true)
							);
						for (String subName : exportOk)
							resultSet.addElement(LookupElementBuilder
									.create(subName)
									.withIcon(PerlIcons.SUB_GUTTER_ICON)
									.withTypeText("optional", true)
							);
					}
			}
		}
	}
}
