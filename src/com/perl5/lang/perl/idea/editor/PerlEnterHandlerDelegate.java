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

package com.perl5.lang.perl.idea.editor;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlHeredocTerminatorElementImpl;
import com.perl5.lang.perl.psi.references.PerlHeredocReference;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Created by hurricup on 06.09.2015.
 */
public class PerlEnterHandlerDelegate implements EnterHandlerDelegate
{
	@Override
	public Result preprocessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull Ref<Integer> caretOffset, @NotNull Ref<Integer> caretAdvance, @NotNull DataContext dataContext, EditorActionHandler originalHandler)
	{
		// here-doc terminator auto-inserting
		PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
		if (element != null && element.getParent() instanceof PerlHeredocElementImpl && element.getPrevSibling() == null)
		{
			PsiElement parent = element.getParent();
			PsiElement nextSibling = parent.getNextSibling();

			boolean needAdd = nextSibling == null || !(nextSibling instanceof PerlHeredocTerminatorElementImpl);

			if (!needAdd)
			{
				// checking for overlapping heredocs
				Pattern openerPattern = Pattern.compile("<<(\\s*)[\"'`]?" + nextSibling.getText() + "[\"'`]?");
				if (openerPattern.matcher(parent.getText()).find())
					needAdd = true;
			}

			if (needAdd)
			{
				PsiElement opener = PerlHeredocReference.getClosestHeredocOpener(element);
				if (opener != null)
				{
					editor.getDocument().insertString(caretOffset.get(), opener.getText());
					return Result.DefaultSkipIndent;
				}
			}
		}

		return Result.Continue;
	}

	@Override
	public Result postProcessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull DataContext dataContext)
	{
		return Result.Continue;
	}
}
