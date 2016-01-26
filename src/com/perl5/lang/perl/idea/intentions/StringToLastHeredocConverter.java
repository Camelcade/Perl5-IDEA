/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.intentions;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 26.01.2016.
 */
public class StringToLastHeredocConverter extends PsiElementBaseIntentionAction implements IntentionAction
{
	protected static String HEREDOC_MARKER = "HEREDOC";

	@Override
	public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException
	{
		PsiElement stringElement = element.getParent();
		assert stringElement instanceof PerlString;
		char quoteSymbol = '"';
		if (stringElement instanceof PsiPerlStringSq)
			quoteSymbol = '\'';
		else if (stringElement instanceof PsiPerlStringXq)
			quoteSymbol = '`';

		List<PsiElement> heredocElements = PerlElementFactory.createHereDocElements(project, quoteSymbol, HEREDOC_MARKER,
				((PerlString) stringElement).getStringContent()
		);

		PsiFile currentFile = stringElement.getContainingFile();
		int newLineIndex = currentFile.getText().indexOf("\n", stringElement.getTextOffset() + stringElement.getTextLength());
		PsiElement anchor = null;

		if (newLineIndex > 1)
		{
			anchor = currentFile.findElementAt(newLineIndex);
			// fixme we should check here if \n in unbreakable entity - regex, qw, string, something else...
			if (anchor != null && (anchor.getParent() instanceof PerlString || anchor.getParent() instanceof PsiPerlStringList))
				anchor = null;
		}

		stringElement = stringElement.replace(heredocElements.get(0)); // replace string with heredoc opener

		if (anchor == null)
			anchor = stringElement;

		PsiElement container = anchor.getParent();

		if (container != null)
		{
			anchor = container.addAfter(heredocElements.get(1), anchor);
			anchor = container.addAfter(heredocElements.get(2), anchor);
			anchor = container.addAfter(heredocElements.get(3), anchor);
			anchor = container.addAfter(heredocElements.get(3), anchor);
		}

	}

	@Override
	public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element)
	{
		if (!element.isWritable())
			return false;
		PsiElement parent = element.getParent();
		PsiElement grandParent = parent.getParent();
		return !(grandParent instanceof PerlHeredocOpener) && (parent instanceof PsiPerlStringDq || parent instanceof PsiPerlStringSq || parent instanceof PsiPerlStringXq);
	}

	@Nls
	@NotNull
	@Override
	public String getFamilyName()
	{
		return getText();
	}

	@NotNull
	@Override
	public String getText()
	{
		return "Convert to heredoc: " + HEREDOC_MARKER;
	}

}
