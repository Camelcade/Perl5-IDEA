package com.perl5.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import java.io.IOException;

/**
 * Created by hurricup on 18.04.2015.
 * Attempt to port toke.c from perl 5.21.6
 * Cloned from JFlex generated PerlLexer
 */
public class PerlLexerPorted implements FlexLexer, PerlTokenTypes, PerlLexerPortedLexicalStates, PerlLexerPortedKeywords
{
	/* error messages for the codes above */
	private static final String ZZ_ERROR_MSG[] = {
			"Unkown internal scanner error",
			"Error: could not match input",
			"Error: pushback value was too large"
	};

	/* error codes */
	private static final int ZZ_UNKNOWN_ERROR = 0;
	private static final int ZZ_NO_MATCH = 1;
	private static final int ZZ_PUSHBACK_2BIG = 2;
	private static final char[] EMPTY_BUFFER = new char[0];
	private static final int YYEOF = -1;

	private int currentLexState = LEX_NORMAL;
	private int deferredLexState = LEX_NORMAL;

	private CharSequence sequenceBuffer = "";
	private char[] charactersBuffer;

	private int currentPosition;
	private int lastPosition;

//	private int markedPosition;

	private int tokenStart;	// char index of first token's char
	private int tokenEnd;	// char index of first char after token's end

	private static java.io.Reader zzReader = null; // Fake
	public PerlLexerPorted(java.io.Reader in)
	{
		zzReader = in;
	}

	/**
	 * Creates a new scanner.
	 * There is also java.io.Reader version of this constructor.
	 *
	 * @param   in  the java.io.Inputstream to read input from.
	 */
	public PerlLexerPorted(java.io.InputStream in) {
		this(new java.io.InputStreamReader(in));
	}


	@Override
	public void yybegin(int state)
	{
		currentLexState = state;
	}

	@Override
	public int yystate()
	{
		return currentLexState;
	}

	@Override
	public int getTokenStart()
	{
		return tokenStart;
	}

	@Override
	public int getTokenEnd()
	{
		return tokenEnd;
	}

	/**
	 * Returns the text matched by the current regular expression (current token).
	 */
	public final CharSequence yytext() {
		System.err.printf("Requested yytext %u-%u\n", tokenStart, tokenEnd);
		return sequenceBuffer.subSequence(tokenStart, tokenEnd);
	}


	/**
	 * Returns the character at position <tt>pos</tt> from the
	 * matched text.
	 *
	 * It is equivalent to yytext().charAt(pos), but faster
	 *
	 * @param pos the position of the character to fetch.
	 *            A value from 0 to yylength()-1.
	 *
	 * @return the character at position pos
	 */
	public final char yycharat(int pos) {
		System.err.printf("Requested yycahrat %u\n", pos);
		return charactersBuffer != null
				? charactersBuffer[tokenStart+pos]
				: sequenceBuffer.charAt(tokenStart+pos);
	}


	/**
	 * Returns the length of the matched text region.
	 */
	public final int yylength() {
		return tokenEnd + 1 - tokenStart;
	}

	/**
	 * Reports an error that occured while scanning.
	 *
	 * In a wellformed scanner (no or only correct usage of
	 * yypushback(int) and a match-all fallback rule) this method
	 * will only be called with things that "Can't Possibly Happen".
	 * If this method is called, something is seriously wrong
	 * (e.g. a JFlex bug producing a faulty scanner etc.).
	 *
	 * Usual syntax/scanner level error handling should be done
	 * in error fallback rules.
	 *
	 * @param   errorCode  the code of the errormessage to display
	 */
	private void zzScanError(int errorCode) {
		String message;
		try {
			message = ZZ_ERROR_MSG[errorCode];
		}
		catch (ArrayIndexOutOfBoundsException e) {
			message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
		}

		throw new Error(message);
	}

	/**
	 * Pushes the specified amount of characters back into the input stream.
	 *
	 * They will be read again by then next call of the scanning method
	 *
	 * @param number  the number of characters to be read again.
	 *                This number must not be greater than yylength()!
	 */
	public void yypushback(int number)  {
		if ( number > yylength() )
			zzScanError(ZZ_PUSHBACK_2BIG);

		currentPosition -= number;
	}


	/*
		Starting point
	 */
	@Override
	public void reset(CharSequence buffer, int start, int end, int initialState)
	{
		System.err.printf("Reset lexer to %d %d %d\n", start, end, initialState);
		sequenceBuffer = buffer;
		charactersBuffer = com.intellij.util.text.CharArrayUtil.fromSequenceWithoutCopying(buffer);

		currentPosition = tokenStart = tokenEnd = start;
		lastPosition = end;

		yybegin(initialState);

	}

	/**
	 * Resumes scanning until the next regular expression is matched,
	 * the end of input is encountered or an I/O-Error occurs.
	 *
	 * @return      the next token
	 * @exception   java.io.IOException  if any I/O-Error occurs
	 */
	@Override
	public IElementType advance() throws IOException
	{
		System.err.printf("Invoked advance for %d of %d\n", currentPosition, lastPosition);
		if (currentPosition == lastPosition)    // end of file
			return null;

		stateCase();
		charCase();

		tokenStart = currentPosition;
		tokenEnd = currentPosition;
		currentPosition = tokenEnd + 1;
		return PERL_BAD_CHARACTER;
	}

	public void stateCase()
	{
//		switch (currentLexState) {
//			case LEX_NORMAL:
//			case LEX_INTERPNORMAL:
//				break;
//
//    /* when we've already built the next token, just pull it out of the queue */
//			case LEX_KNOWNEXT:
//				PL_nexttoke--;
//				pl_yylval = PL_nextval[PL_nexttoke];
//				if (!PL_nexttoke) {
//					currentLexState = deferredLexState;
//					deferredLexState = LEX_NORMAL;
//				}
//			{
//				I32 next_type;
//				next_type = PL_nexttype[PL_nexttoke];
//				if (next_type & (7<<24)) {
//					if (next_type & (1<<24)) {
//						if (PL_lex_brackets > 100)
//							Renew(PL_lex_brackstack, PL_lex_brackets + 10, char);
//						PL_lex_brackstack[PL_lex_brackets++] =
//								(char) ((next_type >> 16) & 0xff);
//					}
//					if (next_type & (2<<24))
//						PL_lex_allbrackets++;
//					if (next_type & (4<<24))
//						PL_lex_allbrackets--;
//					next_type &= 0xffff;
//				}
//				return REPORT(next_type == 'p' ? pending_ident() : next_type);
//			}
//
//    /* interpolated case modifiers like \L \U, including \Q and \E.
//       when we get here, PL_bufptr is at the \
//    */
//			case LEX_INTERPCASEMOD:
//				#ifdef DEBUGGING
//				if (PL_bufptr != PL_bufend && *PL_bufptr != '\\')
//				Perl_croak(aTHX_
//						"panic: INTERPCASEMOD bufptr=%p, bufend=%p, *bufptr=%u",
//						PL_bufptr, PL_bufend, *PL_bufptr);
//				#endif
//	/* handle \E or end of string */
//				if (PL_bufptr == PL_bufend || PL_bufptr[1] == 'E') {
//	    /* if at a \E */
//					if (PL_lex_casemods) {
//						const char oldmod = PL_lex_casestack[--PL_lex_casemods];
//						PL_lex_casestack[PL_lex_casemods] = '\0';
//
//						if (PL_bufptr != PL_bufend
//								&& (oldmod == 'L' || oldmod == 'U' || oldmod == 'Q'
//								|| oldmod == 'F')) {
//							PL_bufptr += 2;
//							currentLexState = LEX_INTERPCONCAT;
//						}
//						PL_lex_allbrackets--;
//						return REPORT(')');
//					}
//					else if ( PL_bufptr != PL_bufend && PL_bufptr[1] == 'E' ) {
//               /* Got an unpaired \E */
//						Perl_ck_warner(aTHX_ packWARN(WARN_MISC),
//								"Useless use of \\E");
//					}
//					if (PL_bufptr != PL_bufend)
//						PL_bufptr += 2;
//					currentLexState = LEX_INTERPCONCAT;
//					return yylex();
//				}
//				else {
//					DEBUG_T({ PerlIO_printf(Perl_debug_log,
//							"### Saw case modifier\n"); });
//					s = PL_bufptr + 1;
//					if (s[1] == '\\' && s[2] == 'E') {
//						PL_bufptr = s + 3;
//						currentLexState = LEX_INTERPCONCAT;
//						return yylex();
//					}
//					else {
//						I32 tmp;
//						if (strnEQ(s, "L\\u", 3) || strnEQ(s, "U\\l", 3))
//							tmp = *s, *s = s[2], s[2] = (char)tmp;	/* misordered... */
//						if ((*s == 'L' || *s == 'U' || *s == 'F') &&
//						(strchr(PL_lex_casestack, 'L')
//								|| strchr(PL_lex_casestack, 'U')
//								|| strchr(PL_lex_casestack, 'F'))) {
//							PL_lex_casestack[--PL_lex_casemods] = '\0';
//							PL_lex_allbrackets--;
//							return REPORT(')');
//						}
//						if (PL_lex_casemods > 10)
//							Renew(PL_lex_casestack, PL_lex_casemods + 2, char);
//						PL_lex_casestack[PL_lex_casemods++] = *s;
//						PL_lex_casestack[PL_lex_casemods] = '\0';
//						currentLexState = LEX_INTERPCONCAT;
//						NEXTVAL_NEXTTOKE.ival = 0;
//						force_next((2<<24)|'(');
//						if (*s == 'l')
//						NEXTVAL_NEXTTOKE.ival = OP_LCFIRST;
//						else if (*s == 'u')
//						NEXTVAL_NEXTTOKE.ival = OP_UCFIRST;
//						else if (*s == 'L')
//						NEXTVAL_NEXTTOKE.ival = OP_LC;
//						else if (*s == 'U')
//						NEXTVAL_NEXTTOKE.ival = OP_UC;
//						else if (*s == 'Q')
//						NEXTVAL_NEXTTOKE.ival = OP_QUOTEMETA;
//						else if (*s == 'F')
//						NEXTVAL_NEXTTOKE.ival = OP_FC;
//						else
//						Perl_croak(aTHX_ "panic: yylex, *s=%u", *s);
//						PL_bufptr = s + 1;
//					}
//					force_next(FUNC);
//					if (PL_lex_starts) {
//						s = PL_bufptr;
//						PL_lex_starts = 0;
//		/* commas only at base level: /$a\Ub$c/ => ($a,uc(b.$c)) */
//						if (PL_lex_casemods == 1 && PL_lex_inpat)
//							TOKEN(',');
//						else
//							AopNOASSIGN(OP_CONCAT);
//					}
//					else
//						return yylex();
//				}
//
//			case LEX_INTERPPUSH:
//				return REPORT(sublex_push());
//
//			case LEX_INTERPSTART:
//				if (PL_bufptr == PL_bufend)
//					return REPORT(sublex_done());
//				DEBUG_T({ if(*PL_bufptr != '(') PerlIO_printf(Perl_debug_log,
//					"### Interpolated variable\n"); });
//				PL_expect = XTERM;
//        /* for /@a/, we leave the joining for the regex engine to do
//         * (unless we're within \Q etc) */
//				PL_lex_dojoin = (*PL_bufptr == '@'
//						&& (!PL_lex_inpat || PL_lex_casemods));
//				currentLexState = LEX_INTERPNORMAL;
//				if (PL_lex_dojoin) {
//					NEXTVAL_NEXTTOKE.ival = 0;
//					force_next(',');
//					force_ident("\"", '$');
//					NEXTVAL_NEXTTOKE.ival = 0;
//					force_next('$');
//					NEXTVAL_NEXTTOKE.ival = 0;
//					force_next((2<<24)|'(');
//					NEXTVAL_NEXTTOKE.ival = OP_JOIN;	/* emulate join($", ...) */
//					force_next(FUNC);
//				}
//	/* Convert (?{...}) and friends to 'do {...}' */
//				if (PL_lex_inpat && *PL_bufptr == '(') {
//				PL_parser->lex_shared->re_eval_start = PL_bufptr;
//				PL_bufptr += 2;
//				if (*PL_bufptr != '{')
//				PL_bufptr++;
//				PL_expect = XTERMBLOCK;
//				force_next(DO);
//			}
//
//			if (PL_lex_starts++) {
//				s = PL_bufptr;
//	    /* commas only at base level: /$a\Ub$c/ => ($a,uc(b.$c)) */
//				if (!PL_lex_casemods && PL_lex_inpat)
//					TOKEN(',');
//				else
//					AopNOASSIGN(OP_CONCAT);
//			}
//			return yylex();
//
//			case LEX_INTERPENDMAYBE:
//				if (intuit_more(PL_bufptr)) {
//					currentLexState = LEX_INTERPNORMAL;	/* false alarm, more expr */
//					break;
//				}
//	/* FALLTHROUGH */
//
//			case LEX_INTERPEND:
//				if (PL_lex_dojoin) {
//					const U8 dojoin_was = PL_lex_dojoin;
//					PL_lex_dojoin = FALSE;
//					currentLexState = LEX_INTERPCONCAT;
//					PL_lex_allbrackets--;
//					return REPORT(dojoin_was == 1 ? ')' : POSTJOIN);
//				}
//				if (PL_lex_inwhat == OP_SUBST && PL_linestr == PL_lex_repl
//						&& SvEVALED(PL_lex_repl))
//				{
//					if (PL_bufptr != PL_bufend)
//						Perl_croak(aTHX_ "Bad evalled substitution pattern");
//					PL_lex_repl = NULL;
//				}
//	/* Paranoia.  re_eval_start is adjusted when S_scan_heredoc sets
//	   re_eval_str.  If the here-doc body’s length equals the previous
//	   value of re_eval_start, re_eval_start will now be null.  So
//	   check re_eval_str as well. */
//				if (PL_parser->lex_shared->re_eval_start
//						|| PL_parser->lex_shared->re_eval_str) {
//					SV *sv;
//					if (*PL_bufptr != ')')
//					Perl_croak(aTHX_ "Sequence (?{...}) not terminated with ')'");
//					PL_bufptr++;
//	    /* having compiled a (?{..}) expression, return the original
//	     * text too, as a const */
//					if (PL_parser->lex_shared->re_eval_str) {
//						sv = PL_parser->lex_shared->re_eval_str;
//						PL_parser->lex_shared->re_eval_str = NULL;
//						SvCUR_set(sv,
//								PL_bufptr - PL_parser->lex_shared->re_eval_start);
//						SvPV_shrink_to_cur(sv);
//					}
//					else sv = newSVpvn(PL_parser->lex_shared->re_eval_start,
//							PL_bufptr - PL_parser->lex_shared->re_eval_start);
//					NEXTVAL_NEXTTOKE.opval =
//							(OP*)newSVOP(OP_CONST, 0,
//							sv);
//					force_next(THING);
//					PL_parser->lex_shared->re_eval_start = NULL;
//					PL_expect = XTERM;
//					return REPORT(',');
//				}
//
//	/* FALLTHROUGH */
//			case LEX_INTERPCONCAT:
//				#ifdef DEBUGGING
//				if (PL_lex_brackets)
//					Perl_croak(aTHX_ "panic: INTERPCONCAT, lex_brackets=%ld",
//							(long) PL_lex_brackets);
//				#endif
//				if (PL_bufptr == PL_bufend)
//					return REPORT(sublex_done());
//
//	/* m'foo' still needs to be parsed for possible (?{...}) */
//				if (SvIVX(PL_linestr) == '\'' && !PL_lex_inpat) {
//					SV *sv = newSVsv(PL_linestr);
//					sv = tokeq(sv);
//					pl_yylval.opval = (OP*)newSVOP(OP_CONST, 0, sv);
//					s = PL_bufend;
//				}
//				else {
//					s = scan_const(PL_bufptr);
//					if (*s == '\\')
//					currentLexState = LEX_INTERPCASEMOD;
//					else
//					currentLexState = LEX_INTERPSTART;
//				}
//
//				if (s != PL_bufptr) {
//					NEXTVAL_NEXTTOKE = pl_yylval;
//					PL_expect = XTERM;
//					force_next(THING);
//					if (PL_lex_starts++) {
//		/* commas only at base level: /$a\Ub$c/ => ($a,uc(b.$c)) */
//						if (!PL_lex_casemods && PL_lex_inpat)
//							TOKEN(',');
//						else
//							AopNOASSIGN(OP_CONCAT);
//					}
//					else {
//						PL_bufptr = s;
//						return yylex();
//					}
//				}
//
//				return yylex();
//			case LEX_FORMLINE:
//				s = scan_formline(PL_bufptr);
//				if (!PL_lex_formbrack)
//				{
//					formbrack = 1;
//					goto rightbracket;
//				}
//				PL_bufptr = s;
//				return yylex();
//		}
		
	}

	public void charCase()
	{

//		retry:
//		switch (*s) {
//		default:
//			if (UTF ? isIDFIRST_utf8((U8*)s) : isALNUMC(*s))
//			goto keylookup;
//		{
//			SV *dsv = newSVpvs_flags("", SVs_TEMP);
//			const char *c = UTF ? sv_uni_display(dsv, newSVpvn_flags(s,
//						UTF8SKIP(s),
//						SVs_TEMP | SVf_UTF8),
//				10, UNI_DISPLAY_ISPRINT)
//				: Perl_form(aTHX_ "\\x%02X", (unsigned char)*s);
//			len = UTF ? Perl_utf8_length(aTHX_ (U8 *) PL_linestart, (U8 *) s) : (STRLEN) (s - PL_linestart);
//			if (len > UNRECOGNIZED_PRECEDE_COUNT) {
//				d = UTF ? (char *) utf8_hop((U8 *) s, -UNRECOGNIZED_PRECEDE_COUNT) : s - UNRECOGNIZED_PRECEDE_COUNT;
//			} else {
//				d = PL_linestart;
//			}
//			Perl_croak(aTHX_  "Unrecognized character %s; marked by <-- HERE after %"UTF8f"<-- HERE near column %d", c,
//					UTF8fARG(UTF, (s - d), d),
//					(int) len + 1);
//		}
//		case 4:
//		case 26:
//			goto fake_eof;			// emulate EOF on ^D or ^Z
//		case 0:
//			if (!PL_rsfp && (!PL_parser->filtered || s+1 < PL_bufend)) {
//				PL_last_uni = 0;
//				PL_last_lop = 0;
//				if (PL_lex_brackets &&
//						PL_lex_brackstack[PL_lex_brackets-1] != XFAKEEOF) {
//					yyerror((const char *)
//					(PL_lex_formbrack
//							? "Format not terminated"
//							: "Missing right curly or square bracket"));
//				}
//				DEBUG_T( { PerlIO_printf(Perl_debug_log,
//						"### Tokener got EOF\n");
//				} );
//				TOKEN(0);
//			}
//			if (s++ < PL_bufend)
//			goto retry;			/* ignore stray nulls */
//			PL_last_uni = 0;
//			PL_last_lop = 0;
//			if (!PL_in_eval && !PL_preambled) {
//				PL_preambled = TRUE;
//				if (PL_perldb) {
//		/* Generate a string of Perl code to load the debugger.
//		 * If PERL5DB is set, it will return the contents of that,
//		 * otherwise a compile-time require of perl5db.pl.  */
//
//					const char * const pdb = PerlEnv_getenv("PERL5DB");
//
//					if (pdb) {
//						sv_setpv(PL_linestr, pdb);
//						sv_catpvs(PL_linestr,";");
//					} else {
//						SETERRNO(0,SS_NORMAL);
//						sv_setpvs(PL_linestr, "BEGIN { require 'perl5db.pl' };");
//					}
//					PL_parser->preambling = CopLINE(PL_curcop);
//				} else
//					sv_setpvs(PL_linestr,"");
//				if (PL_preambleav) {
//					SV **svp = AvARRAY(PL_preambleav);
//					SV **const end = svp + AvFILLp(PL_preambleav);
//					while(svp <= end) {
//						sv_catsv(PL_linestr, *svp);
//						++svp;
//						sv_catpvs(PL_linestr, ";");
//					}
//					sv_free(MUTABLE_SV(PL_preambleav));
//					PL_preambleav = NULL;
//				}
//				if (PL_minus_E)
//					sv_catpvs(PL_linestr,
//							"use feature ':5." STRINGIFY(PERL_VERSION) "';");
//				if (PL_minus_n || PL_minus_p) {
//					sv_catpvs(PL_linestr, "LINE: while (<>) {"/*}*/);
//					if (PL_minus_l)
//						sv_catpvs(PL_linestr,"chomp;");
//					if (PL_minus_a) {
//						if (PL_minus_F) {
//							if ((*PL_splitstr == '/' || *PL_splitstr == '\''
//									|| *PL_splitstr == '"')
//							&& strchr(PL_splitstr + 1, *PL_splitstr))
//							Perl_sv_catpvf(aTHX_ PL_linestr, "our @F=split(%s);", PL_splitstr);
//							else {
//			    /* "q\0${splitstr}\0" is legal perl. Yes, even NUL
//			       bytes can be used as quoting characters.  :-) */
//								const char *splits = PL_splitstr;
//								sv_catpvs(PL_linestr, "our @F=split(q\0");
//								do {
//				/* Need to \ \s  */
//									if (*splits == '\\')
//									sv_catpvn(PL_linestr, splits, 1);
//									sv_catpvn(PL_linestr, splits, 1);
//								} while (*splits++);
//			    /* This loop will embed the trailing NUL of
//			       PL_linestr as the last thing it does before
//			       terminating.  */
//								sv_catpvs(PL_linestr, ");");
//							}
//						}
//						else
//							sv_catpvs(PL_linestr,"our @F=split(' ');");
//					}
//				}
//				sv_catpvs(PL_linestr, "\n");
//				PL_oldoldbufptr = PL_oldbufptr = s = PL_linestart = SvPVX(PL_linestr);
//				PL_bufend = SvPVX(PL_linestr) + SvCUR(PL_linestr);
//				PL_last_lop = PL_last_uni = NULL;
//				if ((PERLDB_LINE || PERLDB_SAVESRC) && PL_curstash != PL_debstash)
//					update_debugger_info(PL_linestr, NULL, 0);
//				goto retry;
//			}
//			do {
//				fake_eof = 0;
//				bof = PL_rsfp ? TRUE : FALSE;
//				if (0) {
//					fake_eof:
//					fake_eof = LEX_FAKE_EOF;
//				}
//				PL_bufptr = PL_bufend;
//				COPLINE_INC_WITH_HERELINES;
//				if (!lex_next_chunk(fake_eof)) {
//					CopLINE_dec(PL_curcop);
//					s = PL_bufptr;
//					TOKEN(';');	/* not infinite loop because rsfp is NULL now */
//				}
//				CopLINE_dec(PL_curcop);
//				s = PL_bufptr;
//	    /* If it looks like the start of a BOM or raw UTF-16,
//	     * check if it in fact is. */
//				if (bof && PL_rsfp &&
//						(*s == 0 ||
//				*(U8*)s == BOM_UTF8_FIRST_BYTE ||
//				*(U8*)s >= 0xFE ||
//						s[1] == 0)) {
//					Off_t offset = (IV)PerlIO_tell(PL_rsfp);
//					bof = (offset == (Off_t)SvCUR(PL_linestr));
//					#if defined(PERLIO_USING_CRLF) && defined(PERL_TEXTMODE_SCRIPTS)
//		/* offset may include swallowed CR */
//					if (!bof)
//						bof = (offset == (Off_t)SvCUR(PL_linestr)+1);
//					#endif
//					if (bof) {
//						PL_bufend = SvPVX(PL_linestr) + SvCUR(PL_linestr);
//						s = swallow_bom((U8*)s);
//					}
//				}
//				if (PL_parser->in_pod) {
//		/* Incest with pod. */
//					if (*s == '=' && strnEQ(s, "=cut", 4) && !isALPHA(s[4])) {
//						sv_setpvs(PL_linestr, "");
//						PL_oldoldbufptr = PL_oldbufptr = s = PL_linestart = SvPVX(PL_linestr);
//						PL_bufend = SvPVX(PL_linestr) + SvCUR(PL_linestr);
//						PL_last_lop = PL_last_uni = NULL;
//						PL_parser->in_pod = 0;
//					}
//				}
//				if (PL_rsfp || PL_parser->filtered)
//					incline(s);
//			} while (PL_parser->in_pod);
//			PL_oldoldbufptr = PL_oldbufptr = PL_bufptr = PL_linestart = s;
//			PL_bufend = SvPVX(PL_linestr) + SvCUR(PL_linestr);
//			PL_last_lop = PL_last_uni = NULL;
//			if (CopLINE(PL_curcop) == 1) {
//				while (s < PL_bufend && isSPACE(*s))
//				s++;
//				if (*s == ':' && s[1] != ':') /* for csh execing sh scripts */
//				s++;
//				d = NULL;
//				if (!PL_in_eval) {
//					if (*s == '#' && *(s+1) == '!')
//					d = s + 2;
//					#ifdef ALTERNATE_SHEBANG
//					else {
//						static char const as[] = ALTERNATE_SHEBANG;
//						if (*s == as[0] && strnEQ(s, as, sizeof(as) - 1))
//						d = s + (sizeof(as) - 1);
//					}
//					#endif /* ALTERNATE_SHEBANG */
//				}
//				if (d) {
//					char *ipath;
//					char *ipathend;
//
//					while (isSPACE(*d))
//					d++;
//					ipath = d;
//					while (*d && !isSPACE(*d))
//					d++;
//					ipathend = d;
//
//					#ifdef ARG_ZERO_IS_SCRIPT
//					if (ipathend > ipath) {
//		    /*
//		     * HP-UX (at least) sets argv[0] to the script name,
//		     * which makes $^X incorrect.  And Digital UNIX and Linux,
//		     * at least, set argv[0] to the basename of the Perl
//		     * interpreter. So, having found "#!", we'll set it right.
//		     */
//						SV* copfilesv = CopFILESV(PL_curcop);
//						if (copfilesv) {
//							SV * const x =
//									GvSV(gv_fetchpvs("\030", GV_ADD|GV_NOTQUAL,
//											SVt_PV)); /* $^X */
//							assert(SvPOK(x) || SvGMAGICAL(x));
//							if (sv_eq(x, copfilesv)) {
//								sv_setpvn(x, ipath, ipathend - ipath);
//								SvSETMAGIC(x);
//							}
//							else {
//								STRLEN blen;
//								STRLEN llen;
//								const char *bstart = SvPV_const(copfilesv, blen);
//								const char * const lstart = SvPV_const(x, llen);
//								if (llen < blen) {
//									bstart += blen - llen;
//									if (strnEQ(bstart, lstart, llen) &&	bstart[-1] == '/') {
//										sv_setpvn(x, ipath, ipathend - ipath);
//										SvSETMAGIC(x);
//									}
//								}
//							}
//						}
//						else {
//                        /* Anything to do if no copfilesv? */
//						}
//						TAINT_NOT;	/* $^X is always tainted, but that's OK */
//					}
//					#endif /* ARG_ZERO_IS_SCRIPT */
//
//		/*
//		 * Look for options.
//		 */
//							d = instr(s,"perl -");
//					if (!d) {
//						d = instr(s,"perl");
//						#if defined(DOSISH)
//		    /* avoid getting into infinite loops when shebang
//		     * line contains "Perl" rather than "perl" */
//						if (!d) {
//							for (d = ipathend-4; d >= ipath; --d) {
//								if (isALPHA_FOLD_EQ(*d, 'p')
//								&& !ibcmp(d, "perl", 4))
//								{
//									break;
//								}
//							}
//							if (d < ipath)
//								d = NULL;
//						}
//						#endif
//					}
//					#ifdef ALTERNATE_SHEBANG
//		/*
//		 * If the ALTERNATE_SHEBANG on this system starts with a
//		 * character that can be part of a Perl expression, then if
//		 * we see it but not "perl", we're probably looking at the
//		 * start of Perl code, not a request to hand off to some
//		 * other interpreter.  Similarly, if "perl" is there, but
//		 * not in the first 'word' of the line, we assume the line
//		 * contains the start of the Perl program.
//		 */
//					if (d && *s != '#') {
//						const char *c = ipath;
//						while (*c && !strchr("; \t\r\n\f\v#", *c))
//						c++;
//						if (c < d)
//							d = NULL;	/* "perl" not in first word; ignore */
//						else
//						*s = '#';	/* Don't try to parse shebang line */
//					}
//					#endif /* ALTERNATE_SHEBANG */
//					if (!d &&
//					*s == '#' &&
//							ipathend > ipath &&
//							!PL_minus_c &&
//							!instr(s,"indir") &&
//							instr(PL_origargv[0],"perl"))
//					{
//						dVAR;
//						char **newargv;
//
//						*ipathend = '\0';
//						s = ipathend + 1;
//						while (s < PL_bufend && isSPACE(*s))
//						s++;
//						if (s < PL_bufend) {
//							Newx(newargv,PL_origargc+3,char*);
//							newargv[1] = s;
//							while (s < PL_bufend && !isSPACE(*s))
//							s++;
//							*s = '\0';
//							Copy(PL_origargv+1, newargv+2, PL_origargc+1, char*);
//						}
//						else
//							newargv = PL_origargv;
//						newargv[0] = ipath;
//						PERL_FPU_PRE_EXEC
//						PerlProc_execv(ipath, EXEC_ARGV_CAST(newargv));
//						PERL_FPU_POST_EXEC
//						Perl_croak(aTHX_ "Can't exec %s", ipath);
//					}
//					if (d) {
//						while (*d && !isSPACE(*d))
//						d++;
//						while (SPACE_OR_TAB(*d))
//						d++;
//
//						if (*d++ == '-') {
//							const bool switches_done = PL_doswitches;
//							const U32 oldpdb = PL_perldb;
//							const bool oldn = PL_minus_n;
//							const bool oldp = PL_minus_p;
//							const char *d1 = d;
//
//							do {
//								bool baduni = FALSE;
//								if (*d1 == 'C') {
//									const char *d2 = d1 + 1;
//									if (parse_unicode_opts((const char **)&d2)
//									!= PL_unicode)
//									baduni = TRUE;
//								}
//								if (baduni || isALPHA_FOLD_EQ(*d1, 'M')) {
//									const char * const m = d1;
//									while (*d1 && !isSPACE(*d1))
//									d1++;
//									Perl_croak(aTHX_ "Too late for \"-%.*s\" option",
//											(int)(d1 - m), m);
//								}
//								d1 = moreswitches(d1);
//							} while (d1);
//							if (PL_doswitches && !switches_done) {
//								int argc = PL_origargc;
//								char **argv = PL_origargv;
//								do {
//									argc--,argv++;
//								} while (argc && argv[0][0] == '-' && argv[0][1]);
//								init_argv_symbols(argc,argv);
//							}
//							if (((PERLDB_LINE || PERLDB_SAVESRC) && !oldpdb) ||
//									((PL_minus_n || PL_minus_p) && !(oldn || oldp)))
//			      /* if we have already added "LINE: while (<>) {",
//			         we must not do it again */
//							{
//								sv_setpvs(PL_linestr, "");
//								PL_oldoldbufptr = PL_oldbufptr = s = PL_linestart = SvPVX(PL_linestr);
//								PL_bufend = SvPVX(PL_linestr) + SvCUR(PL_linestr);
//								PL_last_lop = PL_last_uni = NULL;
//								PL_preambled = FALSE;
//								if (PERLDB_LINE || PERLDB_SAVESRC)
//									(void)gv_fetchfile(PL_origfilename);
//								goto retry;
//							}
//						}
//					}
//				}
//			}
//			if (PL_lex_formbrack && PL_lex_brackets <= PL_lex_formbrack) {
//				currentLexState = LEX_FORMLINE;
//				NEXTVAL_NEXTTOKE.ival = 0;
//				force_next(FORMRBRACK);
//				TOKEN(';');
//			}
//			goto retry;
//		case '\r':
//			#ifdef PERL_STRICT_CR
//			Perl_warn(aTHX_ "Illegal character \\%03o (carriage return)", '\r');
//			Perl_croak(aTHX_
//					"\t(Maybe you didn't strip carriage returns after a network transfer?)\n");
//			#endif
//		case ' ': case '\t': case '\f': case 013:
//			s++;
//			goto retry;
//		case '#':
//		case '\n':
//			if (currentLexState != LEX_NORMAL ||
//					(PL_in_eval && !PL_rsfp && !PL_parser->filtered)) {
//			const bool in_comment = *s == '#';
//			if (*s == '#' && s == PL_linestart && PL_in_eval
//					&& !PL_rsfp && !PL_parser->filtered) {
//		/* handle eval qq[#line 1 "foo"\n ...] */
//				CopLINE_dec(PL_curcop);
//				incline(s);
//			}
//			d = s;
//			while (d < PL_bufend && *d != '\n')
//			d++;
//			if (d < PL_bufend)
//				d++;
//			else if (d > PL_bufend)
//                /* Found by Ilya: feed random input to Perl. */
//				Perl_croak(aTHX_ "panic: input overflow, %p > %p",
//						d, PL_bufend);
//			s = d;
//			if (in_comment && d == PL_bufend
//					&& currentLexState == LEX_INTERPNORMAL
//					&& PL_lex_inwhat == OP_SUBST && PL_lex_repl == PL_linestr
//					&& SvEVALED(PL_lex_repl) && d[-1] == '}') s--;
//			else
//				incline(s);
//			if (PL_lex_formbrack && PL_lex_brackets <= PL_lex_formbrack) {
//				currentLexState = LEX_FORMLINE;
//				NEXTVAL_NEXTTOKE.ival = 0;
//				force_next(FORMRBRACK);
//				TOKEN(';');
//			}
//		}
//		else {
//			while (s < PL_bufend && *s != '\n')
//			s++;
//			if (s < PL_bufend)
//			{
//				s++;
//				if (s < PL_bufend)
//					incline(s);
//			}
//			else if (s > PL_bufend)
//                /* Found by Ilya: feed random input to Perl. */
//				Perl_croak(aTHX_ "panic: input overflow");
//		}
//		goto retry;
//		case '-':
//			if (s[1] && isALPHA(s[1]) && !isWORDCHAR(s[2])) {
//				I32 ftst = 0;
//				char tmp;
//
//				s++;
//				PL_bufptr = s;
//				tmp = *s++;
//
//				while (s < PL_bufend && SPACE_OR_TAB(*s))
//				s++;
//
//				if (strnEQ(s,"=>",2)) {
//					s = force_word(PL_bufptr,WORD,FALSE,FALSE);
//					DEBUG_T( { printbuf("### Saw unary minus before =>, forcing word %s\n", s); } );
//					OPERATOR('-');		/* unary minus */
//				}
//				switch (tmp) {
//					case 'r': ftst = OP_FTEREAD;	break;
//					case 'w': ftst = OP_FTEWRITE;	break;
//					case 'x': ftst = OP_FTEEXEC;	break;
//					case 'o': ftst = OP_FTEOWNED;	break;
//					case 'R': ftst = OP_FTRREAD;	break;
//					case 'W': ftst = OP_FTRWRITE;	break;
//					case 'X': ftst = OP_FTREXEC;	break;
//					case 'O': ftst = OP_FTROWNED;	break;
//					case 'e': ftst = OP_FTIS;		break;
//					case 'z': ftst = OP_FTZERO;		break;
//					case 's': ftst = OP_FTSIZE;		break;
//					case 'f': ftst = OP_FTFILE;		break;
//					case 'd': ftst = OP_FTDIR;		break;
//					case 'l': ftst = OP_FTLINK;		break;
//					case 'p': ftst = OP_FTPIPE;		break;
//					case 'S': ftst = OP_FTSOCK;		break;
//					case 'u': ftst = OP_FTSUID;		break;
//					case 'g': ftst = OP_FTSGID;		break;
//					case 'k': ftst = OP_FTSVTX;		break;
//					case 'b': ftst = OP_FTBLK;		break;
//					case 'c': ftst = OP_FTCHR;		break;
//					case 't': ftst = OP_FTTTY;		break;
//					case 'T': ftst = OP_FTTEXT;		break;
//					case 'B': ftst = OP_FTBINARY;	break;
//					case 'M': case 'A': case 'C':
//						gv_fetchpvs("\024", GV_ADD|GV_NOTQUAL, SVt_PV);
//						switch (tmp) {
//							case 'M': ftst = OP_FTMTIME;	break;
//							case 'A': ftst = OP_FTATIME;	break;
//							case 'C': ftst = OP_FTCTIME;	break;
//							default:			break;
//						}
//						break;
//					default:
//						break;
//				}
//				if (ftst) {
//					PL_last_uni = PL_oldbufptr;
//					PL_last_lop_op = (OPCODE)ftst;
//					DEBUG_T( { PerlIO_printf(Perl_debug_log,
//							"### Saw file test %c\n", (int)tmp);
//					} );
//					FTST(ftst);
//				}
//				else {
//		/* Assume it was a minus followed by a one-letter named
//		 * subroutine call (or a -bareword), then. */
//					DEBUG_T( { PerlIO_printf(Perl_debug_log,
//							"### '-%c' looked like a file test but was not\n",
//							(int) tmp);
//					} );
//					s = --PL_bufptr;
//				}
//			}
//		{
//			const char tmp = *s++;
//			if (*s == tmp) {
//			s++;
//			if (PL_expect == XOPERATOR)
//				TERM(POSTDEC);
//			else
//				OPERATOR(PREDEC);
//		}
//			else if (*s == '>') {
//			s++;
//			s = skipspace(s);
//			if (FEATURE_POSTDEREF_IS_ENABLED && (
//					((*s == '$' || *s == '&') && s[1] == '*')
//			||(*s == '$' && s[1] == '#' && s[2] == '*')
//			||((*s == '@' || *s == '%') && strchr("*[{", s[1]))
//			||(*s == '*' && (s[1] == '*' || s[1] == '{'))
//			))
//			{
//				Perl_ck_warner_d(aTHX_
//						packWARN(WARN_EXPERIMENTAL__POSTDEREF),
//						"Postfix dereference is experimental"
//				);
//				PL_expect = XPOSTDEREF;
//				TOKEN(ARROW);
//			}
//			if (isIDFIRST_lazy_if(s,UTF)) {
//				s = force_word(s,METHOD,FALSE,TRUE);
//				TOKEN(ARROW);
//			}
//			else if (*s == '$')
//			OPERATOR(ARROW);
//			else
//			TERM(ARROW);
//		}
//			if (PL_expect == XOPERATOR) {
//				if (*s == '=' && !PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_ASSIGN) {
//					s--;
//					TOKEN(0);
//				}
//				Aop(OP_SUBTRACT);
//			}
//			else {
//				if (isSPACE(*s) || !isSPACE(*PL_bufptr))
//				check_uni();
//				OPERATOR('-');		/* unary minus */
//			}
//		}
//
//		case '+':
//		{
//			const char tmp = *s++;
//			if (*s == tmp) {
//			s++;
//			if (PL_expect == XOPERATOR)
//				TERM(POSTINC);
//			else
//				OPERATOR(PREINC);
//		}
//			if (PL_expect == XOPERATOR) {
//				if (*s == '=' && !PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_ASSIGN) {
//					s--;
//					TOKEN(0);
//				}
//				Aop(OP_ADD);
//			}
//			else {
//				if (isSPACE(*s) || !isSPACE(*PL_bufptr))
//				check_uni();
//				OPERATOR('+');
//			}
//		}
//
//		case '*':
//			if (PL_expect == XPOSTDEREF) POSTDEREF('*');
//			if (PL_expect != XOPERATOR) {
//				s = scan_ident(s, PL_tokenbuf, sizeof PL_tokenbuf, TRUE);
//				PL_expect = XOPERATOR;
//				force_ident(PL_tokenbuf, '*');
//				if (!*PL_tokenbuf)
//					PREREF('*');
//				TERM('*');
//			}
//			s++;
//			if (*s == '*') {
//			s++;
//			if (*s == '=' && !PL_lex_allbrackets &&
//					PL_lex_fakeeof >= LEX_FAKEEOF_ASSIGN) {
//				s -= 2;
//				TOKEN(0);
//			}
//			PWop(OP_POW);
//		}
//		if (*s == '=' && !PL_lex_allbrackets &&
//				PL_lex_fakeeof >= LEX_FAKEEOF_ASSIGN) {
//			s--;
//			TOKEN(0);
//		}
//		PL_parser->saw_infix_sigil = 1;
//		Mop(OP_MULTIPLY);
//
//		case '%':
//		{
//			if (PL_expect == XOPERATOR) {
//				if (s[1] == '=' && !PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_ASSIGN)
//					TOKEN(0);
//				++s;
//				PL_parser->saw_infix_sigil = 1;
//				Mop(OP_MODULO);
//			}
//			else if (PL_expect == XPOSTDEREF) POSTDEREF('%');
//			PL_tokenbuf[0] = '%';
//			s = scan_ident(s, PL_tokenbuf + 1,
//					sizeof PL_tokenbuf - 1, FALSE);
//			pl_yylval.ival = 0;
//			if (!PL_tokenbuf[1]) {
//				PREREF('%');
//			}
//			if ((PL_expect != XREF || PL_oldoldbufptr == PL_last_lop) && intuit_more(s)) {
//				if (*s == '[')
//				PL_tokenbuf[0] = '@';
//			}
//			PL_expect = XOPERATOR;
//			force_ident_maybe_lex('%');
//			TERM('%');
//		}
//		case '^':
//			if (!PL_lex_allbrackets && PL_lex_fakeeof >=
//					(s[1] == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_BITWISE))
//				TOKEN(0);
//			s++;
//			BOop(OP_BIT_XOR);
//		case '[':
//			if (PL_lex_brackets > 100)
//				Renew(PL_lex_brackstack, PL_lex_brackets + 10, char);
//			PL_lex_brackstack[PL_lex_brackets++] = 0;
//			PL_lex_allbrackets++;
//		{
//			const char tmp = *s++;
//			OPERATOR(tmp);
//		}
//		case '~':
//			if (s[1] == '~'
//					&& (PL_expect == XOPERATOR || PL_expect == XTERMORDORDOR))
//			{
//				if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE)
//					TOKEN(0);
//				s += 2;
//				Perl_ck_warner_d(aTHX_
//						packWARN(WARN_EXPERIMENTAL__SMARTMATCH),
//						"Smartmatch is experimental");
//				Eop(OP_SMARTMATCH);
//			}
//			s++;
//			OPERATOR('~');
//		case ',':
//			if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_COMMA)
//				TOKEN(0);
//			s++;
//			OPERATOR(',');
//		case ':':
//			if (s[1] == ':') {
//				len = 0;
//				goto just_a_word_zero_gv;
//			}
//			s++;
//		{
//			OP *attrs;
//
//			switch (PL_expect) {
//				case XOPERATOR:
//					if (!PL_in_my || currentLexState != LEX_NORMAL)
//						break;
//					PL_bufptr = s;	/* update in case we back off */
//					if (*s == '=') {
//					Perl_croak(aTHX_
//							"Use of := for an empty attribute list is not allowed");
//				}
//				goto grabattrs;
//				case XATTRBLOCK:
//					PL_expect = XBLOCK;
//					goto grabattrs;
//				case XATTRTERM:
//					PL_expect = XTERMBLOCK;
//					grabattrs:
//					s = skipspace(s);
//					attrs = NULL;
//					while (isIDFIRST_lazy_if(s,UTF)) {
//						I32 tmp;
//						SV *sv;
//						d = scan_word(s, PL_tokenbuf, sizeof PL_tokenbuf, FALSE, &len);
//						if (isLOWER(*s) && (tmp = keyword(PL_tokenbuf, len, 0))) {
//							if (tmp < 0) tmp = -tmp;
//							switch (tmp) {
//								case KEY_or:
//								case KEY_and:
//								case KEY_for:
//								case KEY_foreach:
//								case KEY_unless:
//								case KEY_if:
//								case KEY_while:
//								case KEY_until:
//									goto got_attrs;
//								default:
//									break;
//							}
//						}
//						sv = newSVpvn_flags(s, len, UTF ? SVf_UTF8 : 0);
//						if (*d == '(') {
//							d = scan_str(d,TRUE,TRUE,FALSE,NULL);
//							COPLINE_SET_FROM_MULTI_END;
//							if (!d) {
//			/* MUST advance bufptr here to avoid bogus
//			   "at end of line" context messages from yyerror().
//			 */
//								PL_bufptr = s + len;
//								yyerror("Unterminated attribute parameter in attribute list");
//								if (attrs)
//									op_free(attrs);
//								sv_free(sv);
//								return REPORT(0);	/* EOF indicator */
//							}
//						}
//						if (PL_lex_stuff) {
//							sv_catsv(sv, PL_lex_stuff);
//							attrs = op_append_elem(OP_LIST, attrs,
//									newSVOP(OP_CONST, 0, sv));
//							SvREFCNT_dec(PL_lex_stuff);
//							PL_lex_stuff = NULL;
//						}
//						else {
//							if (len == 6 && strnEQ(SvPVX(sv), "unique", len)) {
//								sv_free(sv);
//								if (PL_in_my == KEY_our) {
//									deprecate(":unique");
//								}
//								else
//									Perl_croak(aTHX_ "The 'unique' attribute may only be applied to 'our' variables");
//							}
//
//		    /* NOTE: any CV attrs applied here need to be part of
//		       the CVf_BUILTIN_ATTRS define in cv.h! */
//							else if (!PL_in_my && len == 6 && strnEQ(SvPVX(sv), "lvalue", len)) {
//								sv_free(sv);
//								CvLVALUE_on(PL_compcv);
//							}
//							else if (!PL_in_my && len == 6 && strnEQ(SvPVX(sv), "locked", len)) {
//								sv_free(sv);
//								deprecate(":locked");
//							}
//							else if (!PL_in_my && len == 6 && strnEQ(SvPVX(sv), "method", len)) {
//								sv_free(sv);
//								CvMETHOD_on(PL_compcv);
//							}
//		    /* After we've set the flags, it could be argued that
//		       we don't need to do the attributes.pm-based setting
//		       process, and shouldn't bother appending recognized
//		       flags.  To experiment with that, uncomment the
//		       following "else".  (Note that's already been
//		       uncommented.  That keeps the above-applied built-in
//		       attributes from being intercepted (and possibly
//		       rejected) by a package's attribute routines, but is
//		       justified by the performance win for the common case
//		       of applying only built-in attributes.) */
//							else
//								attrs = op_append_elem(OP_LIST, attrs,
//										newSVOP(OP_CONST, 0,
//												sv));
//						}
//						s = skipspace(d);
//						if (*s == ':' && s[1] != ':')
//						s = skipspace(s+1);
//						else if (s == d)
//							break;	/* require real whitespace or :'s */
//		/* XXX losing whitespace on sequential attributes here */
//					}
//				{
//					if (*s != ';' && *s != '}' &&
//						!(PL_expect == XOPERATOR
//								? (*s == '=' ||  *s == ')')
//					: (*s == '{' ||  *s == '('))) {
//					const char q = ((*s == '\'') ? '"' : '\'');
//		    /* If here for an expression, and parsed no attrs, back
//		       off. */
//					if (PL_expect == XOPERATOR && !attrs) {
//						s = PL_bufptr;
//						break;
//					}
//		    /* MUST advance bufptr here to avoid bogus "at end of line"
//		       context messages from yyerror().
//		    */
//					PL_bufptr = s;
//					yyerror( (const char *)
//					(*s
//							? Perl_form(aTHX_ "Invalid separator character "
//							"%c%c%c in attribute list", q, *s, q)
//					: "Unterminated attribute list" ) );
//					if (attrs)
//						op_free(attrs);
//					OPERATOR(':');
//				}
//				}
//				got_attrs:
//				if (attrs) {
//					NEXTVAL_NEXTTOKE.opval = attrs;
//					force_next(THING);
//				}
//				TOKEN(COLONATTR);
//			}
//		}
//		if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_CLOSING) {
//			s--;
//			TOKEN(0);
//		}
//		PL_lex_allbrackets--;
//		OPERATOR(':');
//		case '(':
//			s++;
//			if (PL_last_lop == PL_oldoldbufptr || PL_last_uni == PL_oldoldbufptr)
//				PL_oldbufptr = PL_oldoldbufptr;		/* allow print(STDOUT 123) */
//			else
//				PL_expect = XTERM;
//			s = skipspace(s);
//			PL_lex_allbrackets++;
//			TOKEN('(');
//		case ';':
//			if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_NONEXPR)
//				TOKEN(0);
//			CLINE;
//			s++;
//			PL_expect = XSTATE;
//			TOKEN(';');
//		case ')':
//			if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_CLOSING)
//				TOKEN(0);
//			s++;
//			PL_lex_allbrackets--;
//			s = skipspace(s);
//			if (*s == '{')
//			PREBLOCK(')');
//			TERM(')');
//		case ']':
//			if (PL_lex_brackets && PL_lex_brackstack[PL_lex_brackets-1] == XFAKEEOF)
//				TOKEN(0);
//			s++;
//			if (PL_lex_brackets <= 0)
//	    /* diag_listed_as: Unmatched right %s bracket */
//				yyerror("Unmatched right square bracket");
//			else
//				--PL_lex_brackets;
//			PL_lex_allbrackets--;
//			if (currentLexState == LEX_INTERPNORMAL) {
//				if (PL_lex_brackets == 0) {
//					if (*s == '-' && s[1] == '>')
//					currentLexState = LEX_INTERPENDMAYBE;
//					else if (*s != '[' && *s != '{')
//					currentLexState = LEX_INTERPEND;
//				}
//			}
//			TERM(']');
//		case '{':
//			s++;
//			leftbracket:
//			if (PL_lex_brackets > 100) {
//				Renew(PL_lex_brackstack, PL_lex_brackets + 10, char);
//			}
//			switch (PL_expect) {
//				case XTERM:
//					PL_lex_brackstack[PL_lex_brackets++] = XOPERATOR;
//					PL_lex_allbrackets++;
//					OPERATOR(HASHBRACK);
//				case XOPERATOR:
//					while (s < PL_bufend && SPACE_OR_TAB(*s))
//					s++;
//					d = s;
//					PL_tokenbuf[0] = '\0';
//					if (d < PL_bufend && *d == '-') {
//					PL_tokenbuf[0] = '-';
//					d++;
//					while (d < PL_bufend && SPACE_OR_TAB(*d))
//					d++;
//				}
//				if (d < PL_bufend && isIDFIRST_lazy_if(d,UTF)) {
//					d = scan_word(d, PL_tokenbuf + 1, sizeof PL_tokenbuf - 1,
//							FALSE, &len);
//					while (d < PL_bufend && SPACE_OR_TAB(*d))
//					d++;
//					if (*d == '}') {
//						const char minus = (PL_tokenbuf[0] == '-');
//						s = force_word(s + minus, WORD, FALSE, TRUE);
//						if (minus)
//							force_next('-');
//					}
//				}
//	    /* FALLTHROUGH */
//				case XATTRTERM:
//				case XTERMBLOCK:
//					PL_lex_brackstack[PL_lex_brackets++] = XOPERATOR;
//					PL_lex_allbrackets++;
//					PL_expect = XSTATE;
//					break;
//				case XATTRBLOCK:
//				case XBLOCK:
//					PL_lex_brackstack[PL_lex_brackets++] = XSTATE;
//					PL_lex_allbrackets++;
//					PL_expect = XSTATE;
//					break;
//				case XBLOCKTERM:
//					PL_lex_brackstack[PL_lex_brackets++] = XTERM;
//					PL_lex_allbrackets++;
//					PL_expect = XSTATE;
//					break;
//				default: {
//					const char *t;
//					if (PL_oldoldbufptr == PL_last_lop)
//						PL_lex_brackstack[PL_lex_brackets++] = XTERM;
//					else
//						PL_lex_brackstack[PL_lex_brackets++] = XOPERATOR;
//					PL_lex_allbrackets++;
//					s = skipspace(s);
//					if (*s == '}') {
//						if (PL_expect == XREF && currentLexState == LEX_INTERPNORMAL) {
//							PL_expect = XTERM;
//			/* This hack is to get the ${} in the message. */
//							PL_bufptr = s+1;
//							yyerror("syntax error");
//							break;
//						}
//						OPERATOR(HASHBRACK);
//					}
//					if (PL_expect == XREF && PL_oldoldbufptr != PL_last_lop) {
//		    /* ${...} or @{...} etc., but not print {...}
//		     * Skip the disambiguation and treat this as a block.
//		     */
//						goto block_expectation;
//					}
//		/* This hack serves to disambiguate a pair of curlies
//		 * as being a block or an anon hash.  Normally, expectation
//		 * determines that, but in cases where we're not in a
//		 * position to expect anything in particular (like inside
//		 * eval"") we have to resolve the ambiguity.  This code
//		 * covers the case where the first term in the curlies is a
//		 * quoted string.  Most other cases need to be explicitly
//		 * disambiguated by prepending a "+" before the opening
//		 * curly in order to force resolution as an anon hash.
//		 *
//		 * XXX should probably propagate the outer expectation
//		 * into eval"" to rely less on this hack, but that could
//		 * potentially break current behavior of eval"".
//		 * GSAR 97-07-21
//		 */
//					t = s;
//					if (*s == '\'' || *s == '"' || *s == '`') {
//		    /* common case: get past first string, handling escapes */
//						for (t++; t < PL_bufend && *t != *s;)
//						if (*t++ == '\\')
//						t++;
//						t++;
//					}
//					else if (*s == 'q') {
//						if (++t < PL_bufend
//								&& (!isWORDCHAR(*t)
//								|| ((*t == 'q' || *t == 'x') && ++t < PL_bufend
//								&& !isWORDCHAR(*t))))
//						{
//			/* skip q//-like construct */
//							const char *tmps;
//							char open, close, term;
//							I32 brackets = 1;
//
//							while (t < PL_bufend && isSPACE(*t))
//							t++;
//			/* check for q => */
//							if (t+1 < PL_bufend && t[0] == '=' && t[1] == '>') {
//								OPERATOR(HASHBRACK);
//							}
//							term = *t;
//							open = term;
//							if (term && (tmps = strchr("([{< )]}> )]}>",term)))
//								term = tmps[5];
//							close = term;
//							if (open == close)
//								for (t++; t < PL_bufend; t++) {
//									if (*t == '\\' && t+1 < PL_bufend && open != '\\')
//									t++;
//									else if (*t == open)
//									break;
//								}
//							else {
//								for (t++; t < PL_bufend; t++) {
//									if (*t == '\\' && t+1 < PL_bufend)
//									t++;
//									else if (*t == close && --brackets <= 0)
//									break;
//									else if (*t == open)
//									brackets++;
//								}
//							}
//							t++;
//						}
//						else
//			/* skip plain q word */
//						while (t < PL_bufend && isWORDCHAR_lazy_if(t,UTF))
//							t += UTF8SKIP(t);
//					}
//					else if (isWORDCHAR_lazy_if(t,UTF)) {
//						t += UTF8SKIP(t);
//						while (t < PL_bufend && isWORDCHAR_lazy_if(t,UTF))
//							t += UTF8SKIP(t);
//					}
//					while (t < PL_bufend && isSPACE(*t))
//					t++;
//		/* if comma follows first term, call it an anon hash */
//		/* XXX it could be a comma expression with loop modifiers */
//					if (t < PL_bufend && ((*t == ',' && (*s == 'q' || !isLOWER(*s)))
//					|| (*t == '=' && t[1] == '>')))
//					OPERATOR(HASHBRACK);
//					if (PL_expect == XREF)
//					{
//						block_expectation:
//		    /* If there is an opening brace or 'sub:', treat it
//		       as a term to make ${{...}}{k} and &{sub:attr...}
//		       dwim.  Otherwise, treat it as a statement, so
//		       map {no strict; ...} works.
//		     */
//						s = skipspace(s);
//						if (*s == '{') {
//						PL_expect = XTERM;
//						break;
//					}
//						if (strnEQ(s, "sub", 3)) {
//							d = s + 3;
//							d = skipspace(d);
//							if (*d == ':') {
//								PL_expect = XTERM;
//								break;
//							}
//						}
//						PL_expect = XSTATE;
//					}
//					else {
//						PL_lex_brackstack[PL_lex_brackets-1] = XSTATE;
//						PL_expect = XSTATE;
//					}
//				}
//				break;
//			}
//			pl_yylval.ival = CopLINE(PL_curcop);
//			PL_copline = NOLINE;   /* invalidate current command line number */
//			TOKEN(formbrack ? '=' : '{');
//		case '}':
//			if (PL_lex_brackets && PL_lex_brackstack[PL_lex_brackets-1] == XFAKEEOF)
//				TOKEN(0);
//			rightbracket:
//			s++;
//			if (PL_lex_brackets <= 0)
//	    /* diag_listed_as: Unmatched right %s bracket */
//				yyerror("Unmatched right curly bracket");
//			else
//				PL_expect = (expectation)PL_lex_brackstack[--PL_lex_brackets];
//			PL_lex_allbrackets--;
//			if (currentLexState == LEX_INTERPNORMAL) {
//				if (PL_lex_brackets == 0) {
//					if (PL_expect & XFAKEBRACK) {
//						PL_expect &= XENUMMASK;
//						currentLexState = LEX_INTERPEND;
//						PL_bufptr = s;
//						return yylex();	/* ignore fake brackets */
//					}
//					if (PL_lex_inwhat == OP_SUBST && PL_lex_repl == PL_linestr
//							&& SvEVALED(PL_lex_repl))
//						currentLexState = LEX_INTERPEND;
//					else if (*s == '-' && s[1] == '>')
//					currentLexState = LEX_INTERPENDMAYBE;
//					else if (*s != '[' && *s != '{')
//					currentLexState = LEX_INTERPEND;
//				}
//			}
//			if (PL_expect & XFAKEBRACK) {
//				PL_expect &= XENUMMASK;
//				PL_bufptr = s;
//				return yylex();		/* ignore fake brackets */
//			}
//			force_next(formbrack ? '.' : '}');
//			if (formbrack) LEAVE;
//			if (formbrack == 2) { /* means . where arguments were expected */
//				force_next(';');
//				TOKEN(FORMRBRACK);
//			}
//			TOKEN(';');
//		case '&':
//			if (PL_expect == XPOSTDEREF) POSTDEREF('&');
//			s++;
//			if (*s++ == '&') {
//			if (!PL_lex_allbrackets && PL_lex_fakeeof >=
//					(*s == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_LOGIC)) {
//				s -= 2;
//				TOKEN(0);
//			}
//			AOPERATOR(ANDAND);
//		}
//		s--;
//		if (PL_expect == XOPERATOR) {
//			if (PL_bufptr == PL_linestart && ckWARN(WARN_SEMICOLON)
//					&& isIDFIRST_lazy_if(s,UTF))
//			{
//				CopLINE_dec(PL_curcop);
//				Perl_warner(aTHX_ packWARN(WARN_SEMICOLON), "%s", PL_warn_nosemi);
//				CopLINE_inc(PL_curcop);
//			}
//			if (!PL_lex_allbrackets && PL_lex_fakeeof >=
//					(*s == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_BITWISE)) {
//				s--;
//				TOKEN(0);
//			}
//			PL_parser->saw_infix_sigil = 1;
//			BAop(OP_BIT_AND);
//		}
//
//		PL_tokenbuf[0] = '&';
//		s = scan_ident(s - 1, PL_tokenbuf + 1,
//				sizeof PL_tokenbuf - 1, TRUE);
//		if (PL_tokenbuf[1]) {
//			PL_expect = XOPERATOR;
//			force_ident_maybe_lex('&');
//		}
//		else
//			PREREF('&');
//		pl_yylval.ival = (OPpENTERSUB_AMPER<<8);
//		TERM('&');
//
//		case '|':
//			s++;
//			if (*s++ == '|') {
//			if (!PL_lex_allbrackets && PL_lex_fakeeof >=
//					(*s == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_LOGIC)) {
//				s -= 2;
//				TOKEN(0);
//			}
//			AOPERATOR(OROR);
//		}
//		s--;
//		if (!PL_lex_allbrackets && PL_lex_fakeeof >=
//				(*s == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_BITWISE)) {
//			s--;
//			TOKEN(0);
//		}
//		BOop(OP_BIT_OR);
//		case '=':
//			s++;
//		{
//			const char tmp = *s++;
//			if (tmp == '=') {
//				if (!PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE) {
//					s -= 2;
//					TOKEN(0);
//				}
//				Eop(OP_EQ);
//			}
//			if (tmp == '>') {
//				if (!PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_COMMA) {
//					s -= 2;
//					TOKEN(0);
//				}
//				OPERATOR(',');
//			}
//			if (tmp == '~')
//				PMop(OP_MATCH);
//			if (tmp && isSPACE(*s) && ckWARN(WARN_SYNTAX)
//				&& strchr("+-*/%.^&|<",tmp))
//			Perl_warner(aTHX_ packWARN(WARN_SYNTAX),
//					"Reversed %c= operator",(int)tmp);
//			s--;
//			if (PL_expect == XSTATE && isALPHA(tmp) &&
//					(s == PL_linestart+1 || s[-2] == '\n') )
//			{
//				if ((PL_in_eval && !PL_rsfp && !PL_parser->filtered)
//				|| currentLexState != LEX_NORMAL) {
//				d = PL_bufend;
//				while (s < d) {
//					if (*s++ == '\n') {
//						incline(s);
//						if (strnEQ(s,"=cut",4)) {
//							s = strchr(s,'\n');
//							if (s)
//								s++;
//							else
//								s = d;
//							incline(s);
//							goto retry;
//						}
//					}
//				}
//				goto retry;
//			}
//				s = PL_bufend;
//				PL_parser->in_pod = 1;
//				goto retry;
//			}
//		}
//		if (PL_expect == XBLOCK) {
//			const char *t = s;
//			#ifdef PERL_STRICT_CR
//			while (SPACE_OR_TAB(*t))
//			#else
//			while (SPACE_OR_TAB(*t) || *t == '\r')
//			#endif
//			t++;
//			if (*t == '\n' || *t == '#') {
//				formbrack = 1;
//				ENTER;
//				SAVEI8(PL_parser->form_lex_state);
//				SAVEI32(PL_lex_formbrack);
//				PL_parser->form_lex_state = currentLexState;
//				PL_lex_formbrack = PL_lex_brackets + 1;
//				goto leftbracket;
//			}
//		}
//		if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_ASSIGN) {
//			s--;
//			TOKEN(0);
//		}
//		pl_yylval.ival = 0;
//		OPERATOR(ASSIGNOP);
//		case '!':
//			s++;
//		{
//			const char tmp = *s++;
//			if (tmp == '=') {
//		/* was this !=~ where !~ was meant?
//		 * warn on m:!=~\s+([/?]|[msy]\W|tr\W): */
//
//				if (*s == '~' && ckWARN(WARN_SYNTAX)) {
//					const char *t = s+1;
//
//					while (t < PL_bufend && isSPACE(*t))
//					++t;
//
//					if (*t == '/' || *t == '?' ||
//							((*t == 'm' || *t == 's' || *t == 'y')
//					&& !isWORDCHAR(t[1])) ||
//					(*t == 't' && t[1] == 'r' && !isWORDCHAR(t[2])))
//					Perl_warner(aTHX_ packWARN(WARN_SYNTAX),
//							"!=~ should be !~");
//				}
//				if (!PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE) {
//					s -= 2;
//					TOKEN(0);
//				}
//				Eop(OP_NE);
//			}
//			if (tmp == '~')
//				PMop(OP_NOT);
//		}
//		s--;
//		OPERATOR('!');
//		case '<':
//			if (PL_expect != XOPERATOR) {
//				if (s[1] != '<' && !strchr(s,'>'))
//					check_uni();
//				if (s[1] == '<' && s[2] != '>')
//					s = scan_heredoc(s);
//				else
//					s = scan_inputsymbol(s);
//				PL_expect = XOPERATOR;
//				TOKEN(sublex_start());
//			}
//			s++;
//		{
//			char tmp = *s++;
//			if (tmp == '<') {
//				if (*s == '=' && !PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_ASSIGN) {
//					s -= 2;
//					TOKEN(0);
//				}
//				SHop(OP_LEFT_SHIFT);
//			}
//			if (tmp == '=') {
//				tmp = *s++;
//				if (tmp == '>') {
//					if (!PL_lex_allbrackets &&
//							PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE) {
//						s -= 3;
//						TOKEN(0);
//					}
//					Eop(OP_NCMP);
//				}
//				s--;
//				if (!PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE) {
//					s -= 2;
//					TOKEN(0);
//				}
//				Rop(OP_LE);
//			}
//		}
//		s--;
//		if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE) {
//			s--;
//			TOKEN(0);
//		}
//		Rop(OP_LT);
//		case '>':
//			s++;
//		{
//			const char tmp = *s++;
//			if (tmp == '>') {
//				if (*s == '=' && !PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_ASSIGN) {
//					s -= 2;
//					TOKEN(0);
//				}
//				SHop(OP_RIGHT_SHIFT);
//			}
//			else if (tmp == '=') {
//				if (!PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE) {
//					s -= 2;
//					TOKEN(0);
//				}
//				Rop(OP_GE);
//			}
//		}
//		s--;
//		if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE) {
//			s--;
//			TOKEN(0);
//		}
//		Rop(OP_GT);
//
//		case '$':
//			CLINE;
//
//			if (PL_expect == XOPERATOR) {
//				if (PL_lex_formbrack && PL_lex_brackets == PL_lex_formbrack) {
//					return deprecate_commaless_var_list();
//				}
//			}
//			else if (PL_expect == XPOSTDEREF) {
//				if (s[1] == '#') {
//					s++;
//					POSTDEREF(DOLSHARP);
//				}
//				POSTDEREF('$');
//			}
//
//			if (s[1] == '#' && (isIDFIRST_lazy_if(s+2,UTF) || strchr("{$:+-@", s[2]))) {
//				PL_tokenbuf[0] = '@';
//				s = scan_ident(s + 1, PL_tokenbuf + 1,
//						sizeof PL_tokenbuf - 1, FALSE);
//				if (PL_expect == XOPERATOR)
//					no_op("Array length", s);
//				if (!PL_tokenbuf[1])
//					PREREF(DOLSHARP);
//				PL_expect = XOPERATOR;
//				force_ident_maybe_lex('#');
//				TOKEN(DOLSHARP);
//			}
//
//			PL_tokenbuf[0] = '$';
//			s = scan_ident(s, PL_tokenbuf + 1,
//					sizeof PL_tokenbuf - 1, FALSE);
//			if (PL_expect == XOPERATOR)
//				no_op("Scalar", s);
//			if (!PL_tokenbuf[1]) {
//				if (s == PL_bufend)
//					yyerror("Final $ should be \\$ or $name");
//				PREREF('$');
//			}
//
//			d = s;
//		{
//			const char tmp = *s;
//			if (currentLexState == LEX_NORMAL || PL_lex_brackets)
//				s = skipspace(s);
//
//			if ((PL_expect != XREF || PL_oldoldbufptr == PL_last_lop)
//					&& intuit_more(s)) {
//				if (*s == '[') {
//					PL_tokenbuf[0] = '@';
//					if (ckWARN(WARN_SYNTAX)) {
//						char *t = s+1;
//
//						while (isSPACE(*t) || isWORDCHAR_lazy_if(t,UTF) || *t == '$')
//						t++;
//						if (*t++ == ',') {
//							PL_bufptr = skipspace(PL_bufptr); /* XXX can realloc */
//							while (t < PL_bufend && *t != ']')
//							t++;
//							Perl_warner(aTHX_ packWARN(WARN_SYNTAX),
//									"Multidimensional syntax %.*s not supported",
//									(int)((t - PL_bufptr) + 1), PL_bufptr);
//						}
//					}
//				}
//				else if (*s == '{') {
//					char *t;
//					PL_tokenbuf[0] = '%';
//					if (strEQ(PL_tokenbuf+1, "SIG")  && ckWARN(WARN_SYNTAX)
//							&& (t = strchr(s, '}')) && (t = strchr(t, '=')))
//					{
//						char tmpbuf[sizeof PL_tokenbuf];
//						do {
//							t++;
//						} while (isSPACE(*t));
//						if (isIDFIRST_lazy_if(t,UTF)) {
//							STRLEN len;
//							t = scan_word(t, tmpbuf, sizeof tmpbuf, TRUE,
//									&len);
//							while (isSPACE(*t))
//							t++;
//							if (*t == ';'
//									&& get_cvn_flags(tmpbuf, len, UTF ? SVf_UTF8 : 0))
//							Perl_warner(aTHX_ packWARN(WARN_SYNTAX),
//									"You need to quote \"%"UTF8f"\"",
//									UTF8fARG(UTF, len, tmpbuf));
//						}
//					}
//				}
//			}
//
//			PL_expect = XOPERATOR;
//			if (currentLexState == LEX_NORMAL && isSPACE((char)tmp)) {
//				const bool islop = (PL_last_lop == PL_oldoldbufptr);
//				if (!islop || PL_last_lop_op == OP_GREPSTART)
//					PL_expect = XOPERATOR;
//				else if (strchr("$@\"'`q", *s))
//				PL_expect = XTERM;		/* e.g. print $fh "foo" */
//				else if (strchr("&*<%", *s) && isIDFIRST_lazy_if(s+1,UTF))
//				PL_expect = XTERM;		/* e.g. print $fh &sub */
//				else if (isIDFIRST_lazy_if(s,UTF)) {
//					char tmpbuf[sizeof PL_tokenbuf];
//					int t2;
//					scan_word(s, tmpbuf, sizeof tmpbuf, TRUE, &len);
//					if ((t2 = keyword(tmpbuf, len, 0))) {
//			/* binary operators exclude handle interpretations */
//						switch (t2) {
//							case -KEY_x:
//							case -KEY_eq:
//							case -KEY_ne:
//							case -KEY_gt:
//							case -KEY_lt:
//							case -KEY_ge:
//							case -KEY_le:
//							case -KEY_cmp:
//								break;
//							default:
//								PL_expect = XTERM;	/* e.g. print $fh length() */
//								break;
//						}
//					}
//					else {
//						PL_expect = XTERM;	/* e.g. print $fh subr() */
//					}
//				}
//				else if (isDIGIT(*s))
//				PL_expect = XTERM;		/* e.g. print $fh 3 */
//				else if (*s == '.' && isDIGIT(s[1]))
//				PL_expect = XTERM;		/* e.g. print $fh .3 */
//				else if ((*s == '?' || *s == '-' || *s == '+')
//				&& !isSPACE(s[1]) && s[1] != '=')
//				PL_expect = XTERM;		/* e.g. print $fh -1 */
//				else if (*s == '/' && !isSPACE(s[1]) && s[1] != '='
//						&& s[1] != '/')
//				PL_expect = XTERM;		/* e.g. print $fh /.../
//						   XXX except DORDOR operator
//						*/
//				else if (*s == '<' && s[1] == '<' && !isSPACE(s[2])
//						&& s[2] != '=')
//				PL_expect = XTERM;		/* print $fh <<"EOF" */
//			}
//		}
//		force_ident_maybe_lex('$');
//		TOKEN('$');
//
//		case '@':
//			if (PL_expect == XOPERATOR)
//				no_op("Array", s);
//			else if (PL_expect == XPOSTDEREF) POSTDEREF('@');
//			PL_tokenbuf[0] = '@';
//			s = scan_ident(s, PL_tokenbuf + 1, sizeof PL_tokenbuf - 1, FALSE);
//			pl_yylval.ival = 0;
//			if (!PL_tokenbuf[1]) {
//				PREREF('@');
//			}
//			if (currentLexState == LEX_NORMAL)
//				s = skipspace(s);
//			if ((PL_expect != XREF || PL_oldoldbufptr == PL_last_lop) && intuit_more(s)) {
//				if (*s == '{')
//				PL_tokenbuf[0] = '%';
//
//	    /* Warn about @ where they meant $. */
//				if (*s == '[' || *s == '{') {
//					if (ckWARN(WARN_SYNTAX)) {
//						S_check_scalar_slice(aTHX_ s);
//					}
//				}
//			}
//			PL_expect = XOPERATOR;
//			force_ident_maybe_lex('@');
//			TERM('@');
//
//		case '/':			/* may be division, defined-or, or pattern */
//			if ((PL_expect == XOPERATOR || PL_expect == XTERMORDORDOR) && s[1] == '/') {
//				if (!PL_lex_allbrackets && PL_lex_fakeeof >=
//						(s[2] == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_LOGIC))
//					TOKEN(0);
//				s += 2;
//				AOPERATOR(DORDOR);
//			}
//			else if (PL_expect == XOPERATOR) {
//				s++;
//				if (*s == '=' && !PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_ASSIGN) {
//					s--;
//					TOKEN(0);
//				}
//				Mop(OP_DIVIDE);
//			}
//			else {
//	    /* Disable warning on "study /blah/" */
//				if (PL_oldoldbufptr == PL_last_uni
//						&& (*PL_last_uni != 's' || s - PL_last_uni < 5
//						|| memNE(PL_last_uni, "study", 5)
//						|| isWORDCHAR_lazy_if(PL_last_uni+5,UTF)
//						))
//				check_uni();
//				s = scan_pat(s,OP_MATCH);
//				TERM(sublex_start());
//			}
//
//		case '?':			/* conditional */
//			s++;
//			if (!PL_lex_allbrackets &&
//					PL_lex_fakeeof >= LEX_FAKEEOF_IFELSE) {
//				s--;
//				TOKEN(0);
//			}
//			PL_lex_allbrackets++;
//			OPERATOR('?');
//
//		case '.':
//			if (PL_lex_formbrack && PL_lex_brackets == PL_lex_formbrack
//			#ifdef PERL_STRICT_CR
//			&& s[1] == '\n'
//			#else
//			&& (s[1] == '\n' || (s[1] == '\r' && s[2] == '\n'))
//			#endif
//				&& (s == PL_linestart || s[-1] == '\n') )
//		{
//			PL_expect = XSTATE;
//			formbrack = 2; /* dot seen where arguments expected */
//			goto rightbracket;
//		}
//		if (PL_expect == XSTATE && s[1] == '.' && s[2] == '.') {
//			s += 3;
//			OPERATOR(YADAYADA);
//		}
//		if (PL_expect == XOPERATOR || !isDIGIT(s[1])) {
//			char tmp = *s++;
//			if (*s == tmp) {
//				if (!PL_lex_allbrackets &&
//						PL_lex_fakeeof >= LEX_FAKEEOF_RANGE) {
//					s--;
//					TOKEN(0);
//				}
//				s++;
//				if (*s == tmp) {
//					s++;
//					pl_yylval.ival = OPf_SPECIAL;
//				}
//				else
//				pl_yylval.ival = 0;
//				OPERATOR(DOTDOT);
//			}
//			if (*s == '=' && !PL_lex_allbrackets &&
//					PL_lex_fakeeof >= LEX_FAKEEOF_ASSIGN) {
//				s--;
//				TOKEN(0);
//			}
//			Aop(OP_CONCAT);
//		}
//	/* FALLTHROUGH */
//		case '0': case '1': case '2': case '3': case '4':
//		case '5': case '6': case '7': case '8': case '9':
//			s = scan_num(s, &pl_yylval);
//			DEBUG_T( { printbuf("### Saw number in %s\n", s); } );
//			if (PL_expect == XOPERATOR)
//				no_op("Number",s);
//			TERM(THING);
//
//		case '\'':
//			s = scan_str(s,FALSE,FALSE,FALSE,NULL);
//			if (!s)
//				missingterm(NULL);
//			COPLINE_SET_FROM_MULTI_END;
//			DEBUG_T( { printbuf("### Saw string before %s\n", s); } );
//			if (PL_expect == XOPERATOR) {
//				if (PL_lex_formbrack && PL_lex_brackets == PL_lex_formbrack) {
//					return deprecate_commaless_var_list();
//				}
//				else
//					no_op("String",s);
//			}
//			pl_yylval.ival = OP_CONST;
//			TERM(sublex_start());
//
//		case '"':
//			s = scan_str(s,FALSE,FALSE,FALSE,NULL);
//			DEBUG_T( {
//			if (s)
//				printbuf("### Saw string before %s\n", s);
//			else
//				PerlIO_printf(Perl_debug_log,
//						"### Saw unterminated string\n");
//			} );
//			if (PL_expect == XOPERATOR) {
//				if (PL_lex_formbrack && PL_lex_brackets == PL_lex_formbrack) {
//					return deprecate_commaless_var_list();
//				}
//				else
//					no_op("String",s);
//			}
//			if (!s)
//				missingterm(NULL);
//			pl_yylval.ival = OP_CONST;
//	/* FIXME. I think that this can be const if char *d is replaced by
//	   more localised variables.  */
//			for (d = SvPV(PL_lex_stuff, len); len; len--, d++) {
//				if (*d == '$' || *d == '@' || *d == '\\' || !UTF8_IS_INVARIANT((U8)*d)) {
//					pl_yylval.ival = OP_STRINGIFY;
//					break;
//				}
//			}
//			if (pl_yylval.ival == OP_CONST)
//				COPLINE_SET_FROM_MULTI_END;
//			TERM(sublex_start());
//
//		case '`':
//			s = scan_str(s,FALSE,FALSE,FALSE,NULL);
//			DEBUG_T( { printbuf("### Saw backtick string before %s\n", s); } );
//			if (PL_expect == XOPERATOR)
//				no_op("Backticks",s);
//			if (!s)
//				missingterm(NULL);
//			pl_yylval.ival = OP_BACKTICK;
//			TERM(sublex_start());
//
//		case '\\':
//			s++;
//			if (PL_lex_inwhat == OP_SUBST && PL_lex_repl == PL_linestr
//					&& isDIGIT(*s))
//			Perl_ck_warner(aTHX_ packWARN(WARN_SYNTAX),"Can't use \\%c to mean $%c in expression",
//					*s, *s);
//			if (PL_expect == XOPERATOR)
//				no_op("Backslash",s);
//			OPERATOR(REFGEN);
//
//		case 'v':
//			if (isDIGIT(s[1]) && PL_expect != XOPERATOR) {
//				char *start = s + 2;
//				while (isDIGIT(*start) || *start == '_')
//				start++;
//				if (*start == '.' && isDIGIT(start[1])) {
//					s = scan_num(s, &pl_yylval);
//					TERM(THING);
//				}
//				else if ((*start == ':' && start[1] == ':')
//				|| (PL_expect == XSTATE && *start == ':'))
//				goto keylookup;
//				else if (PL_expect == XSTATE) {
//					d = start;
//					while (d < PL_bufend && isSPACE(*d)) d++;
//					if (*d == ':') goto keylookup;
//				}
//	    /* avoid v123abc() or $h{v1}, allow C<print v10;> */
//				if (!isALPHA(*start) && (PL_expect == XTERM
//						|| PL_expect == XSTATE
//						|| PL_expect == XTERMORDORDOR)) {
//					GV *const gv = gv_fetchpvn_flags(s, start - s,
//							UTF ? SVf_UTF8 : 0, SVt_PVCV);
//					if (!gv) {
//						s = scan_num(s, &pl_yylval);
//						TERM(THING);
//					}
//				}
//			}
//			goto keylookup;
//		case 'x':
//			if (isDIGIT(s[1]) && PL_expect == XOPERATOR) {
//				s++;
//				Mop(OP_REPEAT);
//			}
//			goto keylookup;
//
//		case '_':
//		case 'a': case 'A':
//		case 'b': case 'B':
//		case 'c': case 'C':
//		case 'd': case 'D':
//		case 'e': case 'E':
//		case 'f': case 'F':
//		case 'g': case 'G':
//		case 'h': case 'H':
//		case 'i': case 'I':
//		case 'j': case 'J':
//		case 'k': case 'K':
//		case 'l': case 'L':
//		case 'm': case 'M':
//		case 'n': case 'N':
//		case 'o': case 'O':
//		case 'p': case 'P':
//		case 'q': case 'Q':
//		case 'r': case 'R':
//		case 's': case 'S':
//		case 't': case 'T':
//		case 'u': case 'U':
//		case 'V':
//		case 'w': case 'W':
//		case 'X':
//		case 'y': case 'Y':
//		case 'z': case 'Z':
//
//			keylookup: {
//				bool anydelim;
//				bool lex;
//				I32 tmp;
//				SV *sv;
//				CV *cv;
//				PADOFFSET off;
//				OP *rv2cv_op;
//
//				lex = FALSE;
//				orig_keyword = 0;
//				off = 0;
//				sv = NULL;
//				cv = NULL;
//				gv = NULL;
//				gvp = NULL;
//				rv2cv_op = NULL;
//
//				PL_bufptr = s;
//				s = scan_word(s, PL_tokenbuf, sizeof PL_tokenbuf, FALSE, &len);
//
//	/* Some keywords can be followed by any delimiter, including ':' */
//				anydelim = word_takes_any_delimeter(PL_tokenbuf, len);
//
//	/* x::* is just a word, unless x is "CORE" */
//				if (!anydelim && *s == ':' && s[1] == ':') {
//					if (strEQ(PL_tokenbuf, "CORE")) goto case_KEY_CORE;
//					goto just_a_word;
//				}
//
//				d = s;
//				while (d < PL_bufend && isSPACE(*d))
//				d++;	/* no comments skipped here, or s### is misparsed */
//
//	/* Is this a word before a => operator? */
//				if (*d == '=' && d[1] == '>') {
//					fat_arrow:
//					CLINE;
//					pl_yylval.opval
//							= (OP*)newSVOP(OP_CONST, 0,
//							S_newSV_maybe_utf8(aTHX_ PL_tokenbuf, len));
//					pl_yylval.opval->op_private = OPpCONST_BARE;
//					TERM(WORD);
//				}
//
//	/* Check for plugged-in keyword */
//				{
//					OP *o;
//					int result;
//					char *saved_bufptr = PL_bufptr;
//					PL_bufptr = s;
//					result = PL_keyword_plugin(aTHX_ PL_tokenbuf, len, &o);
//					s = PL_bufptr;
//					if (result == KEYWORD_PLUGIN_DECLINE) {
//		/* not a plugged-in keyword */
//						PL_bufptr = saved_bufptr;
//					} else if (result == KEYWORD_PLUGIN_STMT) {
//						pl_yylval.opval = o;
//						CLINE;
//						if (!PL_nexttoke) PL_expect = XSTATE;
//						return REPORT(PLUGSTMT);
//					} else if (result == KEYWORD_PLUGIN_EXPR) {
//						pl_yylval.opval = o;
//						CLINE;
//						if (!PL_nexttoke) PL_expect = XOPERATOR;
//						return REPORT(PLUGEXPR);
//					} else {
//						Perl_croak(aTHX_ "Bad plugin affecting keyword '%s'",
//								PL_tokenbuf);
//					}
//				}
//
//	/* Check for built-in keyword */
//				tmp = keyword(PL_tokenbuf, len, 0);
//
//	/* Is this a label? */
//				if (!anydelim && PL_expect == XSTATE
//						&& d < PL_bufend && *d == ':' && *(d + 1) != ':') {
//					s = d + 1;
//					pl_yylval.pval = savepvn(PL_tokenbuf, len+1);
//					pl_yylval.pval[len] = '\0';
//					pl_yylval.pval[len+1] = UTF ? 1 : 0;
//					CLINE;
//					TOKEN(LABEL);
//				}
//
//	/* Check for lexical sub */
//				if (PL_expect != XOPERATOR) {
//					char tmpbuf[sizeof PL_tokenbuf + 1];
//					*tmpbuf = '&';
//					Copy(PL_tokenbuf, tmpbuf+1, len, char);
//					off = pad_findmy_pvn(tmpbuf, len+1, UTF ? SVf_UTF8 : 0);
//					if (off != NOT_IN_PAD) {
//						assert(off); /* we assume this is boolean-true below */
//						if (PAD_COMPNAME_FLAGS_isOUR(off)) {
//							HV *  const stash = PAD_COMPNAME_OURSTASH(off);
//							HEK * const stashname = HvNAME_HEK(stash);
//							sv = newSVhek(stashname);
//							sv_catpvs(sv, "::");
//							sv_catpvn_flags(sv, PL_tokenbuf, len,
//									(UTF ? SV_CATUTF8 : SV_CATBYTES));
//							gv = gv_fetchsv(sv, GV_NOADD_NOINIT | SvUTF8(sv),
//									SVt_PVCV);
//							off = 0;
//							if (!gv) {
//								sv_free(sv);
//								sv = NULL;
//								goto just_a_word;
//							}
//						}
//						else {
//							rv2cv_op = newOP(OP_PADANY, 0);
//							rv2cv_op->op_targ = off;
//							cv = find_lexical_cv(off);
//						}
//						lex = TRUE;
//						goto just_a_word;
//					}
//					off = 0;
//				}
//
//				if (tmp < 0) {			/* second-class keyword? */
//					GV *ogv = NULL;	/* override (winner) */
//					GV *hgv = NULL;	/* hidden (loser) */
//					if (PL_expect != XOPERATOR && (*s != ':' || s[1] != ':')) {
//						CV *cv;
//						if ((gv = gv_fetchpvn_flags(PL_tokenbuf, len,
//								(UTF ? SVf_UTF8 : 0)|GV_NOTQUAL,
//								SVt_PVCV)) &&
//								(cv = GvCVu(gv)))
//						{
//							if (GvIMPORTED_CV(gv))
//								ogv = gv;
//							else if (! CvMETHOD(cv))
//								hgv = gv;
//						}
//						if (!ogv &&
//								(gvp = (GV**)hv_fetch(PL_globalstash, PL_tokenbuf,
//								len, FALSE)) &&
//						(gv = *gvp) && (
//								isGV_with_GP(gv)
//										? GvCVu(gv) && GvIMPORTED_CV(gv)
//										:   SvPCS_IMPORTED(gv)
//										&& (gv_init(gv, PL_globalstash, PL_tokenbuf,
//										len, 0), 1)
//						))
//						{
//							ogv = gv;
//						}
//					}
//					if (ogv) {
//						orig_keyword = tmp;
//						tmp = 0;		/* overridden by import or by GLOBAL */
//					}
//					else if (gv && !gvp
//							&& -tmp==KEY_lock	/* XXX generalizable kludge */
//							&& GvCVu(gv))
//					{
//						tmp = 0;		/* any sub overrides "weak" keyword */
//					}
//					else {			/* no override */
//						tmp = -tmp;
//						if (tmp == KEY_dump) {
//							Perl_ck_warner(aTHX_ packWARN(WARN_MISC),
//									"dump() better written as CORE::dump()");
//						}
//						gv = NULL;
//						gvp = 0;
//						if (hgv && tmp != KEY_x)	/* never ambiguous */
//							Perl_ck_warner(aTHX_ packWARN(WARN_AMBIGUOUS),
//									"Ambiguous call resolved as CORE::%s(), "
//									"qualify as such or use &",
//									GvENAME(hgv));
//					}
//				}
//
//				if (tmp && tmp != KEY___DATA__ && tmp != KEY___END__
//						&& (!anydelim || *s != '#')) {
//	    /* no override, and not s### either; skipspace is safe here
//	     * check for => on following line */
//					bool arrow;
//					STRLEN bufoff = PL_bufptr - SvPVX(PL_linestr);
//					STRLEN   soff = s         - SvPVX(PL_linestr);
//					s = skipspace_flags(s, LEX_NO_INCLINE);
//					arrow = *s == '=' && s[1] == '>';
//					PL_bufptr = SvPVX(PL_linestr) + bufoff;
//					s         = SvPVX(PL_linestr) +   soff;
//					if (arrow)
//					goto fat_arrow;
//				}
//
//				reserved_word:
//				switch (tmp) {
//
//					default:			/* not a keyword */
//	    /* Trade off - by using this evil construction we can pull the
//	       variable gv into the block labelled keylookup. If not, then
//	       we have to give it function scope so that the goto from the
//	       earlier ':' case doesn't bypass the initialisation.  */
//						if (0) {
//							just_a_word_zero_gv:
//							sv = NULL;
//							cv = NULL;
//							gv = NULL;
//							gvp = NULL;
//							rv2cv_op = NULL;
//							orig_keyword = 0;
//							lex = 0;
//							off = 0;
//						}
//						just_a_word: {
//							int pkgname = 0;
//							const char lastchar = (PL_bufptr == PL_oldoldbufptr ? 0 : PL_bufptr[-1]);
//							bool safebw;
//
//
//		/* Get the rest if it looks like a package qualifier */
//
//							if (*s == '\'' || (*s == ':' && s[1] == ':')) {
//								STRLEN morelen;
//								s = scan_word(s, PL_tokenbuf + len, sizeof PL_tokenbuf - len,
//										TRUE, &morelen);
//								if (!morelen)
//									Perl_croak(aTHX_ "Bad name after %"UTF8f"%s",
//											UTF8fARG(UTF, len, PL_tokenbuf),
//											*s == '\'' ? "'" : "::");
//								len += morelen;
//								pkgname = 1;
//							}
//
//							if (PL_expect == XOPERATOR) {
//								if (PL_bufptr == PL_linestart) {
//									CopLINE_dec(PL_curcop);
//									Perl_warner(aTHX_ packWARN(WARN_SEMICOLON), "%s", PL_warn_nosemi);
//									CopLINE_inc(PL_curcop);
//								}
//								else
//									no_op("Bareword",s);
//							}
//
//		/* See if the name is "Foo::",
//		   in which case Foo is a bareword
//		   (and a package name). */
//
//							if (len > 2 &&
//									PL_tokenbuf[len - 2] == ':' && PL_tokenbuf[len - 1] == ':')
//							{
//								if (ckWARN(WARN_BAREWORD)
//										&& ! gv_fetchpvn_flags(PL_tokenbuf, len, UTF ? SVf_UTF8 : 0, SVt_PVHV))
//									Perl_warner(aTHX_ packWARN(WARN_BAREWORD),
//											"Bareword \"%"UTF8f"\" refers to nonexistent package",
//											UTF8fARG(UTF, len, PL_tokenbuf));
//								len -= 2;
//								PL_tokenbuf[len] = '\0';
//								gv = NULL;
//								gvp = 0;
//								safebw = TRUE;
//							}
//							else {
//								safebw = FALSE;
//							}
//
//		/* if we saw a global override before, get the right name */
//
//							if (!sv)
//								sv = S_newSV_maybe_utf8(aTHX_ PL_tokenbuf,
//										len);
//							if (gvp) {
//								SV * const tmp_sv = sv;
//								sv = newSVpvs("CORE::GLOBAL::");
//								sv_catsv(sv, tmp_sv);
//								SvREFCNT_dec(tmp_sv);
//							}
//
//
//		/* Presume this is going to be a bareword of some sort. */
//							CLINE;
//							pl_yylval.opval = (OP*)newSVOP(OP_CONST, 0, sv);
//							pl_yylval.opval->op_private = OPpCONST_BARE;
//
//		/* And if "Foo::", then that's what it certainly is. */
//							if (safebw)
//							goto safe_bareword;
//
//							if (!off)
//							{
//								OP *const_op = newSVOP(OP_CONST, 0, SvREFCNT_inc_NN(sv));
//								const_op->op_private = OPpCONST_BARE;
//								rv2cv_op =
//										newCVREF(OPpMAY_RETURN_CONSTANT<<8, const_op);
//								cv = lex
//										? isGV(gv)
//										? GvCV(gv)
//										: SvROK(gv) && SvTYPE(SvRV(gv)) == SVt_PVCV
//										? (CV *)SvRV(gv)
//								: ((CV *)gv)
//								: rv2cv_op_cv(rv2cv_op, RV2CVOPCV_RETURN_STUB);
//							}
//
//		/* Use this var to track whether intuit_method has been
//		   called.  intuit_method returns 0 or > 255.  */
//							tmp = 1;
//
//		/* See if it's the indirect object for a list operator. */
//
//							if (PL_oldoldbufptr &&
//									PL_oldoldbufptr < PL_bufptr &&
//									(PL_oldoldbufptr == PL_last_lop
//											|| PL_oldoldbufptr == PL_last_uni) &&
//		    /* NO SKIPSPACE BEFORE HERE! */
//									(PL_expect == XREF ||
//											((PL_opargs[PL_last_lop_op] >> OASHIFT)& 7) == OA_FILEREF))
//							{
//								bool immediate_paren = *s == '(';
//
//		    /* (Now we can afford to cross potential line boundary.) */
//								s = skipspace(s);
//
//		    /* Two barewords in a row may indicate method call. */
//
//								if ((isIDFIRST_lazy_if(s,UTF) || *s == '$') &&
//								(tmp = intuit_method(s, lex ? NULL : sv, cv))) {
//								goto method;
//							}
//
//		    /* If not a declared subroutine, it's an indirect object. */
//		    /* (But it's an indir obj regardless for sort.) */
//		    /* Also, if "_" follows a filetest operator, it's a bareword */
//
//								if (
//										( !immediate_paren && (PL_last_lop_op == OP_SORT ||
//												(!cv &&
//														(PL_last_lop_op != OP_MAPSTART &&
//																PL_last_lop_op != OP_GREPSTART))))
//												|| (PL_tokenbuf[0] == '_' && PL_tokenbuf[1] == '\0'
//												&& ((PL_opargs[PL_last_lop_op] & OA_CLASS_MASK) == OA_FILESTATOP))
//										)
//								{
//									PL_expect = (PL_last_lop == PL_oldoldbufptr) ? XTERM : XOPERATOR;
//									goto bareword;
//								}
//							}
//
//							PL_expect = XOPERATOR;
//							s = skipspace(s);
//
//		/* Is this a word before a => operator? */
//							if (*s == '=' && s[1] == '>' && !pkgname) {
//								op_free(rv2cv_op);
//								CLINE;
//								if (gvp || (lex && !off)) {
//									assert (cSVOPx(pl_yylval.opval)->op_sv == sv);
//			/* This is our own scalar, created a few lines
//			   above, so this is safe. */
//									SvREADONLY_off(sv);
//									sv_setpv(sv, PL_tokenbuf);
//									if (UTF && !IN_BYTES
//											&& is_utf8_string((U8*)PL_tokenbuf, len))
//										SvUTF8_on(sv);
//									SvREADONLY_on(sv);
//								}
//								TERM(WORD);
//							}
//
//		/* If followed by a paren, it's certainly a subroutine. */
//							if (*s == '(') {
//								CLINE;
//								if (cv) {
//									d = s + 1;
//									while (SPACE_OR_TAB(*d))
//									d++;
//									if (*d == ')' && (sv = cv_const_sv_or_av(cv))) {
//										s = d + 1;
//										goto its_constant;
//									}
//								}
//								NEXTVAL_NEXTTOKE.opval =
//										off ? rv2cv_op : pl_yylval.opval;
//								if (off)
//									op_free(pl_yylval.opval), force_next(PRIVATEREF);
//								else op_free(rv2cv_op),	   force_next(WORD);
//								pl_yylval.ival = 0;
//								TOKEN('&');
//							}
//
//		/* If followed by var or block, call it a method (unless sub) */
//
//							if ((*s == '$' || *s == '{') && !cv) {
//								op_free(rv2cv_op);
//								PL_last_lop = PL_oldbufptr;
//								PL_last_lop_op = OP_METHOD;
//								if (!PL_lex_allbrackets &&
//										PL_lex_fakeeof > LEX_FAKEEOF_LOWLOGIC)
//									PL_lex_fakeeof = LEX_FAKEEOF_LOWLOGIC;
//								PL_expect = XBLOCKTERM;
//								PL_bufptr = s;
//								return REPORT(METHOD);
//							}
//
//		/* If followed by a bareword, see if it looks like indir obj. */
//
//							if (tmp == 1 && !orig_keyword
//									&& (isIDFIRST_lazy_if(s,UTF) || *s == '$')
//							&& (tmp = intuit_method(s, lex ? NULL : sv, cv))) {
//								method:
//								if (lex && !off) {
//									assert(cSVOPx(pl_yylval.opval)->op_sv == sv);
//									SvREADONLY_off(sv);
//									sv_setpvn(sv, PL_tokenbuf, len);
//									if (UTF && !IN_BYTES
//											&& is_utf8_string((U8*)PL_tokenbuf, len))
//										SvUTF8_on (sv);
//									else SvUTF8_off(sv);
//								}
//								op_free(rv2cv_op);
//								if (tmp == METHOD && !PL_lex_allbrackets &&
//										PL_lex_fakeeof > LEX_FAKEEOF_LOWLOGIC)
//									PL_lex_fakeeof = LEX_FAKEEOF_LOWLOGIC;
//								return REPORT(tmp);
//							}
//
//		/* Not a method, so call it a subroutine (if defined) */
//
//							if (cv) {
//		    /* Check for a constant sub */
//								if ((sv = cv_const_sv_or_av(cv))) {
//									its_constant:
//									op_free(rv2cv_op);
//									SvREFCNT_dec(((SVOP*)pl_yylval.opval)->op_sv);
//									((SVOP*)pl_yylval.opval)->op_sv = SvREFCNT_inc_simple(sv);
//									if (SvTYPE(sv) == SVt_PVAV)
//										pl_yylval.opval = newUNOP(OP_RV2AV, OPf_PARENS,
//												pl_yylval.opval);
//									else {
//										pl_yylval.opval->op_private = 0;
//										pl_yylval.opval->op_folded = 1;
//										pl_yylval.opval->op_flags |= OPf_SPECIAL;
//									}
//									TOKEN(WORD);
//								}
//
//								op_free(pl_yylval.opval);
//								pl_yylval.opval =
//										off ? (OP *)newCVREF(0, rv2cv_op) : rv2cv_op;
//								pl_yylval.opval->op_private |= OPpENTERSUB_NOPAREN;
//								PL_last_lop = PL_oldbufptr;
//								PL_last_lop_op = OP_ENTERSUB;
//		    /* Is there a prototype? */
//								if (
//										SvPOK(cv))
//								{
//									STRLEN protolen = CvPROTOLEN(cv);
//									const char *proto = CvPROTO(cv);
//									bool optional;
//									proto = S_strip_spaces(aTHX_ proto, &protolen);
//									if (!protolen)
//										TERM(FUNC0SUB);
//									if ((optional = *proto == ';'))
//									do
//										proto++;
//									while (*proto == ';');
//									if (
//											(
//													(
//															*proto == '$' || *proto == '_'
//													|| *proto == '*' || *proto == '+'
//									)
//									&& proto[1] == '\0'
//									)
//									|| (
//										*proto == '\\' && proto[1] && proto[2] == '\0'
//									)
//									)
//									UNIPROTO(UNIOPSUB,optional);
//									if (*proto == '\\' && proto[1] == '[') {
//									const char *p = proto + 2;
//									while(*p && *p != ']')
//									++p;
//									if(*p == ']' && !p[1])
//									UNIPROTO(UNIOPSUB,optional);
//								}
//									if (*proto == '&' && *s == '{') {
//									if (PL_curstash)
//										sv_setpvs(PL_subname, "__ANON__");
//									else
//										sv_setpvs(PL_subname, "__ANON__::__ANON__");
//									if (!PL_lex_allbrackets &&
//											PL_lex_fakeeof > LEX_FAKEEOF_LOWLOGIC)
//										PL_lex_fakeeof = LEX_FAKEEOF_LOWLOGIC;
//									PREBLOCK(LSTOPSUB);
//								}
//								}
//								NEXTVAL_NEXTTOKE.opval = pl_yylval.opval;
//								PL_expect = XTERM;
//								force_next(off ? PRIVATEREF : WORD);
//								if (!PL_lex_allbrackets &&
//										PL_lex_fakeeof > LEX_FAKEEOF_LOWLOGIC)
//									PL_lex_fakeeof = LEX_FAKEEOF_LOWLOGIC;
//								TOKEN(NOAMP);
//							}
//
//		/* Call it a bare word */
//
//							if (PL_hints & HINT_STRICT_SUBS)
//								pl_yylval.opval->op_private |= OPpCONST_STRICT;
//							else {
//								bareword:
//		    /* after "print" and similar functions (corresponding to
//		     * "F? L" in opcode.pl), whatever wasn't already parsed as
//		     * a filehandle should be subject to "strict subs".
//		     * Likewise for the optional indirect-object argument to system
//		     * or exec, which can't be a bareword */
//								if ((PL_last_lop_op == OP_PRINT
//										|| PL_last_lop_op == OP_PRTF
//										|| PL_last_lop_op == OP_SAY
//										|| PL_last_lop_op == OP_SYSTEM
//										|| PL_last_lop_op == OP_EXEC)
//										&& (PL_hints & HINT_STRICT_SUBS))
//									pl_yylval.opval->op_private |= OPpCONST_STRICT;
//								if (lastchar != '-') {
//									if (ckWARN(WARN_RESERVED)) {
//										d = PL_tokenbuf;
//										while (isLOWER(*d))
//										d++;
//										if (!*d && !gv_stashpv(PL_tokenbuf, UTF ? SVf_UTF8 : 0))
//										{
//                                /* PL_warn_reserved is constant */
//											GCC_DIAG_IGNORE(-Wformat-nonliteral);
//											Perl_warner(aTHX_ packWARN(WARN_RESERVED), PL_warn_reserved,
//													PL_tokenbuf);
//											GCC_DIAG_RESTORE;
//										}
//									}
//								}
//							}
//							op_free(rv2cv_op);
//
//							safe_bareword:
//							if ((lastchar == '*' || lastchar == '%' || lastchar == '&')
//									&& saw_infix_sigil) {
//								Perl_ck_warner_d(aTHX_ packWARN(WARN_AMBIGUOUS),
//										"Operator or semicolon missing before %c%"UTF8f,
//										lastchar,
//										UTF8fARG(UTF, strlen(PL_tokenbuf),
//												PL_tokenbuf));
//								Perl_ck_warner_d(aTHX_ packWARN(WARN_AMBIGUOUS),
//										"Ambiguous use of %c resolved as operator %c",
//										lastchar, lastchar);
//							}
//							TOKEN(WORD);
//						}
//
//					case KEY___FILE__:
//						FUN0OP(
//								(OP*)newSVOP(OP_CONST, 0, newSVpv(CopFILE(PL_curcop),0))
//						);
//
//					case KEY___LINE__:
//						FUN0OP(
//								(OP*)newSVOP(OP_CONST, 0,
//										Perl_newSVpvf(aTHX_ "%"IVdf, (IV)CopLINE(PL_curcop)))
//						);
//
//					case KEY___PACKAGE__:
//						FUN0OP(
//								(OP*)newSVOP(OP_CONST, 0,
//										(PL_curstash
//												? newSVhek(HvNAME_HEK(PL_curstash))
//												: &PL_sv_undef))
//						);
//
//					case KEY___DATA__:
//					case KEY___END__: {
//						GV *gv;
//						if (PL_rsfp && (!PL_in_eval || PL_tokenbuf[2] == 'D')) {
//							HV * const stash = PL_tokenbuf[2] == 'D' && PL_curstash
//									? PL_curstash
//									: PL_defstash;
//							gv = (GV *)*hv_fetchs(stash, "DATA", 1);
//							if (!isGV(gv))
//								gv_init(gv,stash,"DATA",4,0);
//							GvMULTI_on(gv);
//							if (!GvIO(gv))
//								GvIOp(gv) = newIO();
//							IoIFP(GvIOp(gv)) = PL_rsfp;
//							#if defined(HAS_FCNTL) && defined(F_SETFD)
//							{
//								const int fd = PerlIO_fileno(PL_rsfp);
//								fcntl(fd,F_SETFD,fd >= 3);
//							}
//							#endif
//		/* Mark this internal pseudo-handle as clean */
//							IoFLAGS(GvIOp(gv)) |= IOf_UNTAINT;
//							if ((PerlIO*)PL_rsfp == PerlIO_stdin())
//							IoTYPE(GvIOp(gv)) = IoTYPE_STD;
//							else
//							IoTYPE(GvIOp(gv)) = IoTYPE_RDONLY;
//							#if defined(WIN32) && !defined(PERL_TEXTMODE_SCRIPTS)
//		/* if the script was opened in binmode, we need to revert
//		 * it to text mode for compatibility; but only iff it has CRs
//		 * XXX this is a questionable hack at best. */
//							if (PL_bufend-PL_bufptr > 2
//									&& PL_bufend[-1] == '\n' && PL_bufend[-2] == '\r')
//							{
//								Off_t loc = 0;
//								if (IoTYPE(GvIOp(gv)) == IoTYPE_RDONLY) {
//									loc = PerlIO_tell(PL_rsfp);
//									(void)PerlIO_seek(PL_rsfp, 0L, 0);
//								}
//								#ifdef NETWARE
//								if (PerlLIO_setmode(PL_rsfp, O_TEXT) != -1) {
//									#else
//									if (PerlLIO_setmode(PerlIO_fileno(PL_rsfp), O_TEXT) != -1) {
//										#endif	/* NETWARE */
//										if (loc > 0)
//											PerlIO_seek(PL_rsfp, loc, 0);
//									}
//								}
//								#endif
//								#ifdef PERLIO_LAYERS
//								if (!IN_BYTES) {
//									if (UTF)
//										PerlIO_apply_layers(aTHX_ PL_rsfp, NULL, ":utf8");
//									else if (PL_encoding) {
//										SV *name;
//										dSP;
//										ENTER;
//										SAVETMPS;
//										PUSHMARK(sp);
//										XPUSHs(PL_encoding);
//										PUTBACK;
//										call_method("name", G_SCALAR);
//										SPAGAIN;
//										name = POPs;
//										PUTBACK;
//										PerlIO_apply_layers(aTHX_ PL_rsfp, NULL,
//												Perl_form(aTHX_ ":encoding(%"SVf")",
//														SVfARG(name)));
//										FREETMPS;
//										LEAVE;
//									}
//								}
//								#endif
//									PL_rsfp = NULL;
//							}
//							goto fake_eof;
//						}
//
//						case KEY___SUB__:
//							FUN0OP(CvCLONE(PL_compcv)
//									? newOP(OP_RUNCV, 0)
//									: newPVOP(OP_RUNCV,0,NULL));
//
//						case KEY_AUTOLOAD:
//						case KEY_DESTROY:
//						case KEY_BEGIN:
//						case KEY_UNITCHECK:
//						case KEY_CHECK:
//						case KEY_INIT:
//						case KEY_END:
//							if (PL_expect == XSTATE) {
//								s = PL_bufptr;
//								goto really_sub;
//							}
//							goto just_a_word;
//
//							case_KEY_CORE:
//							{
//								STRLEN olen = len;
//								d = s;
//								s += 2;
//								s = scan_word(s, PL_tokenbuf, sizeof PL_tokenbuf, FALSE, &len);
//								if ((*s == ':' && s[1] == ':')
//								|| (!(tmp = keyword(PL_tokenbuf, len, 1)) && *s == '\''))
//								{
//									s = d;
//									len = olen;
//									Copy(PL_bufptr, PL_tokenbuf, olen, char);
//									goto just_a_word;
//								}
//								if (!tmp)
//									Perl_croak(aTHX_ "CORE::%"UTF8f" is not a keyword",
//											UTF8fARG(UTF, len, PL_tokenbuf));
//								if (tmp < 0)
//									tmp = -tmp;
//								else if (tmp == KEY_require || tmp == KEY_do
//										|| tmp == KEY_glob)
//		    /* that's a way to remember we saw "CORE::" */
//									orig_keyword = tmp;
//								goto reserved_word;
//							}
//
//						case KEY_abs:
//							UNI(OP_ABS);
//
//						case KEY_alarm:
//							UNI(OP_ALARM);
//
//						case KEY_accept:
//							LOP(OP_ACCEPT,XTERM);
//
//						case KEY_and:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_LOWLOGIC)
//								return REPORT(0);
//							OPERATOR(ANDOP);
//
//						case KEY_atan2:
//							LOP(OP_ATAN2,XTERM);
//
//						case KEY_bind:
//							LOP(OP_BIND,XTERM);
//
//						case KEY_binmode:
//							LOP(OP_BINMODE,XTERM);
//
//						case KEY_bless:
//							LOP(OP_BLESS,XTERM);
//
//						case KEY_break:
//							FUN0(OP_BREAK);
//
//						case KEY_chop:
//							UNI(OP_CHOP);
//
//						case KEY_continue:
//		    /* We have to disambiguate the two senses of
//		      "continue". If the next token is a '{' then
//		      treat it as the start of a continue block;
//		      otherwise treat it as a control operator.
//		     */
//							s = skipspace(s);
//							if (*s == '{')
//							PREBLOCK(CONTINUE);
//							else
//							FUN0(OP_CONTINUE);
//
//						case KEY_chdir:
//	    /* may use HOME */
//							(void)gv_fetchpvs("ENV", GV_ADD|GV_NOTQUAL, SVt_PVHV);
//							UNI(OP_CHDIR);
//
//						case KEY_close:
//							UNI(OP_CLOSE);
//
//						case KEY_closedir:
//							UNI(OP_CLOSEDIR);
//
//						case KEY_cmp:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE)
//								return REPORT(0);
//							Eop(OP_SCMP);
//
//						case KEY_caller:
//							UNI(OP_CALLER);
//
//						case KEY_crypt:
//							#ifdef FCRYPT
//							if (!PL_cryptseen) {
//								PL_cryptseen = TRUE;
//								init_des();
//							}
//							#endif
//							LOP(OP_CRYPT,XTERM);
//
//						case KEY_chmod:
//							LOP(OP_CHMOD,XTERM);
//
//						case KEY_chown:
//							LOP(OP_CHOWN,XTERM);
//
//						case KEY_connect:
//							LOP(OP_CONNECT,XTERM);
//
//						case KEY_chr:
//							UNI(OP_CHR);
//
//						case KEY_cos:
//							UNI(OP_COS);
//
//						case KEY_chroot:
//							UNI(OP_CHROOT);
//
//						case KEY_default:
//							PREBLOCK(DEFAULT);
//
//						case KEY_do:
//							s = skipspace(s);
//							if (*s == '{')
//							PRETERMBLOCK(DO);
//							if (*s != '\'') {
//							*PL_tokenbuf = '&';
//							d = scan_word(s, PL_tokenbuf + 1, sizeof PL_tokenbuf - 1,
//									1, &len);
//							if (len && (len != 4 || strNE(PL_tokenbuf+1, "CORE"))
//									&& !keyword(PL_tokenbuf + 1, len, 0)) {
//								d = skipspace(d);
//								if (*d == '(') {
//									force_ident_maybe_lex('&');
//									s = d;
//								}
//							}
//						}
//						if (orig_keyword == KEY_do) {
//							orig_keyword = 0;
//							pl_yylval.ival = 1;
//						}
//						else
//							pl_yylval.ival = 0;
//						OPERATOR(DO);
//
//						case KEY_die:
//							PL_hints |= HINT_BLOCK_SCOPE;
//							LOP(OP_DIE,XTERM);
//
//						case KEY_defined:
//							UNI(OP_DEFINED);
//
//						case KEY_delete:
//							UNI(OP_DELETE);
//
//						case KEY_dbmopen:
//							Perl_populate_isa(aTHX_ STR_WITH_LEN("AnyDBM_File::ISA"),
//									STR_WITH_LEN("NDBM_File::"),
//									STR_WITH_LEN("DB_File::"),
//									STR_WITH_LEN("GDBM_File::"),
//									STR_WITH_LEN("SDBM_File::"),
//									STR_WITH_LEN("ODBM_File::"),
//									NULL);
//							LOP(OP_DBMOPEN,XTERM);
//
//						case KEY_dbmclose:
//							UNI(OP_DBMCLOSE);
//
//						case KEY_dump:
//							LOOPX(OP_DUMP);
//
//						case KEY_else:
//							PREBLOCK(ELSE);
//
//						case KEY_elsif:
//							pl_yylval.ival = CopLINE(PL_curcop);
//							OPERATOR(ELSIF);
//
//						case KEY_eq:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE)
//								return REPORT(0);
//							Eop(OP_SEQ);
//
//						case KEY_exists:
//							UNI(OP_EXISTS);
//
//						case KEY_exit:
//							UNI(OP_EXIT);
//
//						case KEY_eval:
//							s = skipspace(s);
//							if (*s == '{') { /* block eval */
//							PL_expect = XTERMBLOCK;
//							UNIBRACK(OP_ENTERTRY);
//						}
//						else { /* string eval */
//							PL_expect = XTERM;
//							UNIBRACK(OP_ENTEREVAL);
//						}
//
//						case KEY_evalbytes:
//							PL_expect = XTERM;
//							UNIBRACK(-OP_ENTEREVAL);
//
//						case KEY_eof:
//							UNI(OP_EOF);
//
//						case KEY_exp:
//							UNI(OP_EXP);
//
//						case KEY_each:
//							UNI(OP_EACH);
//
//						case KEY_exec:
//							LOP(OP_EXEC,XREF);
//
//						case KEY_endhostent:
//							FUN0(OP_EHOSTENT);
//
//						case KEY_endnetent:
//							FUN0(OP_ENETENT);
//
//						case KEY_endservent:
//							FUN0(OP_ESERVENT);
//
//						case KEY_endprotoent:
//							FUN0(OP_EPROTOENT);
//
//						case KEY_endpwent:
//							FUN0(OP_EPWENT);
//
//						case KEY_endgrent:
//							FUN0(OP_EGRENT);
//
//						case KEY_for:
//						case KEY_foreach:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							pl_yylval.ival = CopLINE(PL_curcop);
//							s = skipspace(s);
//							if (PL_expect == XSTATE && isIDFIRST_lazy_if(s,UTF)) {
//								char *p = s;
//
//								if ((PL_bufend - p) >= 3 &&
//										strnEQ(p, "my", 2) && isSPACE(*(p + 2)))
//								p += 2;
//								else if ((PL_bufend - p) >= 4 &&
//										strnEQ(p, "our", 3) && isSPACE(*(p + 3)))
//								p += 3;
//								p = skipspace(p);
//                /* skip optional package name, as in "for my abc $x (..)" */
//								if (isIDFIRST_lazy_if(p,UTF)) {
//									p = scan_word(p, PL_tokenbuf, sizeof PL_tokenbuf, TRUE, &len);
//									p = skipspace(p);
//								}
//								if (*p != '$')
//								Perl_croak(aTHX_ "Missing $ on loop variable");
//							}
//							OPERATOR(FOR);
//
//						case KEY_formline:
//							LOP(OP_FORMLINE,XTERM);
//
//						case KEY_fork:
//							FUN0(OP_FORK);
//
//						case KEY_fc:
//							UNI(OP_FC);
//
//						case KEY_fcntl:
//							LOP(OP_FCNTL,XTERM);
//
//						case KEY_fileno:
//							UNI(OP_FILENO);
//
//						case KEY_flock:
//							LOP(OP_FLOCK,XTERM);
//
//						case KEY_gt:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE)
//								return REPORT(0);
//							Rop(OP_SGT);
//
//						case KEY_ge:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE)
//								return REPORT(0);
//							Rop(OP_SGE);
//
//						case KEY_grep:
//							LOP(OP_GREPSTART, XREF);
//
//						case KEY_goto:
//							LOOPX(OP_GOTO);
//
//						case KEY_gmtime:
//							UNI(OP_GMTIME);
//
//						case KEY_getc:
//							UNIDOR(OP_GETC);
//
//						case KEY_getppid:
//							FUN0(OP_GETPPID);
//
//						case KEY_getpgrp:
//							UNI(OP_GETPGRP);
//
//						case KEY_getpriority:
//							LOP(OP_GETPRIORITY,XTERM);
//
//						case KEY_getprotobyname:
//							UNI(OP_GPBYNAME);
//
//						case KEY_getprotobynumber:
//							LOP(OP_GPBYNUMBER,XTERM);
//
//						case KEY_getprotoent:
//							FUN0(OP_GPROTOENT);
//
//						case KEY_getpwent:
//							FUN0(OP_GPWENT);
//
//						case KEY_getpwnam:
//							UNI(OP_GPWNAM);
//
//						case KEY_getpwuid:
//							UNI(OP_GPWUID);
//
//						case KEY_getpeername:
//							UNI(OP_GETPEERNAME);
//
//						case KEY_gethostbyname:
//							UNI(OP_GHBYNAME);
//
//						case KEY_gethostbyaddr:
//							LOP(OP_GHBYADDR,XTERM);
//
//						case KEY_gethostent:
//							FUN0(OP_GHOSTENT);
//
//						case KEY_getnetbyname:
//							UNI(OP_GNBYNAME);
//
//						case KEY_getnetbyaddr:
//							LOP(OP_GNBYADDR,XTERM);
//
//						case KEY_getnetent:
//							FUN0(OP_GNETENT);
//
//						case KEY_getservbyname:
//							LOP(OP_GSBYNAME,XTERM);
//
//						case KEY_getservbyport:
//							LOP(OP_GSBYPORT,XTERM);
//
//						case KEY_getservent:
//							FUN0(OP_GSERVENT);
//
//						case KEY_getsockname:
//							UNI(OP_GETSOCKNAME);
//
//						case KEY_getsockopt:
//							LOP(OP_GSOCKOPT,XTERM);
//
//						case KEY_getgrent:
//							FUN0(OP_GGRENT);
//
//						case KEY_getgrnam:
//							UNI(OP_GGRNAM);
//
//						case KEY_getgrgid:
//							UNI(OP_GGRGID);
//
//						case KEY_getlogin:
//							FUN0(OP_GETLOGIN);
//
//						case KEY_given:
//							pl_yylval.ival = CopLINE(PL_curcop);
//							Perl_ck_warner_d(aTHX_
//									packWARN(WARN_EXPERIMENTAL__SMARTMATCH),
//									"given is experimental");
//							OPERATOR(GIVEN);
//
//						case KEY_glob:
//							LOP(
//									orig_keyword==KEY_glob ? -OP_GLOB : OP_GLOB,
//									XTERM
//							);
//
//						case KEY_hex:
//							UNI(OP_HEX);
//
//						case KEY_if:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							pl_yylval.ival = CopLINE(PL_curcop);
//							OPERATOR(IF);
//
//						case KEY_index:
//							LOP(OP_INDEX,XTERM);
//
//						case KEY_int:
//							UNI(OP_INT);
//
//						case KEY_ioctl:
//							LOP(OP_IOCTL,XTERM);
//
//						case KEY_join:
//							LOP(OP_JOIN,XTERM);
//
//						case KEY_keys:
//							UNI(OP_KEYS);
//
//						case KEY_kill:
//							LOP(OP_KILL,XTERM);
//
//						case KEY_last:
//							LOOPX(OP_LAST);
//
//						case KEY_lc:
//							UNI(OP_LC);
//
//						case KEY_lcfirst:
//							UNI(OP_LCFIRST);
//
//						case KEY_local:
//							pl_yylval.ival = 0;
//							OPERATOR(LOCAL);
//
//						case KEY_length:
//							UNI(OP_LENGTH);
//
//						case KEY_lt:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE)
//								return REPORT(0);
//							Rop(OP_SLT);
//
//						case KEY_le:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE)
//								return REPORT(0);
//							Rop(OP_SLE);
//
//						case KEY_localtime:
//							UNI(OP_LOCALTIME);
//
//						case KEY_log:
//							UNI(OP_LOG);
//
//						case KEY_link:
//							LOP(OP_LINK,XTERM);
//
//						case KEY_listen:
//							LOP(OP_LISTEN,XTERM);
//
//						case KEY_lock:
//							UNI(OP_LOCK);
//
//						case KEY_lstat:
//							UNI(OP_LSTAT);
//
//						case KEY_m:
//							s = scan_pat(s,OP_MATCH);
//							TERM(sublex_start());
//
//						case KEY_map:
//							LOP(OP_MAPSTART, XREF);
//
//						case KEY_mkdir:
//							LOP(OP_MKDIR,XTERM);
//
//						case KEY_msgctl:
//							LOP(OP_MSGCTL,XTERM);
//
//						case KEY_msgget:
//							LOP(OP_MSGGET,XTERM);
//
//						case KEY_msgrcv:
//							LOP(OP_MSGRCV,XTERM);
//
//						case KEY_msgsnd:
//							LOP(OP_MSGSND,XTERM);
//
//						case KEY_our:
//						case KEY_my:
//						case KEY_state:
//							PL_in_my = (U16)tmp;
//							s = skipspace(s);
//							if (isIDFIRST_lazy_if(s,UTF)) {
//								s = scan_word(s, PL_tokenbuf, sizeof PL_tokenbuf, TRUE, &len);
//								if (len == 3 && strnEQ(PL_tokenbuf, "sub", 3))
//								{
//									if (!FEATURE_LEXSUBS_IS_ENABLED)
//										Perl_croak(aTHX_
//												"Experimental \"%s\" subs not enabled",
//												tmp == KEY_my    ? "my"    :
//														tmp == KEY_state ? "state" : "our");
//									Perl_ck_warner_d(aTHX_
//											packWARN(WARN_EXPERIMENTAL__LEXICAL_SUBS),
//											"The lexical_subs feature is experimental");
//									goto really_sub;
//								}
//								PL_in_my_stash = find_in_my_stash(PL_tokenbuf, len);
//								if (!PL_in_my_stash) {
//									char tmpbuf[1024];
//									int len;
//									PL_bufptr = s;
//									len = my_snprintf(tmpbuf, sizeof(tmpbuf), "No such class %.1000s", PL_tokenbuf);
//									PERL_MY_SNPRINTF_POST_GUARD(len, sizeof(tmpbuf));
//									yyerror_pv(tmpbuf, UTF ? SVf_UTF8 : 0);
//								}
//							}
//							pl_yylval.ival = 1;
//							OPERATOR(MY);
//
//						case KEY_next:
//							LOOPX(OP_NEXT);
//
//						case KEY_ne:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_COMPARE)
//								return REPORT(0);
//							Eop(OP_SNE);
//
//						case KEY_no:
//							s = tokenize_use(0, s);
//							TOKEN(USE);
//
//						case KEY_not:
//							if (*s == '(' || (s = skipspace(s), *s == '('))
//							FUN1(OP_NOT);
//							else {
//							if (!PL_lex_allbrackets &&
//									PL_lex_fakeeof > LEX_FAKEEOF_LOWLOGIC)
//								PL_lex_fakeeof = LEX_FAKEEOF_LOWLOGIC;
//							OPERATOR(NOTOP);
//						}
//
//						case KEY_open:
//							s = skipspace(s);
//							if (isIDFIRST_lazy_if(s,UTF)) {
//								const char *t;
//								d = scan_word(s, PL_tokenbuf, sizeof PL_tokenbuf, FALSE,
//										&len);
//								for (t=d; isSPACE(*t);)
//								t++;
//								if ( *t && strchr("|&*+-=!?:.", *t) && ckWARN_d(WARN_PRECEDENCE)
//		    /* [perl #16184] */
//										&& !(t[0] == '=' && t[1] == '>')
//										&& !(t[0] == ':' && t[1] == ':')
//										&& !keyword(s, d-s, 0)
//								) {
//									Perl_warner(aTHX_ packWARN(WARN_PRECEDENCE),
//											"Precedence problem: open %"UTF8f" should be open(%"UTF8f")",
//											UTF8fARG(UTF, d-s, s), UTF8fARG(UTF, d-s, s));
//								}
//							}
//							LOP(OP_OPEN,XTERM);
//
//						case KEY_or:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_LOWLOGIC)
//								return REPORT(0);
//							pl_yylval.ival = OP_OR;
//							OPERATOR(OROP);
//
//						case KEY_ord:
//							UNI(OP_ORD);
//
//						case KEY_oct:
//							UNI(OP_OCT);
//
//						case KEY_opendir:
//							LOP(OP_OPEN_DIR,XTERM);
//
//						case KEY_print:
//							checkcomma(s,PL_tokenbuf,"filehandle");
//							LOP(OP_PRINT,XREF);
//
//						case KEY_printf:
//							checkcomma(s,PL_tokenbuf,"filehandle");
//							LOP(OP_PRTF,XREF);
//
//						case KEY_prototype:
//							UNI(OP_PROTOTYPE);
//
//						case KEY_push:
//							LOP(OP_PUSH,XTERM);
//
//						case KEY_pop:
//							UNIDOR(OP_POP);
//
//						case KEY_pos:
//							UNIDOR(OP_POS);
//
//						case KEY_pack:
//							LOP(OP_PACK,XTERM);
//
//						case KEY_package:
//							s = force_word(s,WORD,FALSE,TRUE);
//							s = skipspace(s);
//							s = force_strict_version(s);
//							PREBLOCK(PACKAGE);
//
//						case KEY_pipe:
//							LOP(OP_PIPE_OP,XTERM);
//
//						case KEY_q:
//							s = scan_str(s,FALSE,FALSE,FALSE,NULL);
//							if (!s)
//								missingterm(NULL);
//							COPLINE_SET_FROM_MULTI_END;
//							pl_yylval.ival = OP_CONST;
//							TERM(sublex_start());
//
//						case KEY_quotemeta:
//							UNI(OP_QUOTEMETA);
//
//						case KEY_qw: {
//							OP *words = NULL;
//							s = scan_str(s,FALSE,FALSE,FALSE,NULL);
//							if (!s)
//								missingterm(NULL);
//							COPLINE_SET_FROM_MULTI_END;
//							PL_expect = XOPERATOR;
//							if (SvCUR(PL_lex_stuff)) {
//								int warned_comma = !ckWARN(WARN_QW);
//								int warned_comment = warned_comma;
//								d = SvPV_force(PL_lex_stuff, len);
//								while (len) {
//									for (; isSPACE(*d) && len; --len, ++d)
//			/**/;
//									if (len) {
//										SV *sv;
//										const char *b = d;
//										if (!warned_comma || !warned_comment) {
//											for (; !isSPACE(*d) && len; --len, ++d) {
//												if (!warned_comma && *d == ',') {
//													Perl_warner(aTHX_ packWARN(WARN_QW),
//															"Possible attempt to separate words with commas");
//													++warned_comma;
//												}
//												else if (!warned_comment && *d == '#') {
//													Perl_warner(aTHX_ packWARN(WARN_QW),
//															"Possible attempt to put comments in qw() list");
//													++warned_comment;
//												}
//											}
//										}
//										else {
//											for (; !isSPACE(*d) && len; --len, ++d)
//				/**/;
//										}
//										sv = newSVpvn_utf8(b, d-b, DO_UTF8(PL_lex_stuff));
//										words = op_append_elem(OP_LIST, words,
//												newSVOP(OP_CONST, 0, tokeq(sv)));
//									}
//								}
//							}
//							if (!words)
//								words = newNULLLIST();
//							if (PL_lex_stuff) {
//								SvREFCNT_dec(PL_lex_stuff);
//								PL_lex_stuff = NULL;
//							}
//							PL_expect = XOPERATOR;
//							pl_yylval.opval = sawparens(words);
//							TOKEN(QWLIST);
//						}
//
//						case KEY_qq:
//							s = scan_str(s,FALSE,FALSE,FALSE,NULL);
//							if (!s)
//								missingterm(NULL);
//							pl_yylval.ival = OP_STRINGIFY;
//							if (SvIVX(PL_lex_stuff) == '\'')
//								SvIV_set(PL_lex_stuff, 0);	/* qq'$foo' should interpolate */
//							TERM(sublex_start());
//
//						case KEY_qr:
//							s = scan_pat(s,OP_QR);
//							TERM(sublex_start());
//
//						case KEY_qx:
//							s = scan_str(s,FALSE,FALSE,FALSE,NULL);
//							if (!s)
//								missingterm(NULL);
//							pl_yylval.ival = OP_BACKTICK;
//							TERM(sublex_start());
//
//						case KEY_return:
//							OLDLOP(OP_RETURN);
//
//						case KEY_require:
//							s = skipspace(s);
//							if (isDIGIT(*s)) {
//							s = force_version(s, FALSE);
//						}
//						else if (*s != 'v' || !isDIGIT(s[1])
//								|| (s = force_version(s, TRUE), *s == 'v'))
//						{
//							*PL_tokenbuf = '\0';
//							s = force_word(s,WORD,TRUE,TRUE);
//							if (isIDFIRST_lazy_if(PL_tokenbuf,UTF))
//								gv_stashpvn(PL_tokenbuf, strlen(PL_tokenbuf),
//										GV_ADD | (UTF ? SVf_UTF8 : 0));
//							else if (*s == '<')
//							yyerror("<> at require-statement should be quotes");
//						}
//						if (orig_keyword == KEY_require) {
//							orig_keyword = 0;
//							pl_yylval.ival = 1;
//						}
//						else
//							pl_yylval.ival = 0;
//						PL_expect = PL_nexttoke ? XOPERATOR : XTERM;
//						PL_bufptr = s;
//						PL_last_uni = PL_oldbufptr;
//						PL_last_lop_op = OP_REQUIRE;
//						s = skipspace(s);
//						return REPORT( (int)REQUIRE );
//
//						case KEY_reset:
//							UNI(OP_RESET);
//
//						case KEY_redo:
//							LOOPX(OP_REDO);
//
//						case KEY_rename:
//							LOP(OP_RENAME,XTERM);
//
//						case KEY_rand:
//							UNI(OP_RAND);
//
//						case KEY_rmdir:
//							UNI(OP_RMDIR);
//
//						case KEY_rindex:
//							LOP(OP_RINDEX,XTERM);
//
//						case KEY_read:
//							LOP(OP_READ,XTERM);
//
//						case KEY_readdir:
//							UNI(OP_READDIR);
//
//						case KEY_readline:
//							UNIDOR(OP_READLINE);
//
//						case KEY_readpipe:
//							UNIDOR(OP_BACKTICK);
//
//						case KEY_rewinddir:
//							UNI(OP_REWINDDIR);
//
//						case KEY_recv:
//							LOP(OP_RECV,XTERM);
//
//						case KEY_reverse:
//							LOP(OP_REVERSE,XTERM);
//
//						case KEY_readlink:
//							UNIDOR(OP_READLINK);
//
//						case KEY_ref:
//							UNI(OP_REF);
//
//						case KEY_s:
//							s = scan_subst(s);
//							if (pl_yylval.opval)
//								TERM(sublex_start());
//							else
//								TOKEN(1);	/* force error */
//
//						case KEY_say:
//							checkcomma(s,PL_tokenbuf,"filehandle");
//							LOP(OP_SAY,XREF);
//
//						case KEY_chomp:
//							UNI(OP_CHOMP);
//
//						case KEY_scalar:
//							UNI(OP_SCALAR);
//
//						case KEY_select:
//							LOP(OP_SELECT,XTERM);
//
//						case KEY_seek:
//							LOP(OP_SEEK,XTERM);
//
//						case KEY_semctl:
//							LOP(OP_SEMCTL,XTERM);
//
//						case KEY_semget:
//							LOP(OP_SEMGET,XTERM);
//
//						case KEY_semop:
//							LOP(OP_SEMOP,XTERM);
//
//						case KEY_send:
//							LOP(OP_SEND,XTERM);
//
//						case KEY_setpgrp:
//							LOP(OP_SETPGRP,XTERM);
//
//						case KEY_setpriority:
//							LOP(OP_SETPRIORITY,XTERM);
//
//						case KEY_sethostent:
//							UNI(OP_SHOSTENT);
//
//						case KEY_setnetent:
//							UNI(OP_SNETENT);
//
//						case KEY_setservent:
//							UNI(OP_SSERVENT);
//
//						case KEY_setprotoent:
//							UNI(OP_SPROTOENT);
//
//						case KEY_setpwent:
//							FUN0(OP_SPWENT);
//
//						case KEY_setgrent:
//							FUN0(OP_SGRENT);
//
//						case KEY_seekdir:
//							LOP(OP_SEEKDIR,XTERM);
//
//						case KEY_setsockopt:
//							LOP(OP_SSOCKOPT,XTERM);
//
//						case KEY_shift:
//							UNIDOR(OP_SHIFT);
//
//						case KEY_shmctl:
//							LOP(OP_SHMCTL,XTERM);
//
//						case KEY_shmget:
//							LOP(OP_SHMGET,XTERM);
//
//						case KEY_shmread:
//							LOP(OP_SHMREAD,XTERM);
//
//						case KEY_shmwrite:
//							LOP(OP_SHMWRITE,XTERM);
//
//						case KEY_shutdown:
//							LOP(OP_SHUTDOWN,XTERM);
//
//						case KEY_sin:
//							UNI(OP_SIN);
//
//						case KEY_sleep:
//							UNI(OP_SLEEP);
//
//						case KEY_socket:
//							LOP(OP_SOCKET,XTERM);
//
//						case KEY_socketpair:
//							LOP(OP_SOCKPAIR,XTERM);
//
//						case KEY_sort:
//							checkcomma(s,PL_tokenbuf,"subroutine name");
//							s = skipspace(s);
//							PL_expect = XTERM;
//							s = force_word(s,WORD,TRUE,TRUE);
//							LOP(OP_SORT,XREF);
//
//						case KEY_split:
//							LOP(OP_SPLIT,XTERM);
//
//						case KEY_sprintf:
//							LOP(OP_SPRINTF,XTERM);
//
//						case KEY_splice:
//							LOP(OP_SPLICE,XTERM);
//
//						case KEY_sqrt:
//							UNI(OP_SQRT);
//
//						case KEY_srand:
//							UNI(OP_SRAND);
//
//						case KEY_stat:
//							UNI(OP_STAT);
//
//						case KEY_study:
//							UNI(OP_STUDY);
//
//						case KEY_substr:
//							LOP(OP_SUBSTR,XTERM);
//
//						case KEY_format:
//						case KEY_sub:
//							really_sub:
//							{
//								char * const tmpbuf = PL_tokenbuf + 1;
//								expectation attrful;
//								bool have_name, have_proto;
//								const int key = tmp;
//								SV *format_name = NULL;
//
//								d = s;
//								s = skipspace(s);
//
//								if (isIDFIRST_lazy_if(s,UTF) || *s == '\'' ||
//									(*s == ':' && s[1] == ':'))
//								{
//
//									PL_expect = XBLOCK;
//									attrful = XATTRBLOCK;
//									d = scan_word(s, tmpbuf, sizeof PL_tokenbuf - 1, TRUE,
//											&len);
//									if (key == KEY_format)
//										format_name = S_newSV_maybe_utf8(aTHX_ s, d - s);
//									*PL_tokenbuf = '&';
//									if (memchr(tmpbuf, ':', len) || key != KEY_sub
//											|| pad_findmy_pvn(
//											PL_tokenbuf, len + 1, UTF ? SVf_UTF8 : 0
//									) != NOT_IN_PAD)
//										sv_setpvn(PL_subname, tmpbuf, len);
//									else {
//										sv_setsv(PL_subname,PL_curstname);
//										sv_catpvs(PL_subname,"::");
//										sv_catpvn(PL_subname,tmpbuf,len);
//									}
//									if (SvUTF8(PL_linestr))
//										SvUTF8_on(PL_subname);
//									have_name = TRUE;
//
//
//									s = skipspace(d);
//								}
//								else {
//								if (key == KEY_my || key == KEY_our || key==KEY_state)
//								{
//									*d = '\0';
//			/* diag_listed_as: Missing name in "%s sub" */
//									Perl_croak(aTHX_
//											"Missing name in \"%s\"", PL_bufptr);
//								}
//								PL_expect = XTERMBLOCK;
//								attrful = XATTRTERM;
//								sv_setpvs(PL_subname,"?");
//								have_name = FALSE;
//							}
//
//								if (key == KEY_format) {
//									if (format_name) {
//										NEXTVAL_NEXTTOKE.opval
//												= (OP*)newSVOP(OP_CONST,0, format_name);
//										NEXTVAL_NEXTTOKE.opval->op_private |= OPpCONST_BARE;
//										force_next(WORD);
//									}
//									PREBLOCK(FORMAT);
//								}
//
//		/* Look for a prototype */
//								if (*s == '(' && !FEATURE_SIGNATURES_IS_ENABLED) {
//								s = scan_str(s,FALSE,FALSE,FALSE,NULL);
//								COPLINE_SET_FROM_MULTI_END;
//								if (!s)
//									Perl_croak(aTHX_ "Prototype not terminated");
//								(void)validate_proto(PL_subname, PL_lex_stuff, ckWARN(WARN_ILLEGALPROTO));
//								have_proto = TRUE;
//
//								s = skipspace(s);
//							}
//								else
//								have_proto = FALSE;
//
//								if (*s == ':' && s[1] != ':')
//								PL_expect = attrful;
//								else if ((*s != '{' && *s != '(') && key == KEY_sub) {
//								if (!have_name)
//									Perl_croak(aTHX_ "Illegal declaration of anonymous subroutine");
//								else if (*s != ';' && *s != '}')
//								Perl_croak(aTHX_ "Illegal declaration of subroutine %"SVf, SVfARG(PL_subname));
//							}
//
//								if (have_proto) {
//									NEXTVAL_NEXTTOKE.opval =
//											(OP*)newSVOP(OP_CONST, 0, PL_lex_stuff);
//									PL_lex_stuff = NULL;
//									force_next(THING);
//								}
//								if (!have_name) {
//									if (PL_curstash)
//										sv_setpvs(PL_subname, "__ANON__");
//									else
//										sv_setpvs(PL_subname, "__ANON__::__ANON__");
//									TOKEN(ANONSUB);
//								}
//								force_ident_maybe_lex('&');
//								TOKEN(SUB);
//							}
//
//						case KEY_system:
//							LOP(OP_SYSTEM,XREF);
//
//						case KEY_symlink:
//							LOP(OP_SYMLINK,XTERM);
//
//						case KEY_syscall:
//							LOP(OP_SYSCALL,XTERM);
//
//						case KEY_sysopen:
//							LOP(OP_SYSOPEN,XTERM);
//
//						case KEY_sysseek:
//							LOP(OP_SYSSEEK,XTERM);
//
//						case KEY_sysread:
//							LOP(OP_SYSREAD,XTERM);
//
//						case KEY_syswrite:
//							LOP(OP_SYSWRITE,XTERM);
//
//						case KEY_tr:
//						case KEY_y:
//							s = scan_trans(s);
//							TERM(sublex_start());
//
//						case KEY_tell:
//							UNI(OP_TELL);
//
//						case KEY_telldir:
//							UNI(OP_TELLDIR);
//
//						case KEY_tie:
//							LOP(OP_TIE,XTERM);
//
//						case KEY_tied:
//							UNI(OP_TIED);
//
//						case KEY_time:
//							FUN0(OP_TIME);
//
//						case KEY_times:
//							FUN0(OP_TMS);
//
//						case KEY_truncate:
//							LOP(OP_TRUNCATE,XTERM);
//
//						case KEY_uc:
//							UNI(OP_UC);
//
//						case KEY_ucfirst:
//							UNI(OP_UCFIRST);
//
//						case KEY_untie:
//							UNI(OP_UNTIE);
//
//						case KEY_until:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							pl_yylval.ival = CopLINE(PL_curcop);
//							OPERATOR(UNTIL);
//
//						case KEY_unless:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							pl_yylval.ival = CopLINE(PL_curcop);
//							OPERATOR(UNLESS);
//
//						case KEY_unlink:
//							LOP(OP_UNLINK,XTERM);
//
//						case KEY_undef:
//							UNIDOR(OP_UNDEF);
//
//						case KEY_unpack:
//							LOP(OP_UNPACK,XTERM);
//
//						case KEY_utime:
//							LOP(OP_UTIME,XTERM);
//
//						case KEY_umask:
//							UNIDOR(OP_UMASK);
//
//						case KEY_unshift:
//							LOP(OP_UNSHIFT,XTERM);
//
//						case KEY_use:
//							s = tokenize_use(1, s);
//							TOKEN(USE);
//
//						case KEY_values:
//							UNI(OP_VALUES);
//
//						case KEY_vec:
//							LOP(OP_VEC,XTERM);
//
//						case KEY_when:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							pl_yylval.ival = CopLINE(PL_curcop);
//							Perl_ck_warner_d(aTHX_
//									packWARN(WARN_EXPERIMENTAL__SMARTMATCH),
//									"when is experimental");
//							OPERATOR(WHEN);
//
//						case KEY_while:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							pl_yylval.ival = CopLINE(PL_curcop);
//							OPERATOR(WHILE);
//
//						case KEY_warn:
//							PL_hints |= HINT_BLOCK_SCOPE;
//							LOP(OP_WARN,XTERM);
//
//						case KEY_wait:
//							FUN0(OP_WAIT);
//
//						case KEY_waitpid:
//							LOP(OP_WAITPID,XTERM);
//
//						case KEY_wantarray:
//							FUN0(OP_WANTARRAY);
//
//						case KEY_write:
//            /* Make sure $^L is defined. 0x0C is CTRL-L on ASCII platforms, and
//             * we use the same number on EBCDIC */
//							gv_fetchpvs("\x0C", GV_ADD|GV_NOTQUAL, SVt_PV);
//							UNI(OP_ENTERWRITE);
//
//						case KEY_x:
//							if (PL_expect == XOPERATOR) {
//								if (*s == '=' && !PL_lex_allbrackets &&
//										PL_lex_fakeeof >= LEX_FAKEEOF_ASSIGN)
//								return REPORT(0);
//								Mop(OP_REPEAT);
//							}
//							check_uni();
//							goto just_a_word;
//
//						case KEY_xor:
//							if (!PL_lex_allbrackets && PL_lex_fakeeof >= LEX_FAKEEOF_LOWLOGIC)
//								return REPORT(0);
//							pl_yylval.ival = OP_XOR;
//							OPERATOR(OROP);
//					}
//				}}
//		*/
	}

}