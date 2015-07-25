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
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.PerlInsertHandlers;
import com.perl5.lang.perl.psi.PsiPerlArrayVariable;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.PsiPerlHashVariable;
import com.perl5.lang.perl.psi.PsiPerlScalarVariable;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 01.06.2015.
 */
public class PerlVariableBuiltInCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public static final List<LookupElementBuilder> BUILT_IN_SCALARS = new ArrayList<>();
	public static final List<LookupElementBuilder> BUILT_IN_ARRAYS = new ArrayList<>();
	public static final List<LookupElementBuilder> BUILT_IN_HASHES = new ArrayList<>();
	public static final List<LookupElementBuilder> BUILT_IN_GLOBS = new ArrayList<>();

	// fill scalars
	static
	{
		for (String name : PerlScalarUtil.BUILT_IN)
			BUILT_IN_SCALARS.add(LookupElementBuilder
							.create(name)
							.withIcon(PerlIcons.SCALAR_GUTTER_ICON)
							.withBoldness(true)
			);

		for (String name : PerlArrayUtil.BUILT_IN)
			BUILT_IN_SCALARS.add(LookupElementBuilder
							.create(name)
							.withIcon(PerlIcons.ARRAY_GUTTER_ICON)
							.withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER)
							.withTailText("[]")
							.withBoldness(true)
			);

		for (String name : PerlHashUtil.BUILT_IN)
			BUILT_IN_SCALARS.add(LookupElementBuilder
							.create(name)
							.withIcon(PerlIcons.HASH_GUTTER_ICON)
							.withBoldness(true)
							.withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER)
							.withTailText("{}")
			);
	}

	// fill arrays
	static
	{
		for (String name : PerlArrayUtil.BUILT_IN)
			BUILT_IN_ARRAYS.add(LookupElementBuilder
					.create(name)
					.withIcon(PerlIcons.ARRAY_GUTTER_ICON)
					.withBoldness(true)
			);

		for (String name : PerlHashUtil.BUILT_IN)
			BUILT_IN_ARRAYS.add(LookupElementBuilder
							.create(name)
							.withIcon(PerlIcons.HASH_GUTTER_ICON)
							.withBoldness(true)
							.withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER)
							.withTailText("{}")
			);
	}

	// fill hashes
	static
	{
		for (String name : PerlHashUtil.BUILT_IN)
			BUILT_IN_HASHES.add(LookupElementBuilder
							.create(name)
							.withIcon(PerlIcons.HASH_GUTTER_ICON)
							.withBoldness(true)
			);
	}

	// fill globs
	static
	{
		// built-in globs
		for (String name : PerlGlobUtil.BUILT_IN)
			BUILT_IN_GLOBS.add(LookupElementBuilder
							.create(name)
							.withIcon(PerlIcons.GLOB_GUTTER_ICON)
							.withBoldness(true)
			);
	}


	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull CompletionResultSet resultSet)
	{
		PsiElement variableName = parameters.getPosition();
		PsiElement variable = variableName.getParent();

		if (variable instanceof PsiPerlScalarVariable)
			resultSet.addAllElements(BUILT_IN_SCALARS);
		else if (variable instanceof PsiPerlArrayVariable)
			resultSet.addAllElements(BUILT_IN_ARRAYS);
		else if (variable instanceof PsiPerlHashVariable)
			resultSet.addAllElements(BUILT_IN_HASHES);
		else if (variable instanceof PsiPerlGlobVariable)
			resultSet.addAllElements(BUILT_IN_GLOBS);
	}
}
