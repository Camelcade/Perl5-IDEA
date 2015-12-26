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
import com.perl5.lang.perl.lexer.CustomToken;
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
	public static final Pattern MOJO_END_IN_BLOCK = Pattern.compile(KEYWORD_MOJO_BLOCK_OPENER + "\\s*" + KEYWORD_MOJO_END + "\\s*" + KEYWORD_MOJO_BLOCK_CLOSER);
	public static final Pattern MOJO_END_IN_LINE = Pattern.compile(KEYWORD_MOJO_LINE_OPENER + "\\s*" + KEYWORD_MOJO_END + "\\s*\\n");

	public MojoliciousPerlLexer(Project project)
	{
		super(project);
		setCustomState(LEX_HTML_BLOCK);
	}

	@Override
	public int getInitialCustomState()
	{
		return LEX_HTML_BLOCK;
	}

	@Override
	public int getPerlCustomState()
	{
		return LEX_PERL_BLOCK;
	}

	public IElementType perlAdvance() throws IOException
	{
		CharSequence buffer = getBuffer();
		int tokenStart = getNextTokenStart();
		int bufferEnd = buffer.length();
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
			// todo begin block
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
			// todo begin block
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
			setTokenStart(tokenStart);
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

			setTokenEnd(offset);

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
						yybegin(LEX_PERL_EXPR_BLOCK);
					}
					else
					{
						addPreparsedToken(offset, offset + KEYWORD_MOJO_BLOCK_EXPR_OPENER.length(), MOJO_BLOCK_EXPR_OPENER);
						yybegin(LEX_PERL_EXPR_BLOCK);
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
					yybegin(LEX_PERL_BLOCK);
				}
			}
			else  // begin of perl line
			{
				boolean oneMoreCharLeft = offset < bufferEnd - 1;
				char extraChar = oneMoreCharLeft ? buffer.charAt(offset + 1) : 0;

				if (extraChar == '=')
				{
					if (offset < bufferEnd - 2 && buffer.charAt(offset + 2) == '=') // %==
					{
						addPreparsedToken(offset, offset + KEYWORD_MOJO_LINE_EXPR_ESCAPED_OPENER.length(), MOJO_LINE_EXPR_ESCAPED_OPENER);
						yybegin(LEX_PERL_EXPR_LINE);
					}
					else
					{
						addPreparsedToken(offset, offset + KEYWORD_MOJO_LINE_EXPR_OPENER.length(), MOJO_LINE_EXPR_OPENER);
						yybegin(LEX_PERL_EXPR_LINE);
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
					yybegin(LEX_PERL_LINE);
				}
			}
			return MOJO_TEMPLATE_BLOCK_HTML;

		}
		return super.perlAdvance();
	}

	/**
	 * Parsing begin statement from line or block
	 *
	 * @param offset current offset
	 * @param m      matcher, which is matches begin statement
	 * @return token type
	 */
/*
	public IElementType parseBeginBlock(int offset, Matcher m, boolean endBlock)
	{
		if (!m.group(1).isEmpty())
		{
			preparsedTokensList.add(new CustomToken(offset, offset + m.group(1).length(), TokenType.WHITE_SPACE));
			offset += m.group(1).length();
		}

		preparsedTokensList.add(new CustomToken(offset, offset + m.group(2).length(), RESERVED_SUB));
		offset += m.group(2).length();

		if (!m.group(3).isEmpty())    // spaces as a left brace
			preparsedTokensList.add(new CustomToken(offset, offset + m.group(3).length(), LEFT_BRACE));
		else    // close token as a left brace
		{
			if (endBlock)
			{
				preparsedTokensList.add(new CustomToken(offset, offset + 1, LEFT_BRACE));
				preparsedTokensList.add(new CustomToken(offset + 1, offset + m.group(4).length(), EMBED_MARKER_CLOSE));
			}
			else
				preparsedTokensList.add(new CustomToken(offset, offset + m.group(4).length(), LEFT_BRACE));
			setCustomState(LEX_HTML_BLOCK);
		}

		return getPreParsedToken();
	}
*/

	@Override
	public boolean isLineCommentEnd(int currentPosition)
	{
		CharSequence buffer = getBuffer();
		char currentChar = buffer.charAt(currentPosition);
		return currentChar == '\n'
				|| currentChar == '%' && currentPosition + 1 < buffer.length() && buffer.charAt(currentChar + 1) == '>'
				;
	}
}
