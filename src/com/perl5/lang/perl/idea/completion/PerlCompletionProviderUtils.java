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

package com.perl5.lang.perl.idea.completion;

import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hurricup on 25.07.2015.
 */
public class PerlCompletionProviderUtils
{
	public static final ConcurrentHashMap<String, LookupElementBuilder> PACKAGE_LOOKUP_ELEMENTS = new ConcurrentHashMap<>();
	public static final ConcurrentHashMap<String, LookupElementBuilder> PACKAGE_REOPEN_LOOKUP_ELEMENTS = new ConcurrentHashMap<>();

	public static final HashSet<LookupElementBuilder> BUILT_IN_SUB_LOOKUP_ELEMENTS = new HashSet<>();

	static
	{
		for( String subName: PerlSubUtil.BUILT_IN)
			BUILT_IN_SUB_LOOKUP_ELEMENTS.add(LookupElementBuilder
							.create(subName)
							.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
							.withBoldness(true)
			);
	}

	/**
	 * Returns package lookup element by package name
	 *
	 * @param packageName package name in any form
	 * @return lookup element
	 */
	public static @NotNull LookupElementBuilder getPackageLookupElement(String packageName)
	{
		LookupElementBuilder result = PACKAGE_LOOKUP_ELEMENTS.get(packageName);

		if (result == null)
		{
			result = LookupElementBuilder
					.create(packageName)
					.withIcon(PerlIcons.PACKAGE_GUTTER_ICON);

			if (PerlPackageUtil.isBuiltIn(packageName))
				result = result.withBoldness(true);

			// todo add pragma decoration for lookup element
//			if( PerlPackageUtil.isPragma(packageName))
//				result = result.withBoldness(true);

			if (PerlPackageUtil.isDeprecated(packageName))
				result = result.withStrikeoutness(true);


			PACKAGE_LOOKUP_ELEMENTS.put(packageName, result);
		}

		return result;
	}


	public static final InsertHandler COMPLETION_REOPENER = new CompletionOpener();

	/**
	 * Returns package lookup element with automatic re-opening autocompeltion
	 *
	 * @param packageName package name
	 * @return lookup element
	 */
	public static LookupElementBuilder getPackageLookupElementWithAutocomplete(String packageName)
	{
		LookupElementBuilder result = PACKAGE_REOPEN_LOOKUP_ELEMENTS.get(packageName);

		if (result == null)
		{
			result = getPackageLookupElement(packageName).withInsertHandler(COMPLETION_REOPENER);
			result = result.withTailText("...");
			PACKAGE_REOPEN_LOOKUP_ELEMENTS.put(packageName, result);
		}

		return result;
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
			if( context.getCompletionChar() != '\u0000')
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
