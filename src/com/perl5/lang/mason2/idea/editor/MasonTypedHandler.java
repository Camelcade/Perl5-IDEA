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

package com.perl5.lang.mason2.idea.editor;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlTokenType;
import com.perl5.lang.mason2.Mason2TemplatingFileViewProvider;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 07.01.2016.
 */
public class MasonTypedHandler extends TypedHandlerDelegate implements Mason2ElementTypes, XmlTokenType, PerlElementTypes
{
	private static final THashMap<String, String> SIMPLE_COMPLETION_MAP = new THashMap<String, String>();

	static
	{
		SIMPLE_COMPLETION_MAP.put(KEYWORD_DOC_OPENER_UNCLOSED, KEYWORD_DOC_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_CLASS_OPENER_UNCLOSED, KEYWORD_CLASS_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_INIT_OPENER_UNCLOSED, KEYWORD_INIT_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_PERL_OPENER_UNCLOSED, KEYWORD_PERL_CLOSER);
		SIMPLE_COMPLETION_MAP.put(KEYWORD_TEXT_OPENER_UNCLOSED, KEYWORD_TEXT_CLOSER);
	}

	@Override
	public Result charTyped(char c, final Project project, @NotNull final Editor editor, @NotNull PsiFile file)
	{
		if (file.getViewProvider() instanceof Mason2TemplatingFileViewProvider)
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
					else if (elementText.equals(KEYWORD_FLAGS_OPENER_UNCLOSED))
					{
						EditorModificationUtil.insertStringAtCaret(editor, " extends => '' " + KEYWORD_FLAGS_CLOSER, false, true, 13);
					}
				}
			}
			else if (c == ' ')
			{
				PsiElement element = file.findElementAt(editor.getCaretModel().getOffset() - 2);
				if (element != null)
				{
					IElementType elementType = element.getNode().getElementType();
					if (elementType == MASON_BLOCK_OPENER && !isNextElement(element, MASON_BLOCK_CLOSER))
					{
						EditorModificationUtil.insertStringAtCaret(editor, KEYWORD_BLOCK_CLOSER, false, false);
					}
					else if (elementType == MASON_CALL_OPENER && !isNextElement(element, MASON_CALL_CLOSER))
					{
						EditorModificationUtil.insertStringAtCaret(editor, KEYWORD_CALL_CLOSER, false, false);
					}
					else if (elementType == MASON_METHOD_OPENER)
					{
						EditorModificationUtil.insertStringAtCaret(editor, ">\n" + KEYWORD_METHOD_CLOSER, false, false);
					}
					else if (elementType == MASON_FILTER_OPENER)
					{
						EditorModificationUtil.insertStringAtCaret(editor, ">\n" + KEYWORD_FILTER_CLOSER, false, false);
					}
					else if (elementType == MASON_OVERRIDE_OPENER)
					{
						EditorModificationUtil.insertStringAtCaret(editor, ">\n" + KEYWORD_OVERRIDE_CLOSER, false, false);
					}
				}
			}
			else if (c == '{')
			{
				int offset = editor.getCaretModel().getOffset();
				PsiElement element = file.findElementAt(offset - 2);
				if (element != null && element.getNode().getElementType() == LEFT_BRACE)
				{
					Document document = editor.getDocument();
					int lineNumber = document.getLineNumber(offset - 1);
					PsiElement lineStartElement = file.findElementAt(document.getLineStartOffset(lineNumber));

					if (lineStartElement != null && lineStartElement.getNode().getElementType() == MASON_LINE_OPENER)
					{
						PsiElement nextElement = file.findElementAt(offset - 1);
						if (nextElement != null && nextElement.getNode().getElementType() == RIGHT_BRACE)
						{
							EditorModificationUtil.insertStringAtCaret(editor, "\n\n% ", false, true, 1);
						}
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
