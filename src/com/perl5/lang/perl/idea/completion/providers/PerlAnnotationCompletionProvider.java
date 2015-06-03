package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.lexer.PerlAnnotations;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 03.06.2015.
 *
 */
public class PerlAnnotationCompletionProvider extends CompletionProvider<CompletionParameters>
{
	@Override
	protected void addCompletions(@NotNull final CompletionParameters parameters, ProcessingContext context, @NotNull final CompletionResultSet resultSet)
	{
		for (String annotation : PerlAnnotations.TOKEN_TYPES.keySet())
		{
			// todo add an icon here
			LookupElementBuilder newElement = LookupElementBuilder.create(annotation);

			if( "returns".equals(annotation))
				newElement = newElement.withInsertHandler(RETURNS_INSERT_HANDLER);

			resultSet.addElement(newElement);
		}
	}


	public static final InsertHandler<LookupElement> RETURNS_INSERT_HANDLER = new ReturnsInsertHandler();
	/**
	 * Auto-open autocompletion with package after space
	 */
	static class ReturnsInsertHandler implements InsertHandler<LookupElement>
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
