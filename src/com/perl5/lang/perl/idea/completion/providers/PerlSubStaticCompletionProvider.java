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
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.inserthandlers.SubSelectionHandler;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 01.06.2015.
 */
public class PerlSubStaticCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public static final SubSelectionHandler SUB_SELECTION_HANDLER = new SubSelectionHandler();

	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull CompletionResultSet resultSet)
	{
		PsiElement method = parameters.getPosition().getParent();
		assert method instanceof PsiPerlMethod;

		String packageName = ((PsiPerlMethod) method).getPackageName();
		assert packageName != null;

		String subPrefix = resultSet.getPrefixMatcher().getPrefix();

//				System.err.println("Invoked with package: " + packageName + " and prefix " + subPrefix);

		Project project = parameters.getPosition().getProject();

		// defined subs
		for (PerlSubDefinition subDefinition : PerlSubUtil.getSubDefinitions(project, "*" + packageName))
		{
			if (!subDefinition.isMethod())
			{
				String argsString = subDefinition.getSubArgumentsListAsString();

				LookupElementBuilder newElement = LookupElementBuilder
						.create(subDefinition.getSubName())
						.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
						.withStrikeoutness(subDefinition.getSubAnnotations().isDeprecated());

				if (!argsString.isEmpty())
					newElement = newElement
							.withInsertHandler(SUB_SELECTION_HANDLER)
							.withTailText(argsString);

				resultSet.addElement(newElement);
			}
		}


		// defined subs with prefix
		if (!subPrefix.isEmpty())
			for (PerlSubDefinition subDefinition : PerlSubUtil.getSubDefinitions(project, "*" + packageName + "::" + subPrefix))
			{
				String argsString = subDefinition.getSubArgumentsListAsString();

				String delimiter = subDefinition.isMethod() ? "->" : "::";

				LookupElementBuilder newElement = LookupElementBuilder
						.create(subPrefix + delimiter + subDefinition.getSubName())
						.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
						.withStrikeoutness(subDefinition.getSubAnnotations().isDeprecated())
						.withPresentableText(subDefinition.getSubName());

				if (!argsString.isEmpty())
					newElement = newElement
							.withInsertHandler(SUB_SELECTION_HANDLER)
							.withTailText(argsString);

				resultSet.addElement(newElement);

			}

		// declared subs
		for (PerlSubDeclaration subDeclaration : PerlSubUtil.getSubDeclarations(project, "*" + packageName))
		{
			if (!subDeclaration.isMethod())
			{
				PerlSubAnnotations subAnnotations = subDeclaration.getSubAnnotations();

				LookupElementBuilder newElement = LookupElementBuilder
						.create(subDeclaration.getSubName())
						.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
						.withStrikeoutness(subAnnotations.isDeprecated())
						.withInsertHandler(SUB_SELECTION_HANDLER)
						.withTailText("(?)");
				;

				resultSet.addElement(newElement);
			}
		}


		// declared subs with prefix
		if (!subPrefix.isEmpty())
			for (PerlSubDeclaration subDeclaration : PerlSubUtil.getSubDeclarations(project, "*" + packageName + "::" + subPrefix))
			{
				PerlSubAnnotations subAnnotations = subDeclaration.getSubAnnotations();
				String delimiter = subDeclaration.isMethod() ? "->" : "::";

				LookupElementBuilder newElement = LookupElementBuilder
						.create(subPrefix + delimiter + subDeclaration.getSubName())
						.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
						.withStrikeoutness(subAnnotations.isDeprecated())
						.withPresentableText(subDeclaration.getSubName())
						.withInsertHandler(SUB_SELECTION_HANDLER)
						.withTailText("(?)");
				;

				resultSet.addElement(newElement);
			}

		// Globs
		for (PerlGlobVariable globVariable : PerlGlobUtil.getGlobsDefinitions(project, "*" + packageName))
			if (globVariable.getName() != null)
				resultSet.addElement(LookupElementBuilder
						.create(globVariable.getName())
						.withIcon(PerlIcons.GLOB_GUTTER_ICON)
						.withInsertHandler(SUB_SELECTION_HANDLER)
						.withTailText("(?)"));


		// Globs with prefix
		if (!subPrefix.isEmpty())
			for (PerlGlobVariable globVariable : PerlGlobUtil.getGlobsDefinitions(project, "*" + packageName + "::" + subPrefix))
				if (globVariable.getName() != null)
					resultSet.addElement(LookupElementBuilder
							.create(subPrefix + "::" + globVariable.getName())
							.withIcon(PerlIcons.GLOB_GUTTER_ICON)
							.withPresentableText(globVariable.getName())
							.withInsertHandler(SUB_SELECTION_HANDLER)
							.withTailText("(?)"));

		// Constants
		for (PerlConstant stringConstant : PerlSubUtil.getConstantsDefinitions(project, "*" + packageName))
			if (stringConstant.getName() != null)
				resultSet.addElement(LookupElementBuilder
						.create(stringConstant.getName())
						.withIcon(PerlIcons.CONSTANT_GUTTER_ICON));


		// Constatns with prefix
		if (!subPrefix.isEmpty())
			for (PerlConstant stringConstant : PerlSubUtil.getConstantsDefinitions(project, "*" + packageName + "::" + subPrefix))
				if (stringConstant.getName() != null)
					resultSet.addElement(LookupElementBuilder
							.create(subPrefix + "::" + stringConstant.getName())
							.withIcon(PerlIcons.CONSTANT_GUTTER_ICON)
							.withPresentableText(stringConstant.getName()));

	}
}
