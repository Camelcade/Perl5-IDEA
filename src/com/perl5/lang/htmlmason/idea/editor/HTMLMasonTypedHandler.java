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

package com.perl5.lang.htmlmason.idea.editor;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlTokenType;
import com.perl5.lang.htmlmason.HTMLMasonElementPatterns;
import com.perl5.lang.htmlmason.HTMLMasonFileViewProvider;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by hurricup on 09.03.2016.
 */
public class HTMLMasonTypedHandler extends TypedHandlerDelegate implements HTMLMasonElementTypes, XmlTokenType, PerlElementTypes, HTMLMasonElementPatterns
{
	@Override
	public Result charTyped(char c, Project project, @NotNull Editor editor, @NotNull PsiFile file)
	{
		if (file.getViewProvider() instanceof HTMLMasonFileViewProvider)
		{
			if (c == '>')
			{
				PsiElement element = file.findElementAt(editor.getCaretModel().getOffset() - 2);
				if (HTML_MASON_TEMPLATE_CONTEXT_PATTERN.accepts(element) || HTML_MASON_TEMPLATE_CONTEXT_PATTERN_BROKEN.accepts(element))
				{
					assert element != null;
					String elementText = element.getText();
					String closeTag;
					if (elementText.equals(KEYWORD_FLAGS))
					{
						EditorModificationUtil.insertStringAtCaret(editor, "\ninherit => ''\n" + KEYWORD_FLAGS_CLOSER, false, true, 13);
					}
					else
					{
						if ((closeTag = getCloseTag(project, "<%" + elementText + ">")) != null)
						{
							EditorModificationUtil.insertStringAtCaret(editor, closeTag, false, false);
						}
					}
				}
			}
			else if (c == ' ')
			{
				PsiElement element = file.findElementAt(editor.getCaretModel().getOffset() - 2);
				if (element != null)
				{
					IElementType elementType = element.getNode().getElementType();
					if (elementType == HTML_MASON_BLOCK_OPENER && !isNextElement(element, HTML_MASON_BLOCK_CLOSER))
					{
						EditorModificationUtil.insertStringAtCaret(editor, " " + KEYWORD_BLOCK_CLOSER, false, false);
					}
					else if (elementType == HTML_MASON_CALL_OPENER && !isNextElement(element, HTML_MASON_CALL_CLOSER))
					{
						EditorModificationUtil.insertStringAtCaret(editor, " " + KEYWORD_CALL_CLOSER, false, false);
					}
					else if (elementType == HTML_MASON_CALL_FILTERING_OPENER && !isNextElement(element, HTML_MASON_CALL_CLOSER))
					{
						EditorModificationUtil.insertStringAtCaret(editor, " " + KEYWORD_CALL_CLOSER + KEYWORD_CALL_CLOSE_TAG_START + KEYWORD_TAG_CLOSER, false, false);
					}
					else if (elementType == HTML_MASON_METHOD_OPENER)
					{
						String closeTag = getCloseTag(project, element.getText());
						if (closeTag != null)
						{
							EditorModificationUtil.insertStringAtCaret(editor, ">\n" + closeTag, false, false);
						}
					}
					else if (elementType == HTML_MASON_DEF_OPENER)
					{
						String closeTag = getCloseTag(project, element.getText());
						if (closeTag != null)
						{
							EditorModificationUtil.insertStringAtCaret(editor, ">\n" + closeTag, false, false);
						}
					}
				}
			}

		}

		return super.charTyped(c, project, editor, file);
	}

	protected String getCloseTag(Project project, String openTag)
	{
		HTMLMasonSettings settings = HTMLMasonSettings.getInstance(project);
		Map<String, String> openCloseMap = settings.getOpenCloseMap();
		return openCloseMap.get(openTag);
	}

	protected boolean isNextElement(PsiElement element, IElementType typeToCheck)
	{
		PsiElement nextElement = PerlPsiUtil.getNextSignificantSibling(element);
		return nextElement != null && nextElement.getNode().getElementType() == typeToCheck;
	}

}
