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
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.psi.PsiPerlSubDefinition;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 01.06.2015.
 */
public class PerlSubCompletionProvider extends CompletionProvider<CompletionParameters>
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

				String packagePrefix = packageName + "::";

				Project project = parameters.getPosition().getProject();

				// todo we should show declared and not defined subs too for XS extensions
				Collection<String> definedSubs = PerlSubUtil.getDefinedSubsNames(project);
				definedSubs.addAll(PerlGlobUtil.getDefinedGlobsNames(project));

				// todo take SUPER into account, search in superclasses
				for (String canonicalSubName : definedSubs)
				{
					if (canonicalSubName.startsWith(packagePrefix))
					{
						String subName = canonicalSubName.substring(packagePrefix.length());

						if (!subName.contains("::"))
						{
							Collection<PsiPerlSubDefinition> subDefinitions = PerlSubUtil.findSubDefinitions(project, canonicalSubName);

							for (PsiPerlSubDefinition subDefinition : subDefinitions)
							{
								// todo set method icon if isMethod is true
								// todo omit first argument is isMethod is true
								// todo think about decorations for other annotations
								Collection<PerlSubArgument> subArguments = subDefinition.getSubArgumentsList();
								PerlSubAnnotations subAnnotations = subDefinition.getSubAnnotations();

								int argumentsNumber = subArguments.size();

								List<String> argumentsList = new ArrayList<String>();
								for (PerlSubArgument argument : subArguments)
								{
									// todo we can mark optional subArguments after prototypes implementation
									argumentsList.add(argument.toStringShort());

									int compiledListSize = argumentsList.size();
									if (compiledListSize > 4 && argumentsNumber > compiledListSize)
									{
										argumentsList.add("...");
										break;
									}
								}

								String argsString = "(" + StringUtils.join(argumentsList, ", ") + ")";

								resultSet.addElement(LookupElementBuilder
												.create(subDefinition, subName)
												.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
												.withPresentableText(subName + argsString)
												.withInsertHandler(SUB_SELECTION_HANDLER)
												.withStrikeoutness(subAnnotations.isDeprecated())
								);
							}
						}
					}
				}
			}
		});
	}


	static class SubSelectionHandler implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, LookupElement item)
		{
			final Editor editor = context.getEditor();

			PsiElement subDefitnition = item.getPsiElement();
			EditorModificationUtil.insertStringAtCaret(editor, "()");

			// todo why autocompletion is not auto-opening after -> or :: ?
			// todo if prefix is :: and target function is a method (got self/this/proto) replace :: with ->
			// todo here we could check a method prototype and position caret accordingly
			// todo we need hint with prototype here, but prototypes handling NYI
			if (!(subDefitnition instanceof PerlSubDefinition && ((PerlSubDefinition) subDefitnition).getSubArgumentsList().size() == 0))
				editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
		}
	}

}
