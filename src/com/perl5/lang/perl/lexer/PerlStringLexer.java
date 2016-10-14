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
	public int yystate()
	{
		return preparsedTokensList.isEmpty() ? super.yystate() : LEX_PREPARSED_ITEMS;
	}

	@Override
	public IElementType perlAdvance() throws IOException
	{
		int bufferEnd = getBufferEnd();
		boolean wasPreparsed = !preparsedTokensList.isEmpty();
		IElementType tokenType = super.perlAdvance();

		// handling tailing spaces
		int tokenEnd = getTokenEnd();

		if (!wasPreparsed && preparsedTokensList.isEmpty())
		{
			if (tokenType == LEFT_BRACE && (getTokenHistory().getLastSignificantTokenType() == SIGIL_ARRAY || getTokenHistory().getLastSignificantTokenType() == SIGIL_SCALAR))
			{
				captureInterpolatedCode();
			}
			else if (tokenType == OPERATOR_REFERENCE && bufferEnd > tokenEnd)
			{
				pushPreparsedToken(tokenEnd, tokenEnd + 1, STRING_CONTENT);
			}
		}
		return tokenType;
	}

	@Override
	public IElementType parseEscape()
	{
		if (getBufferEnd() > getTokenEnd() && Character.isWhitespace(getBuffer().charAt(getTokenEnd())))
		{
			pushPreparsedToken(getTokenEnd(), getTokenEnd() + 1, STRING_CONTENT);
		}

		return OPERATOR_REFERENCE;
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
				pushPreparsedToken(lexer.getTokenStart(), lexer.getTokenEnd(), tokenType);
			}
		}
	}

	@Override
	public IElementType parseSimpleVariable(IElementType sigilTokenType)
	{
		int tokenStart = getTokenStart();
		CharSequence tokenText = yytext();

		setTokenEnd(++tokenStart);

		pushPreparsedToken(tokenStart++, tokenStart, LEFT_BRACE);
		pushPreparsedToken(tokenStart, tokenStart += tokenText.length() - 3, IDENTIFIER);
		pushPreparsedToken(tokenStart++, tokenStart, RIGHT_BRACE);

		return sigilTokenType;
	}
}
