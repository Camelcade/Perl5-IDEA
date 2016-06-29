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

package com.perl5.lang.mojolicious.idea.liveTemplates;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.impl.TemplateOptionalProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.mojolicious.MojoliciousLanguage;
import com.perl5.lang.mojolicious.psi.impl.MojoliciousFileImpl;
import org.jetbrains.annotations.Nls;

/**
 * Created by hurricup on 29.06.2016.
 */
public class MojoliciousLiveTemplateProcessor implements TemplateOptionalProcessor
{
	@Override
	public void processText(Project project, Template template, Document document, RangeMarker templateRange, Editor editor)
	{
		PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
		if (!(file instanceof MojoliciousFileImpl))
		{
			return;
		}

		int startOffset = templateRange.getStartOffset();
		int startLine = document.getLineNumber(startOffset);
		int endLine = document.getLineNumber(templateRange.getEndOffset());
		if (startLine == endLine)
		{
			return;
		}

		int startLineBeginOffset = document.getLineStartOffset(startLine);

		PsiElement elementAtStart = file.getViewProvider().findElementAt(startLineBeginOffset, MojoliciousLanguage.INSTANCE);

		while (elementAtStart instanceof PsiWhiteSpace)
		{
			elementAtStart = elementAtStart.getNextSibling();
		}

		if (PsiUtilCore.getElementType(elementAtStart) != MojoliciousElementTypes.MOJO_LINE_OPENER)
		{
			return;
		}

		assert elementAtStart != null;
		CharSequence charsSequence = document.getCharsSequence();
		for (int currentLine = endLine; currentLine > startLine; currentLine--)
		{
			int lineStartOffset = document.getLineStartOffset(currentLine);
			if (charsSequence.charAt(lineStartOffset) != '%')
			{
				document.insertString(lineStartOffset, "% ");
			}
		}
	}

	@Nls
	@Override
	public String getOptionName()
	{
		return "Please report a bug";
	}

	@Override
	public boolean isEnabled(Template template)
	{
		return true;
	}

	@Override
	public void setEnabled(Template template, boolean value)
	{

	}

	@Override
	public boolean isVisible(Template template)
	{
		return false;
	}
}
