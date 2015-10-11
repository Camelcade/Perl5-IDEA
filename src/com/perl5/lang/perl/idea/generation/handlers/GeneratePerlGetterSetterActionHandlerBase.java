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

package com.perl5.lang.perl.idea.generation.handlers;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PerlSubDeclaration;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;

import javax.print.Doc;

/**
 * Created by hurricup on 11.10.2015.
 */
public abstract class GeneratePerlGetterSetterActionHandlerBase implements CodeInsightActionHandler
{
	@Override
	public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file)
	{
		Caret currentCaret = editor.getCaretModel().getCurrentCaret();

		// look for current element
		int currentOffset = currentCaret.getOffset();
		PsiElement currentElement = file.findElementAt(currentOffset);

		if (currentElement == null)
		{
			currentOffset--;
			currentElement = file.findElementAt(currentOffset);
		}

		if (currentElement == null)
		{
			return;
		}

		// look for an anchor
		PsiElement anchor = PsiTreeUtil.getParentOfType(currentElement, PerlSubDefinition.class);
		if (anchor == null)
		{
			anchor = PsiTreeUtil.getParentOfType(currentElement, PsiPerlStatement.class);
		}
		if (anchor == null)
		{
			anchor = PsiTreeUtil.getParentOfType(currentElement, PerlHeredocElementImpl.class);
			if (anchor != null && anchor.getNextSibling() != null)
			{
				anchor = anchor.getNextSibling();
			}
		}
		if (anchor == null)
		{
			anchor = currentElement;
		}

		int targetOffset = anchor.getNode().getStartOffset() + anchor.getNode().getTextLength();

		String name = Messages.showInputDialog(project, getPromtText(), getPromtTitle(), Messages.getQuestionIcon(), "somegetter", null);

		if (name != null)
		{
			Document document = editor.getDocument();

			doGenerate(document, name, targetOffset);

			PsiDocumentManager.getInstance(project).commitDocument(document);
		}
	}

	protected abstract String getPromtText();

	protected abstract String getPromtTitle();

	public abstract void doGenerate(Document document, String name, int offset);

	@Override
	public boolean startInWriteAction()
	{
		return true;
	}


}
