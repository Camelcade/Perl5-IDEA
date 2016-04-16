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
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.PodElementFactory;
import com.perl5.lang.pod.parser.psi.PodFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 14.04.2016.
 */
public class PodTypedHandler extends TypedHandlerDelegate implements PodElementTypes
{
	private static final String POD_COMMANDS = "IBCLEFSXZ";
	private static final TokenSet POD_COMMANDS_TOKENSET = TokenSet.create(
			POD_I,
			POD_B,
			POD_C,
			POD_L,
			POD_E,
			POD_F,
			POD_S,
			POD_X,
			POD_Z
	);

	protected static void extendAngles(PsiElement formatterBlock)
	{
		if (formatterBlock == null)
			return;

		PsiElement openAngles = formatterBlock.getFirstChild();
		openAngles = openAngles == null ? null : openAngles.getNextSibling();

		if (openAngles == null)
			return;

		PsiElement closeAngles = formatterBlock.getLastChild();

		if (closeAngles == null)
			return;

		int currentLength = openAngles.getTextRange().getLength();
		if (currentLength == 1) // need to check spacing
		{
			PsiElement openSpace = openAngles.getNextSibling();
			PsiElement closeSpace = openAngles.getPrevSibling();
			PsiElement space = null;

			if (!(openSpace instanceof PsiWhiteSpace))
			{
				space = PodElementFactory.getSpace(formatterBlock.getProject());
				formatterBlock.addAfter(space, openAngles);
			}
			if (!(closeSpace instanceof PsiWhiteSpace))
			{
				if (space == null)
					space = PodElementFactory.getSpace(formatterBlock.getProject());
				formatterBlock.addBefore(space, closeAngles);
			}
		}

		((LeafPsiElement) openAngles).replaceWithText(openAngles.getText() + "<");
		((LeafPsiElement) closeAngles).replaceWithText(closeAngles.getText() + ">");
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
				else if (elementType == POD_ANGLE_LEFT || POD_COMMANDS_TOKENSET.contains(elementType))
				{
					//noinspection ConstantConditions
					extendAngles(element.getParent());
					caretModel.moveToOffset(offset + 2);
					return Result.STOP;
				}
			}
			else if (c == '>')    // '>'
			{
				CaretModel caretModel = editor.getCaretModel();
				int offset = caretModel.getOffset();
				PsiElement element = file.findElementAt(offset);
				IElementType elementType = element == null ? null : element.getNode().getElementType();

				if (elementType != POD_ANGLE_RIGHT)
				{
					offset--;
					element = file.findElementAt(offset);
					elementType = element == null ? null : element.getNode().getElementType();
				}

				if (elementType == POD_ANGLE_RIGHT)
				{
					//noinspection ConstantConditions
					extendAngles(element.getParent());
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
