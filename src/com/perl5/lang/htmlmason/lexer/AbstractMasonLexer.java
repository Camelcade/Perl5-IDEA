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

package com.perl5.lang.htmlmason.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.psi.TokenType;
import com.perl5.lang.perl.lexer.CustomToken;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 09.03.2016.
 */
public abstract class AbstractMasonLexer extends PerlTemplatingLexer
{
	public AbstractMasonLexer(Project project)
	{
		super(project);
	}

	protected int preparseNewLinesAndSpaces(CharSequence buffer, int bufferEnd, int offset)
	{
		while (offset < bufferEnd)
		{
			char currentChar = buffer.charAt(offset);
			if (currentChar == '\n')
			{
				pushPreparsedToken(offset++, offset, TokenType.NEW_LINE_INDENT);
			}
			else if (Character.isWhitespace(currentChar))
			{
				int tokenStart = offset;
				while (offset < bufferEnd && Character.isWhitespace(buffer.charAt(offset)))
				{
					offset++;
				}
				pushPreparsedToken(tokenStart, offset, TokenType.WHITE_SPACE);
			}
			else
			{
				break;
			}
		}
		return offset;
	}

	protected boolean parseTailSpaces(CharSequence buffer, int tokenStart, int bufferEnd, String endToken)
	{
		CustomToken whiteSpaceToken = getWhiteSpacesToken(buffer, tokenStart, bufferEnd, endToken);
		if (whiteSpaceToken != null)
		{
			setTokenStart(whiteSpaceToken.getTokenStart());
			setTokenEnd(whiteSpaceToken.getTokenEnd());
			return true;
		}
		return false;
	}

	@Nullable
	protected CustomToken getWhiteSpacesToken(CharSequence buffer, int tokenStart, int bufferEnd, String endToken)
	{
		int offset = tokenStart;
		char currentChar;

		while (offset != bufferEnd && (currentChar = buffer.charAt(offset)) != '\n' && Character.isWhitespace(currentChar))
		{
			offset++;
		}

		if (bufferAtString(buffer, offset - 1, endToken)) // got closer after spaces
		{
			offset--;
		}

		if (offset > tokenStart) // got spaces
		{
			return getCustomToken(tokenStart, offset, TokenType.WHITE_SPACE);
		}
		return null;
	}

	protected abstract String getKeywordCallCloser();

	protected void parseCallComponentPath(int offset)
	{
		CharSequence buffer = getBuffer();
		int bufferEnd = getBufferEnd();

		// heading spaces
		while (offset < bufferEnd)
		{
			char currentChar = buffer.charAt(offset);
			if (currentChar == '\n')    // newline
			{
				pushPreparsedToken(offset, ++offset, TokenType.NEW_LINE_INDENT);
			}
			else if (Character.isWhitespace(currentChar)) // whitespaces
			{
				CustomToken whiteSpaceToken = getWhiteSpacesToken(buffer, offset, bufferEnd, getKeywordCallCloser());
				if (whiteSpaceToken != null)
				{
					pushPreparsedToken(whiteSpaceToken);
					offset = whiteSpaceToken.getTokenEnd();
				}
				else // we are at ' &>'
				{
					return;
				}
			}
			else // not newline or space
			{
				break;
			}
		}

		// word
		if (offset < bufferEnd)
		{
			char currentChar = buffer.charAt(offset);
			// this is a bit weak, according to Perl docs:
			// \w        [3]  Match a "word" character (alphanumeric plus "_",
			// plus other connector punctuation chars plus Unicode marks)
			// but regex here would be really heavy
			if (currentChar == '/' || currentChar == '.' || currentChar == '_' || Character.isLetterOrDigit(currentChar)) // bareword path
			{
				int tokenStart = offset;
				int lastNonSpaceOffset = 0;

				while (offset < bufferEnd)
				{
					currentChar = buffer.charAt(offset);

					if (isAtComponentPathEnd(currentChar, offset, bufferEnd, buffer))
					{
						break;
					}

					if (!Character.isWhitespace(currentChar))
					{
						lastNonSpaceOffset = offset;
					}

					offset++;
				}

				if (++lastNonSpaceOffset > tokenStart)
				{
					pushPreparsedToken(tokenStart, lastNonSpaceOffset, STRING_CONTENT);

					if (offset > lastNonSpaceOffset)
					{
						pushPreparsedToken(lastNonSpaceOffset, offset, TokenType.WHITE_SPACE);
					}
				}
			}
		}
	}


	protected boolean isAtComponentPathEnd(char currentChar, int offset, int bufferEnd, CharSequence buffer)
	{
		return currentChar == ',' ||                                                                                    // comma
				currentChar == '=' && offset + 1 < bufferEnd && buffer.charAt(offset + 1) == '>' ||    // arrow comma
				currentChar == ' ' && offset + 2 < bufferEnd && buffer.charAt(offset + 1) == '&' && buffer.charAt(offset + 2) == '>';    // close marker
	}

}
