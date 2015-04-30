package com.perl5.lang.perl.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

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

	// My JFlex upgrades
	public abstract int getNextTokenStart();
	public abstract void setTokenStart(int position);
	public abstract boolean isLastToken();

	// Lexer state changes (we don't know LEX constants in advance
	public abstract void yybegin_LEX_MULTILINE();
	public abstract void yybegin_YYINITIAL();
	public abstract void yybegin_LEX_MULTILINE_TOKEN();
	public abstract void yybegin_LEX_MULTILINE_WAITING();
	public abstract void yybegin_LEX_EOF();
	public abstract void yybegin_LEX_POD();
	public abstract void yybegin_LEX_QUOTE_LIKE_OPENER();

	public abstract boolean yystate_LEX_MULTILINE_WAITING();

	/**
	 *  Quote-like string procesors
	 **/

	protected boolean allowSharp = true;
	protected char charOpener;
	protected char charCloser;
	protected int stringContentStart;

	public IElementType processQuoteLikeStringOpener()
	{
		allowSharp = true;
		yybegin_LEX_QUOTE_LIKE_OPENER();
		return PERL_KEYWORD;
	}

	public IElementType processQuoteLikeStringSpace()
	{
		allowSharp = false;
		return TokenType.WHITE_SPACE;
	}

	public abstract void yybegin_LEX_QUOTE_LIKE_CHARS();

	public IElementType processQuoteLikeQuote()
	{
		charOpener = yytext().charAt(0);

		if( charOpener == '#' && !allowSharp )
		{
			yypushback(1);
			yybegin_YYINITIAL();
			return null;
		}
		else if( charOpener == '<' )
			charCloser = '>';
		else if( charOpener == '{' )
			charCloser = '}';
		else if( charOpener == '(' )
			charCloser = ')';
		else if( charOpener == '[' )
			charCloser = ']';
		else
			charCloser = charOpener;

		yybegin_LEX_QUOTE_LIKE_CHARS();
		stringContentStart = getTokenStart() + 1;

		return PERL_QUOTE;
	}


	protected boolean isEscaped = false;
	public abstract void yybegin_LEX_QUOTE_LIKE_CLOSER();

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
		} else // shouldn't happen
		{
			throw new Error("Could not match opening multiline marker: " + openToken);
		}
		yybegin_LEX_MULTILINE_WAITING();

		IElementType markerType = PERL_MULTILINE_MARKER;

		if( multilineMarker.equals("HTML"))
			markerType = PERL_MULTILINE_MARKER_HTML;
		else if( multilineMarker.equals("XML"))
			markerType = PERL_MULTILINE_MARKER_XML;
		else if( multilineMarker.equals("XHTML"))
			markerType = PERL_MULTILINE_MARKER_XHTML;

		return markerType;
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
