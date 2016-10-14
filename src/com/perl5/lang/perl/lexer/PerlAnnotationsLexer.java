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

package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;

import java.io.Reader;

/**
 * Created by hurricup on 20.04.2016.
 */
public class PerlAnnotationsLexer extends PerlAnnotationsLexerGenerated
{


	public PerlAnnotationsLexer(Reader in)
	{
		super(in);
	}

	protected IElementType parseFallback()
	{
		int tokenStart = getTokenStart();
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();
		IElementType targetElementType = IDENTIFIER;

		if (tokenStart < bufferEnd)
		{
			char currentChar = buffer.charAt(tokenStart);
			if (isIdentifierCharacter(currentChar))
			{
				int tokenEnd = getTokenEnd();

				while (tokenEnd < bufferEnd)
				{
					currentChar = buffer.charAt(tokenEnd);

					if (currentChar == ':' && tokenEnd + 1 < bufferEnd && buffer.charAt(tokenEnd + 1) == ':')
					{
						targetElementType = IDENTIFIER;
						tokenEnd++;
					}
					else if (!isIdentifierCharacter(currentChar))
					{
						break;
					}
					tokenEnd++;
				}

				setTokenEnd(tokenEnd);
			}

			if (tokenStart == getBufferStart())
			{
				setTokenEnd(tokenStart + 2); // we can't be here if there is no 2 symbols ahead
				return ANNOTATION_PREFIX;
			}
			else if (tokenStart == getBufferStart() + 2 && targetElementType == IDENTIFIER)
			{
				String tokenText = yytext().toString();
				IElementType mappedTokenType = PerlAnnotations.TOKENS_MAP.get(tokenText);
				if (mappedTokenType != null)
				{
					return mappedTokenType;
				}
			}
			return targetElementType;
		}
		throw new RuntimeException("Can't be");
	}

	protected boolean isIdentifierCharacter(char myChar)
	{
		return myChar == '_' || Character.isLetterOrDigit(myChar);
	}

	@Override
	public int yystate()
	{
		return LEX_PREPARSED;    // annotations should be reparsed completely all the time
	}

}
