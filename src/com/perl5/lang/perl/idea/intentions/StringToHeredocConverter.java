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

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.07.2015.
 */
public class StringToHeredocConverter extends StringToLastHeredocConverter
{

	@Override
	public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException
	{
		String markerText = Messages.showInputDialog(project, "What here-doc marker should we use?", "Input a Heredoc Marker", Messages.getQuestionIcon(), HEREDOC_MARKER, null);
		if (markerText != null)
		{
			if (markerText.isEmpty())
			{
				Messages.showErrorDialog(project, "Empty heredoc markers are not supported", "Marker Error");
			}
			else    // converting
			{
				HEREDOC_MARKER = markerText;
				super.invoke(project, editor, element);
			}
		}
	}

	@NotNull
	@Override
	public String getText()
	{
		return "Convert to heredoc...";
	}
}
