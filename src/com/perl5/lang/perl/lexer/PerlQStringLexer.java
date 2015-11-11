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

package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;

import java.io.IOException;

/**
 * Created by hurricup on 10.09.2015.
 */
public class PerlQStringLexer extends PerlQQStringLexer
{
	public PerlQStringLexer()
	{
		super(null);
	}

	@Override
	public IElementType perlAdvance() throws IOException
	{
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();
		int tokenStart = getTokenEnd();

		if (tokenStart > getBufferStart() && tokenStart < bufferEnd - 1)
		{
			// fixme not dry with PerlHeredocLexer
			setTokenStart(tokenStart);

			int currentPosition = tokenStart;

			char currentChar = buffer.charAt(currentPosition);

			if (currentChar == '\n')
				setTokenEnd(currentPosition + 1);
			else if (Character.isWhitespace(currentChar))
			{
				do
				{
					currentChar = buffer.charAt(++currentPosition);
				} while (currentPosition < bufferEnd - 1 && currentChar != '\n' && Character.isWhitespace(currentChar));
				setTokenEnd(currentPosition);
			}
			else
			{
				do
				{
					currentChar = buffer.charAt(++currentPosition);
				} while (currentPosition < bufferEnd - 1 && !Character.isWhitespace(currentChar));
				setTokenEnd(currentPosition);
			}

			return STRING_CONTENT;
		}

		return super.perlAdvance();
	}

	@Override
	public void captureInterpolatedCode()
	{

	}

	@Override
	protected IElementType getOpenQuoteToken()
	{
		return QUOTE_SINGLE_OPEN;
	}

	@Override
	protected IElementType getCloseQuoteToken()
	{
		return QUOTE_SINGLE_CLOSE;
	}
}
