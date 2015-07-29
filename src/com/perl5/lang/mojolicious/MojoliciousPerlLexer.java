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

package com.perl5.lang.mojolicious;

import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlLexer;

import java.io.IOException;

/**
 * Created by hurricup on 21.07.2015.
 */
public class MojoliciousPerlLexer extends PerlLexer
{
	public MojoliciousPerlLexer(Project project)
	{
		super(project);
	}

	int mojoState = LEX_HTML_BLOCK;

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
		int currentState = yystate();
		int currentMojoState = getMojoState();

		if (preparsedTokensList.size() > 0 || bufferEnd == 0 || tokenStart >= bufferEnd)
			return super.advance();
		else
		{
			setTokenEnd(bufferEnd);
			return TEMPLATE_BLOCK_HTML;
		}
/*
		if (preparsedTokensList.size() > 0)
			return super.advance();
		else if( bufferEnd == 0 || tokenStart >= bufferEnd)
			return null;
		else if (currentMojoState == LEX_HTML_BLOCK)
		{
			setTokenStart(tokenStart);
			int offset = tokenStart;

			boolean blockStart = false;

			for (; offset < bufferEnd; offset++)
			{
				if (offset < bufferEnd - 1 && buffer.charAt(offset) == '<' && buffer.charAt(offset + 1) == '%')
				{
					blockStart = true;
					break;
				} else if (offset < bufferEnd && buffer.charAt(offset) == '%')
					break;
			}

			if (offset == bufferEnd)    // end of file
				setState(YYINITIAL);
			else if (blockStart)    // begin of perl block
			{
				if (offset + 2 < bufferEnd)
				{
					char controlCharacter = buffer.charAt(offset + 2);
					int endOffset = offset + 3;
					if (controlCharacter == '#') // block comment
					{

					} else if (controlCharacter == '%') // <%% macro
					{

					} else    // just code with possible control ==
					{
						if (endOffset < bufferEnd && buffer.charAt(endOffset) == '=')
							endOffset++;
						if (endOffset < bufferEnd && buffer.charAt(endOffset) == '=')
							endOffset++;

					}
				}
				else	// just <%<eof>
				{

				}
			} else // begin of perl line
			{

			}

			setTokenEnd(offset);
			return TEMPLATE_BLOCK_HTML;

		} else if (currentMojoState == LEX_MOJO_BLOCK_COMMENT)
		{

		} else if (currentMojoState == LEX_MOJO_LINE_COMMENT)
		{

		} else if (currentMojoState == LEX_MOJO_PERL_BLOCK)
		{

		} else if (currentMojoState == LEX_MOJO_PERL_LINE)
		{

		}
*/

//		return super.advance();
	}

	@Override
	public boolean isCommentEnd(int currentPosition)
	{
		CharSequence buffer = getBuffer();
		return buffer.charAt(currentPosition) == '\n'
				|| (currentPosition < buffer.length() - 2 && buffer.charAt(currentPosition + 1) == '?' && buffer.charAt(currentPosition + 2) == '>');
	}


}
