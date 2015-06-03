package com.perl5.lang.perl.idea.completion.inserthandlers;

import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlVariableDeclaration;
import com.perl5.lang.perl.psi.PsiPerlAnnotation;

/**
 * Created by hurricup on 03.06.2015.
 * Handles afterwork for packages autocompletion
 */
public class PackageInsertHandler implements InsertHandler<LookupElement>
{
	public static final InsertHandler<LookupElement> INSTANCE = new PackageInsertHandler();

	@Override
	public void handleInsert(final InsertionContext context, LookupElement item)
	{
		PsiElement parent = null;

		PsiElement element = item.getPsiElement();
		if (element != null)
			parent = element.getParent();

		if (parent != null)
		{
			final Editor editor = context.getEditor();

			if (parent instanceof PerlVariableDeclaration)
			{
				EditorModificationUtil.insertStringAtCaret(editor, " ()");
				editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
			} else if (parent instanceof PsiPerlAnnotation)
				EditorModificationUtil.insertStringAtCaret(editor, "\n");
			else if( "parent".equals(item.getLookupString()) || "base".equals(item.getLookupString()))
			{
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
			else
				EditorModificationUtil.insertStringAtCaret(editor, ";\n");
		}
	}
}
