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

package com.perl5.lang.tt2.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlLexerWithCustomStates;
import com.perl5.lang.tt2.idea.settings.TemplateToolkitSettings;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by hurricup on 05.06.2016.
 */
public class TemplateToolkitLexer extends TemplateToolkitLexerGenerated implements PerlLexerWithCustomStates
{
	private final Project myProject;
	private final TemplateToolkitSettings mySettings;
	private int customState = 0;
	private boolean isEscaped = false;

	public TemplateToolkitLexer(Project project)
	{
		super((Reader) null);
		myProject = project;
		mySettings = myProject == null ? null : TemplateToolkitSettings.getInstance(project);
	}

	@Override
	public IElementType perlAdvance() throws IOException
	{
		CharSequence buffer = getBuffer();
		int tokenStart = getNextTokenStart();
		int bufferEnd = getBufferEnd();
		int currentCustomState = getCustomState();
		int currentState = yystate();

		if (bufferEnd == 0 || tokenStart >= bufferEnd)
		{
			return super.perlAdvance();
		}
		else if (currentCustomState == LEX_HTML)
		{
			int offset = tokenStart;
			boolean blockStart = false;
			for (; offset < bufferEnd; offset++)
			{
				char currentChar = buffer.charAt(offset);
				if (isBufferAtString(buffer, offset, getStartTag()))
				{
					blockStart = true;
					break;
				}
				else if (isOutlineOpener(buffer, offset))
				{
					break;
				}
			}

			if (offset > tokenStart)
			{
				pushPreparsedToken(tokenStart, offset, TT2_HTML);
			}

			if (blockStart)
			{
				pushPreparsedToken(offset, offset + getStartTag().length(), TT2_OPEN_TAG);
				int nextCharOffset = offset + getStartTag().length();

				if (nextCharOffset < bufferEnd && buffer.charAt(nextCharOffset) == '#')
				{
					int blockCommentEnd = nextCharOffset + 1;
					String endTag = getEndTag();
					while (blockCommentEnd < bufferEnd && !isBufferAtString(buffer, blockCommentEnd, endTag))
					{
						blockCommentEnd++;
					}
					pushPreparsedToken(nextCharOffset, blockCommentEnd, LINE_COMMENT);
				}
				setCustomState(LEX_TEMPLATE_BLOCK);
			}
			else if (offset < bufferEnd)
			{
				pushPreparsedToken(offset, offset + getOutlineTag().length(), TT2_OUTLINE_TAG);
				setCustomState(LEX_TEMPLATE_LINE);
			}

			assert !preparsedTokensList.isEmpty();
			return getPreParsedToken();

		}
		else if (currentCustomState == LEX_TEMPLATE_BLOCK)
		{
			if (currentState != LEX_DQ_STRING && currentState != LEX_SQ_STRING && isBufferAtString(buffer, tokenStart, getEndTag()))
			{
				endTemplate(tokenStart, getEndTag().length());
				return TT2_CLOSE_TAG;
			}
			else if (isLineComment(buffer, tokenStart, bufferEnd))
			{
				return lexLineComment(buffer, tokenStart, bufferEnd);
			}
		}
		else if (currentCustomState == LEX_TEMPLATE_LINE)
		{
			if (tokenStart < bufferEnd && buffer.charAt(tokenStart) == '\n')
			{
				endTemplate(tokenStart, 1);
				return TT2_HARD_NEWLINE;
			}
			else if (isLineComment(buffer, tokenStart, bufferEnd))
			{
				return lexLineComment(buffer, tokenStart, bufferEnd);
			}
		}

		IElementType result = super.perlAdvance();

		if (currentCustomState == LEX_TEMPLATE_BLOCK || currentCustomState == LEX_TEMPLATE_LINE)
		{
			currentState = yystate();
			if (currentState == LEX_DQ_STRING)
			{
				if (result == TT2_DQ && !isEscaped)
				{
					popState();
					result = TT2_DQ_CLOSE;
				}
				else
				{
					isEscaped = !isEscaped && result == TT2_ESCAPE;
					result = TT2_STRING_CONTENT;
				}
			}
			else if (currentState == LEX_SQ_STRING)
			{
				if (result == TT2_SQ && !isEscaped)
				{
					popState();
					result = TT2_SQ_CLOSE;
				}
				else
				{
					isEscaped = !isEscaped && result == TT2_ESCAPE;
					result = TT2_STRING_CONTENT;
				}
			}
			else if (result == TT2_SQ)
			{
				pushState();
				yybegin(LEX_SQ_STRING);
				isEscaped = false;
				result = TT2_SQ_OPEN;
			}
			else if (result == TT2_DQ)
			{
				pushState();
				yybegin(LEX_DQ_STRING);
				isEscaped = false;
				result = TT2_DQ_OPEN;
			}
		}

		return result;
	}

	protected boolean isLineComment(CharSequence buffer, int offset, int bufferEnd)
	{
		int currentState = yystate();
		return currentState != LEX_DQ_STRING &&
				currentState != LEX_SQ_STRING &&
				getTokenHistory().getLastTokenType() != TT2_OPEN_TAG &&
				offset < bufferEnd &&
				buffer.charAt(offset) == '#';
	}

	protected IElementType lexLineComment(CharSequence buffer, int offset, int bufferEnd)
	{
		int endOffset = offset;
		String endTag = getEndTag();
		boolean isTemplateLine = getCustomState() == LEX_TEMPLATE_LINE;

		while (endOffset < bufferEnd)
		{
			if (buffer.charAt(endOffset) == '\n' || !isTemplateLine && isBufferAtString(buffer, endOffset, endTag))
			{
				break;
			}
			endOffset++;
		}
		setTokenStart(offset);
		setTokenEnd(endOffset);

		return LINE_COMMENT;
	}

	protected void endTemplate(int tokenStart, int tokenLength)
	{
		setTokenStart(tokenStart);
		setTokenEnd(tokenStart + tokenLength);
		setCustomState(LEX_HTML);

		int currentState = yystate();
		if (currentState == LEX_SQ_STRING || currentState == LEX_DQ_STRING)
		{
			popState();
		}
	}

	@Override
	protected int getPreparsedLexicalState()
	{
		return LEX_PREPARSED_ITEMS;
	}

	public Project getProject()
	{
		return myProject;
	}

	public int getCustomState()
	{
		return customState;
	}

	public void setCustomState(int newState)
	{
		customState = newState;
	}

	@Override
	public int getInitialCustomState()
	{
		return LEX_HTML;
	}

	protected String getStartTag()
	{
		return mySettings == null ? TemplateToolkitSettings.DEFAULT_START_TAG : mySettings.START_TAG;
	}

	protected String getEndTag()
	{
		return mySettings == null ? TemplateToolkitSettings.DEFAULT_END_TAG : mySettings.END_TAG;
	}

	protected String getOutlineTag()
	{
		return mySettings == null ? TemplateToolkitSettings.DEFAULT_OUTLINE_TAG : mySettings.OUTLINE_TAG;
	}

	protected boolean isOutlineEnabled()
	{
		return StringUtil.isNotEmpty(getOutlineTag());
	}

	protected boolean isOutlineOpener(CharSequence buffer, int offset)
	{
		return isOutlineEnabled() && !(offset > 0 && buffer.charAt(offset - 1) != '\n') && isBufferAtString(buffer, offset, getOutlineTag());
	}

	protected boolean isAnycaseEnabled()
	{
		return mySettings != null && mySettings.ENABLE_ANYCASE;
	}

	@Override
	public IElementType parseIdentifier()
	{
		String identifier = yytext().toString();

		// check for operator
		IElementType tokenType = TemplateToolkitSyntaxElements.TEXT_OPERATORS.get(identifier);

		if (tokenType == null)
		{
			// check for derictive
			if (isAnycaseEnabled())
			{
				identifier = identifier.toUpperCase();
			}

			tokenType = TemplateToolkitSyntaxElements.KEYWORDS.get(identifier);
		}

		return tokenType == null ? TT2_IDENTIFIER : tokenType;
	}
}
