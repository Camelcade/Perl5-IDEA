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
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionProviderUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * Created by hurricup on 31.05.2015.
 */
public class PerlPackageBuiltInCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public static final HashSet<LookupElementBuilder> PACKAGES_LOOKUP_ELEMENTS = new HashSet<LookupElementBuilder>();


	@Override
	protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet)
	{
		if (PACKAGES_LOOKUP_ELEMENTS.size() == 0)
		{
			Project project = parameters.getPosition().getProject();
			// fixme need workaround here; We've should make pre-set list to add, but project required for deprecation
			for (String packageName : PerlPackageUtil.BUILT_IN_ALL)
				PACKAGES_LOOKUP_ELEMENTS.add(PerlPackageCompletionProviderUtil.getPackageLookupElement(project, packageName));
		}
		resultSet.addAllElements(PACKAGES_LOOKUP_ELEMENTS);
	}
}
