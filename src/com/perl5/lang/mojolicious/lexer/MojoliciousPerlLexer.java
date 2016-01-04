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

package com.perl5.lang.mojolicious.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mojolicious.MojoliciousPerlElementTypes;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexerWithCustomStates;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 21.07.2015.
 */
public class MojoliciousPerlLexer extends PerlLexerWithCustomStates implements MojoliciousPerlElementTypes
{
	// lexical states
	public static final int LEX_HTML_BLOCK = LEX_CUSTOM1;             // template block
	public static final int LEX_PERL_BLOCK = LEX_CUSTOM2;
	public static final int LEX_PERL_LINE = LEX_CUSTOM3;
	public static final int LEX_PERL_EXPR_BLOCK = LEX_CUSTOM4;
	public static final int LEX_PERL_EXPR_LINE = LEX_CUSTOM5;

	public static final Pattern MOJO_BEGIN_IN_BLOCK = Pattern.compile(KEYWORD_MOJO_BEGIN + "\\s*" + KEYWORD_MOJO_BLOCK_CLOSER);
	public static final Pattern MOJO_BEGIN_IN_LINE = Pattern.compile(KEYWORD_MOJO_BEGIN + "\\s*\\n");
	public static final Pattern MOJO_END_IN_BLOCK = Pattern.compile(KEYWORD_MOJO_BLOCK_OPENER + "(\\s*)" + KEYWORD_MOJO_END + "(\\s*)" + KEYWORD_MOJO_BLOCK_CLOSER);
	public static final Pattern MOJO_END_IN_LINE = Pattern.compile(KEYWORD_MOJO_LINE_OPENER + "(\\s*)" + KEYWORD_MOJO_END + "(\\s*)\\n");

	public MojoliciousPerlLexer(Project project)
	{
		super(project);
		setCustomState(LEX_HTML_BLOCK);
	}

	public boolean isAtBegin(CharSequence buffer, int offset)
	{
		return offset + 4 < getBufferEnd() &&
				buffer.charAt(offset) == 'b' &&
				buffer.charAt(offset + 1) == 'e' &&
				buffer.charAt(offset + 2) == 'g' &&
				buffer.charAt(offset + 3) == 'i' &&
				buffer.charAt(offset + 4) == 'n';
	}

	@Override
	public int getInitialCustomState()
	{
		return LEX_HTML_BLOCK;
	}

	public IElementType perlAdvance() throws IOException
	{
		CharSequence buffer = getBuffer();
		int tokenStart = getNextTokenStart();
		int bufferEnd = getBufferEnd();
		int currentMojoState = getCustomState();

		if (bufferEnd == 0 || tokenStart >= bufferEnd)
		{
			return super.perlAdvance();
		}

		char currentChar = buffer.charAt(tokenStart);

		if (currentMojoState == LEX_PERL_LINE)    // eol for statement
		{
			if (currentChar == '\n')
			{
				setCustomState(LEX_HTML_BLOCK);
			}
			else if (isAtBegin(buffer, tokenStart))
			{
				Matcher m = MOJO_BEGIN_IN_LINE.matcher(buffer);
				m.region(tokenStart, bufferEnd);
				if (m.lookingAt())    // % ... begin
				{
					return getBeginToken(tokenStart);
				}
			}
		}
		else if (currentMojoState == LEX_PERL_EXPR_LINE && currentChar == '\n')    // eol for expression
		{
			setTokenStart(tokenStart);
			setTokenEnd(tokenStart + 1);
			setCustomState(LEX_HTML_BLOCK);
			return SEMICOLON;
		}
		else if (currentMojoState == LEX_PERL_BLOCK)
		{
			if (tokenStart + 1 < bufferEnd && currentChar == '%' && buffer.charAt(tokenStart + 1) == '>')    // %>
			{
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + KEYWORD_MOJO_BLOCK_CLOSER.length());
				setCustomState(LEX_HTML_BLOCK);
				return MOJO_BLOCK_CLOSER;
			}
			else if (isAtBegin(buffer, tokenStart))
			{
				Matcher m = MOJO_BEGIN_IN_BLOCK.matcher(buffer);
				m.region(tokenStart, bufferEnd);
				if (m.lookingAt())    //  ... begin %>
				{
					return getBeginToken(tokenStart);
				}
			}
		}
		else if (currentMojoState == LEX_PERL_EXPR_BLOCK)
		{
			if (currentChar == '=' && tokenStart + 2 < bufferEnd && buffer.charAt(tokenStart + 1) == '%' && buffer.charAt(tokenStart + 2) == '>')    // =%>
			{
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + KEYWORD_MOJO_BLOCK_EXPR_NOSPACE_CLOSER.length());
				setCustomState(LEX_HTML_BLOCK);
				return MOJO_BLOCK_EXPR_NOSPACE_CLOSER;

			}
			else if (currentChar == '%' && tokenStart + 1 < bufferEnd && buffer.charAt(tokenStart + 1) == '>') // %>
			{
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + KEYWORD_MOJO_BLOCK_CLOSER.length());
				setCustomState(LEX_HTML_BLOCK);
				return MOJO_BLOCK_EXPR_CLOSER;

			}
		}
		else if (currentMojoState == LEX_HTML_BLOCK)
		{
			int offset = tokenStart;

			boolean blockStart = false;
			boolean clearLine = true;

			for (; offset < bufferEnd; offset++)
			{
				currentChar = buffer.charAt(offset);
				if (offset < bufferEnd - 1 && currentChar == '<' && buffer.charAt(offset + 1) == '%')
				{
					blockStart = true;
					break;
				}
				else if (offset < bufferEnd && clearLine && currentChar == '%')
				{
					break;
				}
				else if (currentChar == '\n')
				{
					clearLine = true;
				}
				else if (!Character.isWhitespace(currentChar))
				{
					clearLine = false;
				}
			}

			if (offset > tokenStart)
			{
				addPreparsedToken(tokenStart, offset, MOJO_TEMPLATE_BLOCK_HTML);
			}

			if (offset == bufferEnd)  // end of file, html block
			{
				yybegin(YYINITIAL);
			}
			else if (blockStart) // begin of perl block
			{
				boolean oneMoreCharLeft = offset < bufferEnd - 2;
				char extraChar = oneMoreCharLeft ? buffer.charAt(offset + 2) : 0;

				if (extraChar == '=')    // <%=
				{
					if (offset < bufferEnd - 3 && buffer.charAt(offset + 3) == '=') // <%==
					{
						addPreparsedToken(offset, offset + KEYWORD_MOJO_BLOCK_EXPR_ESCAPED_OPENER.length(), MOJO_BLOCK_EXPR_ESCAPED_OPENER);
						setCustomState(LEX_PERL_EXPR_BLOCK);
					}
					else
					{
						addPreparsedToken(offset, offset + KEYWORD_MOJO_BLOCK_EXPR_OPENER.length(), MOJO_BLOCK_EXPR_OPENER);
						setCustomState(LEX_PERL_EXPR_BLOCK);
					}
				}
				else if (extraChar == '%')    // <%%
				{
					addPreparsedToken(offset, offset + KEYWORD_MOJO_BLOCK_OPENER_TAG.length(), MOJO_BLOCK_OPENER_TAG);
				}
				else if (extraChar == '#') // <%#
				{
					int commentEnd = offset + 3;
					while (commentEnd < bufferEnd)
					{
						if (commentEnd + 1 < bufferEnd && buffer.charAt(commentEnd) == '%' && buffer.charAt(commentEnd + 1) == '>')
						{
							commentEnd += 2;
							break;
						}
						commentEnd++;
					}
					addPreparsedToken(offset, commentEnd, PerlElementTypes.COMMENT_LINE);
				}
				else    // <%
				{
					addPreparsedToken(offset, offset + KEYWORD_MOJO_BLOCK_OPENER.length(), MOJO_BLOCK_OPENER);
					setCustomState(LEX_PERL_BLOCK);

					Matcher m = MOJO_END_IN_BLOCK.matcher(buffer);
					m.region(offset, bufferEnd);
					if (m.lookingAt())    // <% end %>
					{
						offset += KEYWORD_MOJO_BLOCK_OPENER.length();
						if (!m.group(1).isEmpty())
						{
							addPreparsedToken(offset, offset += m.group(1).length(), TokenType.WHITE_SPACE);
						}

						addPreparsedToken(offset, offset += KEYWORD_MOJO_END.length(), MOJO_END);

						if (!m.group(2).isEmpty())
						{
							addPreparsedToken(offset, offset += m.group(2).length(), TokenType.WHITE_SPACE);
						}

						addPreparsedToken(offset, offset + KEYWORD_MOJO_BLOCK_CLOSER.length(), MOJO_BLOCK_CLOSER);
						setCustomState(LEX_HTML_BLOCK);
					}

				}
			}
			else  // begin of perl line
			{
				boolean oneMoreCharLeft = offset < bufferEnd - 1;
				char extraChar = oneMoreCharLeft ? buffer.charAt(offset + 1) : 0;

				if (extraChar == '=')    // %=
				{
					if (offset < bufferEnd - 2 && buffer.charAt(offset + 2) == '=') // %==
					{
						addPreparsedToken(offset, offset + KEYWORD_MOJO_LINE_EXPR_ESCAPED_OPENER.length(), MOJO_LINE_EXPR_ESCAPED_OPENER);
						setCustomState(LEX_PERL_EXPR_LINE);
					}
					else
					{
						addPreparsedToken(offset, offset + KEYWORD_MOJO_LINE_EXPR_OPENER.length(), MOJO_LINE_EXPR_OPENER);
						setCustomState(LEX_PERL_EXPR_LINE);
					}
				}
				else if (extraChar == '%')    // %%
				{
					addPreparsedToken(offset, offset + KEYWORD_MOJO_LINE_OPENER_TAG.length(), MOJO_LINE_OPENER_TAG);
				}
				else if (extraChar == '#') // %#
				{
					int commentEnd = offset + 2;
					while (commentEnd < bufferEnd)
					{
						if (buffer.charAt(commentEnd) == '\n')
						{
							commentEnd++;
							break;
						}
						commentEnd++;
					}
					addPreparsedToken(offset, commentEnd, PerlElementTypes.COMMENT_LINE);
				}
				else    // %
				{
					addPreparsedToken(offset, offset + KEYWORD_MOJO_LINE_OPENER.length(), MOJO_LINE_OPENER);
					setCustomState(LEX_PERL_LINE);

					Matcher m = MOJO_END_IN_LINE.matcher(buffer);
					m.region(offset, bufferEnd);
					if (m.lookingAt())    // % end
					{
						offset += KEYWORD_MOJO_LINE_OPENER.length();
						if (!m.group(1).isEmpty())
						{
							addPreparsedToken(offset, offset += m.group(1).length(), TokenType.WHITE_SPACE);
						}

						addPreparsedToken(offset, offset += KEYWORD_MOJO_END.length(), MOJO_END);

						if (!m.group(2).isEmpty())
						{
							addPreparsedToken(offset, offset += m.group(2).length(), TokenType.WHITE_SPACE);
						}

						addPreparsedToken(offset, offset + 1, TokenType.NEW_LINE_INDENT);
						setCustomState(LEX_HTML_BLOCK);
					}
				}
			}
			return getPreParsedToken();
		}
		return super.perlAdvance();
	}

	protected IElementType getBeginToken(int offset)
	{
		setTokenStart(offset);
		setTokenEnd(offset + KEYWORD_MOJO_BEGIN.length());
		return MOJO_BEGIN;
	}

	@Override
	public boolean isLineCommentEnd(int currentPosition)
	{
		CharSequence buffer = getBuffer();
		char currentChar = buffer.charAt(currentPosition);
		return currentChar == '\n'
				|| currentChar == '%' && currentPosition + 1 < getBufferEnd() && buffer.charAt(currentChar + 1) == '>'
				;
	}
}
