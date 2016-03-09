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
import com.perl5.lang.htmlmason.HTMLMasonFileViewProvider;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 09.03.2016.
 */
public class HTMLMasonTypedHandler extends TypedHandlerDelegate implements HTMLMasonElementTypes, XmlTokenType, PerlElementTypes
{
	private static final THashMap<String, String> SIMPLE_COMPLETION_MAP = new THashMap<String, String>();

	static
	{
		SIMPLE_COMPLETION_MAP.put(KEYWORD_PERL_OPENER_UNCLOSED, KEYWORD_PERL_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_INIT_OPENER_UNCLOSED, KEYWORD_INIT_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_CLEANUP_OPENER_UNCLOSED, KEYWORD_CLEANUP_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_ONCE_OPENER_UNCLOSED, KEYWORD_ONCE_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_SHARED_OPENER_UNCLOSED, KEYWORD_SHARED_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_FLAGS_OPENER_UNCLOSED, KEYWORD_FLAGS_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_ATTR_OPENER_UNCLOSED, KEYWORD_ATTR_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_ARGS_OPENER_UNCLOSED, KEYWORD_ARGS_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_FILTER_OPENER_UNCLOSED, KEYWORD_FILTER_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_TEXT_OPENER_UNCLOSED, KEYWORD_TEXT_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_DOC_OPENER_UNCLOSED, KEYWORD_DOC_CLOSER);
	}

	@Override
	public Result charTyped(char c, Project project, @NotNull Editor editor, @NotNull PsiFile file)
	{
		if (file.getViewProvider() instanceof HTMLMasonFileViewProvider)
		{
			if (c == '>')
			{
				PsiElement element = file.findElementAt(editor.getCaretModel().getOffset() - 2);
				if (element != null && element.getNode().getElementType() == XML_DATA_CHARACTERS)
				{
					String elementText = element.getText();
					String closeTag;
					if (elementText.equals(KEYWORD_FLAGS_OPENER_UNCLOSED))
					{
						EditorModificationUtil.insertStringAtCaret(editor, "\ninherit => ''\n" + KEYWORD_FLAGS_CLOSER, false, true, 13);
					}
					else if ((closeTag = SIMPLE_COMPLETION_MAP.get(elementText)) != null)
					{
						EditorModificationUtil.insertStringAtCaret(editor, closeTag, false, false);
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
						EditorModificationUtil.insertStringAtCaret(editor, KEYWORD_BLOCK_CLOSER, false, false);
					}
					else if (elementType == HTML_MASON_CALL_OPENER && !isNextElement(element, HTML_MASON_CALL_CLOSER))
					{
						EditorModificationUtil.insertStringAtCaret(editor, KEYWORD_CALL_CLOSER, false, false);
					}
					else if (elementType == HTML_MASON_CALL_FILTERING_OPENER && !isNextElement(element, HTML_MASON_CALL_CLOSER))
					{
						EditorModificationUtil.insertStringAtCaret(editor, KEYWORD_CALL_CLOSER + KEYWORD_CALL_CLOSE_TAG_START + KEYWORD_TAG_CLOSER, false, false);
					}
					else if (elementType == HTML_MASON_METHOD_OPENER)
					{
						EditorModificationUtil.insertStringAtCaret(editor, ">\n" + KEYWORD_METHOD_CLOSER, false, false);
					}
					else if (elementType == HTML_MASON_DEF_OPENER)
					{
						EditorModificationUtil.insertStringAtCaret(editor, ">\n" + KEYWORD_DEF_CLOSER, false, false);
					}
				}
			}

		}

		return super.charTyped(c, project, editor, file);
	}

	protected boolean isNextElement(PsiElement element, IElementType typeToCheck)
	{
		PsiElement nextElement = PerlPsiUtil.getNextSignificantSibling(element);
		return nextElement != null && nextElement.getNode().getElementType() == typeToCheck;
	}

}
