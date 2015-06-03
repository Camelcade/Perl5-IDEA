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
