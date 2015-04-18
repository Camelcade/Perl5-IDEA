package com.perl5.lang.lexer;

/**
 * Created by hurricup on 18.04.2015.
 */
public interface PerlLexerPortedLexicalStates
{
	/** lexical states */
	public static final int LEX_NORMAL =		10; /* normal code (ie not within "...")     */
	public static final int LEX_INTERPNORMAL =  9; /* code within a string, eg "$foo[$x+1]" */
	public static final int LEX_INTERPCASEMOD =  8; /* expecting a \U, \Q or \E etc          */
	public static final int LEX_INTERPPUSH =	 7; /* starting a new sublex parse level     */
	public static final int LEX_INTERPSTART	= 	 6; /* expecting the start of a $var         */

	/* at end of code, eg "$x" followed by:  */
	public static final int LEX_INTERPEND = 	 5; /* ... eg not one of [, { or ->          */
	public static final int LEX_INTERPENDMAYBE = 4; /* ... eg one of [, { or ->              */

	public static final int LEX_INTERPCONCAT =	 3; /* expecting anything, eg at start of
					string or after \E, $foo, etc       */
	public static final int LEX_INTERPCONST =	 2; /* NOT USED */
	public static final int LEX_FORMLINE =		 1; /* expecting a format line               */
	public static final int LEX_KNOWNEXT =		 0; /* next token known; just return it      */

}
