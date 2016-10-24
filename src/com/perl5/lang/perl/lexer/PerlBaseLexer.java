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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes;
import com.perl5.lang.perl.parser.moose.MooseElementTypes;
import com.perl5.lang.perl.parser.perlswitch.PerlSwitchElementTypes;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import static com.perl5.lang.perl.lexer.PerlLexerGenerated.*;

/**
 * Created by hurricup on 10.08.2015.
 */
public abstract class PerlBaseLexer extends PerlProtoLexer
		implements PerlElementTypes,
		PerlSwitchElementTypes,
		ClassAccessorElementTypes,
		MojoliciousElementTypes,
		MooseElementTypes
{

	private static final List<IElementType> DQ_TOKENS = Arrays.asList(QUOTE_DOUBLE_OPEN, LP_STRING_QQ, QUOTE_DOUBLE_CLOSE);
	private static final List<IElementType> SQ_TOKENS = Arrays.asList(QUOTE_SINGLE_OPEN, STRING_CONTENT, QUOTE_SINGLE_CLOSE);
	private static final List<IElementType> XQ_TOKENS = Arrays.asList(QUOTE_TICK_OPEN, LP_STRING_XQ, QUOTE_TICK_CLOSE);
	private static final List<IElementType> QW_TOKENS = Arrays.asList(QUOTE_SINGLE_OPEN, LP_STRING_QW, QUOTE_SINGLE_CLOSE);

	private static final String BASIC_IDENTIFIER_PATTERN_TEXT = "[_\\p{L}\\d][_\\p{L}\\d]*"; // something strange in Java with unicode props; Added digits to opener for package Encode::KR::2022_KR;
	private static final String PACKAGE_SEPARATOR_PATTERN_TEXT =
			"(?:" +
					"(?:::)+'?" +
					"|" +
					"(?:::)*'" +
					")";
	public static final Pattern AMBIGUOUS_PACKAGE_PATTERN = Pattern.compile(
			"(" +
					PACKAGE_SEPARATOR_PATTERN_TEXT + "?" +        // optional opening separator,
					"(?:" +
					BASIC_IDENTIFIER_PATTERN_TEXT +
					PACKAGE_SEPARATOR_PATTERN_TEXT +
					")*" +
					")" +
					"(" +
					BASIC_IDENTIFIER_PATTERN_TEXT +
					")");

	private static final Map<IElementType, IElementType> SIGILS_TO_TOKENS_MAP = new THashMap<>();
	private static final String SUB_SIGNATURE = "Sub.Signature";

	static
	{
		SIGILS_TO_TOKENS_MAP.put(SIGIL_SCALAR, SCALAR_NAME);
		SIGILS_TO_TOKENS_MAP.put(SIGIL_SCALAR_INDEX, SCALAR_NAME);
		SIGILS_TO_TOKENS_MAP.put(SIGIL_ARRAY, ARRAY_NAME);
		SIGILS_TO_TOKENS_MAP.put(SIGIL_HASH, HASH_NAME);
		SIGILS_TO_TOKENS_MAP.put(SIGIL_CODE, CODE_NAME);
		SIGILS_TO_TOKENS_MAP.put(SIGIL_GLOB, GLOB_NAME);
	}

	// last captured heredoc marker
	protected final Stack<PerlHeredocQueueElement> heredocQueue = new Stack<>();
	protected final PerlBracesStack myBracesStack = new PerlBracesStack();
	protected final PerlBracesStack myBracketsStack = new PerlBracesStack();
	protected final PerlBracesStack myParensStack = new PerlBracesStack();
	private IElementType myCurrentSigilToken;
	private IElementType myNonLabelTokenType;
	private int myNonLabelState;

	/**
	 * Choosing closing character by opening one
	 *
	 * @param charOpener - char with which sequence started
	 * @return - ending char
	 */
	public static char getQuoteCloseChar(char charOpener)
	{
		if (charOpener == '<')
		{
			return '>';
		}
		else if (charOpener == '{')
		{
			return '}';
		}
		else if (charOpener == '(')
		{
			return ')';
		}
		else if (charOpener == '[')
		{
			return ']';
		}
		else
		{
			return charOpener;
		}
	}

	/**
	 * We've met any sigil
	 */
	protected IElementType startUnbracedVariable(@NotNull IElementType sigilToken)
	{
		return startUnbracedVariable(getRealLexicalState(), sigilToken);
	}

	/**
	 * We've met any sigil
	 */
	protected IElementType startUnbracedVariable(int state, @NotNull IElementType sigilToken)
	{
		myCurrentSigilToken = sigilToken;
		yybegin(state);
		pushStateAndBegin(PerlLexer.VARIABLE_UNBRACED);
		return sigilToken;
	}

	/**
	 * We've met one of the $ / [${]
	 */
	protected IElementType processUnbracedScalarSigil()
	{
		myCurrentSigilToken = SIGIL_SCALAR;
		return SIGIL_SCALAR;
	}

	protected IElementType startBracedVariable()
	{
		myBracesStack.push(0);
		return getLeftBrace(LEFT_BRACE, VARIABLE_BRACED);
	}

	protected IElementType startBracedBlock(int afterState)
	{
		myBracesStack.push(0);
		yybegin(afterState);
		pushState();
		return getLeftBrace(PerlLexer.YYINITIAL);
	}

	protected IElementType getLeftBraceCode(int newState)
	{
		return getLeftBrace(LEFT_BRACE_CODE, newState);
	}

	protected IElementType getLeftBrace(int newState)
	{
		return getLeftBrace(LEFT_BRACE, newState);
	}

	private IElementType getLeftBrace(IElementType braceType, int newState)
	{
		if (!myBracesStack.isEmpty())
		{
			myBracesStack.incLast();
		}
		yybegin(newState);
		return braceType;
	}

	@SuppressWarnings("Duplicates")
	protected IElementType getRightBrace(int afterState)
	{
		if (!myBracesStack.isEmpty())
		{
			if (myBracesStack.decLast() == 0)
			{
				myBracesStack.pop();
				popState();
				return RIGHT_BRACE;
			}
		}
		yybegin(afterState);
		return RIGHT_BRACE;
	}

	protected IElementType startBracketedBlock(int afterState)
	{
		myBracketsStack.push(0);
		yybegin(afterState);
		pushState();
		return getLeftBracket(PerlLexer.YYINITIAL);
	}

	protected IElementType getLeftBracket(int newState)
	{
		if (!myBracketsStack.isEmpty())
		{
			myBracketsStack.incLast();
		}
		yybegin(newState);
		return LEFT_BRACKET;
	}

	@SuppressWarnings("Duplicates")
	protected IElementType getRightBracket(int afterState)
	{
		if (!myBracketsStack.isEmpty())
		{
			if (myBracketsStack.decLast() == 0)
			{
				myBracketsStack.pop();
				popState();
				return RIGHT_BRACKET;
			}
		}
		yybegin(afterState);
		return RIGHT_BRACKET;
	}

	protected IElementType startSubSignatureBlock()
	{
		return startParethesizedBlock(PerlLexer.SUB_ATTRIBUTES, PerlLexer.YYINITIAL, SUB_SIGNATURE);
	}

	protected IElementType startParethesizedBlock(int afterState)
	{
		return startParethesizedBlock(afterState, PerlLexer.YYINITIAL, null);
	}

	protected IElementType startParethesizedBlock(int afterState, int afterParenState)
	{
		return startParethesizedBlock(afterState, afterParenState, null);
	}

	protected IElementType startParethesizedBlock(int afterState, int afterParenState, Object userData)
	{
		myParensStack.push(0, userData);
		yybegin(afterState);
		pushState();
		return getLeftParen(afterParenState);
	}

	protected IElementType getLeftParen(int newState)
	{
		if (!myParensStack.isEmpty())
		{
			myParensStack.incLast();
		}
		yybegin(newState);
		return LEFT_PAREN;
	}

	@SuppressWarnings("Duplicates")
	protected IElementType getRightParen(int afterState)
	{
		if (!myParensStack.isEmpty())
		{
			if (myParensStack.decLast() == 0)
			{
				myParensStack.pop();
				popState();
				return RIGHT_PAREN;
			}
		}
		yybegin(afterState);
		return RIGHT_PAREN;
	}

	/**
	 * We've met identifier (variable name)
	 */
	@NotNull
	protected IElementType getUnbracedVariableNameToken()
	{
		popState();
		char currentChar;
		if (
				!myParensStack.isEmpty() &&
						yylength() == 1 &&
						StringUtil.containsChar(",=)", currentChar = yytext().charAt(0)) &&
						myParensStack.peek() == 1 &&
						myParensStack.peekAdditional() == SUB_SIGNATURE)
		{
			if (currentChar == ',')
			{
				return COMMA;
			}
			else if (currentChar == '=')
			{
				return OPERATOR_ASSIGN;
			}
			else if (currentChar == ')')
			{
				return getRightParen(PerlLexer.SUB_ATTRIBUTES);
			}
		}
		return SIGILS_TO_TOKENS_MAP.get(myCurrentSigilToken);
	}

	@NotNull
	protected IElementType getBracedVariableNameToken()
	{
		yybegin(PerlLexer.YYINITIAL);
		return SIGILS_TO_TOKENS_MAP.get(myCurrentSigilToken);
	}

	@Override
	protected void resetInternals()
	{
		super.resetInternals();
		myBracesStack.clear();
		myBracketsStack.clear();
		myParensStack.clear();
	}

	protected void checkIfLabel(int newState, IElementType tokenType)
	{
		myNonLabelTokenType = tokenType;
		myNonLabelState = newState;
		yybegin(PerlLexer.LEX_LABEL);
	}

	protected IElementType getLabelToken()
	{
		yypushback(yylength());
		yybegin(PerlLexerGenerated.YYINITIAL);
		return IDENTIFIER;
	}

	protected IElementType getNonLabelToken()
	{
		yypushback(yylength());
		yybegin(myNonLabelState);
		return myNonLabelTokenType;
	}

	/**
	 * Implemented for templating languages, which can seek through the comment token (current one) and adjust end
	 * position with setTokenEnd() method
	 */
	public void adjustCommentToken()
	{
	}

	protected IElementType getEofToken()
	{
		int realLexicalState = getRealLexicalState();
		if (realLexicalState == PerlLexer.LEX_LABEL)
		{
			return getNonLabelToken();
		}
		if (realLexicalState == PerlLexer.ANNOTATION_FALLBACK)
		{
			yybegin(PerlLexer.YYINITIAL);
			return COMMENT_LINE;
		}
		if (realLexicalState == PerlLexer.ANNOTATION_KEY)
		{
			yybegin(PerlLexer.YYINITIAL);
			return COMMENT_LINE;
		}
		return null;
	}

	private List<IElementType> getStringTokens()
	{
		int currentState = getRealLexicalState();

		if (currentState == QUOTE_LIKE_OPENER_Q || currentState == QUOTE_LIKE_OPENER_Q_NOSHARP)
		{
			return SQ_TOKENS;
		}
		if (currentState == QUOTE_LIKE_OPENER_QQ || currentState == QUOTE_LIKE_OPENER_QQ_NOSHARP)
		{
			return DQ_TOKENS;
		}
		if (currentState == QUOTE_LIKE_OPENER_QX || currentState == QUOTE_LIKE_OPENER_QX_NOSHARP)
		{
			return XQ_TOKENS;
		}
		if (currentState == QUOTE_LIKE_OPENER_QW || currentState == QUOTE_LIKE_OPENER_QW_NOSHARP)
		{
			return QW_TOKENS;
		}

		throw new RuntimeException("Unknown lexical state for string token " + currentState);
	}

	/**
	 * Captures string token from current position according to the current lexical state
	 *
	 * @return string token
	 */
	protected IElementType captureString()
	{
		CharSequence buffer = getBuffer();
		int currentPosition = getTokenStart();
		int bufferEnd = getBufferEnd();

		char openQuote = buffer.charAt(currentPosition);
		char closeQuote = getQuoteCloseChar(openQuote);
		boolean quotesDiffer = openQuote != closeQuote;

		boolean isEscaped = false;
		int quotesDepth = 0;    // for using with different quotes

		List<IElementType> stringTokens = getStringTokens();
		pushPreparsedToken(currentPosition++, currentPosition, stringTokens.get(0));

		int contentStart = currentPosition;

		while (currentPosition < bufferEnd)
		{
			char currentChar = buffer.charAt(currentPosition);

			if (!isEscaped && quotesDepth == 0 && currentChar == closeQuote)
			{
				break;
			}

			//noinspection Duplicates
			if (!isEscaped && quotesDiffer)
			{
				if (currentChar == openQuote)
				{
					quotesDepth++;
				}
				else if (currentChar == closeQuote)
				{
					quotesDepth--;
				}
			}

			isEscaped = !isEscaped && currentChar == '\\';

			currentPosition++;
		}

		if (currentPosition > contentStart)
		{
			pushPreparsedToken(contentStart, currentPosition, stringTokens.get(1));
		}

		if (currentPosition < bufferEnd)    // got close quote
		{
			pushPreparsedToken(currentPosition++, currentPosition, stringTokens.get(2));
		}
		popState();
		return getPreParsedToken();
	}


	/**
	 * Changes current state to nosharp one if necessary
	 */
	protected void setNoSharpState()
	{
		int realLexicalState = getRealLexicalState();
		if (realLexicalState == PerlLexer.QUOTE_LIKE_OPENER_Q)
		{
			yybegin(PerlLexer.QUOTE_LIKE_OPENER_Q_NOSHARP);
		}
		else if (realLexicalState == PerlLexer.QUOTE_LIKE_OPENER_QQ)
		{
			yybegin(PerlLexer.QUOTE_LIKE_OPENER_QQ_NOSHARP);
		}
		else if (realLexicalState == PerlLexer.QUOTE_LIKE_OPENER_QX)
		{
			yybegin(PerlLexer.QUOTE_LIKE_OPENER_QX_NOSHARP);
		}
		else if (realLexicalState == PerlLexer.QUOTE_LIKE_OPENER_QW)
		{
			yybegin(PerlLexer.QUOTE_LIKE_OPENER_QW_NOSHARP);
		}
	}


}
