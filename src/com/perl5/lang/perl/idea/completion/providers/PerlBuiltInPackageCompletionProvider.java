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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.PerlInsertHandlers;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlStatement;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 31.05.2015.
 *
 */
public class PerlBuiltInPackageCompletionProvider  extends CompletionProvider<CompletionParameters>
{
	public static final InsertHandler<LookupElement> PARENT_PRAGMA_INSERT_HANDLER = new ParentPragmaInsertHandler();

	@Override
	protected void addCompletions(@NotNull final CompletionParameters parameters, final ProcessingContext context, @NotNull final CompletionResultSet resultSet)
	{
		ApplicationManager.getApplication().runReadAction(new Runnable()
		{
			@Override
			public void run()
			{
				// built in packages
				PerlUseStatement useStatement = PsiTreeUtil.getParentOfType(parameters.getOriginalPosition(), PerlUseStatement.class, true, PerlStatement.class);

				for (String packageName : PerlPackageUtil.BUILT_IN_MAP.keySet())
				{
					IElementType packageType = PerlPackageUtil.BUILT_IN_MAP.get(packageName);
					InsertHandler<LookupElement> insertHandler = null;

					LookupElementBuilder newElement = LookupElementBuilder.create(packageName).withIcon(PerlIcons.PM_FILE).withBoldness(true);

					if (packageType == PerlElementTypes.PERL_PACKAGE_DEPRECATED)
						newElement = newElement.withStrikeoutness(true);
					else if (packageType == PerlElementTypes.PERL_PACKAGE_PRAGMA)
					{
						if ( useStatement != null )
						{
							// additional arguments
							if( packageName.equals("parent") || packageName.equals("base"))
								insertHandler = PARENT_PRAGMA_INSERT_HANDLER;
						}
					}

					if (insertHandler == null)
						insertHandler = PerlInsertHandlers.SEMI_NEWLINE_INSERT_HANDLER;

					resultSet.addElement(newElement.withInsertHandler(insertHandler));
				}
			}
		});
	}

	/**
	 * Parent pragma additional insert
	 */
	static class ParentPragmaInsertHandler implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, LookupElement item)
		{
			final Editor editor = context.getEditor();
			EditorModificationUtil.insertStringAtCaret(editor, " qw//;");
			editor.getCaretModel().moveCaretRelatively(-2, 0, false, false, true);

			context.setLaterRunnable(new Runnable()
			{
				@Override
				public void run()
				{
					new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(context.getProject(), editor);
				}
			});
		}
	}

}
