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

package com.perl5.lang.perl.idea.intentions;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PsiPerlStringDq;
import com.perl5.lang.perl.psi.PsiPerlStringSq;
import com.perl5.lang.perl.psi.PsiPerlStringXq;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 19.07.2015.
 */
public class StringToHeredocConverter extends PsiElementBaseIntentionAction implements IntentionAction
{
	private static String HEREDOC_MARKER = "HEREDOC";

	@Override
	public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException
	{
		String markerText = Messages.showInputDialog(project, "What here-doc marker should we use?", "Input a Heredoc Marker", Messages.getQuestionIcon(), HEREDOC_MARKER, null);
		if( markerText != null )
			if (markerText.isEmpty())
				Messages.showErrorDialog(project, "Empty heredoc markers are not supported", "Marker Error");
			else	// converting
			{
				HEREDOC_MARKER = markerText;
				PsiElement parentElement = element.getParent();
				char quoteSymbol = '"';
				if( parentElement instanceof PsiPerlStringSq)
					quoteSymbol = '\'';
				else if( parentElement instanceof PsiPerlStringXq)
					quoteSymbol = '`';

				List<PsiElement> heredocElements = PerlElementFactory.createHereDocElements(project, quoteSymbol, markerText, element.getText());

				PsiFile currentFile = element.getContainingFile();

				PsiElement newLineItem = null;
				int newLineIndex = currentFile.getText().indexOf("\n", element.getTextOffset());
				if( newLineIndex > 1 )
					newLineItem = currentFile.findElementAt(newLineIndex);

				if( newLineItem == null ) // last statement without newline
				{
					currentFile.addAfter(heredocElements.get(3), currentFile.getLastChild());
					currentFile.addAfter(heredocElements.get(1), currentFile.getLastChild());
					currentFile.addAfter(heredocElements.get(2), currentFile.getLastChild());
					currentFile.addAfter(heredocElements.get(3), currentFile.getLastChild());
				}
				else
				{
					PsiElement container = newLineItem.getParent();
					container.addAfter(heredocElements.get(3),
							container.addAfter(heredocElements.get(2),
									container.addAfter(heredocElements.get(1), newLineItem))
					);
				}
				parentElement.replace(heredocElements.get(0));
			}
	}

	@Override
	public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element)
	{
		if( !element.isWritable())
			return false;
		PsiElement parent = element.getParent();
		return
				element instanceof PerlStringContentElement
						&&
						(parent instanceof PsiPerlStringDq || parent instanceof PsiPerlStringSq || parent instanceof PsiPerlStringXq);
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
		return "Convert to heredoc";
	}
}
