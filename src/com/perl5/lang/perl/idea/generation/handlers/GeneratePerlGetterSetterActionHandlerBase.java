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

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 11.10.2015.
 */
public abstract class GeneratePerlGetterSetterActionHandlerBase extends GeneratePackageMemberHandlerBase
{
	@Override
	protected void generateAfterElement(PsiElement anchor, Editor editor, PsiFile file)
	{
		String name = Messages.showInputDialog(anchor.getProject(), getPromtText(), getPromtTitle(), Messages.getQuestionIcon(), "", null);

		if (!StringUtil.isEmpty(name))
		{

			StringBuilder code = new StringBuilder();

			for (String nameChunk : name.split("[ ,]+"))
			{
				if (!nameChunk.isEmpty() && PerlLexer.IDENTIFIER_PATTERN.matcher(nameChunk).matches())
				{
					code.append(getCode(nameChunk));
				}
			}

			insertCodeAfterElement(anchor, code.toString());
		}

	}

	protected abstract String getPromtText();

	protected abstract String getPromtTitle();

	@NotNull
	protected abstract String getCode(String name);
}
