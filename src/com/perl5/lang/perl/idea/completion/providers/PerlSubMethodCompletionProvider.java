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
import com.perl5.lang.perl.idea.completion.inserthandlers.SubSelectionHandler;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlDefaultMro;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.07.2015.
 */
public class PerlSubMethodCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public static final SubSelectionHandler SUB_SELECTION_HANDLER = new SubSelectionHandler();

	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull CompletionResultSet resultSet)
	{

		PsiElement method = parameters.getPosition().getParent();
		assert method instanceof PsiPerlMethod;

		String packageName = ((PsiPerlMethod) method).getPackageName();

		if (packageName == null)
			return;

		PerlNamespaceElement namespaceElement = ((PsiPerlMethod) method).getNamespaceElement();
		boolean isSuper = namespaceElement != null && "SUPER".equals(namespaceElement.getCanonicalName());

//				System.out.println("Autocomplete for " + packageName);

		for (PsiElement element : PerlDefaultMro.getPackagePossibleMethods(method.getProject(), packageName, isSuper))
		{
			if (element instanceof PerlSubDefinition && ((PerlSubDefinition) element).isMethod())
			{
				String argsString = ((PerlSubDefinition) element).getSubArgumentsListAsString();

				LookupElementBuilder newElement = LookupElementBuilder
						.create(((PerlSubDefinition) element).getSubName())
						.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
						.withStrikeoutness(((PerlSubDefinition) element).getSubAnnotations().isDeprecated());

				if (!argsString.isEmpty())
					newElement = newElement
							.withInsertHandler(SUB_SELECTION_HANDLER)
							.withTailText(argsString);

				resultSet.addElement(newElement);
			} else if (element instanceof PerlSubDeclaration && ((PerlSubDeclaration) element).isMethod())
			{
				LookupElementBuilder newElement = LookupElementBuilder
						.create(((PerlSubDeclaration) element).getSubName())
						.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
						.withStrikeoutness(((PerlSubDeclaration) element).getSubAnnotations().isDeprecated())
						.withInsertHandler(SUB_SELECTION_HANDLER)
						.withTailText("(?)");

				resultSet.addElement(newElement);
			} else if (element instanceof PerlGlobVariable && ((PerlGlobVariable) element).getName() != null)
			{
				LookupElementBuilder newElement = LookupElementBuilder
						.create(((PerlGlobVariable) element).getName())
						.withIcon(PerlIcons.GLOB_GUTTER_ICON)
						.withInsertHandler(SUB_SELECTION_HANDLER)
						.withTailText("(?)");

				resultSet.addElement(newElement);
			} else if (element instanceof PerlString && ((PerlString) element).getName() != null)
			{
				LookupElementBuilder newElement = LookupElementBuilder
						.create(((PerlString) element).getName())
						.withIcon(PerlIcons.CONSTANT_GUTTER_ICON);

				resultSet.addElement(newElement);
			}
		}
	}
}
