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

package com.perl5.lang.htmlmason.idea.completion;

import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.htmlmason.parser.psi.references.HTMLMasonMethodReference;
import com.perl5.lang.perl.psi.PerlString;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.03.2016.
 */
public class HTMLMasonComponentCompletionProvider extends CompletionProvider<CompletionParameters>
{
	@Override
	protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
	{
		PsiElement position = parameters.getPosition();
		PsiElement parent = position.getParent();
		PsiFile file = position.getContainingFile();

		if (parent instanceof PerlString && file instanceof HTMLMasonFileImpl)
		{
			PsiReference[] references = parent.getReferences();
			for (PsiReference reference : references)
			{
				TextRange refRange = reference.getRangeInElement();
				if (refRange.contains(position.getStartOffsetInParent()))
				{
					Project project = position.getProject();

					String fullPrefix = refRange.substring(parent.getText())
							.replace(CompletionInitializationContext.DUMMY_IDENTIFIER, "")
							.replace(CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED, "");

					final CompletionResultSet finalResultSet = result.withPrefixMatcher(fullPrefix);
					;

					if (reference instanceof HTMLMasonMethodReference)
					{
						PsiElement firstReferenceTarget = references[0].resolve();
						if (firstReferenceTarget instanceof HTMLMasonFileImpl)
						{
							HTMLMasonCompletionUtil.fillWithMethods(finalResultSet, (HTMLMasonFileImpl) firstReferenceTarget);
						}
					}
					else
					{
						HTMLMasonCompletionUtil.fillWithComponentSlugs(finalResultSet);
						HTMLMasonCompletionUtil.fillWithSubcomponents(finalResultSet, (HTMLMasonFileImpl) file);
						if (!StringUtil.startsWith(fullPrefix, "/"))
						{
							HTMLMasonCompletionUtil.fillWithRelativeSubcomponents(finalResultSet, (HTMLMasonFileImpl) file);
						}
						else
						{
							HTMLMasonCompletionUtil.fillWithAbsoluteSubcomponents(finalResultSet, project);
						}
					}

					result.stopHere();
					break;
				}
			}
		}
	}
}
