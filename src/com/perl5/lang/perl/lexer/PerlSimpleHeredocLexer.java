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

import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;

import java.io.IOException;

/**
 * Created by hurricup on 10.10.2015.
 */
public class PerlSimpleHeredocLexer extends PerlInterpolatedHeredocLexer
{
	public PerlSimpleHeredocLexer()
	{
		super(null);
	}

	@Override
	public IElementType perlAdvance() throws IOException
	{
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();
		int tokenStart = getTokenEnd();

		if (tokenStart > bufferStart && tokenStart < bufferEnd)
		{
			setTokenStart(tokenStart);

			int currentPosition = tokenStart;

			char currentChar = buffer.charAt(currentPosition);

			if (currentChar == '\n')
			{
				currentPosition++;
			}
			else if (Character.isWhitespace(currentChar))
			{
				while (currentPosition < bufferEnd && (currentChar = buffer.charAt(currentPosition)) != '\n' && Character.isWhitespace(currentChar))
				{
					currentPosition++;
				}
			}
			else
			{
				while (currentPosition < bufferEnd && !Character.isWhitespace(buffer.charAt(currentPosition)))
				{
					currentPosition++;
				}
			}
			setTokenEnd(currentPosition);

			return STRING_CONTENT;
		}
		return super.perlAdvance();
	}

	@Override
	public void captureInterpolatedCode()
	{

	}

}
