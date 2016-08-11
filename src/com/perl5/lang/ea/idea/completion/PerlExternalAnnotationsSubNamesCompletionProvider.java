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

package com.perl5.lang.ea.idea.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.ea.psi.PerlExternalAnnotationNamespace;
import com.perl5.lang.perl.idea.completion.util.PerlSubCompletionUtil;
import com.perl5.lang.perl.psi.PerlSubBase;
import com.perl5.lang.perl.psi.PerlSubDeclaration;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import com.perl5.lang.perl.util.PerlAnnotationsUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 11.08.2016.
 */
public class PerlExternalAnnotationsSubNamesCompletionProvider extends CompletionProvider<CompletionParameters>
{
	@Override
	protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
	{
		PsiElement position = parameters.getPosition();
		PerlExternalAnnotationNamespace containingNamespace = PsiTreeUtil.getParentOfType(position, PerlExternalAnnotationNamespace.class);
		if (containingNamespace == null)
		{
			return;
		}

		String packageName = containingNamespace.getPackageName();
		if (StringUtil.isEmpty(packageName))
		{
			return;
		}

		Project project = position.getProject();
		int elementLevel = PerlAnnotationsUtil.getPsiElementLevel(parameters.getOriginalFile());

		// defined subs
		for (PerlSubDefinitionBase subDefinition : PerlSubUtil.getSubDefinitions(project, "*" + packageName))
		{
			if (isNotAnnotatedOnCurrentLevel(subDefinition, elementLevel))
			{
				result.addElement(PerlSubCompletionUtil.getSubDefinitionLookupElementWithoutArguments(subDefinition));
			}
		}

		// declared subs
		for (PerlSubDeclaration subDeclaration : PerlSubUtil.getSubDeclarations(project, "*" + packageName))
		{
			if (isNotAnnotatedOnCurrentLevel(subDeclaration, elementLevel))
			{
				result.addElement(PerlSubCompletionUtil.getSubDeclarationLookupElementWithoutInsertHandler(subDeclaration));
			}
		}
	}

	private boolean isNotAnnotatedOnCurrentLevel(PerlSubBase subBase, int currentLevel)
	{
		return subBase.getStubbedOrLocalAnnotations() == null &&
				PerlAnnotationsUtil.getSubExternalAnnotations(subBase.getProject(), subBase.getCanonicalName(), currentLevel) == null;
	}
}
