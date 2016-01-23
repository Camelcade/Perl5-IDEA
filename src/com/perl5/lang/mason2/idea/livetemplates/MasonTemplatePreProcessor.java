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

package com.perl5.lang.mason2.idea.livetemplates;

import com.intellij.codeInsight.template.impl.TemplatePreprocessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.perl5.lang.mason2.psi.impl.MasonTemplatingFileImpl;

/**
 * Created by hurricup on 10.01.2016.
 */
public class MasonTemplatePreProcessor implements TemplatePreprocessor
{
	@Override
	public void preprocessTemplate(Editor editor, PsiFile file, int caretOffset, String textToInsert, String templateText)
	{
		if (file instanceof MasonTemplatingFileImpl && textToInsert.startsWith("<%") && caretOffset > 0)
		{
			Document document = editor.getDocument();
			CharSequence text = document.getCharsSequence();

			if (text.charAt(caretOffset - 1) == '<')
			{
				document.deleteString(caretOffset - 1, caretOffset);
				editor.getCaretModel().moveToOffset(caretOffset - 1);
			}
		}
	}

}
