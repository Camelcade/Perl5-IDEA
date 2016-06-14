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

package com.perl5.lang.perl.idea.editor.smartkeys;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.RegexBlock;
import com.perl5.lang.perl.psi.PsiPerlHashIndex;
import com.perl5.lang.perl.psi.PsiPerlStringBare;
import gnu.trove.THashSet;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by hurricup on 25.07.2015.
 */
public class PerlTypedHandler extends TypedHandlerDelegate implements PerlElementTypes
{
	private static final String AMBIGUOUS_Q_SUFFIXES = "wqxr"; // these chars are ignored after

	private static final TokenSet SINGLE_QUOTE_OPENERS = TokenSet.create(
			RESERVED_Q,
			RESERVED_QW,
			RESERVED_QQ,
			RESERVED_QX,

			RESERVED_M,
			RESERVED_QR
	);

	private static final TokenSet DOUBLE_QUOTE_OPENERS = TokenSet.create(
			RESERVED_S,
			RESERVED_TR,
			RESERVED_Y
	);

	private static final Set<String> SINGLE_QUOTE_OPENERS_TEXT = new THashSet<String>(Arrays.asList(
			"q",
			"qx",
			"qq",
			"qw",
			"qr",
			"m"
	));

	private static final Set<String> DOUBLE_QUOTE_OPENERS_TEXT = new THashSet<String>(Arrays.asList(
			"s",
			"tr",
			"y"
	));

	@Override
	public Result beforeCharTyped(char typedChar, Project project, Editor editor, PsiFile file, FileType fileType)
	{
		// regexp quotes
		if (!Character.isWhitespace(typedChar))
		{
			int offset = editor.getCaretModel().getOffset() - 1;
			PsiElement element = file.findElementAt(offset);
			if (element != null)
			{
				boolean hasSpace = false;

				while (offset > 0 && element != null && element instanceof PsiWhiteSpace)
				{
					hasSpace = true;
					offset = element.getTextOffset() - 1;
					element = file.findElementAt(offset);
				}

				if (element != null)
				{
					Result result = null;
					IElementType elementType = element.getNode().getElementType();
					if (SINGLE_QUOTE_OPENERS.contains(elementType) ||
							(typedChar == '/' && (elementType == OPERATOR_RE || elementType == OPERATOR_NOT_RE))
							)
					{
						if (elementType == RESERVED_Q && StringUtil.containsChar(AMBIGUOUS_Q_SUFFIXES, typedChar))
						{
							return Result.CONTINUE;
						}
						result = handleSingleQuote(typedChar, hasSpace, editor);
					}
					else if (DOUBLE_QUOTE_OPENERS.contains(elementType))
					{
						result = handleDoubleQuote(typedChar, hasSpace, editor);
					}
					else if (elementType == STRING_CONTENT && isInHashIndex(element))
					{
						if (SINGLE_QUOTE_OPENERS_TEXT.contains(element.getText()))
						{
							result = handleSingleQuote(typedChar, hasSpace, editor);
						}
						else if (DOUBLE_QUOTE_OPENERS_TEXT.contains(element.getText()))
						{
							result = handleDoubleQuote(typedChar, hasSpace, editor);
						}
					}

					if (result != null)
					{
						return result;
					}
				}
			}
		}

		return super.beforeCharTyped(typedChar, project, editor, file, fileType);
	}


	@Nullable
	private Result handleSingleQuote(char typedChar, boolean hasSpace, Editor editor)
	{
		char closeChar = getRegexCloseChar(typedChar, hasSpace);

		if (closeChar > 0)
		{

			EditorModificationUtil.insertStringAtCaret(editor, typedChar + "" + closeChar, false, true, 1);
			return Result.STOP;
		}
		return null;
	}

	private boolean isInHashIndex(PsiElement element)
	{
		return element.getParent() instanceof PsiPerlStringBare && element.getParent().getParent() instanceof PsiPerlHashIndex;
	}

	@Nullable
	private Result handleDoubleQuote(char typedChar, boolean hasSpace, Editor editor)
	{
		char closeChar = getRegexCloseChar(typedChar, hasSpace);

		if (closeChar > 0)
		{
			String appendix = typedChar + "" + closeChar;

			if (closeChar == typedChar)
			{
				appendix += closeChar;
			}
			else
			{
				appendix += appendix;
			}

			EditorModificationUtil.insertStringAtCaret(editor, appendix, false, true, 1);
			return Result.STOP;
		}
		return null;
	}

	private char getRegexCloseChar(char openingChar, boolean hasSpace)
	{
		if (hasSpace && openingChar == '#')    // sharp may be only without space
		{
			return 0;
		}
//		if (!hasSpace && (openingChar == '_' || Character.isLetterOrDigit(openingChar))) // identifier continuation may be only with space
//			return 0;
		if (openingChar == '_' || Character.isLetterOrDigit(openingChar)) // temporarily disabled auto-insertion of non-delimiter quotes because of race
		{
			return 0;
		}

		return RegexBlock.getQuoteCloseChar(openingChar);
	}
}
