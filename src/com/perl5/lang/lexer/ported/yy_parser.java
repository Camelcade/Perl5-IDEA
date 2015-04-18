package com.perl5.lang.lexer.ported;

/**
 * Created by hurricup on 18.04.2015.
	 parser.h
 */
public class yy_parser
{
    /* parser state */

	public yy_parser old_parser; /* previous value of PL_parser */
	public YYSTYPE	    yylval;	/* value of lookahead symbol, set by yylex() */
	public char	    yychar;	/* The lookahead symbol.  */

	/* Number of tokens to shift before error messages enabled.  */
	public int			yyerrstatus;

	public int		    	stack_size;
	public int		    	yylen;	/* length of active reduction */
	public yy_stack_frame	stack;	/* base of stack */
	public yy_stack_frame  ps;	/* current stack frame */

	/* lexer state */
	public int		lex_brackets;	/* square and curly bracket count */
	public int		lex_casemods;	/* casemod count */
	public char[]	lex_brackstack = new char[120]; /* what kind of brackets to pop */
	public char[]	lex_casestack = new char[12];	/* what kind of case mods in effect */
	public int		lex_defer;	/* state after determined token */
	public int		lex_dojoin;	/* doing an array interpolation
									1 = @{...}  2 = ->@ */
	public int		lex_expect;	/* UNUSED */
	public Perl.expectation		expect;		/* how to interpret ambiguous tokens */
	public int		lex_formbrack;	/* bracket count at outer format level */
	public OP		lex_inpat;	/* in pattern $) and $| are special */
	public OP		lex_op;	/* extra info to pass back on op */
	public SV		lex_repl;	/* runtime replacement from s/// */
	public int		lex_inwhat;	/* what kind of quoting are we in */
	public OPCODE	last_lop_op;	/* last named list or unary operator */
	public int		lex_starts;	/* how many interps done on level */
	public SV		lex_stuff;	/* runtime pattern from m// or s/// */
	public int		multi_start;	/* 1st line of multi-line string */
	public int		multi_end;	/* last line of multi-line string */
	public char	multi_open;	/* delimiter of said string */
	public char		multi_close;	/* delimiter of said string */
	public boolean		preambled;
	public boolean 	lex_re_reparsing; /* we're doing G_RE_REPARSING */
	public int			lex_allbrackets;/* (), [], {}, ?: bracket count */
	public SUBLEXINFO	sublex_info;
	public LEXSHARED	lex_shared = new LEXSHARED();

	public char[]		linestr;			// current chunk of src text */ SV in toke.c
	public int	bufptr;	/* carries the cursor (current parsing
				   				position) from one invocation of yylex
				   				to the next
							Pointer in Perl, offset in Java
				   		*/
	public int	oldbufptr;	/* in yylex, beginning of current token, Pointer in Perl, offset in Java */
	public int	oldoldbufptr;	/* in yylex, beginning of previous token, Pointer in Perl, offset in Java */
	public int	bufend; // Pointer in Perl, offset in Java
	public int	linestart;	// beginning of most recently read line, Pointer in Perl, offset in Java

	public char	last_uni;	/* position of last named-unary op */
	public char	last_lop;	/* position of last list operator */
	/* copline is used to pass a specific line number to newSTATEOP.  It
	   is a one-time line number, as newSTATEOP invalidates it (sets it to
	   NOLINE) after using it.  The purpose of this is to report line num-
	   bers in multiline constructs using the number of the first line. */
	public int	copline;
	public int	in_my = 0;		/* we're compiling a "my"/"our" declaration */
	public int	lex_state;	/* next token is determined */
	public int	error_count;	/* how many compile errors so far, max 10 */
	HV			in_my_stash;	/* declared class of this "my" declaration */
//	PerlIO		rsfp;		/* current source file pointer */ 				Not used in Java implementation
//	AV			rsfp_filters;	/* holds chain of active source filters */	Not used in Java implementation
	public int	form_lex_state;	/* remember lex_state when parsing fmt */

	YYSTYPE[]		nextval = new YYSTYPE[5];	/* value of next token, if any */
	public int[]	nexttype = new int[5];	/* type of next token */
	public int		nexttoke;

	COP				saved_curcop;	/* the previous PL_curcop */
	char[]			tokenbuf = new char[256];
	public int 		herelines;	/* number of lines in here-doc */
	public int 		preambling;	/* line # when processing $ENV{PERL5DB} */
	public int		lex_fakeeof;	/* precedence at which to fake EOF */
	public int		lex_flags;
	public int		in_pod;      /* lexer is within a =pod section */
	public int		filtered;    /* source filters in evalbytes */
	public boolean	saw_infix_sigil = false; /* saw & or * or % operator */
/*				end of parser.h     */
}
