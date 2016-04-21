/*
 * Copyright 2016 Alexandr Evstigneev
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
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import com.perl5.lang.perl.idea.completion.util.PerlStringCompletionUtil;
import com.perl5.lang.perl.psi.impl.PerlAnnotationInjectImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 24.01.2016.
 */
public class PerlStringContentCompletionProvider extends CompletionProvider<CompletionParameters> implements PerlElementPatterns
{
	@Override
	protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull final CompletionResultSet result)
	{
		PsiElement element = parameters.getPosition();

		if (EXPORT_ASSIGNED_STRING_CONTENT.accepts(element)) // exporter assignments
		{
			PerlStringCompletionUtil.fillWithExportableEntities(element, result);
		}
		else if (SIMPLE_HASH_INDEX.accepts(element))    // hash indexes
		{
			PerlStringCompletionUtil.fillWithHashIndexes(element, result);
		}
		else if (USE_PARAMETERS_PATTERN.accepts(element))    // use or no parameters
		{
			PerlStringCompletionUtil.fillWithUseParameters(element, result);
		}
		else if (element.getParent() instanceof PerlAnnotationInjectImpl)
		{
			PerlStringCompletionUtil.fillWithInjectableMarkers(element, result);
		}
		else if (STRING_CONTENT_IN_HEREDOC_OPENER_PATTERN.accepts(element)) // HERE-DOC openers
		{
			PerlStringCompletionUtil.fillWithInjectableMarkers(element, result);
			PerlStringCompletionUtil.fillWithHeredocOpeners(element, result);
		}
		else if (STRING_CONTENT_IN_LIST_OR_STRING_START.accepts(element))    // begin of string or qw element
		{
			PerlStringCompletionUtil.fillWithRefTypes(result);
			PerlPackageCompletionUtil.fillWithAllPackageNames(element, result);
		}
		else
		{
			return;
		}
		result.stopHere();
	}
}
