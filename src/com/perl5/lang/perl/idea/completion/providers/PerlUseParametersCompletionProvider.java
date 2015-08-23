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

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.Processor;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.packageprocessor.IPerlPackageOptionsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.IPerlPackageParentsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.IPerlPackageProcessor;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionProviderUtil;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.PsiPerlUseStatement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by hurricup on 31.05.2015.
 */
public class PerlUseParametersCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public static final InsertHandler USE_OPTION_INSERT_HANDLER = new UseOptionInsertHandler();

	@Override
	protected void addCompletions(@NotNull final CompletionParameters parameters, ProcessingContext context, @NotNull final CompletionResultSet resultSet)
	{
		PsiElement stringContentElement = parameters.getPosition();
		final Project project = stringContentElement.getProject();

		PsiPerlUseStatement useStatement = PsiTreeUtil.getParentOfType(stringContentElement, PsiPerlUseStatement.class, true, PsiPerlStatement.class);

		if (useStatement != null)
		{
			IPerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();
			if (packageProcessor != null)
			{
				// fixme we should allow lookup elements customization by package processor
				if (packageProcessor instanceof IPerlPackageOptionsProvider)
				{
					HashMap<String, String> options = ((IPerlPackageOptionsProvider) packageProcessor).getOptions();

					if (options != null)
						for (Map.Entry<String, String> option : options.entrySet())
							resultSet.addElement(LookupElementBuilder
									.create(option.getKey())
									.withTypeText(option.getValue(), true)
									.withIcon(PerlIcons.PERL_OPTION)
									.withInsertHandler(USE_OPTION_INSERT_HANDLER));

					options = ((IPerlPackageOptionsProvider) packageProcessor).getOptionsBundles();

					if (options != null)
						for (Map.Entry<String, String> option : options.entrySet())
							resultSet.addElement(LookupElementBuilder
									.create(option.getKey())
									.withTypeText(option.getValue(), true)
									.withIcon(PerlIcons.PERL_OPTIONS)
									.withInsertHandler(USE_OPTION_INSERT_HANDLER));
				}

				if (packageProcessor instanceof IPerlPackageParentsProvider && ((IPerlPackageParentsProvider) packageProcessor).hasPackageFilesOptions())
					PerlPackageUtil.processPackageFilesForPsiElement(parameters.getPosition(), new Processor<String>()
					{
						@Override
						public boolean process(String s)
						{
							resultSet.addElement(PerlPackageCompletionProviderUtil.getPackageLookupElement(project, s));
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
											.withInsertHandler(USE_OPTION_INSERT_HANDLER)
							);
						for (String subName : exportOk)
							resultSet.addElement(LookupElementBuilder
											.create(subName)
											.withIcon(PerlIcons.SUB_GUTTER_ICON)
											.withTypeText("optional", true)
											.withInsertHandler(USE_OPTION_INSERT_HANDLER)
							);
					}
			}
		}
	}

	/**
	 * Parent pragma additional insert
	 */
	static class UseOptionInsertHandler implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, LookupElement item)
		{
			final Editor editor = context.getEditor();
			EditorModificationUtil.insertStringAtCaret(editor, " ");

			context.setLaterRunnable(new Runnable()
			{
				@Override
				public void run()
				{
					new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(context.getProject(), editor);
				}
			});
		}
	}
}
