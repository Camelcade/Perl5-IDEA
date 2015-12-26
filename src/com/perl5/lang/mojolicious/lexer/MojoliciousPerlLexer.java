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
	@Deprecated
	public static final int LEX_PERL_BLOCK_SEMI = LEX_CUSTOM4;
	@Deprecated
	public static final int LEX_PERL_LINE_SEMI = LEX_CUSTOM5;
	public static final String MOJO_SPACES = "([ \t\f]*)";
	public static final String MOJO_CLOSE_TAG = MOJO_SPACES + "(=?%>)";
	public static final Pattern BLOCK_START_PERL_LINE = Pattern.compile("^" + MOJO_SPACES + "(begin)" + MOJO_SPACES + "(\n).*");
	public static final Pattern BLOCK_START_PERL_BLOCK = Pattern.compile("^" + MOJO_SPACES + "(begin)" + MOJO_CLOSE_TAG);
	public static final Pattern BLOCK_END_PERL_LINE = Pattern.compile("^(%=?=?)" + MOJO_SPACES + "(end)");
	public static final Pattern BLOCK_END_PERL_BLOCK = Pattern.compile("^(<%=?=?)" + MOJO_SPACES + "(end)");
	public static final Pattern PERL_BLOCK_CLOSER = Pattern.compile("^" + MOJO_CLOSE_TAG);
	Pattern MOJO_BEING_IN_BLOCK = Pattern.compile(KEYWORD_MOJO_BEGIN + "\\s*" + KEYWORD_MOJO_BLOCK_CLOSER);
	Pattern MOJO_BEING_IN_LINE = Pattern.compile(KEYWORD_MOJO_BEGIN + "\\s*\\n");
	Pattern MOJO_END_IN_BLOCK = Pattern.compile(KEYWORD_MOJO_BLOCK_OPENER + "\\s*" + KEYWORD_MOJO_END + "\\s*" + KEYWORD_MOJO_BLOCK_CLOSER);
	Pattern MOJO_END_IN_LINE = Pattern.compile(KEYWORD_MOJO_LINE_OPENER + "\\s*" + KEYWORD_MOJO_END + "\\s*\\n");

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
			return super.perlAdvance();
		else if ((currentMojoState == LEX_PERL_LINE || currentMojoState == LEX_PERL_LINE_SEMI))
		{
			if (buffer.charAt(tokenStart) == '\n')
			{
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + 1);

				IElementType tokenType = currentMojoState == LEX_PERL_LINE_SEMI ? SEMICOLON : TokenType.NEW_LINE_INDENT;
				setCustomState(LEX_HTML_BLOCK);
				return tokenType;
			}
			else // if (currentMojoState == LEX_PERL_LINE) // there is a documentation example with %= ... begin
			{
				Matcher m = BLOCK_START_PERL_LINE.matcher(buffer);
				m.region(tokenStart, bufferEnd);

				if (m.lookingAt())
					return parseBeginBlock(tokenStart, m, false);
			}
		}
		else if (currentMojoState == LEX_PERL_BLOCK || currentMojoState == LEX_PERL_BLOCK_SEMI)
		{
			int closeTokenSize = 0;
			if (bufferAtString(buffer, tokenStart, "=%>"))
				closeTokenSize = 3;
			else if (bufferAtString(buffer, tokenStart, "%>"))
				closeTokenSize = 2;
			else
			{
//				if (currentMojoState == LEX_PERL_BLOCK)
//				{
				Matcher m = BLOCK_START_PERL_BLOCK.matcher(buffer);
				m.region(tokenStart, bufferEnd);

				if (m.lookingAt())
					return parseBeginBlock(tokenStart, m, true);
//				}
				return super.perlAdvance();
			}

			setTokenStart(tokenStart);
			boolean addSemi = currentMojoState == LEX_PERL_BLOCK_SEMI;
			setCustomState(LEX_HTML_BLOCK);

			setTokenEnd(tokenStart + closeTokenSize);
			if (addSemi)
				return EMBED_MARKER_SEMICOLON;
			else
				return EMBED_MARKER_CLOSE;
		}
		else if (currentMojoState == LEX_HTML_BLOCK)
		{
			setTokenStart(tokenStart);
			int offset = tokenStart;

			boolean blockStart = false;
			boolean clearLine = true;

			for (; offset < bufferEnd; offset++)
			{
				char currentChar = buffer.charAt(offset);
				if (offset < bufferEnd - 1 && currentChar == '<' && buffer.charAt(offset + 1) == '%')
				{
					blockStart = true;
					break;
				}
				else if (offset < bufferEnd && clearLine && currentChar == '%')
					break;
				else if (currentChar == '\n')
					clearLine = true;
				else if (currentChar != ' ' && currentChar != '\t' && currentChar != '\f')
					clearLine = false;
			}

			setTokenEnd(offset);

			if (offset == bufferEnd)  // end of file, html block
				yybegin(YYINITIAL);
			else if (!blockStart)        // begin of perl line
			{
				if (bufferAtString(buffer, offset, "%%"))
					preparsedTokensList.add(new CustomToken(offset, offset + 2, EMBED_MARKER));
				else
				{
					Matcher m = BLOCK_END_PERL_LINE.matcher(buffer);
					m.region(offset, bufferEnd);

					if (m.lookingAt())    // % end construction
					{
						// marker
						preparsedTokensList.add(new CustomToken(offset, offset + m.group(1).length(), EMBED_MARKER));
						offset += m.group(1).length();

						if (m.group(1).length() == 1)
							setCustomState(LEX_PERL_LINE);
						else
							setCustomState(LEX_PERL_LINE_SEMI);

						if (!m.group(2).isEmpty())    // spaces if any
						{
							preparsedTokensList.add(new CustomToken(offset, offset + m.group(2).length(), TokenType.WHITE_SPACE));
							offset += m.group(2).length();
						}

						// end as a right brace
						preparsedTokensList.add(new CustomToken(offset, offset + m.group(3).length(), RIGHT_BRACE));
						offset += m.group(3).length();

						if (offset < bufferEnd)
						{
							char semiChar = buffer.charAt(offset);
							preparsedTokensList.add(new CustomToken(offset, offset + 1, SEMICOLON));
							if (semiChar == '\n')
								setCustomState(LEX_HTML_BLOCK);
						}
					}
					else
					{
						int embedTokenSize = 1;
						int newMojoState = LEX_PERL_LINE;

						if (bufferAtString(buffer, offset, "%=="))
						{
							embedTokenSize = 3;
							newMojoState = LEX_PERL_LINE_SEMI;
						}
						else if (bufferAtString(buffer, offset, "%="))
						{
							embedTokenSize = 2;
							newMojoState = LEX_PERL_LINE_SEMI;
						}

						preparsedTokensList.add(new CustomToken(offset, offset + embedTokenSize, EMBED_MARKER));
						setCustomState(newMojoState);
					}
				}
			}
			else if (bufferAtString(buffer, offset, "<%#"))    // block comment
			{
				preparsedTokensList.add(new CustomToken(offset, offset + 3, EMBED_MARKER_OPEN));
				offset += 3;
				int commentEnd = offset;
				while (commentEnd < bufferEnd)
				{
					if (bufferAtString(buffer, commentEnd, "%>"))
						break;
					commentEnd++;
				}

				preparsedTokensList.add(new CustomToken(offset, commentEnd, COMMENT_BLOCK));
				if (commentEnd < bufferEnd) // not eof, got closing marker
					preparsedTokensList.add(new CustomToken(commentEnd, commentEnd + 2, EMBED_MARKER_CLOSE));

			}
			else if (bufferAtString(buffer, offset, "<%%"))    // <% macro
				preparsedTokensList.add(new CustomToken(offset, offset + 3, EMBED_MARKER));
			else   // begin of perl block
			{
				Matcher m = BLOCK_END_PERL_BLOCK.matcher(buffer);
				m.region(offset, bufferEnd);

				if (m.lookingAt())    // <% end construction
				{
					// marker
					preparsedTokensList.add(new CustomToken(offset, offset + m.group(1).length(), EMBED_MARKER_OPEN));
					offset += m.group(1).length();

					// change state
					if (m.group(1).length() == 2)
						setCustomState(LEX_PERL_BLOCK);
					else
						setCustomState(LEX_PERL_BLOCK_SEMI);

					if (!m.group(2).isEmpty())    // spaces if any
					{
						preparsedTokensList.add(new CustomToken(offset, offset + m.group(2).length(), TokenType.WHITE_SPACE));
						offset += m.group(2).length();
					}

					// end as a right brace
					preparsedTokensList.add(new CustomToken(offset, offset + m.group(3).length(), RIGHT_BRACE));
					offset += m.group(3).length();

					if (offset < bufferEnd)
					{
						m.region(offset, bufferEnd);

						if (m.lookingAt())    // immediate close
						{
							if (!m.group(1).isEmpty()) // got some spaces
							{
								preparsedTokensList.add(new CustomToken(offset, offset + m.group(1).length(), TokenType.WHITE_SPACE));
								offset += m.group(1).length();
							}
							preparsedTokensList.add(new CustomToken(offset, offset + m.group(2).length(), EMBED_MARKER_SEMICOLON));
							setCustomState(LEX_HTML_BLOCK);
						}
						else    // something else is there
							preparsedTokensList.add(new CustomToken(offset, offset + 1, SEMICOLON));
					}

				}
				else
				{
					int embedTokenSize = 2;
					int newMojoState = LEX_PERL_BLOCK;

					if (bufferAtString(buffer, offset, "<%=="))
					{
						embedTokenSize = 4;
						newMojoState = LEX_PERL_BLOCK_SEMI;
					}
					else if (bufferAtString(buffer, offset, "<%="))
					{
						embedTokenSize = 3;
						newMojoState = LEX_PERL_BLOCK_SEMI;
					}

					preparsedTokensList.add(new CustomToken(offset, offset + embedTokenSize, EMBED_MARKER_OPEN));
					setCustomState(newMojoState);
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

	@Override
	public boolean isLineCommentEnd(int currentPosition)
	{
		CharSequence buffer = getBuffer();
		return buffer.charAt(currentPosition) == '\n' || bufferAtString(buffer, currentPosition, "=%>") || bufferAtString(buffer, currentPosition, "%>");
	}
}
