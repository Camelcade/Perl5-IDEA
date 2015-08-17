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
import com.intellij.util.ProcessingContext;
import com.intellij.util.Processor;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionProviderUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 31.05.2015.
 */
public class PerlPackageCompletionProvider extends CompletionProvider<CompletionParameters>
{
	@Override
	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull final CompletionResultSet resultSet)
	{
		final Project project = parameters.getPosition().getProject();
		PerlPackageUtil.processPackageFilesForPsiElement(parameters.getPosition(), new Processor<String>()
		{
			@Override
			public boolean process(String s)
			{
				resultSet.addElement(PerlPackageCompletionProviderUtil.getPackageLookupElement(project, s));
				return true;
			}
		});
	}
}
