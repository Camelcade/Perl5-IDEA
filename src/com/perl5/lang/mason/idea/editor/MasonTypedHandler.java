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

package com.perl5.lang.mason.idea.editor;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlTokenType;
import com.perl5.lang.mason.MasonFileViewProvider;
import com.perl5.lang.mason.elementType.MasonElementTypes;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 07.01.2016.
 */
public class MasonTypedHandler extends TypedHandlerDelegate implements MasonElementTypes, XmlTokenType
{
	private static final THashMap<String, String> SIMPLE_COMPLETION_MAP = new THashMap<String, String>();

	static
	{
		SIMPLE_COMPLETION_MAP.put("<%doc", "</%doc>");
		SIMPLE_COMPLETION_MAP.put("<%class", "</%class>");
		SIMPLE_COMPLETION_MAP.put("<%init", "</%init>");
		SIMPLE_COMPLETION_MAP.put("<%perl", "</%perl>");
		SIMPLE_COMPLETION_MAP.put("<%text", "</%text>");
	}

	@Override
	public Result charTyped(char c, final Project project, @NotNull final Editor editor, @NotNull PsiFile file)
	{
		if (file.getViewProvider() instanceof MasonFileViewProvider)
		{
			if (c == '>')
			{
				PsiElement element = file.findElementAt(editor.getCaretModel().getOffset() - 2);
				if (element != null && element.getNode().getElementType() == XML_DATA_CHARACTERS)
				{
					String elementText = element.getText();
					String closeTag;
					if ((closeTag = SIMPLE_COMPLETION_MAP.get(elementText)) != null)
					{
						EditorModificationUtil.insertStringAtCaret(editor, closeTag, false, false);
					}
					else if (elementText.equals("<%flags"))
					{
						EditorModificationUtil.insertStringAtCaret(editor, " extends => '' </%flags>", false, true, 13);
					}
				}
			}
			else if (c == ' ')
			{
				PsiElement element = file.findElementAt(editor.getCaretModel().getOffset() - 2);
				if (element != null)
				{
					IElementType elementType = element.getNode().getElementType();
					if (elementType == MASON_BLOCK_OPENER)
					{
						EditorModificationUtil.insertStringAtCaret(editor, " %>", false, false);
					}
					else if (elementType == MASON_CALL_OPENER)
					{
						EditorModificationUtil.insertStringAtCaret(editor, " &>", false, false);
					}
					else if (elementType == MASON_METHOD_OPENER)
					{
						EditorModificationUtil.insertStringAtCaret(editor, ">\n</%method>", false, false);
					}
					else if (elementType == MASON_FILTER_OPENER)
					{
						EditorModificationUtil.insertStringAtCaret(editor, ">\n</%filter>", false, false);
					}
					else if (elementType == MASON_OVERRIDE_OPENER)
					{
						EditorModificationUtil.insertStringAtCaret(editor, ">\n</%override>", false, false);
					}
				}

			}

		}
		return super.charTyped(c, project, editor, file);
	}
}
