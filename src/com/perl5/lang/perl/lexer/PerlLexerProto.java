package com.perl5.lang.perl.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 19.04.2015.
 */
public abstract class PerlLexerProto implements FlexLexer, PerlElementTypes
{
	// JFlex generated methods
	public abstract CharSequence yytext();
	public abstract void yypushback(int number);
	public abstract void setState(int newstate);

	// My JFlex upgrades
	public abstract int getNextTokenStart();
	public abstract void setTokenStart(int position);
	public abstract void setTokenEnd(int position);
	public abstract CharSequence getBuffer();
	public abstract char[] getBufferArray();
	public abstract int getBufferEnd();
	public abstract boolean isLastToken();

	// Lexer state changes (we don't know LEX constants in advance
	public abstract void yybegin_LEX_MULTILINE();
	public abstract void yybegin_YYINITIAL();
	public abstract void yybegin_LEX_MULTILINE_TOKEN();
	public abstract void yybegin_LEX_MULTILINE_WAITING();
	public abstract void yybegin_LEX_EOF();
	public abstract void yybegin_LEX_POD();

	public abstract boolean yystate_LEX_MULTILINE_WAITING();

	/**
	 *  States stack
 	 **/
	private final Stack<Integer> stateStack = new Stack<Integer>();

	protected void pushState()
	{
		stateStack.push(yystate());
	}

	protected void popState()
	{
		setState(stateStack.pop());
	}

	/**
	 *  Quote-like, transliteration and regexps common part
	 */
	protected boolean allowSharp = true;
	protected char charOpener;
	protected char charCloser;
	protected int stringContentStart;
	protected boolean isEscaped = false;

	protected int currentSectionNumber = 0;	// needs for regexps replaces and tr
	protected int sectionsNumber = 0; 	// number of sections one or two

	protected final LinkedList<CustomToken> tokensList = new LinkedList<CustomToken>();

	private IElementType restoreToken( CustomToken token)
	{
		setTokenStart(token.getTokenStart());
		setTokenEnd(token.getTokenEnd());
		return token.getTokenType();
	}

	/**
	 * Disallows sharp delimiter on space occurance for quote-like operations
	 * @return whitespace token type
	 */
	public IElementType processOpenerWhiteSpace()
	{
		allowSharp = false;
		return TokenType.WHITE_SPACE;
	}

	/**
	 *  Reading tokens from parsed queue, setting start and end and returns them one by one
	 * @return token type or null if queue is empty
	 */
	public IElementType getParsedToken()
	{
		if(tokensList.size() == 0 )
		{
			popState();
			yypushback(1); // no tokens in this lex state, push back
			return null;
		}
		else
		{
			return restoreToken(tokensList.removeFirst());
		}
	}

	/**
	 *	Regex processor qr{} m{} s{}{}
	 **/
	public abstract void yybegin_LEX_REGEX_OPENER();
	public abstract void yybegin_LEX_REGEX_ITEMS();

	/**
	 * Extended regex flags, special treatment for spaces and comments
	 */
	private boolean isExtended = false;

	/**
	 * Sets up regex parser
	 * @return command keyword
	 */
	public IElementType processRegexOpener()
	{
		allowSharp = true;
		isExtended = false;
		isEscaped = false;

		if( "s".equals(yytext()) )	// two sections s
			sectionsNumber = 2;
		else						// one section qr m
			sectionsNumber = 1;

		currentSectionNumber = 0;
		pushState();
		yybegin_LEX_REGEX_OPENER();
		return PERL_KEYWORD;
	}

	/**
	 *  Parses regexp from the current position (opening delimiter) and preserves tokens in tokensList
	 *  REGEX_MODIFIERS = [msixpodualgcer]
	 */
	public void parseRegex()
	{
		tokensList.clear();
		int currentPosition = getTokenStart();
		int endPosition = getBufferEnd();

		// find block 1

		// find block 2

		// check modifiers for x

		// parse block 1

		// parse block 2

		// parse modifiers

		yypushback(1);
		yybegin_LEX_REGEX_ITEMS();
	}


	/**
	 *	Transliteration processors tr y
	 **/
	public abstract void yybegin_LEX_TRANS_OPENER();
	public abstract void yybegin_LEX_TRANS_CHARS();
	public abstract void yybegin_LEX_TRANS_CLOSER();
	public abstract void yybegin_LEX_TRANS_MODIFIERS();

	public IElementType processTransOpener()
	{
		allowSharp = true;
		isEscaped = false;
		pushState();
		currentSectionNumber = 0;
		yybegin_LEX_TRANS_OPENER();
		return PERL_KEYWORD;
	}

	public IElementType processTransQuote()
	{
		charOpener = yytext().charAt(0);

		if( charOpener == '#' && !allowSharp )
		{
			yypushback(1);
			popState();
			return null;
		}
		else charCloser = RegexBlock.getQuoteCloseChar(charOpener);

		yybegin_LEX_TRANS_CHARS();
		stringContentStart = getTokenStart() + 1;

		return PERL_QUOTE;
	}

	public IElementType processTransChar()
	{
		char currentChar = yytext().charAt(0);

		if( currentChar == charCloser && !isEscaped )
		{
			yypushback(1);
			setTokenStart(stringContentStart);
			yybegin_LEX_TRANS_CLOSER();
			return PERL_STRING_CONTENT;
		}
		else if( isLastToken() )
		{
			setTokenStart(stringContentStart);
			return PERL_STRING_CONTENT;
		}
		else
			isEscaped = ( currentChar == '\\' && !isEscaped );

		return null;
	}

	public IElementType processTransCloser()
	{
		if( currentSectionNumber == 0 ) // first section
		{
			currentSectionNumber++;
			if( charCloser == charOpener ) // next is replacements block
			{
				yybegin_LEX_TRANS_CHARS();
				stringContentStart = getTokenStart() + 1;
			}
			else	// next is new opener, possibly other
			{
				yybegin_LEX_TRANS_OPENER();
			}
		}
		else // last section
		{
			yybegin_LEX_TRANS_MODIFIERS();
		}
		return PERL_QUOTE;
	}



	/**
	 *  Quote-like string procesors
	 **/
	public abstract void yybegin_LEX_QUOTE_LIKE_CLOSER();
	public abstract void yybegin_LEX_QUOTE_LIKE_OPENER();
	public abstract void yybegin_LEX_QUOTE_LIKE_CHARS();

	public IElementType processQuoteLikeStringOpener()
	{
		allowSharp = true;
		isEscaped = false;
		pushState();
		yybegin_LEX_QUOTE_LIKE_OPENER();
		return PERL_KEYWORD;
	}

	public IElementType processQuoteLikeQuote()
	{
		charOpener = yytext().charAt(0);

		if( charOpener == '#' && !allowSharp )
		{
			yypushback(1);
			yybegin_YYINITIAL();
			return null;
		}
		else charCloser = RegexBlock.getQuoteCloseChar(charOpener);

		yybegin_LEX_QUOTE_LIKE_CHARS();
		stringContentStart = getTokenStart() + 1;

		return PERL_QUOTE;
	}

	public IElementType processQuoteLikeChar()
	{
		char currentChar = yytext().charAt(0);

		if( currentChar == charCloser && !isEscaped )
		{
			yypushback(1);
			setTokenStart(stringContentStart);
			yybegin_LEX_QUOTE_LIKE_CLOSER();
			return PERL_STRING_CONTENT;
		}
		else if( isLastToken() )
		{
			setTokenStart(stringContentStart);
			return PERL_STRING_CONTENT;
		}
		else
			isEscaped = ( currentChar == '\\' && !isEscaped );

		{
		}

		return null;
	}

	/**
	 *  Strings handler
	 */
	protected IElementType processStringOpener()
	{
		isEscaped = false;
		charOpener = charCloser = yytext().charAt(0);
		stringContentStart = getTokenStart() + 1;
		pushState();
		yybegin_LEX_QUOTE_LIKE_CHARS();
		return PERL_QUOTE;
	}

	/**
	 *  Quote-like list procesors
	 **/

	public abstract void yybegin_LEX_QUOTE_LIKE_WORDS();
	public abstract void yybegin_LEX_QUOTE_LIKE_LIST_OPENER();
	public abstract void yybegin_LEX_QUOTE_LIKE_LIST_CLOSER();

	public IElementType processQuoteLikeListOpener()
	{
		allowSharp = true;
		pushState();
		yybegin_LEX_QUOTE_LIKE_LIST_OPENER();
		return PERL_KEYWORD;
	}

	public IElementType processQuoteLikeListQuote()
	{
		charOpener = yytext().charAt(0);

		if( charOpener == '#' && !allowSharp )
		{
			yypushback(1);
			yybegin_YYINITIAL();
			return null;
		}
		else charCloser = RegexBlock.getQuoteCloseChar(charOpener);

		yybegin_LEX_QUOTE_LIKE_WORDS();

		return PERL_QUOTE;
	}


	public IElementType processQuoteLikeWord()
	{
		CharSequence currentToken = yytext();

		for( int i = 0; i < currentToken.length(); i++ )
		{
			if( currentToken.charAt(i) == charCloser )
			{
				yypushback(currentToken.length() - i);
				yybegin_LEX_QUOTE_LIKE_LIST_CLOSER();

				return i == 0 ? null: PERL_STRING_CONTENT;
			}
		}
		return PERL_STRING_CONTENT;
	}

	/**
	 *  Multiline part <<'smth'
	 **/

	public IElementType processSemicolon()
	{
		if( !yystate_LEX_MULTILINE_WAITING() )
			yybegin_YYINITIAL();
		return PERL_SEMI;
	}

	public IElementType processNewLine()
	{
		if( yystate_LEX_MULTILINE_WAITING() )
			startMultiLine();
		return TokenType.NEW_LINE_INDENT;
	}

	/**
	 *  Data block related code
	 */
	protected int dataBlockStart = 0;

	protected void processDataOpener()
	{
		dataBlockStart = getTokenStart();
		yybegin_LEX_EOF();
	}

	protected IElementType endDataBlock()
	{
		setTokenStart(dataBlockStart);
		return PERL_COMMENT_BLOCK;
	}


	/**
	 *  Pod block-related code
	 */
	protected int podBlockStart = 0;

	protected void processPodOpener()
	{
		podBlockStart = getTokenStart();
		yybegin_LEX_POD();
	}

	protected IElementType endPodBlock()
	{
		setTokenStart(podBlockStart);
		if( !isLastToken())
			yybegin_YYINITIAL();
		return PERL_POD;
	}

	/** pre-set multiline type, depends on opener **/
	protected IElementType declaredMultiLineType;

	/** stored multiline start position **/
	protected int multiLineStart;

	/** contains marker for multiline end **/
	protected String multilineMarker;

	protected Pattern markerPattern = Pattern.compile("<<\\s*['\"`]?([^\"\'`]+)['\"`]?");

	/**
	 * Invoken on opening token, waiting for a newline
	 */
	protected IElementType processMultilineOpener()
	{
		String openToken = yytext().toString();
		Matcher m = markerPattern.matcher(openToken);
		if (m.matches())
		{
			multilineMarker = m.group(1);
		}

		yybegin_LEX_MULTILINE_WAITING();
		yypushback(openToken.length() - 2);

		return PERL_OPERATOR;
	}

	/**
	 * Starts multiline reading
	 */
	protected void startMultiLine()
	{
		multiLineStart = getNextTokenStart();
		yybegin_LEX_MULTILINE();
	}

	/**
	 * Checks if current token is multiline marker, therefore multiline ended
	 * @return
	 */
	protected boolean isMultilineEnd()
	{
		return multilineMarker.equals(yytext().toString());
	}

	/**
	 * Ends multiline, pushback marker
	 * @return - type of string (all the same)
	 */
	protected IElementType endMultiline()
	{
		if( isMultilineEnd() ) // got marker
		{
			setTokenStart(multiLineStart);
			yypushback(multilineMarker.length());
			yybegin_LEX_MULTILINE_TOKEN();
		}
		else	// got eof without a marker
		{
			setTokenStart(multiLineStart);
			yybegin_YYINITIAL();
		}
		return PERL_STRING_MULTILINE;
	}
}
