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

package com.perl5.lang.pod.lexer;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hurricup on 22.03.2016.
 */
@SuppressWarnings("ALL")
public class PodLexer extends PodLexerGenerated
{
	private final List<Integer> myOpenedAngles = new LinkedList<Integer>();

	public PodLexer(Reader in)
	{
		super(in);
	}

	@Override
	public void reset(CharSequence buffer, int start, int end, int initialState)
	{
		super.reset(buffer, start, end, initialState);
		if (start == 0 || start > 0 && buffer.charAt(start - 1) == '\n')
		{
			yybegin(LEX_COMMAND_WAITING);
		}
	}

	@Override
	protected void resetInternals()
	{
		super.resetInternals();
		myOpenedAngles.clear();
	}

	@Override
	public IElementType advance() throws IOException
	{
		IElementType result = super.advance();
		if (result == POD_NEWLINE)
		{
			myOpenedAngles.clear();
		}
		return result;
	}

	@Override
	public int yystate()
	{
		return myOpenedAngles.isEmpty() ? super.yystate() : LEX_IN_ANGLES;
	}

	@Override
	protected int getPreparsedLexicalState()
	{
		return LEX_PREPARSED_ITEMS;
	}

	@Override
	public IElementType perlAdvance() throws IOException
	{
		IElementType result = super.perlAdvance();
		int state = getRealLexicalState();

		if (state == LEX_COMMAND_WAITING && result != TokenType.NEW_LINE_INDENT && result != POD_NEWLINE && result != POD_CODE && result != POD_CUT ||
				state == LEX_COMMAND_READY && result != TokenType.NEW_LINE_INDENT && result != TokenType.WHITE_SPACE && result != POD_NEWLINE && result != POD_CODE)
		{
			yybegin(YYINITIAL);
		}

		return result;
	}

	protected IElementType parseFallback()
	{
		int tokenStart = getTokenStart();
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();

		if (tokenStart < bufferEnd)
		{
			char currentChar = buffer.charAt(tokenStart);
			if (isIdentifierCharacter(currentChar))
			{
				int tokenEnd = getTokenEnd();

				while (tokenEnd < bufferEnd && isIdentifierCharacter(buffer.charAt(tokenEnd)) && !isPodTag(buffer, tokenEnd, bufferEnd))
				{
					tokenEnd++;
				}

				setTokenEnd(tokenEnd);
				return POD_IDENTIFIER;
			}
			else
			{
				return POD_SYMBOL;
			}
		}
		throw new RuntimeException("Can't be");
	}

	protected boolean isIdentifierCharacter(char myChar)
	{
		return myChar == '_' || Character.isLetterOrDigit(myChar);
	}

	protected boolean isPodTag(CharSequence buffer, int offset, int bufferEnd)
	{
		if (offset + 1 < bufferEnd)
		{
			return StringUtil.containsChar("IBCLEFSXZ", buffer.charAt(offset)) && buffer.charAt(offset + 1) == '<';
		}
		return false;
	}

	protected IElementType parseExample()
	{
		int offset = getTokenEnd();
		int tokenEnd = offset;
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();

		boolean newLine = false;
		boolean clearLine = false;

		while (offset < bufferEnd)
		{
			char currentChar = buffer.charAt(offset);

			if (currentChar == '\n')
			{
				newLine = true;

				if (!clearLine)
				{
					tokenEnd = offset + 1;
				}
				clearLine = true;
			}
			else
			{
				if (newLine && !Character.isWhitespace(currentChar))
				{
					break;
				}

				newLine = false;
			}

			clearLine = clearLine && Character.isWhitespace(currentChar);

			offset++;
		}

		setTokenEnd(tokenEnd);
		yybegin(LEX_COMMAND_WAITING);

		return POD_CODE;
	}

	@Override
	protected IElementType parseCutToken()
	{
		int tokenEnd = getTokenEnd();
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();

		// this is a hack for joined tags from second psi tree, we may do this in separate lexer, to use it only in MultiPsi docs, but it's ok for now
		if (!(tokenEnd < bufferEnd && buffer.charAt(tokenEnd) == '='))
		{
			yybegin(YYINITIAL);
		}
		return POD_CUT;
	}

	@Override
	protected IElementType parseFormatter(IElementType tokenType)
	{
		yypushback(1);
		checkOpenAngle();
		return tokenType;
	}

	/**
	 * Pre-parses opening angles
	 */
	protected void checkOpenAngle()
	{
		int bufferEnd = getBufferEnd();
		int tokenStart = getTokenEnd();
		int run = tokenStart;
		CharSequence buffer = getBuffer();

		while (run < bufferEnd && buffer.charAt(run) == '<')
		{
			run++;
		}

		if (run > tokenStart) // got something
		{
			if (run == bufferEnd || run == tokenStart + 1 || Character.isWhitespace(buffer.charAt(run)))
			{
				pushPreparsedToken(tokenStart, run, POD_ANGLE_LEFT);
				myOpenedAngles.add(run - tokenStart);
			}
			else
			{
				pushPreparsedToken(tokenStart, tokenStart + 1, POD_ANGLE_LEFT);
				myOpenedAngles.add(1);
			}
		}

	}

	/**
	 * Pre-parses closing angles
	 */
	protected IElementType parseCloseAngle()
	{
		if (myOpenedAngles.isEmpty())
		{
			return POD_SYMBOL;
		}

		int bufferEnd = getBufferEnd();
		int tokenStart = getTokenStart();
		int run = tokenStart;
		CharSequence buffer = getBuffer();

		int lastIndex = myOpenedAngles.size() - 1;
		int anglesNumber = myOpenedAngles.get(lastIndex);

		if (anglesNumber == 1)
		{
			myOpenedAngles.remove(lastIndex);
			return POD_ANGLE_RIGHT;
		}

		int endOffset = tokenStart + anglesNumber;

		if (tokenStart > 0 && Character.isWhitespace(buffer.charAt(tokenStart - 1)))
		{
			while (run < bufferEnd && run < endOffset && buffer.charAt(run) == '>')
			{
				run++;
			}

			if (run == endOffset)
			{
				setTokenEnd(run);
				myOpenedAngles.remove(lastIndex);
				return POD_ANGLE_RIGHT;
			}
		}
		return POD_SYMBOL;
	}
}
