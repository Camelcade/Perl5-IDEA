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

/**
 * Created by hurricup on 21.07.2015.
 */
public class MojoliciousPerlLexer extends PerlLexer
{
	int mojoState = LEX_HTML_BLOCK;
	boolean addSemicolon = false;

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

	@Override
	public void reset(CharSequence buf, int start, int end, int initialState)
	{
		if (start == 0)
			setMojoState(LEX_HTML_BLOCK);

		super.reset(buf, start, end, initialState);
	}

	public IElementType advance() throws IOException
	{
		CharSequence buffer = getBuffer();
		int tokenStart = getNextTokenStart();
		int bufferEnd = buffer.length();
		int currentMojoState = getMojoState();

		if (preparsedTokensList.size() > 0 || bufferEnd == 0 || tokenStart >= bufferEnd)
			return super.advance();
		else if (currentMojoState == LEX_MOJO_PERL_LINE && buffer.charAt(tokenStart) == '\n')
		{
			setTokenStart(tokenStart);
			setTokenEnd(tokenStart + 1);
			setMojoState(LEX_HTML_BLOCK);
			return addSemicolon ? SEMICOLON : TokenType.NEW_LINE_INDENT;
		} else if (currentMojoState == LEX_MOJO_PERL_BLOCK)
		{
			int closeTokenSize = 0;
			if (bufferAtString(buffer, tokenStart, " =%>"))
				closeTokenSize = 4;
			else if (bufferAtString(buffer, tokenStart, " %>"))
				closeTokenSize = 3;
			else
				return super.advance();

			setTokenStart(tokenStart);
			setMojoState(LEX_HTML_BLOCK);
			if( addSemicolon )
			{
				setTokenEnd(tokenStart + 1);
				preparsedTokensList.add(new CustomToken(tokenStart + 1, tokenStart + closeTokenSize, EMBED_MARKER));
				return SEMICOLON;
			} else
			{
				setTokenEnd(tokenStart + closeTokenSize);
				return EMBED_MARKER;
			}
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
					addSemicolon = false;

					if (bufferAtString(buffer, offset, "%=="))
					{
						embedTokenSize = 3;
						addSemicolon = true;
					}
					else if (bufferAtString(buffer, offset, "%="))
					{
						addSemicolon = true;
						embedTokenSize = 2;
					}

					preparsedTokensList.add(new CustomToken(offset, offset + embedTokenSize, EMBED_MARKER));
					setMojoState(LEX_MOJO_PERL_LINE);
				}
			} else if (bufferAtString(buffer, offset, "<%#"))    // block comment
			{
				preparsedTokensList.add(new CustomToken(offset, offset + 2, EMBED_MARKER));
				offset += 2;
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
				addSemicolon = false;

				if (bufferAtString(buffer, offset, "<%=="))
				{
					addSemicolon = true;
					embedTokenSize = 4;
				}
				else if (bufferAtString(buffer, offset, "<%="))
				{
					addSemicolon = true;
					embedTokenSize = 3;
				}

				preparsedTokensList.add(new CustomToken(offset, offset + embedTokenSize, EMBED_MARKER));
				setMojoState(LEX_MOJO_PERL_BLOCK);
			}

			return TEMPLATE_BLOCK_HTML;

		}
		return super.advance();
	}

	@Override
	public boolean isCommentEnd(int currentPosition)
	{
		CharSequence buffer = getBuffer();
		return buffer.charAt(currentPosition) == '\n'
				|| (currentPosition < buffer.length() - 2 && buffer.charAt(currentPosition + 1) == '?' && buffer.charAt(currentPosition + 2) == '>');
	}


}
