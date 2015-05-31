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

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 31.05.2015.
 *
 */
public class PerlPackageCompletionProvider extends CompletionProvider<CompletionParameters>
{
	@Override
	public void addCompletions(@NotNull final CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull final CompletionResultSet resultSet)
	{

		ApplicationManager.getApplication().runReadAction(new Runnable()
		{
			@Override
			public void run()
			{
				PsiFile file = parameters.getOriginalFile();

				// fixme actually, we should fill files here, not packages, or make a diff provider
				for (String packageName : PerlPackageUtil.listDefinedPackageNames(file.getProject()))
				{
					InsertHandler<LookupElement> insertHandler = null;

					LookupElementBuilder newElement = LookupElementBuilder.create(packageName).withIcon(PerlIcons.PM_FILE);

					if (insertHandler == null)
						insertHandler = PerlInsertHandlers.SEMI_NEWLINE_INSERT_HANDLER;

					resultSet.addElement(newElement.withInsertHandler(insertHandler));

				}
			}
		});
	}

}
