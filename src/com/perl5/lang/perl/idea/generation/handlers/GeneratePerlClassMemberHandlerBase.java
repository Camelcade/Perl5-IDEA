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
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.PsiPerlNamespaceContent;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 11.10.2015.
 */
public abstract class GeneratePerlClassMemberHandlerBase implements CodeInsightActionHandler, PerlElementTypes
{
	@Override
	public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file)
	{
		PsiElement currentElement = getCurrentElement(editor, file);
		if (currentElement == null)
		{
			return;
		}

		while (true)
		{
			if (currentElement instanceof PsiFile || currentElement == null || currentElement.getParent() instanceof PsiFile)
				return;

			PsiElement parent = currentElement.getParent();
			if (parent instanceof PsiPerlNamespaceContent || parent instanceof PsiPerlBlock && parent.getParent() instanceof PerlNamespaceDefinition)
			{
				while (currentElement != null && (currentElement instanceof PsiComment || currentElement instanceof PerlHeredocElementImpl))
				{
					currentElement = currentElement.getNextSibling();
				}

				if (currentElement != null)
				{
					generateAfterElement(currentElement, editor, file);
				}
				return;
			}
			else
			{
				currentElement = currentElement.getParent();
			}
		}

	}

	protected abstract void generateAfterElement(PsiElement anchor, Editor editor, PsiFile file);

	protected PsiElement getCurrentElement(Editor editor, PsiFile file)
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

		return currentElement;
	}

	@Override
	public boolean startInWriteAction()
	{
		return true;
	}

}
