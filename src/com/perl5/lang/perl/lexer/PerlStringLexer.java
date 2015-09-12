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
import java.io.Reader;
import java.util.regex.Matcher;

/**
 * Created by hurricup on 10.08.2015.
 */
public class PerlStringLexer extends PerlStringLexerGenerated
{

	public PerlStringLexer()
	{
		super((Reader) null);
	}

	@Override
	public void reset(CharSequence buffer, int start, int end, int initialState)
	{
		super.reset(buffer, start, end, initialState);
		resetInternals();
//		if( end > 0 )
//			System.err.println("Reset buffer to: " + buffer.subSequence(start, end).toString());
	}

	@Override
	public IElementType perlAdvance() throws IOException
	{
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();
		int tokenEnd = getTokenEnd();

		if (tokenEnd == getBufferStart() + 1 && bufferEnd > getBufferStart() + 1 && Character.isWhitespace(buffer.charAt(tokenEnd)))
		{
			// hack for leading spaces
			setTokenStart(tokenEnd);
			while (tokenEnd < bufferEnd && Character.isWhitespace(buffer.charAt(tokenEnd)))
				tokenEnd++;
			setTokenEnd(tokenEnd);
			return STRING_CONTENT;
		}
		return super.perlAdvance();
	}

	/**
	 * Splitting ambiguous package to PACKAGE_IDENTIFIER and IDENTIFIER
	 *
	 * @return token type
	 */
	public IElementType parsePackage()
	{
		String tokenText = yytext().toString();

		Matcher m = AMBIGUOUS_PACKAGE_RE.matcher(tokenText);
		if (m.matches())
		{
			String packageIdentifier = m.group(1);

			preparsedTokensList.clear();
			int packageIdentifierEnd = getTokenStart() + packageIdentifier.length();
			CustomToken barewordToken = new CustomToken(packageIdentifierEnd, getTokenEnd(), IDENTIFIER);
			preparsedTokensList.add(barewordToken);
			setTokenEnd(packageIdentifierEnd);

			return parsePackageCanonical();

		} else
			throw new RuntimeException("Inappropriate package name " + tokenText);
	}

	/**
	 * Parses IDENTIFIER =>
	 * can be string_content => or ->identifier
	 *
	 * @return token type
	 */
	public IElementType parseBarewordMinus()
	{
		return IDENTIFIER;
	}

}
