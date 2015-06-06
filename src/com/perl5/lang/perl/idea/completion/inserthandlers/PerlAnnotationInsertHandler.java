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

package com.perl5.lang.perl.idea.completion.inserthandlers;

/**
 * Created by hurricup on 03.06.2015.
 */

import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;

/**
 * Auto-open autocompletion with package after space
 */
public class PerlAnnotationInsertHandler implements InsertHandler<LookupElement>
{
	public static final InsertHandler<LookupElement> INSTANCE = new PerlAnnotationInsertHandler();

	@Override
	public void handleInsert(final InsertionContext context, LookupElement item)
	{



		final Editor editor = context.getEditor();
		if( "returns".equals(item.getLookupString()))
		{
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
		else
			EditorModificationUtil.insertStringAtCaret(editor, "\n");
	}
}
