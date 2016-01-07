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

package com.perl5.lang.mojolicious.idea.editor;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.mojolicious.MojoliciousFileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 08.01.2016.
 */
public class MojoliciousTypedHandler extends TypedHandlerDelegate implements MojoliciousElementTypes
{
	@Override
	public Result charTyped(char c, final Project project, @NotNull final Editor editor, @NotNull PsiFile file)
	{
		if (file.getViewProvider() instanceof MojoliciousFileViewProvider)
		{
			if (c == ' ')
			{
				PsiElement element = file.findElementAt(editor.getCaretModel().getOffset() - 2);
				if (element != null)
				{
					IElementType elementType = element.getNode().getElementType();
					if (elementType == MOJO_BLOCK_OPENER || elementType == MOJO_BLOCK_EXPR_OPENER || elementType == MOJO_BLOCK_EXPR_ESCAPED_OPENER)
					{
						EditorModificationUtil.insertStringAtCaret(editor, " " + KEYWORD_MOJO_BLOCK_CLOSER, false, false);
					}
				}
			}
		}
		return super.charTyped(c, project, editor, file);
	}
}
