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
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionProviderUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
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
	public static final List<LookupElementBuilder> BUILT_IN_ARRAYS_INDEXES = new ArrayList<>();
	public static final List<LookupElementBuilder> BUILT_IN_HASHES = new ArrayList<>();
	public static final List<LookupElementBuilder> BUILT_IN_GLOBS = new ArrayList<>();

	// fill scalars
	static
	{
		for (String name : PerlScalarUtil.BUILT_IN)
			BUILT_IN_SCALARS.add(PerlVariableCompletionProviderUtil.getScalarLookupElement(name).withBoldness(true));

		for (String name : PerlArrayUtil.BUILT_IN)
			BUILT_IN_SCALARS.add(PerlVariableCompletionProviderUtil.getArrayElementLookupElement(name).withBoldness(true));

		for (String name : PerlHashUtil.BUILT_IN)
			BUILT_IN_SCALARS.add(PerlVariableCompletionProviderUtil.getHashElementLookupElement(name).withBoldness(true));
	}

	// fill arrays
	static
	{
		for (String name : PerlArrayUtil.BUILT_IN)
			BUILT_IN_ARRAYS.add(PerlVariableCompletionProviderUtil.getArrayLookupElement(name).withBoldness(true));

		for (String name : PerlHashUtil.BUILT_IN)
			BUILT_IN_ARRAYS.add(PerlVariableCompletionProviderUtil.getHashSliceElementLookupElement(name).withBoldness(true));
	}

	// fill arrays indexes
	static
	{
		for (String name : PerlArrayUtil.BUILT_IN)
			BUILT_IN_ARRAYS.add(PerlVariableCompletionProviderUtil.getArrayLookupElement(name).withBoldness(true));
	}

	// fill hashes
	static
	{
		for (String name : PerlHashUtil.BUILT_IN)
			BUILT_IN_HASHES.add(PerlVariableCompletionProviderUtil.getHashLookupElement(name).withBoldness(true));
	}

	// fill globs
	static
	{
		// built-in globs
		for (String name : PerlGlobUtil.BUILT_IN)
			BUILT_IN_GLOBS.add(PerlVariableCompletionProviderUtil.getGlobLookupElement(name).withBoldness(true));
	}


	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull CompletionResultSet resultSet)
	{
		PsiElement variableNameElement = parameters.getPosition();
		PsiElement perlVariable = variableNameElement.getParent();

		// fixme move this to pattern
		if (perlVariable instanceof PerlNamespaceElementContainer && ((PerlNamespaceElementContainer) perlVariable).getNamespaceElement() != null)
			return;

		if (perlVariable instanceof PsiPerlScalarVariable
				|| perlVariable instanceof PerlVariable && ((PerlVariable) perlVariable).getScalarSigils() != null  // cast check
				|| perlVariable instanceof PerlGlobVariable && ((PerlGlobVariable) perlVariable).getScalarSigils() != null  // cast check
				)
			resultSet.addAllElements(BUILT_IN_SCALARS);
		else if (perlVariable instanceof PsiPerlArrayVariable)
			resultSet.addAllElements(BUILT_IN_ARRAYS);
		else if (perlVariable instanceof PsiPerlArrayIndexVariable)
			resultSet.addAllElements(BUILT_IN_ARRAYS_INDEXES);
		else if (perlVariable instanceof PsiPerlHashVariable)
			resultSet.addAllElements(BUILT_IN_HASHES);
		else if (perlVariable instanceof PsiPerlGlobVariable)
			resultSet.addAllElements(BUILT_IN_GLOBS);
	}
}
