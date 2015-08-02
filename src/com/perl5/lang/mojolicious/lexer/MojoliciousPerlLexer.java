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
import com.perl5.lang.perl.lexer.CustomToken;
import com.perl5.lang.perl.lexer.PerlLexer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 21.07.2015.
 */
public class MojoliciousPerlLexer extends PerlLexer
{
	int mojoState = LEX_HTML_BLOCK;

	public MojoliciousPerlLexer(Project project)
	{
		super(project);
	}

	protected int getMojoState()
	{
		return mojoState;
	}

	protected void setMojoState(int newState)
	{
		mojoState = newState;
	}

	public static final String MOJO_SPACES = "([ \t\f]*)";

	public static final Pattern BLOCK_START_PERL_LINE = Pattern.compile( "^" + MOJO_SPACES + "(begin)" + MOJO_SPACES + "(\n).*");
	public static final Pattern BLOCK_START_PERL_BLOCK = Pattern.compile( "^" + MOJO_SPACES + "(begin)" + MOJO_SPACES + "(=?%>)");

	public static final Pattern BLOCK_END_PERL_LINE = Pattern.compile("^(%=?=?)" + MOJO_SPACES + "(end)(.?)");
	public static final Pattern BLOCK_END_PERL_BLOCK = Pattern.compile("^(<%=?=?)" + MOJO_SPACES + "(end)(.?)");

	public IElementType advance() throws IOException
	{
		CharSequence buffer = getBuffer();
		int tokenStart = getNextTokenStart();
		int bufferEnd = buffer.length();
		int currentMojoState = getMojoState();

		if (preparsedTokensList.size() > 0 || bufferEnd == 0 || tokenStart >= bufferEnd)
			return super.advance();
		else if ((currentMojoState == LEX_MOJO_PERL_LINE || currentMojoState == LEX_MOJO_PERL_LINE_SEMI))
		{
			if( buffer.charAt(tokenStart) == '\n' )
			{
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + 1);

				IElementType tokenType = currentMojoState == LEX_MOJO_PERL_LINE_SEMI ? SEMICOLON : TokenType.NEW_LINE_INDENT;
				setMojoState(LEX_HTML_BLOCK);
				return tokenType;
			}
			else if(currentMojoState == LEX_MOJO_PERL_LINE )
			{
				Matcher m = BLOCK_START_PERL_LINE.matcher(buffer);
				m.region(tokenStart,bufferEnd);

				if( m.lookingAt() )
				{
					if( !m.group(1).isEmpty())
					{
						preparsedTokensList.add(new CustomToken(tokenStart, tokenStart + m.group(1).length(), TokenType.WHITE_SPACE));
						tokenStart += m.group(1).length();
					}

					preparsedTokensList.add(new CustomToken(tokenStart, tokenStart + 5, RESERVED_SUB));
					tokenStart+=5;

					if( !m.group(3).isEmpty())	// spaces as braces
						preparsedTokensList.add(new CustomToken(tokenStart, tokenStart + m.group(3).length(), LEFT_BRACE));
					else	// newline as brace
					{
						preparsedTokensList.add(new CustomToken(tokenStart, tokenStart + 1, LEFT_BRACE));
						setMojoState(LEX_HTML_BLOCK);
					}

					return getPreParsedToken();
				}
			}
		} else if (currentMojoState == LEX_MOJO_PERL_BLOCK || currentMojoState == LEX_MOJO_PERL_BLOCK_SEMI)
		{
			int closeTokenSize = 0;
			if (bufferAtString(buffer, tokenStart, "=%>"))
				closeTokenSize = 3;
			else if (bufferAtString(buffer, tokenStart, "%>"))
				closeTokenSize = 2;
			else
				return super.advance();

			setTokenStart(tokenStart);
			boolean addSemi = currentMojoState == LEX_MOJO_PERL_BLOCK_SEMI;
			setMojoState(LEX_HTML_BLOCK);

			setTokenEnd(tokenStart + closeTokenSize);
			if( addSemi )
				return EMBED_MARKER_SEMICOLON;
			else
				return EMBED_MARKER;
		} else if (currentMojoState == LEX_HTML_BLOCK)
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
				} else if (offset < bufferEnd && clearLine && currentChar == '%')
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
					int embedTokenSize = 1;
					int newMojoState = LEX_MOJO_PERL_LINE;

					if (bufferAtString(buffer, offset, "%=="))
					{
						embedTokenSize = 3;
						newMojoState = LEX_MOJO_PERL_LINE_SEMI;
					}
					else if (bufferAtString(buffer, offset, "%="))
					{
						embedTokenSize = 2;
						newMojoState = LEX_MOJO_PERL_LINE_SEMI;
					}

					preparsedTokensList.add(new CustomToken(offset, offset + embedTokenSize, EMBED_MARKER));
					setMojoState(newMojoState);
				}
			} else if (bufferAtString(buffer, offset, "<%#"))    // block comment
			{
				preparsedTokensList.add(new CustomToken(offset, offset + 3, EMBED_MARKER));
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
					preparsedTokensList.add(new CustomToken(commentEnd, commentEnd + 2, EMBED_MARKER));

			} else if (bufferAtString(buffer, offset, "<%%"))    // <% macro
				preparsedTokensList.add(new CustomToken(offset, offset + 3, EMBED_MARKER));
			else   // begin of perl block
			{
				int embedTokenSize = 2;
				int newMojoState = LEX_MOJO_PERL_BLOCK;

				if (bufferAtString(buffer, offset, "<%=="))
				{
					embedTokenSize = 4;
					newMojoState = LEX_MOJO_PERL_BLOCK_SEMI;
				}
				else if (bufferAtString(buffer, offset, "<%="))
				{
					embedTokenSize = 3;
					newMojoState = LEX_MOJO_PERL_BLOCK_SEMI;
				}

				preparsedTokensList.add(new CustomToken(offset, offset + embedTokenSize, EMBED_MARKER));
				setMojoState(newMojoState);
			}

			return TEMPLATE_BLOCK_HTML;

		}
		return super.advance();
	}

	@Override
	public boolean isLineCommentEnd(int currentPosition)
	{
		CharSequence buffer = getBuffer();
		return buffer.charAt(currentPosition) == '\n' || bufferAtString(buffer, currentPosition, "=%>") || bufferAtString(buffer, currentPosition, "%>");
	}
}
