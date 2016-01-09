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
import java.io.Reader;
import java.util.regex.Matcher;

/**
 * Created by hurricup on 10.08.2015.
 */
public class PerlStringLexer extends PerlStringLexerGenerated
{
	protected PerlLexer PERL_LEXER = null;
	protected Project myProject = null;


	public PerlStringLexer(Project project)
	{
		super((Reader) null);
		myProject = project;
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
//		CharSequence buffer = getBuffer();
		int tokenEnd = getTokenEnd();
//		char currentChar;

//		if (tokenEnd <= getBufferStart() + 1 && bufferEnd > getBufferStart() + 1 && Character.isWhitespace(currentChar = buffer.charAt(tokenEnd)) && currentChar != '\n')
//		{
//			// hack for leading spaces
//			setTokenStart(tokenEnd);
//			while (tokenEnd < bufferEnd && Character.isWhitespace(buffer.charAt(tokenEnd)))
//				tokenEnd++;
//			setTokenEnd(tokenEnd);
//			return STRING_CONTENT;
//		}

		boolean wasPreparsed = preparsedTokensList.size() > 0;
		IElementType tokenType = super.perlAdvance();

		// handling tailing spaces
		tokenEnd = getTokenEnd();
//		if (tokenEnd == getBufferEnd()
//				&& (tokenType == TokenType.WHITE_SPACE ) // || tokenType == TokenType.NEW_LINE_INDENT
//				)
//		if( !wasPreparsed && tokenType == TokenType.WHITE_SPACE )
//		{
//			tokenType = STRING_WHITESPACE;
//		}

		if (!wasPreparsed && preparsedTokensList.isEmpty())
		{
			if (tokenType == LEFT_BRACE && (getTokenHistory().getLastSignificantTokenType() == SIGIL_ARRAY || getTokenHistory().getLastSignificantTokenType() == SIGIL_SCALAR))
			{
				captureInterpolatedCode();
			}
			else if (tokenType == OPERATOR_REFERENCE && bufferEnd > tokenEnd)
			{
				addPreparsedToken(tokenEnd, tokenEnd + 1, STRING_CONTENT);
			}
		}
		return tokenType;
	}

	/**
	 * Splitting ambiguous package to PACKAGE_IDENTIFIER and IDENTIFIER
	 *
	 * @return token type
	 */
	public IElementType parsePackage()
	{
		String tokenText = yytext().toString();

		Matcher m = AMBIGUOUS_PACKAGE_PATTERN.matcher(tokenText);
		if (m.matches())
		{
			String packageIdentifier = m.group(1);

			preparsedTokensList.clear();
			int packageIdentifierEnd = getTokenStart() + packageIdentifier.length();
			CustomToken barewordToken = new CustomToken(packageIdentifierEnd, getTokenEnd(), IDENTIFIER);
			preparsedTokensList.add(barewordToken);
			setTokenEnd(packageIdentifierEnd);

			return parsePackageCanonical();

		}
		else
			throw new RuntimeException("Inappropriate package name " + tokenText);
	}

	@Override
	public IElementType parseEscape()
	{
		if (getBufferEnd() > getTokenEnd() && Character.isWhitespace(getBuffer().charAt(getTokenEnd())))
		{
			addPreparsedToken(getTokenEnd(), getTokenEnd() + 1, STRING_CONTENT);
		}

		return OPERATOR_REFERENCE;
	}

	/**
	 * Parses IDENTIFIER =>
	 * can be string_content => or ->identifier
	 *
	 * @return token type
	 */
	public IElementType parseBarewordMinus()
	{
		IElementType tokenType = null;
		String tokenText = yytext().toString();

		if (Character.isDigit(tokenText.charAt(0)))
		{
			int endOffset = 1;
			while (Character.isDigit(tokenText.charAt(endOffset)))
				endOffset++;

			yypushback(tokenText.length() - endOffset);
			return NUMBER_SIMPLE;
		}

		return IDENTIFIER;
	}

	public PerlLexer getPerlLexer()
	{
		if (PERL_LEXER == null)
		{
			PERL_LEXER = new PerlLexer(myProject);
		}
		return PERL_LEXER;
	}

	public void captureInterpolatedCode() throws IOException
	{
		int seekStart = getTokenEnd();
		int seekEnd = getBufferEnd();
		int currentPos = seekStart;
		CharSequence buffer = getBuffer();

		int braceLevel = 0;
		boolean isEscaped = false;

		while (currentPos < seekEnd)
		{
			char currentChar = buffer.charAt(currentPos);

			if (!isEscaped && braceLevel == 0 && currentChar == '}')
			{
				break;
			}

			if (!isEscaped)
			{
				if (currentChar == '{')
				{
					braceLevel++;
				}
				else if (currentChar == '}')
				{
					braceLevel--;
				}
			}

			isEscaped = !isEscaped && currentChar == '\\';
			currentPos++;
		}


		if (currentPos > seekStart)
		{
			PerlLexer lexer = getPerlLexer();
			lexer.reset(buffer, seekStart, currentPos, 0);
			IElementType tokenType = null;
			while ((tokenType = lexer.advance()) != null)
			{
				addPreparsedToken(lexer.getTokenStart(), lexer.getTokenEnd(), tokenType);
			}
		}
	}

	@Override
	public IElementType parseSimpleVariable(IElementType sigilTokenType)
	{
		int tokenStart = getTokenStart();
		CharSequence tokenText = yytext();

		setTokenEnd(++tokenStart);

		addPreparsedToken(tokenStart++, tokenStart, LEFT_BRACE);
		addPreparsedToken(tokenStart, tokenStart += tokenText.length() - 3, IDENTIFIER);
		addPreparsedToken(tokenStart++, tokenStart, RIGHT_BRACE);

		return sigilTokenType;
	}
}
