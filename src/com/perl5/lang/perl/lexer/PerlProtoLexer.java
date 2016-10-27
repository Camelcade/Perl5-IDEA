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

import com.intellij.lexer.FlexLexer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.IntStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hurricup on 24.03.2016.
 */
public abstract class PerlProtoLexer implements FlexLexer
{
	protected final LinkedList<CustomToken> preparsedTokensList = new LinkedList<>();
	protected final IntStack stateStack = new IntStack();
	protected final PerlTokenHistory myTokenHistory = new PerlTokenHistory();

	public abstract void setTokenStart(int position);

	public abstract void setTokenEnd(int position);

	public abstract CharSequence getBuffer();

	public abstract int getBufferStart();

	public abstract int getBufferEnd();

	public abstract int getNextTokenStart();

	public abstract void yypushback(int number);

	public abstract int yylength();

	public boolean hasPreparsedTokens()
	{
		return !preparsedTokensList.isEmpty();
	}

	/**
	 * Checks internal lexer state aside of flex states
	 *
	 * @return true if it's safe to return YYINITIAL from yystate
	 */
	public boolean isInitialState()
	{
		return preparsedTokensList.isEmpty() && stateStack.empty();
	}

	public IElementType advance() throws IOException
	{
		IElementType tokenType;

		if (!preparsedTokensList.isEmpty())
		{
			tokenType = getPreParsedToken();
		}
		else
		{
			tokenType = perlAdvance();
		}

		if (tokenType != null)
		{
			registerToken(tokenType, yytext());
		}

		return tokenType;
	}

	public abstract IElementType perlAdvance() throws IOException;

	public abstract int getRealLexicalState();

	public abstract CharSequence yytext();


	/**
	 * Reading tokens from parsed queue, setting start and end and returns them one by one
	 *
	 * @return token type or null if queue is empty
	 */
	public IElementType getPreParsedToken()
	{
		return restoreToken(preparsedTokensList.removeFirst());
	}

	private IElementType restoreToken(CustomToken token)
	{
		setTokenStart(token.getTokenStart());
		setTokenEnd(token.getTokenEnd());
		return token.getTokenType();
	}

	protected void pushStateAndBegin(int newState)
	{
		pushState();
		yybegin(newState);
	}

	public void pushState()
	{
		stateStack.push(getRealLexicalState());
	}

	public void popState()
	{
		yybegin(stateStack.pop());
	}

	public void registerToken(IElementType tokenType, CharSequence tokenText)
	{
		getTokenHistory().addToken(tokenType, tokenText);
	}

	public PerlTokenHistory getTokenHistory()
	{
		return myTokenHistory;
	}

	/**
	 * Adds preparsed token to the queue with consistency control
	 *
	 * @param start     token start
	 * @param end       token end
	 * @param tokenType token type
	 */
	protected void pushPreparsedToken(int start, int end, IElementType tokenType)
	{
		pushPreparsedToken(getCustomToken(start, end, tokenType));
	}

	/**
	 * Checks if range contains only whitespace chars and pushes whitespace or passed tokentype
	 *
	 * @param start     start offset
	 * @param end       end offset
	 * @param tokenType tokentype to push if there are non-space chars
	 */
	protected void pushPreparsedSpaceOrToken(int start, int end, IElementType tokenType)
	{
		pushPreparsedToken(start, end, isWhiteSpacesOnly(start, end) ? TokenType.WHITE_SPACE : tokenType);
	}

	/**
	 * Checks if specified range contains only spaces
	 *
	 * @param start start offset
	 * @param end   end offset
	 * @return check result
	 */
	protected boolean isWhiteSpacesOnly(int start, int end)
	{
		assert end <= getBufferEnd();
		assert start >= 0;
		CharSequence buffer = getBuffer();

		while (start < end)
		{
			if (!Character.isWhitespace(buffer.charAt(start)))
			{
				return false;
			}
			start++;
		}

		return true;
	}


	/**
	 * Helper for creating custom token object
	 *
	 * @param start     token start
	 * @param end       token end
	 * @param tokenType token type
	 * @return custom token object
	 */
	protected CustomToken getCustomToken(int start, int end, IElementType tokenType)
	{
		return new CustomToken(start, end, tokenType);
	}

	/**
	 * Adds preparsed token to the queue with consistency control
	 *
	 * @param token token to add
	 */
	protected void pushPreparsedToken(CustomToken token)
	{
		assert preparsedTokensList.isEmpty() ||
				preparsedTokensList.getLast().getTokenEnd() == token.getTokenStart() :
				"Tokens size is " +
						preparsedTokensList.size() +
						" new token start is " +
						token.getTokenStart() +
						(preparsedTokensList.isEmpty() ? "" :
								" last token end is " +
										preparsedTokensList.getLast().getTokenEnd());

		preparsedTokensList.add(token);
	}

	/**
	 * Adds preparsed token to the queue with consistency control
	 *
	 * @param token token to add
	 */
	protected void unshiftPreparsedToken(CustomToken token)
	{
		assert preparsedTokensList.isEmpty() ||
				preparsedTokensList.getFirst().getTokenStart() == token.getTokenEnd() :
				"Tokens size is " +
						preparsedTokensList.size() +
						" new token end is " +
						token.getTokenEnd() +
						(preparsedTokensList.isEmpty() ? "" :
								" first start end is " +
										preparsedTokensList.getFirst().getTokenStart());

		preparsedTokensList.addFirst(token);
	}

	protected void resetInternals()
	{
		getTokenHistory().reset();
		preparsedTokensList.clear();
		stateStack.clear();
	}

	/**
	 * Checks if buffer at current offset contains specific string
	 *
	 * @param buffer  CharSequence buffer
	 * @param offset  offset
	 * @param pattern string to search
	 * @return search result
	 */
	public boolean isBufferAtString(CharSequence buffer, int offset, CharSequence pattern)
	{
		int patternEnd = offset + pattern.length();
		return getBufferEnd() >= patternEnd && StringUtil.equals(buffer.subSequence(offset, patternEnd), pattern);
	}

	/**
	 * Lexes block cut off heading and tailing spaces/newlines and put them in the beginning of preparsed tokens
	 *
	 * @param blockStart                  block start offset
	 * @param blockEnd                    block end offset
	 * @param lastNonspaceCharacterOffset last non space char in the block, should be -1 if no non-whitespace elements found
	 * @param templateElementType         template element type to assign
	 */
	protected void reLexHTMLBLock(int blockStart, int blockEnd, int lastNonspaceCharacterOffset, IElementType templateElementType)
	{
		List<CustomToken> tokens = new ArrayList<>();
		int myOffset = lexSpacesInRange(blockStart, blockEnd, tokens);

		// real template
		if (myOffset <= lastNonspaceCharacterOffset)
		{
			tokens.add(new CustomToken(myOffset, lastNonspaceCharacterOffset + 1, templateElementType));
		}

		if (lastNonspaceCharacterOffset > -1)
		{
			lexSpacesInRange(lastNonspaceCharacterOffset + 1, blockEnd, tokens);
		}

		for (int i = tokens.size() - 1; i >= 0; i--)
		{
			unshiftPreparsedToken(tokens.get(i));
		}
	}

	/**
	 * lex spaces and new lines in the given range until first nonspace char
	 *
	 * @param blockStart start offset
	 * @param blockEnd   end offset
	 * @param tokens     result tokens list
	 * @return offset of the blockEnd or first non-space character
	 */
	protected int lexSpacesInRange(int blockStart, int blockEnd, List<CustomToken> tokens)
	{
		int whiteSpaceTokenStart = -1;
		CharSequence buffer = getBuffer();
		while (blockStart < blockEnd)
		{
			char currentChar = buffer.charAt(blockStart);
			if (currentChar == '\n')
			{
				if (whiteSpaceTokenStart != -1)
				{
					tokens.add(new CustomToken(whiteSpaceTokenStart, blockStart, TokenType.WHITE_SPACE));
					whiteSpaceTokenStart = -1;
				}
				tokens.add(new CustomToken(blockStart, blockStart + 1, TokenType.NEW_LINE_INDENT));
			}
			else if (Character.isWhitespace(currentChar))
			{
				if (whiteSpaceTokenStart == -1)
				{
					whiteSpaceTokenStart = blockStart;
				}
			}
			else
			{
				if (whiteSpaceTokenStart != -1)
				{
					tokens.add(new CustomToken(whiteSpaceTokenStart, blockStart, TokenType.WHITE_SPACE));
					whiteSpaceTokenStart = -1;
				}
				break;
			}
			blockStart++;
		}
		if (whiteSpaceTokenStart != -1)
		{
			tokens.add(new CustomToken(whiteSpaceTokenStart, blockStart, TokenType.WHITE_SPACE));
		}
		return blockStart;
	}
}
