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
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.PerlCompletionProviderUtils;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.PsiPerlUseStatement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 31.05.2015.
 */
public class PerlUseParametersCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public static final InsertHandler USE_OPTION_INSERT_HANDLER = new UseOptionInsertHandler();

	@Override
	protected void addCompletions(@NotNull final CompletionParameters parameters, ProcessingContext context, @NotNull final CompletionResultSet resultSet)
	{
		ApplicationManager.getApplication().runReadAction(new Runnable()
		{
			@Override
			public void run()
			{
				PsiFile file = parameters.getOriginalFile();
				PsiElement stringContentElement = parameters.getPosition();

				PsiPerlUseStatement useStatement = PsiTreeUtil.getParentOfType(stringContentElement, PsiPerlUseStatement.class, true, PsiPerlStatement.class);

				if (useStatement != null && useStatement.isParentPragma())
				{
					for (String packageName : PerlPackageUtil.getPackageFilesForPsiElement(parameters.getPosition()))
						resultSet.addElement(PerlCompletionProviderUtils.getPackageLookupElement(packageName));

					if (useStatement.isParentPragma())
						resultSet.addElement(LookupElementBuilder.create("-norequire").withIcon(PerlIcons.PERL_OPTION).withInsertHandler(USE_OPTION_INSERT_HANDLER));
				}
			}
		});
	}

	/**
	 * Parent pragma additional insert
	 */
	static class UseOptionInsertHandler implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, LookupElement item)
		{
			final Editor editor = context.getEditor();
			EditorModificationUtil.insertStringAtCaret(editor, " ");

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
