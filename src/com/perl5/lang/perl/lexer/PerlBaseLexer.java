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
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.Trinity;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.Queue;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.idea.project.PerlNamesCache;
import com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes;
import com.perl5.lang.perl.parser.moose.MooseElementTypes;
import com.perl5.lang.perl.parser.perlswitch.PerlSwitchElementTypes;
import com.perl5.lang.perl.util.PerlPackageUtil;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.perl5.lang.perl.lexer.PerlLexer.*;

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
	// fixme move somewhere
	public static final String STRING_UNDEF = "undef";
	public static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[_\\p{L}][_\\p{L}\\d]*");


	public static final Map<IElementType, String> ALLOWED_REGEXP_MODIFIERS = new THashMap<>();
	public static final String ALLOWED_TR_MODIFIERS = "cdsr";
	public static final Pattern POSIX_CHAR_CLASS_PATTERN = Pattern.compile("\\[\\[:\\^?\\w*:\\]\\]");
	public static final Map<String, IElementType> RESERVED_TOKEN_TYPES = new THashMap<>();
	public static final Map<String, IElementType> CUSTOM_TOKEN_TYPES = new THashMap<>();
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

	private static final Map<IElementType, Trinity<IElementType, IElementType, IElementType>> SIGILS_TO_TOKENS_MAP = new THashMap<>();

	private static final String SUB_SIGNATURE = "Sub.Signature";
	public static TokenSet BARE_REGEX_PREFIX_TOKENSET = TokenSet.EMPTY;
	public static TokenSet RESERVED_TOKENSET;
	public static TokenSet CUSTOM_TOKENSET;

	static
	{
		ALLOWED_REGEXP_MODIFIERS.put(RESERVED_S, "nmsixpodualgcer");
		ALLOWED_REGEXP_MODIFIERS.put(RESERVED_M, "nmsixpodualgc");
		ALLOWED_REGEXP_MODIFIERS.put(RESERVED_QR, "nmsixpodual");
	}

	static
	{
		SIGILS_TO_TOKENS_MAP.put(SIGIL_SCALAR, Trinity.create(LEFT_BRACE_SCALAR, SCALAR_NAME, RIGHT_BRACE_SCALAR));
		SIGILS_TO_TOKENS_MAP.put(SIGIL_SCALAR_INDEX, Trinity.create(LEFT_BRACE_SCALAR, SCALAR_NAME, RIGHT_BRACE_SCALAR));
		SIGILS_TO_TOKENS_MAP.put(SIGIL_ARRAY, Trinity.create(LEFT_BRACE_ARRAY, ARRAY_NAME, RIGHT_BRACE_ARRAY));
		SIGILS_TO_TOKENS_MAP.put(SIGIL_HASH, Trinity.create(LEFT_BRACE_HASH, HASH_NAME, RIGHT_BRACE_HASH));
		SIGILS_TO_TOKENS_MAP.put(SIGIL_CODE, Trinity.create(LEFT_BRACE_CODE, SUB_NAME, RIGHT_BRACE_CODE));
		SIGILS_TO_TOKENS_MAP.put(SIGIL_GLOB, Trinity.create(LEFT_BRACE_GLOB, GLOB_NAME, RIGHT_BRACE_GLOB));
	}

	// last captured heredoc marker
	protected final Queue<PerlHeredocQueueElement> heredocQueue = new Queue<>(5);
	protected final PerlBracesStack myBracesStack = new PerlBracesStack();
	protected final PerlBracesStack myBracketsStack = new PerlBracesStack();
	protected final PerlBracesStack myParensStack = new PerlBracesStack();
	// flags that we are waiting for format on the next line
	protected boolean myFormatWaiting = false;
	// number of sections for the next regexp
	protected int sectionsNumber = 0;
	/**
	 * Regex processor qr{} m{} s{}{}
	 **/
	protected IElementType regexCommand = null;
	private IElementType myCurrentSigilToken;

	@Nullable
	private Project myProject;
	private AtomicNotNullLazyValue<Set<String>> mySubNamesProvider;
	private AtomicNotNullLazyValue<Set<String>> myPackageNamesProvider;
	private Set<String> myLocalPackages = new THashSet<>();

	public static void initReservedTokensMap()
	{
		RESERVED_TOKEN_TYPES.clear();
		// reserved
	}

	public static void initReservedTokensSet()
	{
		RESERVED_TOKENSET = TokenSet.create(RESERVED_TOKEN_TYPES.values().toArray(new IElementType[RESERVED_TOKEN_TYPES.values().size()]));
		CUSTOM_TOKENSET = TokenSet.create(CUSTOM_TOKEN_TYPES.values().toArray(new IElementType[CUSTOM_TOKEN_TYPES.values().size()]));
	}

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

	public PerlBaseLexer withProject(@Nullable Project project)
	{
		myProject = project;
		return this;
	}

	@Override
	public boolean isInitialState()
	{
		return super.isInitialState() &&
				!myFormatWaiting &&
				heredocQueue.isEmpty() &&
				myBracesStack.isEmpty() &&
				myBracketsStack.isEmpty() &&
				myParensStack.isEmpty();
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
		pushStateAndBegin(VARIABLE_UNBRACED);
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
		Trinity<IElementType, IElementType, IElementType> sigilTokens = SIGILS_TO_TOKENS_MAP.get(myCurrentSigilToken);
		assert sigilTokens != null;
		myBracesStack.push(0, sigilTokens.third);
		return getLeftBrace(sigilTokens.first, VARIABLE_BRACED);
	}

	protected IElementType startBracedBlock()
	{
		return startBracedBlockWithState(YYINITIAL);
	}

	protected IElementType startBracedBlock(int returnState)
	{
		yybegin(returnState);
		return startBracedBlockWithState(YYINITIAL);
	}

	/**
	 * Starts a braced block
	 *
	 * @param blockState state to start after brace
	 * @return token type
	 */
	protected IElementType startBracedBlockWithState(int blockState)
	{
		myBracesStack.push(0);
		pushState();
		return getLeftBrace(blockState);
	}

	protected IElementType getLeftBraceCode(int newState)
	{
		return getLeftBrace(LEFT_BRACE_CODE_START, newState);
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
				Object userData = myBracesStack.peekAdditional();
				myBracesStack.pop();
				popState();
				return userData instanceof IElementType ? (IElementType) userData : RIGHT_BRACE;
			}
		}
		yybegin(afterState);
		return RIGHT_BRACE;
	}

	protected IElementType startBracketedBlock()
	{
		return startBracketedBlockWithState(YYINITIAL);
	}

	protected IElementType startBracketedBlock(int returnState)
	{
		yybegin(returnState);
		return startBracketedBlockWithState(YYINITIAL);
	}

	protected IElementType startBracketedBlockWithState(int blockState)
	{
		myBracketsStack.push(0);
		pushState();
		return getLeftBracket(blockState);
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
		return startParethesizedBlock(SUB_ATTRIBUTES, YYINITIAL, SUB_SIGNATURE);
	}

	protected IElementType startParethesizedBlock(int afterState)
	{
		return startParethesizedBlock(afterState, YYINITIAL, null);
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
				return getRightParen(SUB_ATTRIBUTES);
			}
		}
		return getVariableNameTokenBySigil();
	}

	@NotNull
	protected IElementType getBracedVariableNameToken()
	{
		yybegin(YYINITIAL);
		return getVariableNameTokenBySigil();
	}

	private IElementType getVariableNameTokenBySigil()
	{
		IElementType nameToken = SIGILS_TO_TOKENS_MAP.get(myCurrentSigilToken).second;
		if (nameToken != SUB_NAME)
		{
			return nameToken;
		}

		CharSequence tokenText = yytext();
		int tokenLength = tokenText.length();
		if (tokenLength == 1)
		{
			return nameToken;
		}

		if (StringUtil.endsWithChar(tokenText, ':'))
		{
			return PACKAGE;
		}

		int nameIndex = StringUtil.lastIndexOfAny(tokenText, ":'") + 1;
		if (nameIndex == 0)
		{
			return nameToken;
		}

		yypushback(tokenLength - nameIndex);
		pushStateAndBegin(LEX_SUB_NAME);
		return QUALIFYING_PACKAGE;
	}

	@Override
	protected void resetInternals()
	{
		super.resetInternals();
		myBracesStack.clear();
		myBracketsStack.clear();
		myParensStack.clear();
		heredocQueue.clear();

		mySubNamesProvider = new AtomicNotNullLazyValue<Set<String>>()
		{
			@NotNull
			@Override
			protected Set<String> compute()
			{
				assert myProject != null;
				return myProject.getComponent(PerlNamesCache.class).getSubsNamesSet();
			}
		};

		myPackageNamesProvider = new AtomicNotNullLazyValue<Set<String>>()
		{
			@NotNull
			@Override
			protected Set<String> compute()
			{
				assert myProject != null;
				return myProject.getComponent(PerlNamesCache.class).getPackagesNamesSet();
			}
		};
		myLocalPackages.clear();
	}

	protected IElementType getNewLineToken()
	{
		setNoSharpState();
		if (myFormatWaiting)
		{
			myFormatWaiting = false;
			yybegin(CAPTURE_FORMAT);
		}
		else if (!heredocQueue.isEmpty())
		{
			startHeredocCapture();
		}

		return TokenType.NEW_LINE_INDENT;
	}

	/**
	 * Distincts sub_name from qualified sub_name by :
	 *
	 * @return guessed token
	 */
	protected IElementType getIdentifierTokenWithoutIndex()
	{
		CharSequence tokenText = yytext();
		int lastIndex;
		if (StringUtil.endsWithChar(tokenText, ':'))
		{
			return PACKAGE;
		}

		int tokenLength = tokenText.length();
		if ((lastIndex = StringUtil.lastIndexOfAny(tokenText, ":'") + 1) > 0)
		{
			yypushback(tokenLength - lastIndex);
			pushStateAndBegin(LEX_SUB_NAME);
			return QUALIFYING_PACKAGE;
		}
		else
		{
			return SUB_NAME;
		}
	}

	/**
	 * Bareword parser, resolves built-ins and runs additional processings where it's necessary
	 *
	 * @return token type
	 */
	protected IElementType getIdentifierToken()
	{
		String tokenText = yytext().toString();
		IElementType tokenType;

		if ((tokenType = RESERVED_TOKEN_TYPES.get(tokenText)) == null &&
				(tokenType = CUSTOM_TOKEN_TYPES.get(tokenText)) == null
				)
		{
			if (StringUtil.endsWithChar(tokenText, ':'))
			{
				tokenType = PACKAGE;
			}
			else if (myProject != null)
			{

				String canonicalName = PerlPackageUtil.getCanonicalName(tokenText);
				if (!StringUtil.containsChar(canonicalName, ':'))
				{
					if (StringUtil.isCapitalized(canonicalName) && (myPackageNamesProvider.getValue().contains(canonicalName) || myLocalPackages.contains(canonicalName)))
					{
						tokenType = PACKAGE;
					}
					else
					{
						tokenType = SUB_NAME;
					}
				}
				else if (StringUtil.equals(canonicalName, "UNIVERSAL::can"))
				{
					tokenType = QUALIFYING_PACKAGE;
				}
				else if (mySubNamesProvider.getValue().contains(canonicalName))
				{
					tokenType = QUALIFYING_PACKAGE;
				}
				else if (myPackageNamesProvider.getValue().contains(canonicalName) || myLocalPackages.contains(canonicalName))
				{
					tokenType = PACKAGE;
				}
				else
				{
					tokenType = QUALIFYING_PACKAGE;
				}
			}
			else    // fallback for words scanner
			{
				tokenType = IDENTIFIER;
			}
		}

		yybegin(BARE_REGEX_PREFIX_TOKENSET.contains(tokenType) ? YYINITIAL : AFTER_IDENTIFIER);

		if (tokenType == QUALIFYING_PACKAGE)
		{
			int lastIndex = StringUtil.lastIndexOfAny(tokenText, ":'") + 1;
			yypushback(tokenText.length() - lastIndex);
			pushStateAndBegin(LEX_SUB_NAME);
		}

		return tokenType;
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
	 * Parsing tr/y content
	 *
	 * @return first token
	 */
	public IElementType captureTr()
	{
		popState();
		yybegin(AFTER_VALUE);
		CharSequence buffer = getBuffer();
		int currentOffset = getTokenStart();
		int bufferEnd = getBufferEnd();

		// search block
		char openQuote = buffer.charAt(currentOffset);
		char closeQuote = getQuoteCloseChar(openQuote);
		boolean quotesDiffer = openQuote != closeQuote;
		pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_OPEN);

		currentOffset = parseTrBlockContent(currentOffset, openQuote, closeQuote);

		// close quote
		if (currentOffset < bufferEnd)
		{
			pushPreparsedToken(currentOffset++, currentOffset, quotesDiffer ? REGEX_QUOTE_CLOSE : REGEX_QUOTE);
		}

		// between blocks
		if (quotesDiffer)
		{
			currentOffset = lexWhiteSpacesAndComments(currentOffset);
		}

		// second block
		if (currentOffset < bufferEnd)
		{
			if (quotesDiffer)
			{
				openQuote = buffer.charAt(currentOffset);
				closeQuote = getQuoteCloseChar(openQuote);
				pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_OPEN);
			}

			currentOffset = parseTrBlockContent(currentOffset, openQuote, closeQuote);
		}

		// close quote
		if (currentOffset < bufferEnd)
		{
			pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_CLOSE);
		}


		// trans modifiers
		if (currentOffset < bufferEnd)
		{
			int blockStart = currentOffset;
			while (currentOffset < bufferEnd && StringUtil.containsChar(ALLOWED_TR_MODIFIERS, buffer.charAt(currentOffset)))
			{
				currentOffset++;
			}

			if (blockStart < currentOffset)
			{
				pushPreparsedToken(blockStart, currentOffset, REGEX_MODIFIER);
			}
		}

		return getPreParsedToken();
	}

	/**
	 * Parsing tr block content till close quote
	 *
	 * @param currentOffset start offset
	 * @param closeQuote    close quote character
	 * @return next offset
	 */
	private int parseTrBlockContent(int currentOffset, char openQuote, char closeQuote)
	{
		int blockStartOffset = currentOffset;
		CharSequence buffer = getBuffer();
		int bufferEnd = getBufferEnd();
		boolean isEscaped = false;
		boolean isQuoteDiffers = openQuote != closeQuote;
		int quotesLevel = 0;

		while (currentOffset < bufferEnd)
		{
			char currentChar = buffer.charAt(currentOffset);

			if (!isEscaped && quotesLevel == 0 && currentChar == closeQuote)
			{
				if (currentOffset > blockStartOffset)
				{
					pushPreparsedToken(blockStartOffset, currentOffset, STRING_CONTENT);
				}
				break;
			}
			//noinspection Duplicates
			if (isQuoteDiffers && !isEscaped)
			{
				if (currentChar == openQuote)
				{
					quotesLevel++;
				}
				else if (currentChar == closeQuote)
				{
					quotesLevel--;
				}
			}

			isEscaped = (currentChar == '\\' && !isEscaped);
			currentOffset++;
		}

		return currentOffset;
	}

	/**
	 * Lexing empty spaces and comments between regex/tr blocks and adding tokens to the target list
	 *
	 * @param currentOffset start offset
	 * @return new offset
	 */
	protected int lexWhiteSpacesAndComments(int currentOffset)
	{
		CharSequence buffer = getBuffer();
		int bufferEnd = getBufferEnd();
		while (currentOffset < bufferEnd)
		{
			char currentChar = buffer.charAt(currentOffset);

			if (currentChar == '\n')
			{
				// fixme check heredocs ?
				pushPreparsedToken(currentOffset++, currentOffset, TokenType.NEW_LINE_INDENT);
			}
			else if (Character.isWhitespace(currentChar))    // white spaces
			{
				int whiteSpaceStart = currentOffset;
				while (currentOffset < bufferEnd && Character.isWhitespace(currentChar = buffer.charAt(currentOffset)) && currentChar != '\n')
				{
					currentOffset++;
				}
				pushPreparsedToken(whiteSpaceStart, currentOffset, TokenType.WHITE_SPACE);
			}
			else if (currentChar == '#')    // line comment
			{
				int commentStart = currentOffset;
				while (currentOffset < bufferEnd && buffer.charAt(currentOffset) != '\n')
				{
					currentOffset++;
				}
				pushPreparsedToken(getCustomToken(commentStart, currentOffset, COMMENT_LINE));
			}
			else
			{
				break;
			}
		}

		return currentOffset;
	}

	public int getRegexBlockEndOffset(int startOffset, char openingChar, boolean isSecondBlock)
	{
		char closingChar = getQuoteCloseChar(openingChar);
		CharSequence buffer = getBuffer();
		int bufferEnd = getBufferEnd();

		boolean isEscaped = false;
		boolean isCharGroup = false;
		boolean isQuotesDiffers = closingChar != openingChar;

		int braceLevel = 0;
		int parenLevel = 0;
		int delimiterLevel = 0;

		int currentOffset = startOffset;

		while (true)
		{
			if (currentOffset >= bufferEnd)
			{
				break;
			}

			char currentChar = buffer.charAt(currentOffset);

			if (delimiterLevel == 0 && braceLevel == 0 && !isCharGroup && !isEscaped && parenLevel == 0 && closingChar == currentChar)
			{
				return currentOffset;
			}

			if (!isSecondBlock)
			{
				if (!isEscaped && !isCharGroup && currentChar == '[')
				{
					Matcher m = POSIX_CHAR_CLASS_PATTERN.matcher(buffer.subSequence(currentOffset, bufferEnd));
					if (m.lookingAt())
					{
						currentOffset += m.toMatchResult().group(0).length();
						continue;
					}
					else
					{
						isCharGroup = true;
					}
				}
				else if (!isEscaped && isCharGroup && currentChar == ']')
				{
					isCharGroup = false;
				}
			}

			if (!isEscaped && isQuotesDiffers && !isCharGroup)
			{
				if (currentChar == openingChar)
				{
					delimiterLevel++;
				}
				else if (currentChar == closingChar && delimiterLevel > 0)
				{
					delimiterLevel--;
				}
			}

			isEscaped = !isEscaped && closingChar != '\\' && currentChar == '\\';

			currentOffset++;
		}
		return currentOffset;
	}

	/**
	 * Quote-like string procesors
	 **/
	public IElementType processQuoteLikeStringOpener(IElementType tokenType)
	{
		yybegin(AFTER_VALUE);
		pushState();
		if (tokenType == RESERVED_Q)
		{
			yybegin(QUOTE_LIKE_OPENER_Q);
		}
		else if (tokenType == RESERVED_QQ)
		{
			yybegin(QUOTE_LIKE_OPENER_QQ);
		}
		else if (tokenType == RESERVED_QX)
		{
			yybegin(QUOTE_LIKE_OPENER_QX);
		}
		else if (tokenType == RESERVED_QW)
		{
			yybegin(QUOTE_LIKE_OPENER_QW);
		}
		else
		{
			throw new RuntimeException("Unable to switch state by token " + tokenType);
		}
		return tokenType;
	}

	/**
	 * Sets up regex parser
	 */
	public IElementType processRegexOpener(IElementType tokenType)
	{
		regexCommand = tokenType;

		if (regexCommand == RESERVED_S)    // two sections s
		{
			sectionsNumber = 2;
		}
		else                        // one section qr m
		{
			sectionsNumber = 1;
		}

		pushState();
		yybegin(REGEX_OPENER);
		return tokenType;
	}

	public IElementType captureImplicitRegex()
	{
		regexCommand = RESERVED_M;
		sectionsNumber = 1;
		pushState();
		return captureRegex();
	}

	/**
	 * Parses regexp from the current position (opening delimiter) and preserves tokens in preparsedTokensList
	 * REGEX_MODIFIERS = [msixpodualgcer]
	 *
	 * @return opening delimiter type
	 */
	public IElementType captureRegex()
	{
		popState();
		yybegin(AFTER_VALUE);
		CharSequence buffer = getBuffer();
		int currentOffset = getTokenStart();
		int bufferEnd = getBufferEnd();

		char firstBlockOpeningQuote = buffer.charAt(currentOffset);
		pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_OPEN);

		// find block 1
		int firstBlockEndOffset = getRegexBlockEndOffset(currentOffset, firstBlockOpeningQuote, false);
		CustomToken firstBlockToken = null;
		if (firstBlockEndOffset > currentOffset)
		{
			firstBlockToken = new CustomToken(currentOffset, firstBlockEndOffset, LP_REGEX);
			pushPreparsedToken(firstBlockToken);
		}

		currentOffset = firstBlockEndOffset;

		// find block 2
		CustomToken secondBlockOpeningToken = null;
		CustomToken secondBlockToken = null;

		if (currentOffset < bufferEnd)
		{
			if (sectionsNumber == 1)
			{
				pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_CLOSE);
			}
			else // should have second part
			{
				char secondBlockOpeningQuote = firstBlockOpeningQuote;
				if (firstBlockOpeningQuote == getQuoteCloseChar(firstBlockOpeningQuote))
				{
					secondBlockOpeningToken = new CustomToken(currentOffset++, currentOffset, REGEX_QUOTE);
					pushPreparsedToken(secondBlockOpeningToken);
				}
				else
				{
					pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_CLOSE);
					currentOffset = lexWhiteSpacesAndComments(currentOffset);

					if (currentOffset < bufferEnd)
					{
						secondBlockOpeningQuote = buffer.charAt(currentOffset);
						secondBlockOpeningToken = new CustomToken(currentOffset++, currentOffset, REGEX_QUOTE_OPEN);
						pushPreparsedToken(secondBlockOpeningToken);
					}
				}

				if (currentOffset < bufferEnd)
				{
					int secondBlockEndOffset = getRegexBlockEndOffset(currentOffset, secondBlockOpeningQuote, true);

					if (secondBlockEndOffset > currentOffset)
					{
						secondBlockToken = new CustomToken(currentOffset, secondBlockEndOffset, LP_REGEX_REPLACEMENT);
						pushPreparsedToken(secondBlockToken);
						currentOffset = secondBlockEndOffset;
					}
				}

				if (currentOffset < bufferEnd)
				{
					pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_CLOSE);
				}
			}
		}

		// check modifiers for x
		assert regexCommand != null;
		String allowedModifiers = ALLOWED_REGEXP_MODIFIERS.get(regexCommand);

		while (currentOffset < bufferEnd)
		{
			char currentChar = buffer.charAt(currentOffset);
			if (!StringUtil.containsChar(allowedModifiers, currentChar))    // unknown modifier
			{
				break;
			}
			else if (currentChar == 'x')    // mark as extended
			{
				if (firstBlockToken != null)
				{
					firstBlockToken.setTokenType(LP_REGEX_X);
				}
			}
			else if (currentChar == 'e')    // mark as evaluated
			{
				if (secondBlockOpeningToken != null)
				{
					IElementType secondBlockOpeningTokenType = secondBlockOpeningToken.getTokenType();
					if (secondBlockOpeningTokenType == REGEX_QUOTE_OPEN || secondBlockOpeningTokenType == REGEX_QUOTE_OPEN_E)
					{
						secondBlockOpeningToken.setTokenType(REGEX_QUOTE_OPEN_E);
					}
					else if (secondBlockOpeningTokenType == REGEX_QUOTE || secondBlockOpeningTokenType == REGEX_QUOTE_E)
					{
						secondBlockOpeningToken.setTokenType(REGEX_QUOTE_E);
					}
					else
					{
						throw new RuntimeException("Bug, got: " + secondBlockOpeningTokenType);
					}
				}
				if (secondBlockToken != null)
				{
					secondBlockToken.setTokenType(LP_CODE_BLOCK);
				}
			}

			pushPreparsedToken(currentOffset++, currentOffset, REGEX_MODIFIER);
		}

		return getPreParsedToken();
	}

	/**
	 * Checks if there are more here-docs in stack and immediately starts next one
	 */
	protected void closeEmptyMarkerHeredoc()
	{
	}

	protected void startHeredocCapture()
	{
		pushState();
		if (heredocQueue.peekFirst().getMarker().length() > 0)
		{
			yybegin(CAPTURE_HEREDOC);
		}
		else
		{
			yybegin(CAPTURE_HEREDOC_WITH_EMPTY_MARKER);
		}
	}

	protected boolean isCloseMarker()
	{
		return StringUtil.equals(yytext(), heredocQueue.peekFirst().getMarker());
	}

	protected IElementType registerPackage(IElementType tokenType)
	{
		myLocalPackages.add(PerlPackageUtil.getCanonicalPackageName(yytext().toString()));
		return tokenType;
	}

	/**
	 * Changes current state to nosharp one if necessary
	 */
	protected void setNoSharpState()
	{
		int realLexicalState = getRealLexicalState();
		if (realLexicalState == QUOTE_LIKE_OPENER_Q)
		{
			yybegin(QUOTE_LIKE_OPENER_Q_NOSHARP);
		}
		else if (realLexicalState == QUOTE_LIKE_OPENER_QQ)
		{
			yybegin(QUOTE_LIKE_OPENER_QQ_NOSHARP);
		}
		else if (realLexicalState == QUOTE_LIKE_OPENER_QX)
		{
			yybegin(QUOTE_LIKE_OPENER_QX_NOSHARP);
		}
		else if (realLexicalState == QUOTE_LIKE_OPENER_QW)
		{
			yybegin(QUOTE_LIKE_OPENER_QW_NOSHARP);
		}
		else if (realLexicalState == TRANS_OPENER)
		{
			yybegin(TRANS_OPENER_NO_SHARP);
		}
		else if (realLexicalState == REGEX_OPENER)
		{
			yybegin(REGEX_OPENER_NO_SHARP);
		}
	}


}
