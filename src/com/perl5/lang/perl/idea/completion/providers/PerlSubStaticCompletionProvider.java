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

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlSubDeclaration;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.PsiPerlMethod;
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

	public void addCompletions(@NotNull final CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull final CompletionResultSet resultSet)
	{
		ApplicationManager.getApplication().runReadAction(new Runnable()
		{
			@Override
			public void run()
			{
				PsiElement method = parameters.getPosition().getParent();
				assert method instanceof PsiPerlMethod;

				String packageName = ((PsiPerlMethod) method).getPackageName();
				assert packageName != null;

				String subPrefix = resultSet.getPrefixMatcher().getPrefix();

//				System.err.println("Invoked with package: " + packageName + " and prefix " + subPrefix);

				Project project = parameters.getPosition().getProject();

				// defined subs
				for (PerlSubDefinition subDefinition : PerlSubUtil.findSubDefinitions(project, "*" + packageName))
				{
					if( !subDefinition.isMethod() )
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
					for (PerlSubDefinition subDefinition : PerlSubUtil.findSubDefinitions(project, "*" + packageName + "::" + subPrefix))
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
				for (PerlSubDeclaration subDeclaration : PerlSubUtil.findSubDeclarations(project, "*" + packageName))
				{
					if( !subDeclaration.isMethod())
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
					for (PerlSubDeclaration subDeclaration : PerlSubUtil.findSubDeclarations(project, "*" + packageName + "::" + subPrefix))
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
				for (PerlGlobVariable globVariable : PerlGlobUtil.findGlobsDefinitions(project, "*" + packageName))
					if (globVariable.getName() != null)
						resultSet.addElement(LookupElementBuilder
								.create(globVariable.getName())
								.withIcon(PerlIcons.GLOB_GUTTER_ICON)
								.withInsertHandler(SUB_SELECTION_HANDLER)
								.withTailText("(?)"));


				// Globs with prefix
				if (!subPrefix.isEmpty())
					for (PerlGlobVariable globVariable : PerlGlobUtil.findGlobsDefinitions(project, "*" + packageName + "::" + subPrefix))
						if (globVariable.getName() != null)
							resultSet.addElement(LookupElementBuilder
									.create(subPrefix + "::" + globVariable.getName())
									.withIcon(PerlIcons.GLOB_GUTTER_ICON)
									.withPresentableText(globVariable.getName())
									.withInsertHandler(SUB_SELECTION_HANDLER)
									.withTailText("(?)"));
			}
		});
	}

	static class SubSelectionHandler implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, LookupElement item)
		{
			assert item instanceof LookupElementBuilder;

			final Editor editor = context.getEditor();

			PsiElement subDefitnition = item.getPsiElement();
			EditorModificationUtil.insertStringAtCaret(editor, "()");

			// todo we need hint with prototype here, but prototypes handling NYI
			if (!(subDefitnition instanceof PerlSubDefinition && ((PerlSubDefinition) subDefitnition).getSubArgumentsList().size() == 0))
				editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
		}
	}

}
