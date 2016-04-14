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

package com.perl5.lang.pod.idea.editor;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.PodFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 14.04.2016.
 */
public class PodTypedHandler extends TypedHandlerDelegate implements PodElementTypes
{
	private static final String POD_COMMANDS = "IBCLEFSXZ";

	public static int getRightParentOffset(HighlighterIterator iterator)
	{
		int lastRbraceOffset = -1;
		int bracesCounter = 0;
		for (; !iterator.atEnd(); iterator.advance())
		{
			final IElementType tokenType = iterator.getTokenType();

			if (tokenType == POD_ANGLE_RIGHT)
			{
				if (bracesCounter > 0)
				{
					bracesCounter--;
				}
				else
				{
					return iterator.getStart();
				}
			}
			else if (tokenType == POD_ANGLE_LEFT)
			{
				bracesCounter++;
			}
		}

		return lastRbraceOffset;
	}

	@Override
	public Result beforeCharTyped(char c, Project project, Editor editor, PsiFile file, FileType fileType)
	{
		if (file instanceof PodFile)
		{
			if (c == '<')
			{
				CaretModel caretModel = editor.getCaretModel();
				int offset = caretModel.getOffset() - 1;
				PsiElement element = file.findElementAt(offset);
				IElementType elementType = element == null ? null : element.getNode().getElementType();
				String text = element == null ? null : element.getText();

				if (elementType == POD_IDENTIFIER && text != null)
				{

					if (text.length() == 1 && StringUtil.containsChar(POD_COMMANDS, text.charAt(0)))
					{
						EditorModificationUtil.insertStringAtCaret(editor, ">", false, false, 0);
					}
				}
				else if (elementType == POD_ANGLE_LEFT)
				{
					CharSequence fileText = file.getText();
					EditorHighlighter highlighter = ((EditorEx) editor).getHighlighter();
					HighlighterIterator iterator = highlighter.createIterator(offset + 1);
					int rightOffset = getRightParentOffset(iterator);

					boolean hasSpaceAtStart = Character.isWhitespace(fileText.charAt(offset + 1));

					if (rightOffset > 0)
					{
						int oldOffset = caretModel.getOffset();
						caretModel.moveToOffset(rightOffset);
						EditorModificationUtil.insertStringAtCaret(editor, ">", false, false, 0);
						if (!Character.isWhitespace(fileText.charAt(rightOffset - 1)))
						{
							EditorModificationUtil.insertStringAtCaret(editor, " ", false, false, 0);
						}
						caretModel.moveToOffset(oldOffset);
					}
					if (!hasSpaceAtStart)
					{
						EditorModificationUtil.insertStringAtCaret(editor, "< ", false, true, 1);
					}
					else
					{
						EditorModificationUtil.insertStringAtCaret(editor, "<", false, true, 1);
					}
					return Result.STOP;
				}
			}
		}
		return super.beforeCharTyped(c, project, editor, file, fileType);
	}

	@Override
	public Result charTyped(char c, Project project, @NotNull Editor editor, @NotNull PsiFile file)
	{
		return super.charTyped(c, project, editor, file);
	}
}
