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

package com.perl5.lang.perl.idea.completion.util;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.processors.PerlInternalIndexKeysProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.07.2015.
 */
public class PerlPackageCompletionUtil
{
	public static final InsertHandler COMPLETION_REOPENER = new CompletionOpener();

	public static void fillWithAllPackageNames(@NotNull PsiElement element, @NotNull final CompletionResultSet result)
	{
		final Project project = element.getProject();

		PerlPackageUtil.processDefinedPackageNames(PerlScopes.getProjectAndLibrariesScope(project), new PerlInternalIndexKeysProcessor()
		{
			@Override
			public boolean process(String s)
			{
				if (super.process(s))
					result.addElement(PerlPackageCompletionUtil.getPackageLookupElementWithAutocomplete(project, s));
				return true;
			}
		});
	}


	/**
	 * Returns package lookup element by package name
	 *
	 * @param packageName package name in any form
	 * @return lookup element
	 */
	@NotNull
	public static LookupElementBuilder getPackageLookupElement(Project project, String packageName)
	{
		LookupElementBuilder result = LookupElementBuilder
				.create(packageName)
				.withIcon(PerlIcons.PACKAGE_GUTTER_ICON);

		if (PerlPackageUtil.isBuiltIn(packageName))
			result = result.withBoldness(true);

		if (PerlPackageUtil.isPragma(packageName))
			result = result.withIcon(PerlIcons.PRAGMA_GUTTER_ICON);

		if (PerlPackageUtil.isDeprecated(project, packageName))
			result = result.withStrikeoutness(true);

		return result;
	}

	/**
	 * Returns package lookup element with automatic re-opening autocompeltion
	 *
	 * @param packageName package name
	 * @return lookup element
	 */
	public static LookupElementBuilder getPackageLookupElementWithAutocomplete(Project project, String packageName)
	{
		return getPackageLookupElement(project, packageName)
				.withInsertHandler(COMPLETION_REOPENER)
				.withTailText("...");
	}

	/**
	 * Parent pragma additional insert
	 */
	static class CompletionOpener implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, final LookupElement item)
		{
			// fixme this is bad check for auto-inserting, i belive
			if (context.getCompletionChar() != '\u0000')
				context.setLaterRunnable(new Runnable()
				{
					@Override
					public void run()
					{
						Editor editor = context.getEditor();
						new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(context.getProject(), editor);
					}
				});
		}
	}

}
