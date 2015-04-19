package com.perl5.lang.lexer.ported;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.lexer.PerlTokenTypes;

import java.io.IOException;
import java.util.Arrays;

import static com.perl5.lang.lexer.ported.CharClass.isBLANK;
import static com.perl5.lang.lexer.ported.CharClass.isALPHA;
import static com.perl5.lang.lexer.ported.CharClass.isDIGIT;
import static com.perl5.lang.lexer.ported.CharClass.isSPACE;

/**
 * Created by hurricup on 18.04.2015.
 * Attempt to port toke.c from perl 5.21.6
 * Cloned from JFlex generated PerlLexer
 */
public class Toke implements FlexLexer, PerlTokenTypes, LexicalStates, Keywords, Handy, Perl, SV, opnames
{
	public static final char XENUMMASK  = 0x3f;
	public static final char XFAKEEOF   = 0x40;
	public static final char XFAKEBRACK = 0x80;

	private CharSequence sequenceBuffer = "";

//	private int markedPosition;
	private static java.io.Reader zzReader = null; // Fake
	private yy_parser PL_parser;	// in toke.c it seems local variable, current parser
	COP PL_curcop;

	public Toke(java.io.Reader in)
	{
		zzReader = in;
	}

	/**
	 * Creates a new scanner.
	 * There is also java.io.Reader version of this constructor.
	 *
	 * @param   in  the java.io.Inputstream to read input from.
	 */
	public Toke(java.io.InputStream in) {
		this(new java.io.InputStreamReader(in));
	}


	@Override
	public void yybegin(int state)
	{
		PL_parser.lex_state = state;
	}

	@Override
	public int yystate()
	{
		return PL_parser.lex_state;
	}

	@Override
	public int getTokenStart()
	{
		return PL_parser.oldbufptr;
	}

	@Override
	public int getTokenEnd()
	{
		return PL_parser.bufptr;
	}

	/**
	 * Returns the text matched by the current regular expression (current token).
	 */
	public final CharSequence yytext() {
		System.err.printf("Requested yytext %u-%u\n", getTokenStart(), getTokenEnd());
		return sequenceBuffer.subSequence(getTokenStart(), getTokenEnd());
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
		return PL_parser.linestr != null
				? PL_parser.linestr[getTokenStart()+pos]
				: sequenceBuffer.charAt(getTokenStart()+pos);
	}


	/**
	 * Returns the length of the matched text region.
	 */
	public final int yylength() {
		return getTokenEnd() - getTokenStart();
	}


	/*
		Starting point
	 */
	@Override
	public void reset(CharSequence buffer, int start, int end, int initialState)
	{
		System.err.printf("Reset lexer to %d %d %d\n", start, end, initialState);

		sequenceBuffer = buffer;

/* START OF
 void
	Perl_lex_start(pTHX_ SV *line, PerlIO *rsfp, U32 flags)
*/
		yy_parser parser, oparser;

//    const char PL_parser.linestr[s] = NULL;


//     create and initialise a parser
		parser = new yy_parser();

		parser.old_parser = oparser = PL_parser;
		PL_parser = parser;

		parser.stack_size = 0;

//    /* on scope exit, free this parser and restore any outer one
		parser.saved_curcop = PL_curcop;

//    /* initialise lexer state

		parser.nexttoke = 0;
		parser.error_count = oparser != null
				? oparser.error_count
				: 0;
		parser.copline = parser.preambling = NOLINE;
		parser.lex_state = LEX_NORMAL;
		parser.expect = expectation.XSTATE;


		//  Source filtering releted, not used
//		parser.rsfp_filters =
//				!(flags & LEX_START_SAME_FILTER) || !oparser
//						? NULL
//						: MUTABLE_AV(SvREFCNT_inc(
//						oparser->rsfp_filters
//								? oparser->rsfp_filters
//								: (oparser->rsfp_filters = newAV())
//				));


		parser.lex_casestack[0] = '\0';
		parser.linestr = com.intellij.util.text.CharArrayUtil.fromSequenceWithoutCopying(buffer);;
		parser.oldoldbufptr = parser.oldbufptr = parser.bufptr = parser.linestart = 0;
		parser.bufend = end;
		parser.in_pod = parser.filtered = 0;
/* END OF
 void
	Perl_lex_start(pTHX_ SV *line, PerlIO *rsfp, U32 flags)
*/

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
		System.err.printf("Invoked advance for %d of %d\n", PL_parser.bufptr, PL_parser.bufend);
		if (PL_parser.bufptr == PL_parser.bufend)    // end of file
			return null;

		IElementType tokenType = stateSwitch();

		if( tokenType == null )
			charSwitch();

		if( tokenType == null )
		{
			PL_parser.oldoldbufptr = PL_parser.oldbufptr;
			PL_parser.oldbufptr = PL_parser.bufptr;
			PL_parser.bufptr++;
			tokenType = PERL_BAD_CHARACTER;
		}

		return tokenType;
	}

	public IElementType REPORT(IElementType elementType)
	{
		// some debugging may be here
		return elementType;
	}

	private int memchr( int startOffset, char searchedChar, int maxChars)
	{
		for( int i = 0; i < maxChars; i++ )
		{
			if( PL_parser.linestr[i+startOffset] == searchedChar)
			{
				return startOffset + i;
			}
		}
		return -1;
	}

	private String sv_catpvn(String currentString, int startOffset, int charsNumber)
	{
		return currentString.concat(new String(
			Arrays.copyOfRange(PL_parser.linestr, startOffset, startOffset + charsNumber - 1)
		));
	}

	/*
	#define CopLINE(c)      ((c)->cop_line)
		#define CopLINE_inc(c)      (++CopLINE(c))
		#define CopLINE_dec(c)      (--CopLINE(c))
		#define CopLINE_set(c,l)    (CopLINE(c) = (l))

	void COPLINE_INC_WITH_HERELINES(){
		CopLINE_inc(PL_curcop);
		if (PL_parser.herelines > 0)
			CopLINE(PL_curcop) += PL_parser.herelines,
			PL_parser.herelines = 0;
	}
	*/

/*
 * S_incline
 * This subroutine has nothing to do with tilting, whether at windmills
 * or pinball tables.  Its name is short for "increment line".  It
 * increments the current line number in CopLINE(PL_curcop) and checks
 * to see whether the line starts with a comment of the form
 *    # line 500 "foo.pm"
 * If so, it sets the current line number and file to the values in the comment.
 */

//	void incline(int s)
//	{
//		int t;
//		int n;
//		int e;
//		int line_num;
//
////		PERL_ARGS_ASSERT_INCLINE;
//
//		COPLINE_INC_WITH_HERELINES();
//
//		if (
//			//!PL_rsfp
//			// && !PL_parser.filtered
//			PL_parser.lex_state == LEX_NORMAL
//			&& s+1 == PL_parser.bufend
//			&& PL_parser.linestr[s] == ';')
//		{
//		/* fake newline in string eval */
//			CopLINE_dec(PL_curcop);
//			return;
//		}
//		if (PL_parser.linestr[s]++ != '#')
//			return;
//		while (isBLANK(PL_parser.linestr[s]))
//			s++;
//		if (strnEQ(s, "line", 4))
//			s += 4;
//		else
//			return;
//		if (isBLANK(PL_parser.linestr[s]))
//			s++;
//		else
//			return;
//
//		while (isBLANK(PL_parser.linestr[s]))
//			s++;
//		if (!isDIGIT(PL_parser.linestr[s]))
//			return;
//
//		n = s;
//		while (isDIGIT(PL_parser.linestr[s]))
//			s++;
//		if (!isBLANK(PL_parser.linestr[s]) && PL_parser.linestr[s] != '\r' && PL_parser.linestr[s] != '\n' && PL_parser.linestr[s] != '\0')
//			return;
//		while (isBLANK(PL_parser.linestr[s]))
//			s++;
//		if (PL_parser.linestr[s] == '"' && (t = strchr(s+1, '"')))
//		{
//			s++;
//			e = t + 1;
//		}
//		else
//		{
//			t = s;
//			while (!isSPACE(*t))
//		t++;
//		e = t;
//	}
//		while (isBLANK(*e) || *e == '\r' || *e == '\f')
//		e++;
//		if (*e != '\n' && *e != '\0')
//		return;		/* false alarm */
//
//		line_num = grok_atou(n, &e) - 1;
//
//		if (t - s > 0) {
//			int len = t - s;
//
//			if ( //!PL_rsfp &&
//					PL_parser.filtered == 0
//			) {
//			/* must copy *{"::_<(eval N)[oldfilename:L]"}
//			 * to *{"::_<newfilename"} */
//			/* However, the long form of evals is only turned on by the
//			   debugger - usually they're "(eval %lu)" */
//				GV * const cfgv = CopFILEGV(PL_curcop);
//				if (cfgv) {
//					char smallbuf[128];
//					STRLEN tmplen2 = len;
//					char *tmpbuf2;
//					GV *gv2;
//
//					if (tmplen2 + 2 <= sizeof smallbuf)
//					tmpbuf2 = smallbuf;
//					else
//					Newx(tmpbuf2, tmplen2 + 2, char);
//
//					tmpbuf2[0] = '_';
//					tmpbuf2[1] = '<';
//
//					memcpy(tmpbuf2 + 2, s, tmplen2);
//					tmplen2 += 2;
//
//					gv2 = *(GV**)hv_fetch(PL_defstash, tmpbuf2, tmplen2, TRUE);
//					if (!isGV(gv2)) {
//						gv_init(gv2, PL_defstash, tmpbuf2, tmplen2, FALSE);
//				/* adjust ${"::_<newfilename"} to store the new file name */
//						GvSV(gv2) = newSVpvn(tmpbuf2 + 2, tmplen2 - 2);
//				/* The line number may differ. If that is the case,
//				   alias the saved lines that are in the array.
//				   Otherwise alias the whole array. */
//						if (CopLINE(PL_curcop) == line_num) {
//							GvHV(gv2) = MUTABLE_HV(SvREFCNT_inc(GvHV(cfgv)));
//							GvAV(gv2) = MUTABLE_AV(SvREFCNT_inc(GvAV(cfgv)));
//						}
//						else if (GvAV(cfgv)) {
//							AV * const av = GvAV(cfgv);
//							const I32 start = CopLINE(PL_curcop)+1;
//							I32 items = AvFILLp(av) - start;
//							if (items > 0) {
//								AV * const av2 = GvAVn(gv2);
//								SV *PL_parser.linestr[s]vp = AvARRAY(av) + start;
//								I32 l = (I32)line_num+1;
//								while (items--)
//									av_store(av2, l++, SvREFCNT_inc(PL_parser.linestr[s]vp++));
//							}
//						}
//					}
//
//					if (tmpbuf2 != smallbuf) Safefree(tmpbuf2);
//				}
//			}
//			CopFILE_free(PL_curcop);
//			CopFILE_setn(PL_curcop, s, len);
//		}
//		CopLINE_set(PL_curcop, line_num);
//	}

/*
 * S_force_next
 * When the lexer realizes it knows the next token (for instance,
 * it is reordering tokens for the parser) then it can call S_force_next
 * to know what token to return the next time the lexer is called.  Caller
 * will need to set PL_nextval[] and possibly PL_expect to ensure
 * the lexer handles the token correctly.
 */

	void force_next(IElementType nextType)
{
//	#ifdef DEBUGGING
//	if (DEBUG_T_TEST) {
//		PerlIO_printf(Perl_debug_log, "### forced token:\n");
//		tokereport(type, &NEXTVAL_NEXTTOKE);
//	}
//	#endif
	PL_parser.nexttype[PL_parser.nexttoke] = nextType;
	PL_parser.nexttoke++;
	if (PL_parser.lex_state != LEX_KNOWNEXT) {
		PL_parser.lex_defer = PL_parser.lex_state;
		PL_parser.lex_state = LEX_KNOWNEXT;
	}
}


	/*
		Originaly it takes and returns a pointer
		 Here we work with offsets
	 */
	int scan_formline(int s)
	{
		int eol;
		int t;
		String stuff = "";
		boolean needargs = false;
		boolean eofmt = false;

//		PERL_ARGS_ASSERT_SCAN_FORMLINE;

		_enough:
		while (!needargs) {
			if (PL_parser.linestr[s] == '.') {
				t = s+1;
//				#ifdef PERL_STRICT_CR
//					while (isBLANK(PL_parser.linestr[t]))
//						t++;
//				#else
					while (isBLANK(PL_parser.linestr[t]) || PL_parser.linestr[t] == '\r')
						t++;
//				#endif
				if (PL_parser.linestr[t] == '\n' || t == PL_parser.bufend) {
					eofmt = true;
					break;
				}
			}
			eol = memchr(s,'\n',PL_parser.bufend-s);
			if( eol == -1 ) // (!eol++)
				eol = PL_parser.bufend;
			else
				eol++;

			if (PL_parser.linestr[s] != '#') {
				for (t = s; t < eol; t++) {
					if (PL_parser.linestr[t] == '~' && PL_parser.linestr[t+1] == '~' && stuff.length() > 0) {
						needargs = false;
						break _enough;	/* ~~ must be first line in formline */
					}
					if (PL_parser.linestr[t] == '@' || PL_parser.linestr[t] == '^')
					needargs = true;
				}
				if (eol > s) {
					stuff = sv_catpvn(stuff, s, eol-s);
//					#ifndef PERL_STRICT_CR
//					if (eol-s > 1 && eol[-2] == '\r' && eol[-1] == '\n') {
//						char *end = SvPVX(stuff) + SvCUR(stuff);
//						end[-2] = '\n';
//						end[-1] = '\0';
//						SvCUR_set(stuff, SvCUR(stuff) - 1);
//					}
//					#endif
				}
				else
					break;
			}
			s = eol;

/*	We are not using rsfp
			if ((PL_rsfp || PL_parser.filtered)
				&& PL_parser.form_lex_state == LEX_NORMAL)
			{
				bool got_some;
				PL_bufptr = PL_parser.bufend;
				COPLINE_INC_WITH_HERELINES;
				got_some = lex_next_chunk(0);
				CopLINE_dec(PL_curcop);
				s = PL_bufptr;
				if (!got_some)
					break;
			}
*/
//			incline(s); // NYI
		}

		enough:

		if (stuff.length() == 0|| needargs)
			PL_parser.lex_state = PL_parser.form_lex_state;
		if (stuff.length() > 0) {
			PL_parser.expect = Perl.expectation.XSTATE;
			if (needargs) {
				int s2 = s;
				while (PL_parser.linestr[s2] == '\r' || PL_parser.linestr[s2] == ' ' || PL_parser.linestr[s2] == '\t' || PL_parser.linestr[s2] == '\f'
						|| PL_parser.linestr[s2] == 013)
				s2++;
				if (PL_parser.linestr[s2] == '{') {
					PL_parser.expect = Perl.expectation.XTERMBLOCK;
					PL_parser.nextval[PL_parser.nexttoke].ival = 0;
					force_next(DO);
				}
				PL_parser.nextval[PL_parser.nexttoke].ival = 0;
				force_next(FORMLBRACK);
			}
			/* No encoding staff here
			if (!IN_BYTES) {
				if (UTF && is_utf8_string((U8*)SvPVX_const(stuff), SvCUR(stuff)))
					SvUTF8_on(stuff);
				else if (PL_encoding)
					sv_recode_to_utf8(stuff, PL_encoding);
			}
			*/
			PL_parser.nextval[PL_parser.nexttoke].opval = new OP(OP_CONST, 0, stuff);
			force_next(THING);
		}
		else {
//			SvREFCNT_dec(stuff);
			if (eofmt)
				PL_parser.lex_formbrack = 0;
		}
		return s;
	}


	public IElementType stateSwitch()
	{
		int s = PL_parser.bufptr; // current offset
		int d; // ??
		int len;
		boolean bof = false;
		final boolean saw_infix_sigil = PL_parser.saw_infix_sigil;
		int formbrack = 0;
		int fake_eof = 0;

//    /* orig_keyword, gvp, and gv are initialized here because
//     * jump to the label just_a_word_zero can bypass their
//     * initialization later. */
		int orig_keyword = 0;
		GV gv;
		GV gvp; // this is pointer to pointer, not supported in java

		switch (PL_parser.lex_state) {
			case LEX_NORMAL:
			case LEX_INTERPNORMAL:
				break;

    /* when we've already built the next token, just pull it out of the queue */
			case LEX_KNOWNEXT:
				PL_parser.nexttoke--;
				PL_parser.yylval = PL_parser.nextval[PL_parser.nexttoke];
				if (PL_parser.nexttoke == 0 )
				{
					PL_parser.lex_state = PL_parser.lex_defer;
					PL_parser.lex_defer = LEX_NORMAL;
				}
//			{
//				IElementType next_type;
//				next_type = PL_parser.nexttype[PL_parser.nexttoke];
//				if ((next_type & (7<<24)) > 0) { // what the heck is this? Token has small values
//					if ((next_type & (1<<24)) > 0) {
//						if (PL_parser.lex_brackets > 100)
//						{
//							char[] new_lex_brackstack = new char[PL_parser.lex_brackets + 10];
//							System.arraycopy(
//									PL_parser.lex_brackstack,
//								0,
//									new_lex_brackstack,
//								0,
//								PL_parser.lex_brackstack.length
//							);
//							PL_parser.lex_brackstack = new_lex_brackstack;
//						}
//
//						PL_parser.lex_brackstack[PL_parser.lex_brackets++] =
//								(char) ((next_type >> 16) & 0xff);
//					}
//					if ((next_type & (2<<24)) > 0)
//						PL_parser.lex_allbrackets++;
//					if ((next_type & (4<<24))> 0)
//						PL_parser.lex_allbrackets--;
//					next_type &= 0xffff;
//				}
//				return REPORT(next_type == 'p'
//						? pending_ident()
//						: next_type
//				);
//			}

//    /* interpolated case modifiers like \L \U, including \Q and \E.
//       when we get here, PL_parser.bufptr is at the \
//    */
//			case LEX_INTERPCASEMOD:
//				#ifdef DEBUGGING
//				if (PL_parser.bufptr != PL_parser.bufend && *PL_parser.bufptr != '\\')
//				Perl_croak(aTHX_
//						"panic: INTERPCASEMOD bufptr=%p, bufend=%p, *bufptr=%u",
//						PL_parser.bufptr, PL_parser.bufend, *PL_parser.bufptr);
//				#endif
//	/* handle \E or end of string */
//				if (PL_parser.bufptr == PL_parser.bufend || PL_parser.bufptr[1] == 'E') {
//	    /* if at a \E */
//					if (PL_parser.lex_casemods) {
//						const char oldmod = PL_parser.lex_casestack[--PL_parser.lex_casemods];
//						PL_parser.lex_casestack[PL_parser.lex_casemods] = '\0';
//
//						if (PL_parser.bufptr != PL_parser.bufend
//								&& (oldmod == 'L' || oldmod == 'U' || oldmod == 'Q'
//								|| oldmod == 'F')) {
//							PL_parser.bufptr += 2;
//							PL_parser.lex_state = LEX_INTERPCONCAT;
//						}
//						PL_parser.lex_allbrackets--;
//						return REPORT(')');
//					}
//					else if ( PL_parser.bufptr != PL_parser.bufend && PL_parser.bufptr[1] == 'E' ) {
//               /* Got an unpaired \E */
//						Perl_ck_warner(aTHX_ packWARN(WARN_MISC),
//								"Useless use of \\E");
//					}
//					if (PL_parser.bufptr != PL_parser.bufend)
//						PL_parser.bufptr += 2;
//					PL_parser.lex_state = LEX_INTERPCONCAT;
//					return advance();
//				}
//				else {
//					DEBUG_T({ PerlIO_printf(Perl_debug_log,
//							"### Saw case modifier\n"); });
//					s = PL_parser.bufptr + 1;
//					if (s[1] == '\\' && s[2] == 'E') {
//						PL_parser.bufptr = s + 3;
//						PL_parser.lex_state = LEX_INTERPCONCAT;
//						return advance();
//					}
//					else {
//						private int tmp;
//						if (strnEQ(s, "L\\u", 3) || strnEQ(s, "U\\l", 3))
//							tmp = PL_parser.linestr[s], PL_parser.linestr[s] = s[2], s[2] = (char)tmp;	/* misordered... */
//						if ((PL_parser.linestr[s] == 'L' || PL_parser.linestr[s] == 'U' || PL_parser.linestr[s] == 'F') &&
//						(strchr(PL_parser.lex_casestack, 'L')
//								|| strchr(PL_parser.lex_casestack, 'U')
//								|| strchr(PL_parser.lex_casestack, 'F'))) {
//							PL_parser.lex_casestack[--PL_parser.lex_casemods] = '\0';
//							PL_parser.lex_allbrackets--;
//							return REPORT(')');
//						}
//						if (PL_parser.lex_casemods > 10)
//							Renew(PL_parser.lex_casestack, PL_parser.lex_casemods + 2, char);
//						PL_parser.lex_casestack[PL_parser.lex_casemods++] = PL_parser.linestr[s];
//						PL_parser.lex_casestack[PL_parser.lex_casemods] = '\0';
//						PL_parser.lex_state = LEX_INTERPCONCAT;
//						 PL_parser.nextval[PL_parser.nexttoke].ival = 0;
//						force_next((2<<24)|'(');
//						if (PL_parser.linestr[s] == 'l')
//						 PL_parser.nextval[PL_parser.nexttoke].ival = OP_LCFIRST;
//						else if (PL_parser.linestr[s] == 'u')
//						 PL_parser.nextval[PL_parser.nexttoke].ival = OP_UCFIRST;
//						else if (PL_parser.linestr[s] == 'L')
//						 PL_parser.nextval[PL_parser.nexttoke].ival = OP_LC;
//						else if (PL_parser.linestr[s] == 'U')
//						 PL_parser.nextval[PL_parser.nexttoke].ival = OP_UC;
//						else if (PL_parser.linestr[s] == 'Q')
//						 PL_parser.nextval[PL_parser.nexttoke].ival = OP_QUOTEMETA;
//						else if (PL_parser.linestr[s] == 'F')
//						 PL_parser.nextval[PL_parser.nexttoke].ival = OP_FC;
//						else
//						Perl_croak(aTHX_ "panic: yylex, PL_parser.linestr[s]=%u", PL_parser.linestr[s]);
//						PL_parser.bufptr = s + 1;
//					}
//					force_next(FUNC);
//					if (PL_parser.lex_starts) {
//						s = PL_parser.bufptr;
//						PL_parser.lex_starts = 0;
//		/* commas only at base level: /$a\Ub$c/ => ($a,uc(b.$c)) */
//						if (PL_parser.lex_casemods == 1 && PL_parser.lex_inpat)
//							TOKEN(',');
//						else
//							AopNOASSIGN(OP_CONCAT);
//					}
//					else
//						return advance();
//				}
//
//			case LEX_INTERPPUSH:
//				return REPORT(sublex_push());
//
//			case LEX_INTERPSTART:
//				if (PL_parser.bufptr == PL_parser.bufend)
//					return REPORT(sublex_done());
//				DEBUG_T({ if(*PL_parser.bufptr != '(') PerlIO_printf(Perl_debug_log,
//					"### Interpolated variable\n"); });
//				PL_parser.expect = XTERM;
//        /* for /@a/, we leave the joining for the regex engine to do
//         * (unless we're within \Q etc) */
//				PL_parser.lex_dojoin = (*PL_parser.bufptr == '@'
//						&& (!PL_parser.lex_inpat || PL_parser.lex_casemods));
//				PL_parser.lex_state = LEX_INTERPNORMAL;
//				if (PL_parser.lex_dojoin) {
//					 PL_parser.nextval[PL_parser.nexttoke].ival = 0;
//					force_next(',');
//					force_ident("\"", '$');
//					 PL_parser.nextval[PL_parser.nexttoke].ival = 0;
//					force_next('$');
//					 PL_parser.nextval[PL_parser.nexttoke].ival = 0;
//					force_next((2<<24)|'(');
//					 PL_parser.nextval[PL_parser.nexttoke].ival = OP_JOIN;	/* emulate join($", ...) */
//					force_next(FUNC);
//				}
//	/* Convert (?{...}) and friends to 'do {...}' */
//				if (PL_parser.lex_inpat && *PL_parser.bufptr == '(') {
//				PL_parser.lex_shared->re_eval_start = PL_parser.bufptr;
//				PL_parser.bufptr += 2;
//				if (*PL_parser.bufptr != '{')
//				PL_parser.bufptr++;
//				PL_parser.expect = XTERMBLOCK;
//				force_next(DO);
//			}
//
//			if (PL_parser.lex_starts++) {
//				s = PL_parser.bufptr;
//	    /* commas only at base level: /$a\Ub$c/ => ($a,uc(b.$c)) */
//				if (!PL_parser.lex_casemods && PL_parser.lex_inpat)
//					TOKEN(',');
//				else
//					AopNOASSIGN(OP_CONCAT);
//			}
//			return advance();
//
//			case LEX_INTERPENDMAYBE:
//				if (intuit_more(PL_parser.bufptr)) {
//					PL_parser.lex_state = LEX_INTERPNORMAL;	/* false alarm, more expr */
//					break;
//				}
//	/* FALLTHROUGH */
//
//			case LEX_INTERPEND:
//				if (PL_parser.lex_dojoin) {
//					const private int dojoin_was = PL_parser.lex_dojoin;
//					PL_parser.lex_dojoin = false;
//					PL_parser.lex_state = LEX_INTERPCONCAT;
//					PL_parser.lex_allbrackets--;
//					return REPORT(dojoin_was == 1 ? ')' : POSTJOIN);
//				}
//				if (PL_parser.lex_inwhat == OP_SUBST && PL_parser.linestr == PL_parser.lex_repl
//						&& SvEVALED(PL_parser.lex_repl))
//				{
//					if (PL_parser.bufptr != PL_parser.bufend)
//						Perl_croak(aTHX_ "Bad evalled substitution pattern");
//					PL_parser.lex_repl = NULL;
//				}
//	/* Paranoia.  re_eval_start is adjusted when S_scan_heredoc sets
//	   re_eval_str.  If the here-doc body’s length equals the previous
//	   value of re_eval_start, re_eval_start will now be null.  So
//	   check re_eval_str as well. */
//				if (PL_parser.lex_shared->re_eval_start
//						|| PL_parser.lex_shared->re_eval_str) {
//					SV PL_parser.linestr[s]v;
//					if (*PL_parser.bufptr != ')')
//					Perl_croak(aTHX_ "Sequence (?{...}) not terminated with ')'");
//					PL_parser.bufptr++;
//	    /* having compiled a (?{..}) expression, return the original
//	     * text too, as a const */
//					if (PL_parser.lex_shared->re_eval_str) {
//						sv = PL_parser.lex_shared->re_eval_str;
//						PL_parser.lex_shared->re_eval_str = NULL;
//						SvCUR_set(sv,
//								PL_parser.bufptr - PL_parser.lex_shared->re_eval_start);
//						SvPV_shrink_to_cur(sv);
//					}
//					else sv = newSVpvn(PL_parser.lex_shared->re_eval_start,
//							PL_parser.bufptr - PL_parser.lex_shared->re_eval_start);
//					 PL_parser.nextval[PL_parser.nexttoke].opval =
//							new OP(OP_CONST, 0,
//							sv);
//					force_next(THING);
//					PL_parser.lex_shared->re_eval_start = NULL;
//					PL_parser.expect = XTERM;
//					return REPORT(',');
//				}
//
//	/* FALLTHROUGH */
//			case LEX_INTERPCONCAT:
//				#ifdef DEBUGGING
//				if (PL_parser.lex_brackets)
//					Perl_croak(aTHX_ "panic: INTERPCONCAT, lex_brackets=%ld",
//							(long) PL_parser.lex_brackets);
//				#endif
//				if (PL_parser.bufptr == PL_parser.bufend)
//					return REPORT(sublex_done());
//
//	/* m'foo' still needs to be parsed for possible (?{...}) */
//				if (SvIVX(PL_parser.linestr) == '\'' && !PL_parser.lex_inpat) {
//					SV PL_parser.linestr[s]v = newSVsv(PL_parser.linestr);
//					sv = tokeq(sv);
//					PL_parser.yylval.opval = new OP(OP_CONST, 0, sv);
//					s = PL_parser.bufend;
//				}
//				else {
//					s = scan_const(PL_parser.bufptr);
//					if (PL_parser.linestr[s] == '\\')
//					PL_parser.lex_state = LEX_INTERPCASEMOD;
//					else
//					PL_parser.lex_state = LEX_INTERPSTART;
//				}
//
//				if (s != PL_parser.bufptr) {
//					 PL_parser.nextval[PL_parser.nexttoke] = PL_parser.yylval;
//					PL_parser.expect = XTERM;
//					force_next(THING);
//					if (PL_parser.lex_starts++) {
//		/* commas only at base level: /$a\Ub$c/ => ($a,uc(b.$c)) */
//						if (!PL_parser.lex_casemods && PL_parser.lex_inpat)
//							TOKEN(',');
//						else
//							AopNOASSIGN(OP_CONCAT);
//					}
//					else {
//						PL_parser.bufptr = s;
//						return advance();
//					}
//				}
//
//				return advance();
//			case LEX_FORMLINE:
//				s = scan_formline(PL_parser.bufptr);
//				if ( PL_parser.lex_formbrack == 0)
//				{
//					formbrack = 1;
//					goto rightbracket;
//				}
//				PL_parser.bufptr = s;
//				return advance();
		}

		return null;
	}

	public IElementType charSwitch()
	{

//		retry:
//		switch (PL_parser.linestr[s]) {
//		default:
//			if (UTF ? isIDFIRST_utf8((private int*)s) : isALNUMC(PL_parser.linestr[s]))
//			goto keylookup;
//		{
//			SV *dsv = newSVpvs_flags("", SVs_TEMP);
//			const char *c = UTF ? sv_uni_display(dsv, newSVpvn_flags(s,
//						UTF8SKIP(s),
//						SVs_TEMP | SVf_UTF8),
//				10, UNI_DISPLAY_ISPRINT)
//				: Perl_form(aTHX_ "\\x%02X", (unsigned char)PL_parser.linestr[s]);
//			len = UTF ? Perl_utf8_length(aTHX_ (private int *) PL_parser.linestart, (private int *) s) : (STRLEN) (s - PL_parser.linestart);
//			if (len > UNRECOGNIZED_PRECEDE_COUNT) {
//				d = UTF ? (char *) utf8_hop((private int *) s, -UNRECOGNIZED_PRECEDE_COUNT) : s - UNRECOGNIZED_PRECEDE_COUNT;
//			} else {
//				d = PL_parser.linestart;
//			}
//			Perl_croak(aTHX_  "Unrecognized character %s; marked by <-- HERE after %"UTF8f"<-- HERE near column %d", c,
//					UTF8fARG(UTF, (s - d), d),
//					(int) len + 1);
//		}
//		case 4:
//		case 26:
//			goto fake_eof;			// emulate EOF on ^D or ^Z
//		case 0:
//			if (!PL_parser.rsfp && (!PL_parser.filtered || s+1 < PL_parser.bufend)) {
//				PL_parser.last_uni = 0;
//				PL_parser.last_lop = 0;
//				if (PL_parser.lex_brackets &&
//						PL_parser.lex_brackstack[PL_parser.lex_brackets-1] != XFAKEEOF) {
//					yyerror((const char *)
//					(PL_parser.lex_formbrack
//							? "Format not terminated"
//							: "Missing right curly or square bracket"));
//				}
//				DEBUG_T( { PerlIO_printf(Perl_debug_log,
//						"### Tokener got EOF\n");
//				} );
//				TOKEN(0);
//			}
//			if (s++ < PL_parser.bufend)
//			goto retry;			/* ignore stray nulls */
//			PL_parser.last_uni = 0;
//			PL_parser.last_lop = 0;
//			if (!PL_in_eval && !PL_parser.preambled) {
//				PL_parser.preambled = true;
//				if (PL_perldb) {
//		/* Generate a string of Perl code to load the debugger.
//		 * If PERL5DB is set, it will return the contents of that,
//		 * otherwise a compile-time require of perl5db.pl.  */
//
//					const char * const pdb = PerlEnv_getenv("PERL5DB");
//
//					if (pdb) {
//						sv_setpv(PL_parser.linestr, pdb);
//						sv_catpvs(PL_parser.linestr,";");
//					} else {
//						SETERRNO(0,SS_NORMAL);
//						sv_setpvs(PL_parser.linestr, "BEGIN { require 'perl5db.pl' };");
//					}
//					PL_parser.preambling = CopLINE(PL_curcop);
//				} else
//					sv_setpvs(PL_parser.linestr,"");
//				if (PL_preambleav) {
//					SV *PL_parser.linestr[s]vp = AvARRAY(PL_preambleav);
//					SV **const end = svp + AvFILLp(PL_preambleav);
//					while(svp <= end) {
//						sv_catsv(PL_parser.linestr, PL_parser.linestr[s]vp);
//						++svp;
//						sv_catpvs(PL_parser.linestr, ";");
//					}
//					sv_free(MUTABLE_SV(PL_preambleav));
//					PL_preambleav = NULL;
//				}
//				if (PL_minus_E)
//					sv_catpvs(PL_parser.linestr,
//							"use feature ':5." STRINGIFY(PERL_VERSION) "';");
//				if (PL_minus_n || PL_minus_p) {
//					sv_catpvs(PL_parser.linestr, "LINE: while (<>) {"/*}*/);
//					if (PL_minus_l)
//						sv_catpvs(PL_parser.linestr,"chomp;");
//					if (PL_minus_a) {
//						if (PL_minus_F) {
//							if ((*PL_splitstr == '/' || *PL_splitstr == '\''
//									|| *PL_splitstr == '"')
//							&& strchr(PL_splitstr + 1, *PL_splitstr))
//							Perl_sv_catpvf(aTHX_ PL_parser.linestr, "our @F=split(%s);", PL_splitstr);
//							else {
//			    /* "q\0${splitstr}\0" is legal perl. Yes, even NUL
//			       bytes can be used as quoting characters.  :-) */
//								const char PL_parser.linestr[s]plits = PL_splitstr;
//								sv_catpvs(PL_parser.linestr, "our @F=split(q\0");
//								do {
//				/* Need to \ \s  */
//									if (PL_parser.linestr[s]plits == '\\')
//									sv_catpvn(PL_parser.linestr, splits, 1);
//									sv_catpvn(PL_parser.linestr, splits, 1);
//								} while (PL_parser.linestr[s]plits++);
//			    /* This loop will embed the trailing NUL of
//			       PL_parser.linestr as the last thing it does before
//			       terminating.  */
//								sv_catpvs(PL_parser.linestr, ");");
//							}
//						}
//						else
//							sv_catpvs(PL_parser.linestr,"our @F=split(' ');");
//					}
//				}
//				sv_catpvs(PL_parser.linestr, "\n");
//				PL_parser.oldoldbufptr = PL_parser.oldbufptr = s = PL_parser.linestart = SvPVX(PL_parser.linestr);
//				PL_parser.bufend = SvPVX(PL_parser.linestr) + SvCUR(PL_parser.linestr);
//				PL_parser.last_lop = PL_parser.last_uni = NULL;
//				if ((PERLDB_LINE || PERLDB_SAVESRC) && PL_curstash != PL_debstash)
//					update_debugger_info(PL_parser.linestr, NULL, 0);
//				goto retry;
//			}
//			do {
//				fake_eof = 0;
//				bof = PL_parser.rsfp ? true : false;
//				if (0) {
//					fake_eof:
//					fake_eof = LEX_FAKE_EOF;
//				}
//				PL_parser.bufptr = PL_parser.bufend;
//				COPLINE_INC_WITH_HERELINES;
//				if (!lex_next_chunk(fake_eof)) {
//					CopLINE_dec(PL_curcop);
//					s = PL_parser.bufptr;
//					TOKEN(';');	/* not infinite loop because rsfp is NULL now */
//				}
//				CopLINE_dec(PL_curcop);
//				s = PL_parser.bufptr;
//	    /* If it looks like the start of a BOM or raw UTF-16,
//	     * check if it in fact is. */
//				if (bof && PL_parser.rsfp &&
//						(PL_parser.linestr[s] == 0 ||
//				*(private int*)s == BOM_UTF8_FIRST_BYTE ||
//				*(private int*)s >= 0xFE ||
//						s[1] == 0)) {
//					Off_t offset = (IV)PerlIO_tell(PL_parser.rsfp);
//					bof = (offset == (Off_t)SvCUR(PL_parser.linestr));
//					#if defined(PERLIO_USING_CRLF) && defined(PERL_TEXTMODE_SCRIPTS)
//		/* offset may include swallowed CR */
//					if (!bof)
//						bof = (offset == (Off_t)SvCUR(PL_parser.linestr)+1);
//					#endif
//					if (bof) {
//						PL_parser.bufend = SvPVX(PL_parser.linestr) + SvCUR(PL_parser.linestr);
//						s = swallow_bom((private int*)s);
//					}
//				}
//				if (PL_parser.in_pod) {
//		/* Incest with pod. */
//					if (PL_parser.linestr[s] == '=' && strnEQ(s, "=cut", 4) && !isALPHA(s[4])) {
//						sv_setpvs(PL_parser.linestr, "");
//						PL_parser.oldoldbufptr = PL_parser.oldbufptr = s = PL_parser.linestart = SvPVX(PL_parser.linestr);
//						PL_parser.bufend = SvPVX(PL_parser.linestr) + SvCUR(PL_parser.linestr);
//						PL_parser.last_lop = PL_parser.last_uni = NULL;
//						PL_parser.in_pod = 0;
//					}
//				}
//				if (PL_parser.rsfp || PL_parser.filtered)
//					incline(s);
//			} while (PL_parser.in_pod);
//			PL_parser.oldoldbufptr = PL_parser.oldbufptr = PL_parser.bufptr = PL_parser.linestart = s;
//			PL_parser.bufend = SvPVX(PL_parser.linestr) + SvCUR(PL_parser.linestr);
//			PL_parser.last_lop = PL_parser.last_uni = NULL;
//			if (CopLINE(PL_curcop) == 1) {
//				while (s < PL_parser.bufend && isSPACE(PL_parser.linestr[s]))
//				s++;
//				if (PL_parser.linestr[s] == ':' && s[1] != ':') /* for csh execing sh scripts */
//				s++;
//				d = NULL;
//				if (!PL_in_eval) {
//					if (PL_parser.linestr[s] == '#' && *(s+1) == '!')
//					d = s + 2;
//					#ifdef ALTERNATE_SHEBANG
//					else {
//						static char const as[] = ALTERNATE_SHEBANG;
//						if (PL_parser.linestr[s] == as[0] && strnEQ(s, as, sizeof(as) - 1))
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
//					if (d && PL_parser.linestr[s] != '#') {
//						const char *c = ipath;
//						while (*c && !strchr("; \t\r\n\f\v#", *c))
//						c++;
//						if (c < d)
//							d = NULL;	/* "perl" not in first word; ignore */
//						else
//						PL_parser.linestr[s] = '#';	/* Don't try to parse shebang line */
//					}
//					#endif /* ALTERNATE_SHEBANG */
//					if (!d &&
//					PL_parser.linestr[s] == '#' &&
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
//						while (s < PL_parser.bufend && isSPACE(PL_parser.linestr[s]))
//						s++;
//						if (s < PL_parser.bufend) {
//							Newx(newargv,PL_origargc+3,char*);
//							newargv[1] = s;
//							while (s < PL_parser.bufend && !isSPACE(PL_parser.linestr[s]))
//							s++;
//							PL_parser.linestr[s] = '\0';
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
//						while (isBLANK(*d))
//						d++;
//
//						if (*d++ == '-') {
//							const boolean switches_done = PL_doswitches;
//							const U32 oldpdb = PL_perldb;
//							const boolean oldn = PL_minus_n;
//							const boolean oldp = PL_minus_p;
//							const char *d1 = d;
//
//							do {
//								boolean baduni = false;
//								if (*d1 == 'C') {
//									const char *d2 = d1 + 1;
//									if (parse_unicode_opts((const char **)&d2)
//									!= PL_unicode)
//									baduni = true;
//								}
//								if (baduni || isALPHA_FOLD_EQ(*d1, 'M')) {
//									const char * const m = d1;
//									while (*d1 && !isSPACE(*d1))
//									d1++;
//									Perl_croak(aTHX_ "Too late for \"-%.PL_parser.linestr[s]\" option",
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
//								sv_setpvs(PL_parser.linestr, "");
//								PL_parser.oldoldbufptr = PL_parser.oldbufptr = s = PL_parser.linestart = SvPVX(PL_parser.linestr);
//								PL_parser.bufend = SvPVX(PL_parser.linestr) + SvCUR(PL_parser.linestr);
//								PL_parser.last_lop = PL_parser.last_uni = NULL;
//								PL_parser.preambled = false;
//								if (PERLDB_LINE || PERLDB_SAVESRC)
//									(void)gv_fetchfile(PL_origfilename);
//								goto retry;
//							}
//						}
//					}
//				}
//			}
//			if (PL_parser.lex_formbrack && PL_parser.lex_brackets <= PL_parser.lex_formbrack) {
//				PL_parser.lex_state = LEX_FORMLINE;
//				 PL_parser.nextval[PL_parser.nexttoke].ival = 0;
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
//			if (PL_parser.lex_state != LEX_NORMAL ||
//					(PL_in_eval && !PL_parser.rsfp && !PL_parser.filtered)) {
//			const boolean in_comment = PL_parser.linestr[s] == '#';
//			if (PL_parser.linestr[s] == '#' && s == PL_parser.linestart && PL_in_eval
//					&& !PL_parser.rsfp && !PL_parser.filtered) {
//		/* handle eval qq[#line 1 "foo"\n ...] */
//				CopLINE_dec(PL_curcop);
//				incline(s);
//			}
//			d = s;
//			while (d < PL_parser.bufend && *d != '\n')
//			d++;
//			if (d < PL_parser.bufend)
//				d++;
//			else if (d > PL_parser.bufend)
//                /* Found by Ilya: feed random input to Perl. */
//				Perl_croak(aTHX_ "panic: input overflow, %p > %p",
//						d, PL_parser.bufend);
//			s = d;
//			if (in_comment && d == PL_parser.bufend
//					&& PL_parser.lex_state == LEX_INTERPNORMAL
//					&& PL_parser.lex_inwhat == OP_SUBST && PL_parser.lex_repl == PL_parser.linestr
//					&& SvEVALED(PL_parser.lex_repl) && d[-1] == '}') s--;
//			else
//				incline(s);
//			if (PL_parser.lex_formbrack && PL_parser.lex_brackets <= PL_parser.lex_formbrack) {
//				PL_parser.lex_state = LEX_FORMLINE;
//				 PL_parser.nextval[PL_parser.nexttoke].ival = 0;
//				force_next(FORMRBRACK);
//				TOKEN(';');
//			}
//		}
//		else {
//			while (s < PL_parser.bufend && PL_parser.linestr[s] != '\n')
//			s++;
//			if (s < PL_parser.bufend)
//			{
//				s++;
//				if (s < PL_parser.bufend)
//					incline(s);
//			}
//			else if (s > PL_parser.bufend)
//                /* Found by Ilya: feed random input to Perl. */
//				Perl_croak(aTHX_ "panic: input overflow");
//		}
//		goto retry;
//		case '-':
//			if (s[1] && isALPHA(s[1]) && !isWORDCHAR(s[2])) {
//				private int ftst = 0;
//				char tmp;
//
//				s++;
//				PL_parser.bufptr = s;
//				tmp = PL_parser.linestr[s]++;
//
//				while (s < PL_parser.bufend && isBLANK(PL_parser.linestr[s]))
//				s++;
//
//				if (strnEQ(s,"=>",2)) {
//					s = force_word(PL_parser.bufptr,WORD,false,false);
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
//					PL_parser.last_uni = PL_parser.oldbufptr;
//					PL_parser.last_lop_op = (OPCODE)ftst;
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
//					s = --PL_parser.bufptr;
//				}
//			}
//		{
//			const char tmp = PL_parser.linestr[s]++;
//			if (PL_parser.linestr[s] == tmp) {
//			s++;
//			if (PL_parser.expect == XOPERATOR)
//				TERM(POSTDEC);
//			else
//				OPERATOR(PREDEC);
//		}
//			else if (PL_parser.linestr[s] == '>') {
//			s++;
//			s = skipspace(s);
//			if (FEATURE_POSTDEREF_IS_ENABLED && (
//					((PL_parser.linestr[s] == '$' || PL_parser.linestr[s] == '&') && s[1] == '*')
//			||(PL_parser.linestr[s] == '$' && s[1] == '#' && s[2] == '*')
//			||((PL_parser.linestr[s] == '@' || PL_parser.linestr[s] == '%') && strchr("*[{", s[1]))
//			||(PL_parser.linestr[s] == '*' && (s[1] == '*' || s[1] == '{'))
//			))
//			{
//				Perl_ck_warner_d(aTHX_
//						packWARN(WARN_EXPERIMENTAL__POSTDEREF),
//						"Postfix dereference is experimental"
//				);
//				PL_parser.expect = XPOSTDEREF;
//				TOKEN(ARROW);
//			}
//			if (isIDFIRST_lazy_if(s,UTF)) {
//				s = force_word(s,METHOD,false,true);
//				TOKEN(ARROW);
//			}
//			else if (PL_parser.linestr[s] == '$')
//			OPERATOR(ARROW);
//			else
//			TERM(ARROW);
//		}
//			if (PL_parser.expect == XOPERATOR) {
//				if (PL_parser.linestr[s] == '=' && !PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_ASSIGN) {
//					s--;
//					TOKEN(0);
//				}
//				Aop(OP_SUBTRACT);
//			}
//			else {
//				if (isSPACE(PL_parser.linestr[s]) || !isSPACE(*PL_parser.bufptr))
//				check_uni();
//				OPERATOR('-');		/* unary minus */
//			}
//		}
//
//		case '+':
//		{
//			const char tmp = PL_parser.linestr[s]++;
//			if (PL_parser.linestr[s] == tmp) {
//			s++;
//			if (PL_parser.expect == XOPERATOR)
//				TERM(POSTINC);
//			else
//				OPERATOR(PREINC);
//		}
//			if (PL_parser.expect == XOPERATOR) {
//				if (PL_parser.linestr[s] == '=' && !PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_ASSIGN) {
//					s--;
//					TOKEN(0);
//				}
//				Aop(OP_ADD);
//			}
//			else {
//				if (isSPACE(PL_parser.linestr[s]) || !isSPACE(*PL_parser.bufptr))
//				check_uni();
//				OPERATOR('+');
//			}
//		}
//
//		case '*':
//			if (PL_parser.expect == XPOSTDEREF) POSTDEREF('*');
//			if (PL_parser.expect != XOPERATOR) {
//				s = scan_ident(s, PL_parser.tokenbuf, sizeof PL_parser.tokenbuf, true);
//				PL_parser.expect = XOPERATOR;
//				force_ident(PL_parser.tokenbuf, '*');
//				if (!*PL_parser.tokenbuf)
//					PREREF('*');
//				TERM('*');
//			}
//			s++;
//			if (PL_parser.linestr[s] == '*') {
//			s++;
//			if (PL_parser.linestr[s] == '=' && !PL_parser.lex_allbrackets &&
//					PL_parser.lex_fakeof >= LEX_FAKEEOF_ASSIGN) {
//				s -= 2;
//				TOKEN(0);
//			}
//			PWop(OP_POW);
//		}
//		if (PL_parser.linestr[s] == '=' && !PL_parser.lex_allbrackets &&
//				PL_parser.lex_fakeof >= LEX_FAKEEOF_ASSIGN) {
//			s--;
//			TOKEN(0);
//		}
//		PL_parser.saw_infix_sigil = true;
//		Mop(OP_MULTIPLY);
//
//		case '%':
//		{
//			if (PL_parser.expect == XOPERATOR) {
//				if (s[1] == '=' && !PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_ASSIGN)
//					TOKEN(0);
//				++s;
//				PL_parser.saw_infix_sigil = true;
//				Mop(OP_MODULO);
//			}
//			else if (PL_parser.expect == XPOSTDEREF) POSTDEREF('%');
//			PL_parser.tokenbuf[0] = '%';
//			s = scan_ident(s, PL_parser.tokenbuf + 1,
//					sizeof PL_parser.tokenbuf - 1, false);
//			PL_parser.yylval.ival = 0;
//			if (!PL_parser.tokenbuf[1]) {
//				PREREF('%');
//			}
//			if ((PL_parser.expect != XREF || PL_parser.oldoldbufptr == PL_parser.last_lop) && intuit_more(s)) {
//				if (PL_parser.linestr[s] == '[')
//				PL_parser.tokenbuf[0] = '@';
//			}
//			PL_parser.expect = XOPERATOR;
//			force_ident_maybe_lex('%');
//			TERM('%');
//		}
//		case '^':
//			if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >=
//					(s[1] == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_BITWISE))
//				TOKEN(0);
//			s++;
//			BOop(OP_BIT_XOR);
//		case '[':
//			if (PL_parser.lex_brackets > 100)
//				Renew(PL_parser.lex_brackstack, PL_parser.lex_brackets + 10, char);
//			PL_parser.lex_brackstack[PL_parser.lex_brackets++] = 0;
//			PL_parser.lex_allbrackets++;
//		{
//			const char tmp = PL_parser.linestr[s]++;
//			OPERATOR(tmp);
//		}
//		case '~':
//			if (s[1] == '~'
//					&& (PL_parser.expect == XOPERATOR || PL_parser.expect == XTERMORDORDOR))
//			{
//				if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE)
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
//			if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_COMMA)
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
//			switch (PL_parser.expect) {
//				case XOPERATOR:
//					if (!PL_parser.in_my || PL_parser.lex_state != LEX_NORMAL)
//						break;
//					PL_parser.bufptr = s;	/* update in case we back off */
//					if (PL_parser.linestr[s] == '=') {
//					Perl_croak(aTHX_
//							"Use of := for an empty attribute list is not allowed");
//				}
//				goto grabattrs;
//				case XATTRBLOCK:
//					PL_parser.expect = XBLOCK;
//					goto grabattrs;
//				case XATTRTERM:
//					PL_parser.expect = XTERMBLOCK;
//					grabattrs:
//					s = skipspace(s);
//					attrs = NULL;
//					while (isIDFIRST_lazy_if(s,UTF)) {
//						private int tmp;
//						SV PL_parser.linestr[s]v;
//						d = scan_word(s, PL_parser.tokenbuf, sizeof PL_parser.tokenbuf, false, &len);
//						if (isLOWER(PL_parser.linestr[s]) && (tmp = keyword(PL_parser.tokenbuf, len, 0))) {
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
//							d = scan_str(d,true,true,false,NULL);
//							COPLINE_SET_FROM_MULTI_END;
//							if (!d) {
//			/* MUST advance bufptr here to avoid bogus
//			   "at end of line" context messages from yyerror().
//			 */
//								PL_parser.bufptr = s + len;
//								yyerror("Unterminated attribute parameter in attribute list");
//								if (attrs)
//									op_free(attrs);
//								sv_free(sv);
//								return REPORT(0);	/* EOF indicator */
//							}
//						}
//						if (PL_parser.lex_stuff) {
//							sv_catsv(sv, PL_parser.lex_stuff);
//							attrs = op_append_elem(OP_LIST, attrs,
//									newSVOP(OP_CONST, 0, sv));
//							SvREFCNT_dec(PL_parser.lex_stuff);
//							PL_parser.lex_stuff = NULL;
//						}
//						else {
//							if (len == 6 && strnEQ(SvPVX(sv), "unique", len)) {
//								sv_free(sv);
//								if (PL_parser.in_my == KEY_our) {
//									deprecate(":unique");
//								}
//								else
//									Perl_croak(aTHX_ "The 'unique' attribute may only be applied to 'our' variables");
//							}
//
//		    /* NOTE: any CV attrs applied here need to be part of
//		       the CVf_BUILTIN_ATTRS define in cv.h! */
//							else if (!PL_parser.in_my && len == 6 && strnEQ(SvPVX(sv), "lvalue", len)) {
//								sv_free(sv);
//								CvLVALUE_on(PL_compcv);
//							}
//							else if (!PL_parser.in_my && len == 6 && strnEQ(SvPVX(sv), "locked", len)) {
//								sv_free(sv);
//								deprecate(":locked");
//							}
//							else if (!PL_parser.in_my && len == 6 && strnEQ(SvPVX(sv), "method", len)) {
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
//						if (PL_parser.linestr[s] == ':' && s[1] != ':')
//						s = skipspace(s+1);
//						else if (s == d)
//							break;	/* require real whitespace or :'s */
//		/* XXX losing whitespace on sequential attributes here */
//					}
//				{
//					if (PL_parser.linestr[s] != ';' && PL_parser.linestr[s] != '}' &&
//						!(PL_parser.expect == XOPERATOR
//								? (PL_parser.linestr[s] == '=' ||  PL_parser.linestr[s] == ')')
//					: (PL_parser.linestr[s] == '{' ||  PL_parser.linestr[s] == '('))) {
//					const char q = ((PL_parser.linestr[s] == '\'') ? '"' : '\'');
//		    /* If here for an expression, and parsed no attrs, back
//		       off. */
//					if (PL_parser.expect == XOPERATOR && !attrs) {
//						s = PL_parser.bufptr;
//						break;
//					}
//		    /* MUST advance bufptr here to avoid bogus "at end of line"
//		       context messages from yyerror().
//		    */
//					PL_parser.bufptr = s;
//					yyerror( (const char *)
//					(PL_parser.linestr[s]
//							? Perl_form(aTHX_ "Invalid separator character "
//							"%c%c%c in attribute list", q, PL_parser.linestr[s], q)
//					: "Unterminated attribute list" ) );
//					if (attrs)
//						op_free(attrs);
//					OPERATOR(':');
//				}
//				}
//				got_attrs:
//				if (attrs) {
//					 PL_parser.nextval[PL_parser.nexttoke].opval = attrs;
//					force_next(THING);
//				}
//				TOKEN(COLONATTR);
//			}
//		}
//		if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_CLOSING) {
//			s--;
//			TOKEN(0);
//		}
//		PL_parser.lex_allbrackets--;
//		OPERATOR(':');
//		case '(':
//			s++;
//			if (PL_parser.last_lop == PL_parser.oldoldbufptr || PL_parser.last_uni == PL_parser.oldoldbufptr)
//				PL_parser.oldbufptr = PL_parser.oldoldbufptr;		/* allow print(STDOUT 123) */
//			else
//				PL_parser.expect = XTERM;
//			s = skipspace(s);
//			PL_parser.lex_allbrackets++;
//			TOKEN('(');
//		case ';':
//			if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_NONEXPR)
//				TOKEN(0);
//			PL_parser.copline = (CopLINE(PL_parser.curcop) < PL_parser.copline ? CopLINE(PL_parser.curcop) : PL_parser.copline);
//			s++;
//			PL_parser.expect = XSTATE;
//			TOKEN(';');
//		case ')':
//			if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_CLOSING)
//				TOKEN(0);
//			s++;
//			PL_parser.lex_allbrackets--;
//			s = skipspace(s);
//			if (PL_parser.linestr[s] == '{')
//			PREBLOCK(')');
//			TERM(')');
//		case ']':
//			if (PL_parser.lex_brackets && PL_parser.lex_brackstack[PL_parser.lex_brackets-1] == XFAKEEOF)
//				TOKEN(0);
//			s++;
//			if (PL_parser.lex_brackets <= 0)
//	    /* diag_listed_as: Unmatched right %s bracket */
//				yyerror("Unmatched right square bracket");
//			else
//				--PL_parser.lex_brackets;
//			PL_parser.lex_allbrackets--;
//			if (PL_parser.lex_state == LEX_INTERPNORMAL) {
//				if (PL_parser.lex_brackets == 0) {
//					if (PL_parser.linestr[s] == '-' && s[1] == '>')
//					PL_parser.lex_state = LEX_INTERPENDMAYBE;
//					else if (PL_parser.linestr[s] != '[' && PL_parser.linestr[s] != '{')
//					PL_parser.lex_state = LEX_INTERPEND;
//				}
//			}
//			TERM(']');
//		case '{':
//			s++;
//			leftbracket:
//			if (PL_parser.lex_brackets > 100) {
//				Renew(PL_parser.lex_brackstack, PL_parser.lex_brackets + 10, char);
//			}
//			switch (PL_parser.expect) {
//				case XTERM:
//					PL_parser.lex_brackstack[PL_parser.lex_brackets++] = XOPERATOR;
//					PL_parser.lex_allbrackets++;
//					OPERATOR(HASHBRACK);
//				case XOPERATOR:
//					while (s < PL_parser.bufend && isBLANK(PL_parser.linestr[s]))
//					s++;
//					d = s;
//					PL_parser.tokenbuf[0] = '\0';
//					if (d < PL_parser.bufend && *d == '-') {
//					PL_parser.tokenbuf[0] = '-';
//					d++;
//					while (d < PL_parser.bufend && isBLANK(*d))
//					d++;
//				}
//				if (d < PL_parser.bufend && isIDFIRST_lazy_if(d,UTF)) {
//					d = scan_word(d, PL_parser.tokenbuf + 1, sizeof PL_parser.tokenbuf - 1,
//							false, &len);
//					while (d < PL_parser.bufend && isBLANK(*d))
//					d++;
//					if (*d == '}') {
//						const char minus = (PL_parser.tokenbuf[0] == '-');
//						s = force_word(s + minus, WORD, false, true);
//						if (minus)
//							force_next('-');
//					}
//				}
//	    /* FALLTHROUGH */
//				case XATTRTERM:
//				case XTERMBLOCK:
//					PL_parser.lex_brackstack[PL_parser.lex_brackets++] = XOPERATOR;
//					PL_parser.lex_allbrackets++;
//					PL_parser.expect = XSTATE;
//					break;
//				case XATTRBLOCK:
//				case XBLOCK:
//					PL_parser.lex_brackstack[PL_parser.lex_brackets++] = XSTATE;
//					PL_parser.lex_allbrackets++;
//					PL_parser.expect = XSTATE;
//					break;
//				case XBLOCKTERM:
//					PL_parser.lex_brackstack[PL_parser.lex_brackets++] = XTERM;
//					PL_parser.lex_allbrackets++;
//					PL_parser.expect = XSTATE;
//					break;
//				default: {
//					const char *t;
//					if (PL_parser.oldoldbufptr == PL_parser.last_lop)
//						PL_parser.lex_brackstack[PL_parser.lex_brackets++] = XTERM;
//					else
//						PL_parser.lex_brackstack[PL_parser.lex_brackets++] = XOPERATOR;
//					PL_parser.lex_allbrackets++;
//					s = skipspace(s);
//					if (PL_parser.linestr[s] == '}') {
//						if (PL_parser.expect == XREF && PL_parser.lex_state == LEX_INTERPNORMAL) {
//							PL_parser.expect = XTERM;
//			/* This hack is to get the ${} in the message. */
//							PL_parser.bufptr = s+1;
//							yyerror("syntax error");
//							break;
//						}
//						OPERATOR(HASHBRACK);
//					}
//					if (PL_parser.expect == XREF && PL_parser.oldoldbufptr != PL_parser.last_lop) {
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
//					if (PL_parser.linestr[s] == '\'' || PL_parser.linestr[s] == '"' || PL_parser.linestr[s] == '`') {
//		    /* common case: get past first string, handling escapes */
//						for (t++; t < PL_parser.bufend && PL_parser.linestr[t] != PL_parser.linestr[s];)
//						if (*t++ == '\\')
//						t++;
//						t++;
//					}
//					else if (PL_parser.linestr[s] == 'q') {
//						if (++t < PL_parser.bufend
//								&& (!isWORDCHAR(PL_parser.linestr[t])
//								|| ((PL_parser.linestr[t] == 'q' || PL_parser.linestr[t] == 'x') && ++t < PL_parser.bufend
//								&& !isWORDCHAR(PL_parser.linestr[t]))))
//						{
//			/* skip q//-like construct */
//							const char *tmps;
//							char open, close, term;
//							private int brackets = 1;
//
//							while (t < PL_parser.bufend && isSPACE(PL_parser.linestr[t]))
//							t++;
//			/* check for q => */
//							if (t+1 < PL_parser.bufend && t[0] == '=' && t[1] == '>') {
//								OPERATOR(HASHBRACK);
//							}
//							term = *t;
//							open = term;
//							if (term && (tmps = strchr("([{< )]}> )]}>",term)))
//								term = tmps[5];
//							close = term;
//							if (open == close)
//								for (t++; t < PL_parser.bufend; t++) {
//									if (PL_parser.linestr[t] == '\\' && t+1 < PL_parser.bufend && open != '\\')
//									t++;
//									else if (PL_parser.linestr[t] == open)
//									break;
//								}
//							else {
//								for (t++; t < PL_parser.bufend; t++) {
//									if (PL_parser.linestr[t] == '\\' && t+1 < PL_parser.bufend)
//									t++;
//									else if (PL_parser.linestr[t] == close && --brackets <= 0)
//									break;
//									else if (PL_parser.linestr[t] == open)
//									brackets++;
//								}
//							}
//							t++;
//						}
//						else
//			/* skip plain q word */
//						while (t < PL_parser.bufend && isWORDCHAR_lazy_if(t,UTF))
//							t += UTF8SKIP(t);
//					}
//					else if (isWORDCHAR_lazy_if(t,UTF)) {
//						t += UTF8SKIP(t);
//						while (t < PL_parser.bufend && isWORDCHAR_lazy_if(t,UTF))
//							t += UTF8SKIP(t);
//					}
//					while (t < PL_parser.bufend && isSPACE(PL_parser.linestr[t]))
//					t++;
//		/* if comma follows first term, call it an anon hash */
//		/* XXX it could be a comma expression with loop modifiers */
//					if (t < PL_parser.bufend && ((PL_parser.linestr[t] == ',' && (PL_parser.linestr[s] == 'q' || !isLOWER(PL_parser.linestr[s])))
//					|| (PL_parser.linestr[t] == '=' && t[1] == '>')))
//					OPERATOR(HASHBRACK);
//					if (PL_parser.expect == XREF)
//					{
//						block_expectation:
//		    /* If there is an opening brace or 'sub:', treat it
//		       as a term to make ${{...}}{k} and &{sub:attr...}
//		       dwim.  Otherwise, treat it as a statement, so
//		       map {no strict; ...} works.
//		     */
//						s = skipspace(s);
//						if (PL_parser.linestr[s] == '{') {
//						PL_parser.expect = XTERM;
//						break;
//					}
//						if (strnEQ(s, "sub", 3)) {
//							d = s + 3;
//							d = skipspace(d);
//							if (*d == ':') {
//								PL_parser.expect = XTERM;
//								break;
//							}
//						}
//						PL_parser.expect = XSTATE;
//					}
//					else {
//						PL_parser.lex_brackstack[PL_parser.lex_brackets-1] = XSTATE;
//						PL_parser.expect = XSTATE;
//					}
//				}
//				break;
//			}
//			PL_parser.yylval.ival = CopLINE(PL_curcop);
//			PL_parser.copline = NOLINE;   /* invalidate current command line number */
//			TOKEN(formbrack ? '=' : '{');
//		case '}':
//			if (PL_parser.lex_brackets && PL_parser.lex_brackstack[PL_parser.lex_brackets-1] == XFAKEEOF)
//				TOKEN(0);
//			rightbracket:
//			s++;
//			if (PL_parser.lex_brackets <= 0)
//	    /* diag_listed_as: Unmatched right %s bracket */
//				yyerror("Unmatched right curly bracket");
//			else
//				PL_parser.expect = (expectation)PL_parser.lex_brackstack[--PL_parser.lex_brackets];
//			PL_parser.lex_allbrackets--;
//			if (PL_parser.lex_state == LEX_INTERPNORMAL) {
//				if (PL_parser.lex_brackets == 0) {
//					if (PL_parser.expect & XFAKEBRACK) {
//						PL_parser.expect &= XENUMMASK;
//						PL_parser.lex_state = LEX_INTERPEND;
//						PL_parser.bufptr = s;
//						return advance();	/* ignore fake brackets */
//					}
//					if (PL_parser.lex_inwhat == OP_SUBST && PL_parser.lex_repl == PL_parser.linestr
//							&& SvEVALED(PL_parser.lex_repl))
//						PL_parser.lex_state = LEX_INTERPEND;
//					else if (PL_parser.linestr[s] == '-' && s[1] == '>')
//					PL_parser.lex_state = LEX_INTERPENDMAYBE;
//					else if (PL_parser.linestr[s] != '[' && PL_parser.linestr[s] != '{')
//					PL_parser.lex_state = LEX_INTERPEND;
//				}
//			}
//			if (PL_parser.expect & XFAKEBRACK) {
//				PL_parser.expect &= XENUMMASK;
//				PL_parser.bufptr = s;
//				return advance();		/* ignore fake brackets */
//			}
//			force_next(formbrack ? '.' : '}');
//			if (formbrack) LEAVE;
//			if (formbrack == 2) { /* means . where arguments were expected */
//				force_next(';');
//				TOKEN(FORMRBRACK);
//			}
//			TOKEN(';');
//		case '&':
//			if (PL_parser.expect == XPOSTDEREF) POSTDEREF('&');
//			s++;
//			if (PL_parser.linestr[s]++ == '&') {
//			if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >=
//					(PL_parser.linestr[s] == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_LOGIC)) {
//				s -= 2;
//				TOKEN(0);
//			}
//			AOPERATOR(ANDAND);
//		}
//		s--;
//		if (PL_parser.expect == XOPERATOR) {
//			if (PL_parser.bufptr == PL_parser.linestart && ckWARN(WARN_SEMICOLON)
//					&& isIDFIRST_lazy_if(s,UTF))
//			{
//				CopLINE_dec(PL_curcop);
//				Perl_warner(aTHX_ packWARN(WARN_SEMICOLON), "%s", PL_warn_nosemi);
//				CopLINE_inc(PL_curcop);
//			}
//			if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >=
//					(PL_parser.linestr[s] == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_BITWISE)) {
//				s--;
//				TOKEN(0);
//			}
//			PL_parser.saw_infix_sigil = true;
//			BAop(OP_BIT_AND);
//		}
//
//		PL_parser.tokenbuf[0] = '&';
//		s = scan_ident(s - 1, PL_parser.tokenbuf + 1,
//				sizeof PL_parser.tokenbuf - 1, true);
//		if (PL_parser.tokenbuf[1]) {
//			PL_parser.expect = XOPERATOR;
//			force_ident_maybe_lex('&');
//		}
//		else
//			PREREF('&');
//		PL_parser.yylval.ival = (OPpENTERSUB_AMPER<<8);
//		TERM('&');
//
//		case '|':
//			s++;
//			if (PL_parser.linestr[s]++ == '|') {
//			if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >=
//					(PL_parser.linestr[s] == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_LOGIC)) {
//				s -= 2;
//				TOKEN(0);
//			}
//			AOPERATOR(OROR);
//		}
//		s--;
//		if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >=
//				(PL_parser.linestr[s] == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_BITWISE)) {
//			s--;
//			TOKEN(0);
//		}
//		BOop(OP_BIT_OR);
//		case '=':
//			s++;
//		{
//			const char tmp = PL_parser.linestr[s]++;
//			if (tmp == '=') {
//				if (!PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE) {
//					s -= 2;
//					TOKEN(0);
//				}
//				Eop(OP_EQ);
//			}
//			if (tmp == '>') {
//				if (!PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_COMMA) {
//					s -= 2;
//					TOKEN(0);
//				}
//				OPERATOR(',');
//			}
//			if (tmp == '~')
//				PMop(OP_MATCH);
//			if (tmp && isSPACE(PL_parser.linestr[s]) && ckWARN(WARN_SYNTAX)
//				&& strchr("+-*/%.^&|<",tmp))
//			Perl_warner(aTHX_ packWARN(WARN_SYNTAX),
//					"Reversed %c= operator",(int)tmp);
//			s--;
//			if (PL_parser.expect == XSTATE && isALPHA(tmp) &&
//					(s == PL_parser.linestart+1 || s[-2] == '\n') )
//			{
//				if ((PL_in_eval && !PL_parser.rsfp && !PL_parser.filtered)
//				|| PL_parser.lex_state != LEX_NORMAL) {
//				d = PL_parser.bufend;
//				while (s < d) {
//					if (PL_parser.linestr[s]++ == '\n') {
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
//				s = PL_parser.bufend;
//				PL_parser.in_pod = 1;
//				goto retry;
//			}
//		}
//		if (PL_parser.expect == XBLOCK) {
//			const char PL_parser.linestr[t] = s;
//			#ifdef PERL_STRICT_CR
//			while (isBLANK(PL_parser.linestr[t]))
//			#else
//			while (isBLANK(PL_parser.linestr[t]) || PL_parser.linestr[t] == '\r')
//			#endif
//			t++;
//			if (PL_parser.linestr[t] == '\n' || PL_parser.linestr[t] == '#') {
//				formbrack = 1;
//				ENTER;
//				SAVEI8(PL_parser.form_lex_state);
//				SAVEprivate int(PL_parser.lex_formbrack);
//				PL_parser.form_lex_state = PL_parser.lex_state;
//				PL_parser.lex_formbrack = PL_parser.lex_brackets + 1;
//				goto leftbracket;
//			}
//		}
//		if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_ASSIGN) {
//			s--;
//			TOKEN(0);
//		}
//		PL_parser.yylval.ival = 0;
//		OPERATOR(ASSIGNOP);
//		case '!':
//			s++;
//		{
//			const char tmp = PL_parser.linestr[s]++;
//			if (tmp == '=') {
//		/* was this !=~ where !~ was meant?
//		 * warn on m:!=~\s+([/?]|[msy]\W|tr\W): */
//
//				if (PL_parser.linestr[s] == '~' && ckWARN(WARN_SYNTAX)) {
//					const char PL_parser.linestr[t] = s+1;
//
//					while (t < PL_parser.bufend && isSPACE(PL_parser.linestr[t]))
//					++t;
//
//					if (PL_parser.linestr[t] == '/' || PL_parser.linestr[t] == '?' ||
//							((PL_parser.linestr[t] == 'm' || PL_parser.linestr[t] == 's' || PL_parser.linestr[t] == 'y')
//					&& !isWORDCHAR(t[1])) ||
//					(PL_parser.linestr[t] == 't' && t[1] == 'r' && !isWORDCHAR(t[2])))
//					Perl_warner(aTHX_ packWARN(WARN_SYNTAX),
//							"!=~ should be !~");
//				}
//				if (!PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE) {
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
//			if (PL_parser.expect != XOPERATOR) {
//				if (s[1] != '<' && !strchr(s,'>'))
//					check_uni();
//				if (s[1] == '<' && s[2] != '>')
//					s = scan_heredoc(s);
//				else
//					s = scan_inputsymbol(s);
//				PL_parser.expect = XOPERATOR;
//				TOKEN(sublex_start());
//			}
//			s++;
//		{
//			char tmp = PL_parser.linestr[s]++;
//			if (tmp == '<') {
//				if (PL_parser.linestr[s] == '=' && !PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_ASSIGN) {
//					s -= 2;
//					TOKEN(0);
//				}
//				SHop(OP_LEFT_SHIFT);
//			}
//			if (tmp == '=') {
//				tmp = PL_parser.linestr[s]++;
//				if (tmp == '>') {
//					if (!PL_parser.lex_allbrackets &&
//							PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE) {
//						s -= 3;
//						TOKEN(0);
//					}
//					Eop(OP_NCMP);
//				}
//				s--;
//				if (!PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE) {
//					s -= 2;
//					TOKEN(0);
//				}
//				Rop(OP_LE);
//			}
//		}
//		s--;
//		if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE) {
//			s--;
//			TOKEN(0);
//		}
//		Rop(OP_LT);
//		case '>':
//			s++;
//		{
//			const char tmp = PL_parser.linestr[s]++;
//			if (tmp == '>') {
//				if (PL_parser.linestr[s] == '=' && !PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_ASSIGN) {
//					s -= 2;
//					TOKEN(0);
//				}
//				SHop(OP_RIGHT_SHIFT);
//			}
//			else if (tmp == '=') {
//				if (!PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE) {
//					s -= 2;
//					TOKEN(0);
//				}
//				Rop(OP_GE);
//			}
//		}
//		s--;
//		if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE) {
//			s--;
//			TOKEN(0);
//		}
//		Rop(OP_GT);
//
//		case '$':
//			PL_parser.copline = (CopLINE(PL_parser.curcop) < PL_parser.copline ? CopLINE(PL_parser.curcop) : PL_parser.copline);
//
//			if (PL_parser.expect == XOPERATOR) {
//				if (PL_parser.lex_formbrack && PL_parser.lex_brackets == PL_parser.lex_formbrack) {
//					return deprecate_commaless_var_list();
//				}
//			}
//			else if (PL_parser.expect == XPOSTDEREF) {
//				if (s[1] == '#') {
//					s++;
//					POSTDEREF(DOLSHARP);
//				}
//				POSTDEREF('$');
//			}
//
//			if (s[1] == '#' && (isIDFIRST_lazy_if(s+2,UTF) || strchr("{$:+-@", s[2]))) {
//				PL_parser.tokenbuf[0] = '@';
//				s = scan_ident(s + 1, PL_parser.tokenbuf + 1,
//						sizeof PL_parser.tokenbuf - 1, false);
//				if (PL_parser.expect == XOPERATOR)
//					no_op("Array length", s);
//				if (!PL_parser.tokenbuf[1])
//					PREREF(DOLSHARP);
//				PL_parser.expect = XOPERATOR;
//				force_ident_maybe_lex('#');
//				TOKEN(DOLSHARP);
//			}
//
//			PL_parser.tokenbuf[0] = '$';
//			s = scan_ident(s, PL_parser.tokenbuf + 1,
//					sizeof PL_parser.tokenbuf - 1, false);
//			if (PL_parser.expect == XOPERATOR)
//				no_op("Scalar", s);
//			if (!PL_parser.tokenbuf[1]) {
//				if (s == PL_parser.bufend)
//					yyerror("Final $ should be \\$ or $name");
//				PREREF('$');
//			}
//
//			d = s;
//		{
//			const char tmp = PL_parser.linestr[s];
//			if (PL_parser.lex_state == LEX_NORMAL || PL_parser.lex_brackets)
//				s = skipspace(s);
//
//			if ((PL_parser.expect != XREF || PL_parser.oldoldbufptr == PL_parser.last_lop)
//					&& intuit_more(s)) {
//				if (PL_parser.linestr[s] == '[') {
//					PL_parser.tokenbuf[0] = '@';
//					if (ckWARN(WARN_SYNTAX)) {
//						char PL_parser.linestr[t] = s+1;
//
//						while (isSPACE(PL_parser.linestr[t]) || isWORDCHAR_lazy_if(t,UTF) || PL_parser.linestr[t] == '$')
//						t++;
//						if (*t++ == ',') {
//							PL_parser.bufptr = skipspace(PL_parser.bufptr); /* XXX can realloc */
//							while (t < PL_parser.bufend && PL_parser.linestr[t] != ']')
//							t++;
//							Perl_warner(aTHX_ packWARN(WARN_SYNTAX),
//									"Multidimensional syntax %.PL_parser.linestr[s] not supported",
//									(int)((t - PL_parser.bufptr) + 1), PL_parser.bufptr);
//						}
//					}
//				}
//				else if (PL_parser.linestr[s] == '{') {
//					char *t;
//					PL_parser.tokenbuf[0] = '%';
//					if (strEQ(PL_parser.tokenbuf+1, "SIG")  && ckWARN(WARN_SYNTAX)
//							&& (t = strchr(s, '}')) && (t = strchr(t, '=')))
//					{
//						char tmpbuf[sizeof PL_parser.tokenbuf];
//						do {
//							t++;
//						} while (isSPACE(PL_parser.linestr[t]));
//						if (isIDFIRST_lazy_if(t,UTF)) {
//							STRLEN len;
//							t = scan_word(t, tmpbuf, sizeof tmpbuf, true,
//									&len);
//							while (isSPACE(PL_parser.linestr[t]))
//							t++;
//							if (PL_parser.linestr[t] == ';'
//									&& get_cvn_flags(tmpbuf, len, UTF ? SVf_UTF8 : 0))
//							Perl_warner(aTHX_ packWARN(WARN_SYNTAX),
//									"You need to quote \"%"UTF8f"\"",
//									UTF8fARG(UTF, len, tmpbuf));
//						}
//					}
//				}
//			}
//
//			PL_parser.expect = XOPERATOR;
//			if (PL_parser.lex_state == LEX_NORMAL && isSPACE((char)tmp)) {
//				const boolean islop = (PL_parser.last_lop == PL_parser.oldoldbufptr);
//				if (!islop || PL_parser.last_lop_op == OP_GREPSTART)
//					PL_parser.expect = XOPERATOR;
//				else if (strchr("$@\"'`q", PL_parser.linestr[s]))
//				PL_parser.expect = XTERM;		/* e.g. print $fh "foo" */
//				else if (strchr("&*<%", PL_parser.linestr[s]) && isIDFIRST_lazy_if(s+1,UTF))
//				PL_parser.expect = XTERM;		/* e.g. print $fh &sub */
//				else if (isIDFIRST_lazy_if(s,UTF)) {
//					char tmpbuf[sizeof PL_parser.tokenbuf];
//					int t2;
//					scan_word(s, tmpbuf, sizeof tmpbuf, true, &len);
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
//								PL_parser.expect = XTERM;	/* e.g. print $fh length() */
//								break;
//						}
//					}
//					else {
//						PL_parser.expect = XTERM;	/* e.g. print $fh subr() */
//					}
//				}
//				else if (isDIGIT(PL_parser.linestr[s]))
//				PL_parser.expect = XTERM;		/* e.g. print $fh 3 */
//				else if (PL_parser.linestr[s] == '.' && isDIGIT(s[1]))
//				PL_parser.expect = XTERM;		/* e.g. print $fh .3 */
//				else if ((PL_parser.linestr[s] == '?' || PL_parser.linestr[s] == '-' || PL_parser.linestr[s] == '+')
//				&& !isSPACE(s[1]) && s[1] != '=')
//				PL_parser.expect = XTERM;		/* e.g. print $fh -1 */
//				else if (PL_parser.linestr[s] == '/' && !isSPACE(s[1]) && s[1] != '='
//						&& s[1] != '/')
//				PL_parser.expect = XTERM;		/* e.g. print $fh /.../
//						   XXX except DORDOR operator
//						*/
//				else if (PL_parser.linestr[s] == '<' && s[1] == '<' && !isSPACE(s[2])
//						&& s[2] != '=')
//				PL_parser.expect = XTERM;		/* print $fh <<"EOF" */
//			}
//		}
//		force_ident_maybe_lex('$');
//		TOKEN('$');
//
//		case '@':
//			if (PL_parser.expect == XOPERATOR)
//				no_op("Array", s);
//			else if (PL_parser.expect == XPOSTDEREF) POSTDEREF('@');
//			PL_parser.tokenbuf[0] = '@';
//			s = scan_ident(s, PL_parser.tokenbuf + 1, sizeof PL_parser.tokenbuf - 1, false);
//			PL_parser.yylval.ival = 0;
//			if (!PL_parser.tokenbuf[1]) {
//				PREREF('@');
//			}
//			if (PL_parser.lex_state == LEX_NORMAL)
//				s = skipspace(s);
//			if ((PL_parser.expect != XREF || PL_parser.oldoldbufptr == PL_parser.last_lop) && intuit_more(s)) {
//				if (PL_parser.linestr[s] == '{')
//				PL_parser.tokenbuf[0] = '%';
//
//	    /* Warn about @ where they meant $. */
//				if (PL_parser.linestr[s] == '[' || PL_parser.linestr[s] == '{') {
//					if (ckWARN(WARN_SYNTAX)) {
//						S_check_scalar_slice(aTHX_ s);
//					}
//				}
//			}
//			PL_parser.expect = XOPERATOR;
//			force_ident_maybe_lex('@');
//			TERM('@');
//
//		case '/':			/* may be division, defined-or, or pattern */
//			if ((PL_parser.expect == XOPERATOR || PL_parser.expect == XTERMORDORDOR) && s[1] == '/') {
//				if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >=
//						(s[2] == '=' ? LEX_FAKEEOF_ASSIGN : LEX_FAKEEOF_LOGIC))
//					TOKEN(0);
//				s += 2;
//				AOPERATOR(DORDOR);
//			}
//			else if (PL_parser.expect == XOPERATOR) {
//				s++;
//				if (PL_parser.linestr[s] == '=' && !PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_ASSIGN) {
//					s--;
//					TOKEN(0);
//				}
//				Mop(OP_DIVIDE);
//			}
//			else {
//	    /* Disable warning on "study /blah/" */
//				if (PL_parser.oldoldbufptr == PL_parser.last_uni
//						&& (*PL_parser.last_uni != 's' || s - PL_parser.last_uni < 5
//						|| memNE(PL_parser.last_uni, "study", 5)
//						|| isWORDCHAR_lazy_if(PL_parser.last_uni+5,UTF)
//						))
//				check_uni();
//				s = scan_pat(s,OP_MATCH);
//				TERM(sublex_start());
//			}
//
//		case '?':			/* conditional */
//			s++;
//			if (!PL_parser.lex_allbrackets &&
//					PL_parser.lex_fakeof >= LEX_FAKEEOF_IFELSE) {
//				s--;
//				TOKEN(0);
//			}
//			PL_parser.lex_allbrackets++;
//			OPERATOR('?');
//
//		case '.':
//			if (PL_parser.lex_formbrack && PL_parser.lex_brackets == PL_parser.lex_formbrack
//			#ifdef PERL_STRICT_CR
//			&& s[1] == '\n'
//			#else
//			&& (s[1] == '\n' || (s[1] == '\r' && s[2] == '\n'))
//			#endif
//				&& (s == PL_parser.linestart || s[-1] == '\n') )
//		{
//			PL_parser.expect = XSTATE;
//			formbrack = 2; /* dot seen where arguments expected */
//			goto rightbracket;
//		}
//		if (PL_parser.expect == XSTATE && s[1] == '.' && s[2] == '.') {
//			s += 3;
//			OPERATOR(YADAYADA);
//		}
//		if (PL_parser.expect == XOPERATOR || !isDIGIT(s[1])) {
//			char tmp = PL_parser.linestr[s]++;
//			if (PL_parser.linestr[s] == tmp) {
//				if (!PL_parser.lex_allbrackets &&
//						PL_parser.lex_fakeof >= LEX_FAKEEOF_RANGE) {
//					s--;
//					TOKEN(0);
//				}
//				s++;
//				if (PL_parser.linestr[s] == tmp) {
//					s++;
//					PL_parser.yylval.ival = OPf_SPECIAL;
//				}
//				else
//				PL_parser.yylval.ival = 0;
//				OPERATOR(DOTDOT);
//			}
//			if (PL_parser.linestr[s] == '=' && !PL_parser.lex_allbrackets &&
//					PL_parser.lex_fakeof >= LEX_FAKEEOF_ASSIGN) {
//				s--;
//				TOKEN(0);
//			}
//			Aop(OP_CONCAT);
//		}
//	/* FALLTHROUGH */
//		case '0': case '1': case '2': case '3': case '4':
//		case '5': case '6': case '7': case '8': case '9':
//			s = scan_num(s, &PL_parser.yylval);
//			DEBUG_T( { printbuf("### Saw number in %s\n", s); } );
//			if (PL_parser.expect == XOPERATOR)
//				no_op("Number",s);
//			TERM(THING);
//
//		case '\'':
//			s = scan_str(s,false,false,false,NULL);
//			if (!s)
//				missingterm(NULL);
//			COPLINE_SET_FROM_MULTI_END;
//			DEBUG_T( { printbuf("### Saw string before %s\n", s); } );
//			if (PL_parser.expect == XOPERATOR) {
//				if (PL_parser.lex_formbrack && PL_parser.lex_brackets == PL_parser.lex_formbrack) {
//					return deprecate_commaless_var_list();
//				}
//				else
//					no_op("String",s);
//			}
//			PL_parser.yylval.ival = OP_CONST;
//			TERM(sublex_start());
//
//		case '"':
//			s = scan_str(s,false,false,false,NULL);
//			DEBUG_T( {
//			if (s)
//				printbuf("### Saw string before %s\n", s);
//			else
//				PerlIO_printf(Perl_debug_log,
//						"### Saw unterminated string\n");
//			} );
//			if (PL_parser.expect == XOPERATOR) {
//				if (PL_parser.lex_formbrack && PL_parser.lex_brackets == PL_parser.lex_formbrack) {
//					return deprecate_commaless_var_list();
//				}
//				else
//					no_op("String",s);
//			}
//			if (!s)
//				missingterm(NULL);
//			PL_parser.yylval.ival = OP_CONST;
//	/*  FIXME. I think that this can be const if char *d is replaced by
//	   more localised variables.  */
//			for (d = SvPV(PL_parser.lex_stuff, len); len; len--, d++) {
//				if (*d == '$' || *d == '@' || *d == '\\' || !UTF8_IS_INVARIANT((private int)*d)) {
//					PL_parser.yylval.ival = OP_STRINGIFY;
//					break;
//				}
//			}
//			if (PL_parser.yylval.ival == OP_CONST)
//				COPLINE_SET_FROM_MULTI_END;
//			TERM(sublex_start());
//
//		case '`':
//			s = scan_str(s,false,false,false,NULL);
//			DEBUG_T( { printbuf("### Saw backtick string before %s\n", s); } );
//			if (PL_parser.expect == XOPERATOR)
//				no_op("Backticks",s);
//			if (!s)
//				missingterm(NULL);
//			PL_parser.yylval.ival = OP_BACKTICK;
//			TERM(sublex_start());
//
//		case '\\':
//			s++;
//			if (PL_parser.lex_inwhat == OP_SUBST && PL_parser.lex_repl == PL_parser.linestr
//					&& isDIGIT(PL_parser.linestr[s]))
//			Perl_ck_warner(aTHX_ packWARN(WARN_SYNTAX),"Can't use \\%c to mean $%c in expression",
//					PL_parser.linestr[s], PL_parser.linestr[s]);
//			if (PL_parser.expect == XOPERATOR)
//				no_op("Backslash",s);
//			OPERATOR(REFGEN);
//
//		case 'v':
//			if (isDIGIT(s[1]) && PL_parser.expect != XOPERATOR) {
//				char PL_parser.linestr[s]tart = s + 2;
//				while (isDIGIT(PL_parser.linestr[s]tart) || PL_parser.linestr[s]tart == '_')
//				start++;
//				if (PL_parser.linestr[s]tart == '.' && isDIGIT(start[1])) {
//					s = scan_num(s, &PL_parser.yylval);
//					TERM(THING);
//				}
//				else if ((PL_parser.linestr[s]tart == ':' && start[1] == ':')
//				|| (PL_parser.expect == XSTATE && PL_parser.linestr[s]tart == ':'))
//				goto keylookup;
//				else if (PL_parser.expect == XSTATE) {
//					d = start;
//					while (d < PL_parser.bufend && isSPACE(*d)) d++;
//					if (*d == ':') goto keylookup;
//				}
//	    /* avoid v123abc() or $h{v1}, allow C<print v10;> */
//				if (!isALPHA(PL_parser.linestr[s]tart) && (PL_parser.expect == XTERM
//						|| PL_parser.expect == XSTATE
//						|| PL_parser.expect == XTERMORDORDOR)) {
//					GV *const gv = gv_fetchpvn_flags(s, start - s,
//							UTF ? SVf_UTF8 : 0, SVt_PVCV);
//					if (!gv) {
//						s = scan_num(s, &PL_parser.yylval);
//						TERM(THING);
//					}
//				}
//			}
//			goto keylookup;
//		case 'x':
//			if (isDIGIT(s[1]) && PL_parser.expect == XOPERATOR) {
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
//				boolean anydelim;
//				boolean lex;
//				private int tmp;
//				SV PL_parser.linestr[s]v;
//				CV *cv;
//				PADOFFSET off;
//				OP *rv2cv_op;
//
//				lex = false;
//				orig_keyword = 0;
//				off = 0;
//				sv = NULL;
//				cv = NULL;
//				gv = NULL;
//				gvp = NULL;
//				rv2cv_op = NULL;
//
//				PL_parser.bufptr = s;
//				s = scan_word(s, PL_parser.tokenbuf, sizeof PL_parser.tokenbuf, false, &len);
//
//	/* Some keywords can be followed by any delimiter, including ':' */
//				anydelim = word_takes_any_delimeter(PL_parser.tokenbuf, len);
//
//	/* x::* is just a word, unless x is "CORE" */
//				if (!anydelim && PL_parser.linestr[s] == ':' && s[1] == ':') {
//					if (strEQ(PL_parser.tokenbuf, "CORE")) goto case_KEY_CORE;
//					goto just_a_word;
//				}
//
//				d = s;
//				while (d < PL_parser.bufend && isSPACE(*d))
//				d++;	/* no comments skipped here, or s### is misparsed */
//
//	/* Is this a word before a => operator? */
//				if (*d == '=' && d[1] == '>') {
//					fat_arrow:
//					PL_parser.copline = (CopLINE(PL_parser.curcop) < PL_parser.copline ? CopLINE(PL_parser.curcop) : PL_parser.copline);
//					PL_parser.yylval.opval
//							= new OP(OP_CONST, 0,
//							S_newSV_maybe_utf8(aTHX_ PL_parser.tokenbuf, len));
//					PL_parser.yylval.opval.op_private = OPpCONST_BARE;
//					TERM(WORD);
//				}
//
//	/* Check for plugged-in keyword */
//				{
//					OP *o;
//					int result;
//					char PL_parser.linestr[s]aved_bufptr = PL_parser.bufptr;
//					PL_parser.bufptr = s;
//					result = PL_keyword_plugin(aTHX_ PL_parser.tokenbuf, len, &o);
//					s = PL_parser.bufptr;
//					if (result == KEYWORD_PLUGIN_DEPL_parser.copline = (CopLINE(PL_parser.curcop) < PL_parser.copline ? CopLINE(PL_parser.curcop) : PL_parser.copline)) {
//		/* not a plugged-in keyword */
//						PL_parser.bufptr = saved_bufptr;
//					} else if (result == KEYWORD_PLUGIN_STMT) {
//						PL_parser.yylval.opval = o;
//						PL_parser.copline = (CopLINE(PL_parser.curcop) < PL_parser.copline ? CopLINE(PL_parser.curcop) : PL_parser.copline);
//						if (!PL_parser.nexttoke) PL_parser.expect = XSTATE;
//						return REPORT(PLUGSTMT);
//					} else if (result == KEYWORD_PLUGIN_EXPR) {
//						PL_parser.yylval.opval = o;
//						PL_parser.copline = (CopLINE(PL_parser.curcop) < PL_parser.copline ? CopLINE(PL_parser.curcop) : PL_parser.copline);
//						if (!PL_parser.nexttoke) PL_parser.expect = XOPERATOR;
//						return REPORT(PLUGEXPR);
//					} else {
//						Perl_croak(aTHX_ "Bad plugin affecting keyword '%s'",
//								PL_parser.tokenbuf);
//					}
//				}
//
//	/* Check for built-in keyword */
//				tmp = keyword(PL_parser.tokenbuf, len, 0);
//
//	/* Is this a label? */
//				if (!anydelim && PL_parser.expect == XSTATE
//						&& d < PL_parser.bufend && *d == ':' && *(d + 1) != ':') {
//					s = d + 1;
//					PL_parser.yylval.pval = savepvn(PL_parser.tokenbuf, len+1);
//					PL_parser.yylval.pval[len] = '\0';
//					PL_parser.yylval.pval[len+1] = UTF ? 1 : 0;
//					PL_parser.copline = (CopLINE(PL_parser.curcop) < PL_parser.copline ? CopLINE(PL_parser.curcop) : PL_parser.copline);
//					TOKEN(LABEL);
//				}
//
//	/* Check for lexical sub */
//				if (PL_parser.expect != XOPERATOR) {
//					char tmpbuf[sizeof PL_parser.tokenbuf + 1];
//					*tmpbuf = '&';
//					Copy(PL_parser.tokenbuf, tmpbuf+1, len, char);
//					off = pad_findmy_pvn(tmpbuf, len+1, UTF ? SVf_UTF8 : 0);
//					if (off != NOT_IN_PAD) {
//						assert(off); /* we assume this is booleanean-true below */
//						if (PAD_COMPNAME_FLAGS_isOUR(off)) {
//							HV *  const stash = PAD_COMPNAME_OURSTASH(off);
//							HEK * const stashname = HvNAME_HEK(stash);
//							sv = newSVhek(stashname);
//							sv_catpvs(sv, "::");
//							sv_catpvn_flags(sv, PL_parser.tokenbuf, len,
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
//						lex = true;
//						goto just_a_word;
//					}
//					off = 0;
//				}
//
//				if (tmp < 0) {			/* second-class keyword? */
//					GV *ogv = NULL;	/* override (winner) */
//					GV *hgv = NULL;	/* hidden (loser) */
//					if (PL_parser.expect != XOPERATOR && (PL_parser.linestr[s] != ':' || s[1] != ':')) {
//						CV *cv;
//						if ((gv = gv_fetchpvn_flags(PL_parser.tokenbuf, len,
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
//								(gvp = (GV**)hv_fetch(PL_globalstash, PL_parser.tokenbuf,
//								len, false)) &&
//						(gv = *gvp) && (
//								isGV_with_GP(gv)
//										? GvCVu(gv) && GvIMPORTED_CV(gv)
//										:   SvPCS_IMPORTED(gv)
//										&& (gv_init(gv, PL_globalstash, PL_parser.tokenbuf,
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
//						&& (!anydelim || PL_parser.linestr[s] != '#')) {
//	    /* no override, and not s### either; skipspace is safe here
//	     * check for => on following line */
//					boolean arrow;
//					STRLEN bufoff = PL_parser.bufptr - SvPVX(PL_parser.linestr);
//					STRLEN   soff = s         - SvPVX(PL_parser.linestr);
//					s = skipspace_flags(s, LEX_NO_INPL_parser.copline = (CopLINE(PL_parser.curcop) < PL_parser.copline ? CopLINE(PL_parser.curcop) : PL_parser.copline));
//					arrow = PL_parser.linestr[s] == '=' && s[1] == '>';
//					PL_parser.bufptr = SvPVX(PL_parser.linestr) + bufoff;
//					s         = SvPVX(PL_parser.linestr) +   soff;
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
//							const char lastchar = (PL_parser.bufptr == PL_parser.oldoldbufptr ? 0 : PL_parser.bufptr[-1]);
//							boolean safebw;
//
//
//		/* Get the rest if it looks like a package qualifier */
//
//							if (PL_parser.linestr[s] == '\'' || (PL_parser.linestr[s] == ':' && s[1] == ':')) {
//								STRLEN morelen;
//								s = scan_word(s, PL_parser.tokenbuf + len, sizeof PL_parser.tokenbuf - len,
//										true, &morelen);
//								if (!morelen)
//									Perl_croak(aTHX_ "Bad name after %"UTF8f"%s",
//											UTF8fARG(UTF, len, PL_parser.tokenbuf),
//											PL_parser.linestr[s] == '\'' ? "'" : "::");
//								len += morelen;
//								pkgname = 1;
//							}
//
//							if (PL_parser.expect == XOPERATOR) {
//								if (PL_parser.bufptr == PL_parser.linestart) {
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
//									PL_parser.tokenbuf[len - 2] == ':' && PL_parser.tokenbuf[len - 1] == ':')
//							{
//								if (ckWARN(WARN_BAREWORD)
//										&& ! gv_fetchpvn_flags(PL_parser.tokenbuf, len, UTF ? SVf_UTF8 : 0, SVt_PVHV))
//									Perl_warner(aTHX_ packWARN(WARN_BAREWORD),
//											"Bareword \"%"UTF8f"\" refers to nonexistent package",
//											UTF8fARG(UTF, len, PL_parser.tokenbuf));
//								len -= 2;
//								PL_parser.tokenbuf[len] = '\0';
//								gv = NULL;
//								gvp = 0;
//								safebw = true;
//							}
//							else {
//								safebw = false;
//							}
//
//		/* if we saw a global override before, get the right name */
//
//							if (!sv)
//								sv = S_newSV_maybe_utf8(aTHX_ PL_parser.tokenbuf,
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
//							PL_parser.copline = (CopLINE(PL_parser.curcop) < PL_parser.copline ? CopLINE(PL_parser.curcop) : PL_parser.copline);
//							PL_parser.yylval.opval = new OP(OP_CONST, 0, sv);
//							PL_parser.yylval.opval.op_private = OPpCONST_BARE;
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
//							if (PL_parser.oldoldbufptr &&
//									PL_parser.oldoldbufptr < PL_parser.bufptr &&
//									(PL_parser.oldoldbufptr == PL_parser.last_lop
//											|| PL_parser.oldoldbufptr == PL_parser.last_uni) &&
//		    /* NO SKIPSPACE BEFORE HERE! */
//									(PL_parser.expect == XREF ||
//											((PL_opargs[PL_parser.last_lop_op] >> OASHIFT)& 7) == OA_FILEREF))
//							{
//								boolean immediate_paren = PL_parser.linestr[s] == '(';
//
//		    /* (Now we can afford to cross potential line boundary.) */
//								s = skipspace(s);
//
//		    /* Two barewords in a row may indicate method call. */
//
//								if ((isIDFIRST_lazy_if(s,UTF) || PL_parser.linestr[s] == '$') &&
//								(tmp = intuit_method(s, lex ? NULL : sv, cv))) {
//								goto method;
//							}
//
//		    /* If not a declared subroutine, it's an indirect object. */
//		    /* (But it's an indir obj regardless for sort.) */
//		    /* Also, if "_" follows a filetest operator, it's a bareword */
//
//								if (
//										( !immediate_paren && (PL_parser.last_lop_op == OP_SORT ||
//												(!cv &&
//														(PL_parser.last_lop_op != OP_MAPSTART &&
//																PL_parser.last_lop_op != OP_GREPSTART))))
//												|| (PL_parser.tokenbuf[0] == '_' && PL_parser.tokenbuf[1] == '\0'
//												&& ((PL_opargs[PL_parser.last_lop_op] & OA_CLASS_MASK) == OA_FILESTATOP))
//										)
//								{
//									PL_parser.expect = (PL_parser.last_lop == PL_parser.oldoldbufptr) ? XTERM : XOPERATOR;
//									goto bareword;
//								}
//							}
//
//							PL_parser.expect = XOPERATOR;
//							s = skipspace(s);
//
//		/* Is this a word before a => operator? */
//							if (PL_parser.linestr[s] == '=' && s[1] == '>' && !pkgname) {
//								op_free(rv2cv_op);
//								PL_parser.copline = (CopLINE(PL_parser.curcop) < PL_parser.copline ? CopLINE(PL_parser.curcop) : PL_parser.copline);
//								if (gvp || (lex && !off)) {
//									assert (cSVOPx(PL_parser.yylval.opval)->op_sv == sv);
//			/* This is our own scalar, created a few lines
//			   above, so this is safe. */
//									SvREADONLY_off(sv);
//									sv_setpv(sv, PL_parser.tokenbuf);
//									if (UTF && !IN_BYTES
//											&& is_utf8_string((private int*)PL_parser.tokenbuf, len))
//										SvUTF8_on(sv);
//									SvREADONLY_on(sv);
//								}
//								TERM(WORD);
//							}
//
//		/* If followed by a paren, it's certainly a subroutine. */
//							if (PL_parser.linestr[s] == '(') {
//								PL_parser.copline = (CopLINE(PL_parser.curcop) < PL_parser.copline ? CopLINE(PL_parser.curcop) : PL_parser.copline);
//								if (cv) {
//									d = s + 1;
//									while (isBLANK(*d))
//									d++;
//									if (*d == ')' && (sv = cv_const_sv_or_av(cv))) {
//										s = d + 1;
//										goto its_constant;
//									}
//								}
//								 PL_parser.nextval[PL_parser.nexttoke].opval =
//										off ? rv2cv_op : PL_parser.yylval.opval;
//								if (off)
//									op_free(PL_parser.yylval.opval), force_next(PRIVATEREF);
//								else op_free(rv2cv_op),	   force_next(WORD);
//								PL_parser.yylval.ival = 0;
//								TOKEN('&');
//							}
//
//		/* If followed by var or block, call it a method (unless sub) */
//
//							if ((PL_parser.linestr[s] == '$' || PL_parser.linestr[s] == '{') && !cv) {
//								op_free(rv2cv_op);
//								PL_parser.last_lop = PL_parser.oldbufptr;
//								PL_parser.last_lop_op = OP_METHOD;
//								if (!PL_parser.lex_allbrackets &&
//										PL_parser.lex_fakeof > LEX_FAKEEOF_LOWLOGIC)
//									PL_parser.lex_fakeof = LEX_FAKEEOF_LOWLOGIC;
//								PL_parser.expect = XBLOCKTERM;
//								PL_parser.bufptr = s;
//								return REPORT(METHOD);
//							}
//
//		/* If followed by a bareword, see if it looks like indir obj. */
//
//							if (tmp == 1 && !orig_keyword
//									&& (isIDFIRST_lazy_if(s,UTF) || PL_parser.linestr[s] == '$')
//							&& (tmp = intuit_method(s, lex ? NULL : sv, cv))) {
//								method:
//								if (lex && !off) {
//									assert(cSVOPx(PL_parser.yylval.opval)->op_sv == sv);
//									SvREADONLY_off(sv);
//									sv_setpvn(sv, PL_parser.tokenbuf, len);
//									if (UTF && !IN_BYTES
//											&& is_utf8_string((private int*)PL_parser.tokenbuf, len))
//										SvUTF8_on (sv);
//									else SvUTF8_off(sv);
//								}
//								op_free(rv2cv_op);
//								if (tmp == METHOD && !PL_parser.lex_allbrackets &&
//										PL_parser.lex_fakeof > LEX_FAKEEOF_LOWLOGIC)
//									PL_parser.lex_fakeof = LEX_FAKEEOF_LOWLOGIC;
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
//									SvREFCNT_dec(((SVOP*)PL_parser.yylval.opval)->op_sv);
//									((SVOP*)PL_parser.yylval.opval)->op_sv = SvREFCNT_inc_simple(sv);
//									if (SvTYPE(sv) == SVt_PVAV)
//										PL_parser.yylval.opval = newUNOP(OP_RV2AV, OPf_PARENS,
//												PL_parser.yylval.opval);
//									else {
//										PL_parser.yylval.opval.op_private = 0;
//										PL_parser.yylval.opval.op_folded = 1;
//										PL_parser.yylval.opval.op_flags |= OPf_SPECIAL;
//									}
//									TOKEN(WORD);
//								}
//
//								op_free(PL_parser.yylval.opval);
//								PL_parser.yylval.opval =
//										off ? (OP *)newCVREF(0, rv2cv_op) : rv2cv_op;
//								PL_parser.yylval.opval.op_private |= OPpENTERSUB_NOPAREN;
//								PL_parser.last_lop = PL_parser.oldbufptr;
//								PL_parser.last_lop_op = OP_ENTERSUB;
//		    /* Is there a prototype? */
//								if (
//										SvPOK(cv))
//								{
//									STRLEN protolen = CvPROTOLEN(cv);
//									const char *proto = CvPROTO(cv);
//									boolean optional;
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
//									if (*proto == '&' && PL_parser.linestr[s] == '{') {
//									if (PL_curstash)
//										sv_setpvs(PL_subname, "__ANON__");
//									else
//										sv_setpvs(PL_subname, "__ANON__::__ANON__");
//									if (!PL_parser.lex_allbrackets &&
//											PL_parser.lex_fakeof > LEX_FAKEEOF_LOWLOGIC)
//										PL_parser.lex_fakeof = LEX_FAKEEOF_LOWLOGIC;
//									PREBLOCK(LSTOPSUB);
//								}
//								}
//								 PL_parser.nextval[PL_parser.nexttoke].opval = PL_parser.yylval.opval;
//								PL_parser.expect = XTERM;
//								force_next(off ? PRIVATEREF : WORD);
//								if (!PL_parser.lex_allbrackets &&
//										PL_parser.lex_fakeof > LEX_FAKEEOF_LOWLOGIC)
//									PL_parser.lex_fakeof = LEX_FAKEEOF_LOWLOGIC;
//								TOKEN(NOAMP);
//							}
//
//		/* Call it a bare word */
//
//							if (PL_hints & HINT_STRICT_SUBS)
//								PL_parser.yylval.opval.op_private |= OPpCONST_STRICT;
//							else {
//								bareword:
//		    /* after "print" and similar functions (corresponding to
//		     * "F? L" in opcode.pl), whatever wasn't already parsed as
//		     * a filehandle should be subject to "strict subs".
//		     * Likewise for the optional indirect-object argument to system
//		     * or exec, which can't be a bareword */
//								if ((PL_parser.last_lop_op == OP_PRINT
//										|| PL_parser.last_lop_op == OP_PRTF
//										|| PL_parser.last_lop_op == OP_SAY
//										|| PL_parser.last_lop_op == OP_SYSTEM
//										|| PL_parser.last_lop_op == OP_EXEC)
//										&& (PL_hints & HINT_STRICT_SUBS))
//									PL_parser.yylval.opval.op_private |= OPpCONST_STRICT;
//								if (lastchar != '-') {
//									if (ckWARN(WARN_RESERVED)) {
//										d = PL_parser.tokenbuf;
//										while (isLOWER(*d))
//										d++;
//										if (!*d && !gv_stashpv(PL_parser.tokenbuf, UTF ? SVf_UTF8 : 0))
//										{
//                                /* PL_warn_reserved is constant */
//											GCC_DIAG_IGNORE(-Wformat-nonliteral);
//											Perl_warner(aTHX_ packWARN(WARN_RESERVED), PL_warn_reserved,
//													PL_parser.tokenbuf);
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
//										UTF8fARG(UTF, strlen(PL_parser.tokenbuf),
//												PL_parser.tokenbuf));
//								Perl_ck_warner_d(aTHX_ packWARN(WARN_AMBIGUOUS),
//										"Ambiguous use of %c resolved as operator %c",
//										lastchar, lastchar);
//							}
//							TOKEN(WORD);
//						}
//
//					case KEY___FILE__:
//						FUN0OP(
//								new OP(OP_CONST, 0, newSVpv(CopFILE(PL_curcop),0))
//						);
//
//					case KEY___LINE__:
//						FUN0OP(
//								new OP(OP_CONST, 0,
//										Perl_newSVpvf(aTHX_ "%"IVdf, (IV)CopLINE(PL_curcop)))
//						);
//
//					case KEY___PACKAGE__:
//						FUN0OP(
//								new OP(OP_CONST, 0,
//										(PL_curstash
//												? newSVhek(HvNAME_HEK(PL_curstash))
//												: &PL_sv_undef))
//						);
//
//					case KEY___DATA__:
//					case KEY___END__: {
//						GV *gv;
//						if (PL_parser.rsfp && (!PL_in_eval || PL_parser.tokenbuf[2] == 'D')) {
//							HV * const stash = PL_parser.tokenbuf[2] == 'D' && PL_curstash
//									? PL_curstash
//									: PL_defstash;
//							gv = (GV *)*hv_fetchs(stash, "DATA", 1);
//							if (!isGV(gv))
//								gv_init(gv,stash,"DATA",4,0);
//							GvMULTI_on(gv);
//							if (!GvIO(gv))
//								GvIOp(gv) = newIO();
//							IoIFP(GvIOp(gv)) = PL_parser.rsfp;
//							#if defined(HAS_FCNTL) && defined(F_SETFD)
//							{
//								const int fd = PerlIO_fileno(PL_parser.rsfp);
//								fcntl(fd,F_SETFD,fd >= 3);
//							}
//							#endif
//		/* Mark this internal pseudo-handle as clean */
//							IoFLAGS(GvIOp(gv)) |= IOf_UNTAINT;
//							if ((PerlIO*)PL_parser.rsfp == PerlIO_stdin())
//							IoTYPE(GvIOp(gv)) = IoTYPE_STD;
//							else
//							IoTYPE(GvIOp(gv)) = IoTYPE_RDONLY;
//							#if defined(WIN32) && !defined(PERL_TEXTMODE_SCRIPTS)
//		/* if the script was opened in binmode, we need to revert
//		 * it to text mode for compatibility; but only iff it has CRs
//		 * XXX this is a questionable hack at best. */
//							if (PL_parser.bufend-PL_parser.bufptr > 2
//									&& PL_parser.bufend[-1] == '\n' && PL_parser.bufend[-2] == '\r')
//							{
//								Off_t loc = 0;
//								if (IoTYPE(GvIOp(gv)) == IoTYPE_RDONLY) {
//									loc = PerlIO_tell(PL_parser.rsfp);
//									(void)PerlIO_seek(PL_parser.rsfp, 0L, 0);
//								}
//								#ifdef NETWARE
//								if (PerlLIO_setmode(PL_parser.rsfp, O_TEXT) != -1) {
//									#else
//									if (PerlLIO_setmode(PerlIO_fileno(PL_parser.rsfp), O_TEXT) != -1) {
//										#endif	/* NETWARE */
//										if (loc > 0)
//											PerlIO_seek(PL_parser.rsfp, loc, 0);
//									}
//								}
//								#endif
//								#ifdef PERLIO_LAYERS
//								if (!IN_BYTES) {
//									if (UTF)
//										PerlIO_apply_layers(aTHX_ PL_parser.rsfp, NULL, ":utf8");
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
//										PerlIO_apply_layers(aTHX_ PL_parser.rsfp, NULL,
//												Perl_form(aTHX_ ":encoding(%"SVf")",
//														SVfARG(name)));
//										FREETMPS;
//										LEAVE;
//									}
//								}
//								#endif
//									PL_parser.rsfp = NULL;
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
//							if (PL_parser.expect == XSTATE) {
//								s = PL_parser.bufptr;
//								goto really_sub;
//							}
//							goto just_a_word;
//
//							case_KEY_CORE:
//							{
//								STRLEN olen = len;
//								d = s;
//								s += 2;
//								s = scan_word(s, PL_parser.tokenbuf, sizeof PL_parser.tokenbuf, false, &len);
//								if ((PL_parser.linestr[s] == ':' && s[1] == ':')
//								|| (!(tmp = keyword(PL_parser.tokenbuf, len, 1)) && PL_parser.linestr[s] == '\''))
//								{
//									s = d;
//									len = olen;
//									Copy(PL_parser.bufptr, PL_parser.tokenbuf, olen, char);
//									goto just_a_word;
//								}
//								if (!tmp)
//									Perl_croak(aTHX_ "CORE::%"UTF8f" is not a keyword",
//											UTF8fARG(UTF, len, PL_parser.tokenbuf));
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
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_LOWLOGIC)
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
//							if (PL_parser.linestr[s] == '{')
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
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE)
//								return REPORT(0);
//							Eop(OP_SCMP);
//
//						case KEY_caller:
//							UNI(OP_CALLER);
//
//						case KEY_crypt:
//							#ifdef FCRYPT
//							if (!PL_cryptseen) {
//								PL_cryptseen = true;
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
//							if (PL_parser.linestr[s] == '{')
//							PRETERMBLOCK(DO);
//							if (PL_parser.linestr[s] != '\'') {
//							*PL_parser.tokenbuf = '&';
//							d = scan_word(s, PL_parser.tokenbuf + 1, sizeof PL_parser.tokenbuf - 1,
//									1, &len);
//							if (len && (len != 4 || strNE(PL_parser.tokenbuf+1, "CORE"))
//									&& !keyword(PL_parser.tokenbuf + 1, len, 0)) {
//								d = skipspace(d);
//								if (*d == '(') {
//									force_ident_maybe_lex('&');
//									s = d;
//								}
//							}
//						}
//						if (orig_keyword == KEY_do) {
//							orig_keyword = 0;
//							PL_parser.yylval.ival = 1;
//						}
//						else
//							PL_parser.yylval.ival = 0;
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
//							PL_parser.yylval.ival = CopLINE(PL_curcop);
//							OPERATOR(ELSIF);
//
//						case KEY_eq:
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE)
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
//							if (PL_parser.linestr[s] == '{') { /* block eval */
//							PL_parser.expect = XTERMBLOCK;
//							UNIBRACK(OP_ENTERTRY);
//						}
//						else { /* string eval */
//							PL_parser.expect = XTERM;
//							UNIBRACK(OP_ENTEREVAL);
//						}
//
//						case KEY_evalbytes:
//							PL_parser.expect = XTERM;
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
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							PL_parser.yylval.ival = CopLINE(PL_curcop);
//							s = skipspace(s);
//							if (PL_parser.expect == XSTATE && isIDFIRST_lazy_if(s,UTF)) {
//								char *p = s;
//
//								if ((PL_parser.bufend - p) >= 3 &&
//										strnEQ(p, "my", 2) && isSPACE(*(p + 2)))
//								p += 2;
//								else if ((PL_parser.bufend - p) >= 4 &&
//										strnEQ(p, "our", 3) && isSPACE(*(p + 3)))
//								p += 3;
//								p = skipspace(p);
//                /* skip optional package name, as in "for my abc $x (..)" */
//								if (isIDFIRST_lazy_if(p,UTF)) {
//									p = scan_word(p, PL_parser.tokenbuf, sizeof PL_parser.tokenbuf, true, &len);
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
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE)
//								return REPORT(0);
//							Rop(OP_SGT);
//
//						case KEY_ge:
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE)
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
//							PL_parser.yylval.ival = CopLINE(PL_curcop);
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
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							PL_parser.yylval.ival = CopLINE(PL_curcop);
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
//							PL_parser.yylval.ival = 0;
//							OPERATOR(LOCAL);
//
//						case KEY_length:
//							UNI(OP_LENGTH);
//
//						case KEY_lt:
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE)
//								return REPORT(0);
//							Rop(OP_SLT);
//
//						case KEY_le:
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE)
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
//							PL_parser.in_my = (private int)tmp;
//							s = skipspace(s);
//							if (isIDFIRST_lazy_if(s,UTF)) {
//								s = scan_word(s, PL_parser.tokenbuf, sizeof PL_parser.tokenbuf, true, &len);
//								if (len == 3 && strnEQ(PL_parser.tokenbuf, "sub", 3))
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
//								PL_parser.in_my_stash = find_in_my_stash(PL_parser.tokenbuf, len);
//								if (!PL_parser.in_my_stash) {
//									char tmpbuf[1024];
//									int len;
//									PL_parser.bufptr = s;
//									len = my_snprintf(tmpbuf, sizeof(tmpbuf), "No such class %.1000s", PL_parser.tokenbuf);
//									PERL_MY_SNPRINTF_POST_GUARD(len, sizeof(tmpbuf));
//									yyerror_pv(tmpbuf, UTF ? SVf_UTF8 : 0);
//								}
//							}
//							PL_parser.yylval.ival = 1;
//							OPERATOR(MY);
//
//						case KEY_next:
//							LOOPX(OP_NEXT);
//
//						case KEY_ne:
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_COMPARE)
//								return REPORT(0);
//							Eop(OP_SNE);
//
//						case KEY_no:
//							s = tokenize_use(0, s);
//							TOKEN(USE);
//
//						case KEY_not:
//							if (PL_parser.linestr[s] == '(' || (s = skipspace(s), PL_parser.linestr[s] == '('))
//							FUN1(OP_NOT);
//							else {
//							if (!PL_parser.lex_allbrackets &&
//									PL_parser.lex_fakeof > LEX_FAKEEOF_LOWLOGIC)
//								PL_parser.lex_fakeof = LEX_FAKEEOF_LOWLOGIC;
//							OPERATOR(NOTOP);
//						}
//
//						case KEY_open:
//							s = skipspace(s);
//							if (isIDFIRST_lazy_if(s,UTF)) {
//								const char *t;
//								d = scan_word(s, PL_parser.tokenbuf, sizeof PL_parser.tokenbuf, false,
//										&len);
//								for (t=d; isSPACE(PL_parser.linestr[t]);)
//								t++;
//								if ( PL_parser.linestr[t] && strchr("|&*+-=!?:.", *t) && ckWARN_d(WARN_PRECEDENCE)
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
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_LOWLOGIC)
//								return REPORT(0);
//							PL_parser.yylval.ival = OP_OR;
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
//							checkcomma(s,PL_parser.tokenbuf,"filehandle");
//							LOP(OP_PRINT,XREF);
//
//						case KEY_printf:
//							checkcomma(s,PL_parser.tokenbuf,"filehandle");
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
//							s = force_word(s,WORD,false,true);
//							s = skipspace(s);
//							s = force_strict_version(s);
//							PREBLOCK(PACKAGE);
//
//						case KEY_pipe:
//							LOP(OP_PIPE_OP,XTERM);
//
//						case KEY_q:
//							s = scan_str(s,false,false,false,NULL);
//							if (!s)
//								missingterm(NULL);
//							COPLINE_SET_FROM_MULTI_END;
//							PL_parser.yylval.ival = OP_CONST;
//							TERM(sublex_start());
//
//						case KEY_quotemeta:
//							UNI(OP_QUOTEMETA);
//
//						case KEY_qw: {
//							OP *words = NULL;
//							s = scan_str(s,false,false,false,NULL);
//							if (!s)
//								missingterm(NULL);
//							COPLINE_SET_FROM_MULTI_END;
//							PL_parser.expect = XOPERATOR;
//							if (SvCUR(PL_parser.lex_stuff)) {
//								int warned_comma = !ckWARN(WARN_QW);
//								int warned_comment = warned_comma;
//								d = SvPV_force(PL_parser.lex_stuff, len);
//								while (len) {
//									for (; isSPACE(*d) && len; --len, ++d)
//			/**/;
//									if (len) {
//										SV PL_parser.linestr[s]v;
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
//										sv = newSVpvn_utf8(b, d-b, DO_UTF8(PL_parser.lex_stuff));
//										words = op_append_elem(OP_LIST, words,
//												newSVOP(OP_CONST, 0, tokeq(sv)));
//									}
//								}
//							}
//							if (!words)
//								words = newNULLLIST();
//							if (PL_parser.lex_stuff) {
//								SvREFCNT_dec(PL_parser.lex_stuff);
//								PL_parser.lex_stuff = NULL;
//							}
//							PL_parser.expect = XOPERATOR;
//							PL_parser.yylval.opval = sawparens(words);
//							TOKEN(QWLIST);
//						}
//
//						case KEY_qq:
//							s = scan_str(s,false,false,false,NULL);
//							if (!s)
//								missingterm(NULL);
//							PL_parser.yylval.ival = OP_STRINGIFY;
//							if (SvIVX(PL_parser.lex_stuff) == '\'')
//								SvIV_set(PL_parser.lex_stuff, 0);	/* qq'$foo' should interpolate */
//							TERM(sublex_start());
//
//						case KEY_qr:
//							s = scan_pat(s,OP_QR);
//							TERM(sublex_start());
//
//						case KEY_qx:
//							s = scan_str(s,false,false,false,NULL);
//							if (!s)
//								missingterm(NULL);
//							PL_parser.yylval.ival = OP_BACKTICK;
//							TERM(sublex_start());
//
//						case KEY_return:
//							OLDLOP(OP_RETURN);
//
//						case KEY_require:
//							s = skipspace(s);
//							if (isDIGIT(PL_parser.linestr[s])) {
//							s = force_version(s, false);
//						}
//						else if (PL_parser.linestr[s] != 'v' || !isDIGIT(s[1])
//								|| (s = force_version(s, true), PL_parser.linestr[s] == 'v'))
//						{
//							*PL_parser.tokenbuf = '\0';
//							s = force_word(s,WORD,true,true);
//							if (isIDFIRST_lazy_if(PL_parser.tokenbuf,UTF))
//								gv_stashpvn(PL_parser.tokenbuf, strlen(PL_parser.tokenbuf),
//										GV_ADD | (UTF ? SVf_UTF8 : 0));
//							else if (PL_parser.linestr[s] == '<')
//							yyerror("<> at require-statement should be quotes");
//						}
//						if (orig_keyword == KEY_require) {
//							orig_keyword = 0;
//							PL_parser.yylval.ival = 1;
//						}
//						else
//							PL_parser.yylval.ival = 0;
//						PL_parser.expect = PL_parser.nexttoke ? XOPERATOR : XTERM;
//						PL_parser.bufptr = s;
//						PL_parser.last_uni = PL_parser.oldbufptr;
//						PL_parser.last_lop_op = OP_REQUIRE;
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
//							if (PL_parser.yylval.opval)
//								TERM(sublex_start());
//							else
//								TOKEN(1);	/* force error */
//
//						case KEY_say:
//							checkcomma(s,PL_parser.tokenbuf,"filehandle");
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
//							checkcomma(s,PL_parser.tokenbuf,"subroutine name");
//							s = skipspace(s);
//							PL_parser.expect = XTERM;
//							s = force_word(s,WORD,true,true);
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
//								char * const tmpbuf = PL_parser.tokenbuf + 1;
//								expectation attrful;
//								boolean have_name, have_proto;
//								const int key = tmp;
//								SV *format_name = NULL;
//
//								d = s;
//								s = skipspace(s);
//
//								if (isIDFIRST_lazy_if(s,UTF) || PL_parser.linestr[s] == '\'' ||
//									(PL_parser.linestr[s] == ':' && s[1] == ':'))
//								{
//
//									PL_parser.expect = XBLOCK;
//									attrful = XATTRBLOCK;
//									d = scan_word(s, tmpbuf, sizeof PL_parser.tokenbuf - 1, true,
//											&len);
//									if (key == KEY_format)
//										format_name = S_newSV_maybe_utf8(aTHX_ s, d - s);
//									*PL_parser.tokenbuf = '&';
//									if (memchr(tmpbuf, ':', len) || key != KEY_sub
//											|| pad_findmy_pvn(
//											PL_parser.tokenbuf, len + 1, UTF ? SVf_UTF8 : 0
//									) != NOT_IN_PAD)
//										sv_setpvn(PL_subname, tmpbuf, len);
//									else {
//										sv_setsv(PL_subname,PL_curstname);
//										sv_catpvs(PL_subname,"::");
//										sv_catpvn(PL_subname,tmpbuf,len);
//									}
//									if (SvUTF8(PL_parser.linestr))
//										SvUTF8_on(PL_subname);
//									have_name = true;
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
//											"Missing name in \"%s\"", PL_parser.bufptr);
//								}
//								PL_parser.expect = XTERMBLOCK;
//								attrful = XATTRTERM;
//								sv_setpvs(PL_subname,"?");
//								have_name = false;
//							}
//
//								if (key == KEY_format) {
//									if (format_name) {
//										 PL_parser.nextval[PL_parser.nexttoke].opval
//												= new OP(OP_CONST,0, format_name);
//										 PL_parser.nextval[PL_parser.nexttoke].opval.op_private |= OPpCONST_BARE;
//										force_next(WORD);
//									}
//									PREBLOCK(FORMAT);
//								}
//
//		/* Look for a prototype */
//								if (PL_parser.linestr[s] == '(' && !FEATURE_SIGNATURES_IS_ENABLED) {
//								s = scan_str(s,false,false,false,NULL);
//								COPLINE_SET_FROM_MULTI_END;
//								if (!s)
//									Perl_croak(aTHX_ "Prototype not terminated");
//								(void)validate_proto(PL_subname, PL_parser.lex_stuff, ckWARN(WARN_ILLEGALPROTO));
//								have_proto = true;
//
//								s = skipspace(s);
//							}
//								else
//								have_proto = false;
//
//								if (PL_parser.linestr[s] == ':' && s[1] != ':')
//								PL_parser.expect = attrful;
//								else if ((PL_parser.linestr[s] != '{' && PL_parser.linestr[s] != '(') && key == KEY_sub) {
//								if (!have_name)
//									Perl_croak(aTHX_ "Illegal declaration of anonymous subroutine");
//								else if (PL_parser.linestr[s] != ';' && PL_parser.linestr[s] != '}')
//								Perl_croak(aTHX_ "Illegal declaration of subroutine %"SVf, SVfARG(PL_subname));
//							}
//
//								if (have_proto) {
//									 PL_parser.nextval[PL_parser.nexttoke].opval =
//											new OP(OP_CONST, 0, PL_parser.lex_stuff);
//									PL_parser.lex_stuff = NULL;
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
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							PL_parser.yylval.ival = CopLINE(PL_curcop);
//							OPERATOR(UNTIL);
//
//						case KEY_unless:
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							PL_parser.yylval.ival = CopLINE(PL_curcop);
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
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							PL_parser.yylval.ival = CopLINE(PL_curcop);
//							Perl_ck_warner_d(aTHX_
//									packWARN(WARN_EXPERIMENTAL__SMARTMATCH),
//									"when is experimental");
//							OPERATOR(WHEN);
//
//						case KEY_while:
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_NONEXPR)
//								return REPORT(0);
//							PL_parser.yylval.ival = CopLINE(PL_curcop);
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
//							if (PL_parser.expect == XOPERATOR) {
//								if (PL_parser.linestr[s] == '=' && !PL_parser.lex_allbrackets &&
//										PL_parser.lex_fakeof >= LEX_FAKEEOF_ASSIGN)
//								return REPORT(0);
//								Mop(OP_REPEAT);
//							}
//							check_uni();
//							goto just_a_word;
//
//						case KEY_xor:
//							if (!PL_parser.lex_allbrackets && PL_parser.lex_fakeof >= LEX_FAKEEOF_LOWLOGIC)
//								return REPORT(0);
//							PL_parser.yylval.ival = OP_XOR;
//							OPERATOR(OROP);
//					}
//				}}
//		*/
		return null;
	}

	public void yywarn( final String s)
	{
//		PERL_ARGS_ASSERT_YYWARN;

// 		PL_in_eval |= EVAL_WARNONLY; // embedvar.h #define PL_in_eval      (vTHX->Iin_eval)

		yyerror_pv(s); // this will die
	}

	public void yyerror(final String s)
	{
//		PERL_ARGS_ASSERT_YYERROR;
		yyerror_pvn(s);
	}

	public void yyerror_pv(final String s)
	{
//		PERL_ARGS_ASSERT_YYERROR_PV;
		throw new Error(s);
	}

	// @todo dig this one
	public void yyerror_pvn(final String s)
	{
		throw new Error(s);

//		const char *context = NULL;
//		int contlen = -1;
//		SV *msg;
//		SV * const where_sv = newSVpvs_flags("", SVs_TEMP);
//		int yychar  = PL_parser.yychar;
//
//		PERL_ARGS_ASSERT_YYERROR_PVN;
//
//		if (!yychar || (yychar == ';' && !PL_rsfp))
//			sv_catpvs(where_sv, "at EOF");
//		else if (PL_oldoldbufptr && PL_bufptr > PL_oldoldbufptr &&
//				PL_bufptr - PL_oldoldbufptr < 200 && PL_oldoldbufptr != PL_oldbufptr &&
//				PL_oldbufptr != PL_bufptr) {
//    /*
//        Only for NetWare:
//        The code below is removed for NetWare because it abends/crashes on NetWare
//        when the script has error such as not having the closing quotes like:
//            if ($var eq "value)
//        Checking of white spaces is anyway done in NetWare code.
//    */
//			#ifndef NETWARE
//			while (isSPACE(*PL_oldoldbufptr))
//			PL_oldoldbufptr++;
//			#endif
//					context = PL_oldoldbufptr;
//			contlen = PL_bufptr - PL_oldoldbufptr;
//		}
//		else if (PL_oldbufptr && PL_bufptr > PL_oldbufptr &&
//				PL_bufptr - PL_oldbufptr < 200 && PL_oldbufptr != PL_bufptr) {
//    /*
//        Only for NetWare:
//        The code below is removed for NetWare because it abends/crashes on NetWare
//        when the script has error such as not having the closing quotes like:
//            if ($var eq "value)
//        Checking of white spaces is anyway done in NetWare code.
//    */
//			#ifndef NETWARE
//			while (isSPACE(*PL_oldbufptr))
//			PL_oldbufptr++;
//			#endif
//					context = PL_oldbufptr;
//			contlen = PL_bufptr - PL_oldbufptr;
//		}
//		else if (yychar > 255)
//			sv_catpvs(where_sv, "next token ???");
//		else if (yychar == -2) { /* YYEMPTY */
//			if (PL_lex_state == LEX_NORMAL ||
//					(PL_lex_state == LEX_KNOWNEXT && PL_lex_defer == LEX_NORMAL))
//				sv_catpvs(where_sv, "at end of line");
//			else if (PL_lex_inpat)
//				sv_catpvs(where_sv, "within pattern");
//			else
//				sv_catpvs(where_sv, "within string");
//		}
//		else {
//			sv_catpvs(where_sv, "next char ");
//			if (yychar < 32)
//				Perl_sv_catpvf(aTHX_ where_sv, "^%c", toCTRL(yychar));
//			else if (isPRINT_LC(yychar)) {
//				const char string = yychar;
//				sv_catpvn(where_sv, &string, 1);
//			}
//			else
//				Perl_sv_catpvf(aTHX_ where_sv, "\\%03o", yychar & 255);
//		}
//		msg = newSVpvn_flags(s, len, (flags & SVf_UTF8) | SVs_TEMP);
//		Perl_sv_catpvf(aTHX_ msg, " at %s line %"IVdf", ",
//				OutCopFILE(PL_curcop),
//				(IV)(PL_parser.preambling == NOLINE
//						? CopLINE(PL_curcop)
//						: PL_parser.preambling));
//		if (context)
//			Perl_sv_catpvf(aTHX_ msg, "near \"%"UTF8f"\"\n",
//					UTF8fARG(UTF, contlen, context));
//		else
//			Perl_sv_catpvf(aTHX_ msg, "%"SVf"\n", SVfARG(where_sv));
//		if (PL_multi_start < PL_multi_end && (U32)(CopLINE(PL_curcop) - PL_multi_end) <= 1) {
//			Perl_sv_catpvf(aTHX_ msg,
//					"  (Might be a runaway multi-line %c%c string starting on line %"IVdf")\n",
//					(int)PL_multi_open,(int)PL_multi_close,(IV)PL_multi_start);
//			PL_multi_end = 0;
//		}
//		if (PL_in_eval & EVAL_WARNONLY) {
//			PL_in_eval &= ~EVAL_WARNONLY;
//			Perl_ck_warner_d(aTHX_ packWARN(WARN_SYNTAX), "%"SVf, SVfARG(msg));
//		}
//		else
//			qerror(msg);
//		if (PL_error_count >= 10) {
//			SV * errsv;
//			if (PL_in_eval && ((errsv = ERRSV), SvCUR(errsv)))
//			Perl_croak(aTHX_ "%"SVf"%s has too many errors.\n",
//					SVfARG(errsv), OutCopFILE(PL_curcop));
//			else
//			Perl_croak(aTHX_ "%s has too many errors.\n",
//					OutCopFILE(PL_curcop));
//		}
//		PL_in_my = 0;
//		PL_in_my_stash = NULL;
//		return 0;
	}


	public String Perl_form(String format, Object ... args )
	{
		return String.format(format, args);
	}

	// Perl_allocmy From op.c

/* "register" allocation */

	public int allocmy(final char[] name, final int len, final int flags)
	{
		int off = 0;
		final boolean is_our = PL_parser.in_my == KEY_our;

//		PERL_ARGS_ASSERT_ALLOCMY;

    /* complain about "my $<special_var>" etc etc */
//		if (len > 0 &&
//				!(is_our
//					|| isALPHA(name[1])
//					||(
//						(flags & SVf_UTF8)
//							&& isIDFIRST_utf8((U8 *)name+1)
//					) ||(name[1] == '_' && (*name == '$' || len > 2))))
//		{
//			if (!(flags & SVf_UTF8 && UTF8_IS_START(name[1]))
//					&& (!isPRINT(name[1]) || strchr("\t\n\r\f", name[1]))) {
//				yyerror(
//					Perl_form(
//						"Can't use global %c^%c%.PL_parser.linestr[s] in \"%s\""
//						, name[0]
//						, toCTRL(name[1])
//						, (int)(len - 2)
//						, name + 2
//						, PL_parser.in_my == KEY_state ? "state" : "my"));
//			} else {
//				yyerror_pv(
//					Perl_form(
//						"Can't use global %.PL_parser.linestr[s] in \"%s\""
//						, (int) len
//						, name,
//						PL_parser.in_my == KEY_state ? "state" : "my"
//					)
//				); // , flags & SVf_UTF8
//			}
//		}
//		else if (len == 2 && name[1] == '_' && !is_our)
//    /* diag_listed_as: Use of my $_ is experimental */
//		Perl_ck_warner_d(packWARN(WARN_EXPERIMENTAL__LEXICAL_TOPIC),
//				"Use of %s $_ is experimental",
//				PL_parser.in_my == KEY_state
//						? "state"
//						: "my");
//
//    /* allocate a spare slot and store the name in that slot */
//
//		off = pad_add_name_pvn(name, len,
//				(is_our ? padadd_OUR :
//						PL_parser.in_my == KEY_state ? padadd_STATE : 0)
//				| ( flags & SVf_UTF8 ? SVf_UTF8 : 0 ),
//				PL_parser.in_my_stash,
//				(is_our
//                /* $_ is always in main::, even with our */
//						? (PL_curstash && !memEQs(name,len,"$_")
//						? PL_curstash
//						: PL_defstash)
//						: NULL
//				)
//		);
//    /* anon sub prototypes contains state vars should always be cloned,
//     * otherwise the state var would be shared between anon subs */
//
//		if (PL_parser.in_my == KEY_state && CvANON(PL_compcv))
//			CvCLONE_on(PL_compcv);

		return off;
	}

/* from op.c
=for apidoc Am|OP *|newOP|I32 type|I32 flags

Constructs, checks, and returns an op of any base type (any type that
has no extra fields).  I<type> is the opcode.  I<flags> gives the
eight bits of C<op_flags>, and, shifted up eight bits, the eight bits
of C<op_private>.

=cut
*/

//OP *
//		newOP(int  type, int flags)
//		{
//		dVAR;
//		OP *o;
//
//		if (type == -OP_ENTEREVAL) {
//		type = OP_ENTEREVAL;
//		flags |= OPpEVAL_BYTES<<8;
//		}
//
//		assert((PL_opargs[type] & OA_CLASS_MASK) == OA_BASEOP
//		|| (PL_opargs[type] & OA_CLASS_MASK) == OA_BASEOP_OR_UNOP
//		|| (PL_opargs[type] & OA_CLASS_MASK) == OA_FILESTATOP
//		|| (PL_opargs[type] & OA_CLASS_MASK) == OA_LOOPEXOP);
//
//		NewOp(1101, o, 1, OP);
//		CHANGE_TYPE(o, type);
//		o->op_flags = (U8)flags;
//
//		o->op_next = o;
//		o->op_private = (U8)(0 | (flags >> 8));
//		if (PL_opargs[type] & OA_RETSCALAR)
//		scalar(o);
//		if (PL_opargs[type] & OA_TARGET)
//		o->op_targ = pad_alloc(type, SVs_PADTMP);
//		return CHECKOP(type, o);
//		}


/*
  S_pending_ident

  Looks up an identifier in the pad or in a package

  Returns:
    PRIVATEREF if this is a lexical name.
    WORD       if this belongs to a package.

  Structure:
      if we're in a my declaration
	  croak if they tried to say my($foo::bar)
	  build the ops for a my() declaration
      if it's an access to a my() variable
	  build ops for access to a my() variable
      if in a dq string, and they've said @foo and we can't find @foo
	  warn
      build ops for a bareword
*/


	IElementType pending_ident()
	{
		int tmp = 0;
		final char pit = PL_parser.yylval.ival; // ival is int
		final int tokenbuf_len = PL_parser.tokenbuf.length;

    /* All routes through this function want to know if there is a colon.  */
		final boolean has_colon = Arrays.asList(PL_parser.tokenbuf).contains(':');

    /* if we're in a my(), we can't allow dynamics here.
       $foo'bar has already been turned into $foo::bar, so
       just check for colons.

       if it's a legal name, the OP is a PADANY.
    */
//		if (PL_parser.in_my > 0) {
//			if (PL_parser.in_my == KEY_our) {	/* "our" is merely analogous to "my" */
//				if (has_colon)
//					yyerror_pv(
//						Perl_form("No package name allowed for "
//							"variable %s in \"our\"",
//							PL_parser.tokenbuf)
//					);
//				tmp = allocmy(PL_parser.tokenbuf, tokenbuf_len, UTF ? SVf_UTF8 : 0);
//			}
//			else {
//				if (has_colon) {
//                /* "my" variable %s can't be in a package */
//                /* PL_no_myglob is constant */
//					yyerror_pv(Perl_form(PL_no_myglob,
//									PL_parser.in_my == KEY_my ? "my" : "state",
//									PL_parser.tokenbuf[0] == '&' ? "subroutin" : "variabl",
//							PL_parser.tokenbuf)
//							); // UTF ? SVf_UTF8 : 0
//				}
//
//				PL_parser.yylval.opval = newOP(OP_PADANY, 0);
//				PL_parser.yylval.opval.op_targ = allocmy(PL_parser.tokenbuf, tokenbuf_len,
//						UTF ? SVf_UTF8 : 0);
//				return PRIVATEREF;
//			}
//		}

    /*
       build the ops for accesses to a my() variable.
    */

//		if (!has_colon) {
//			if (PL_parser.in_my == 0)
//				tmp = pad_findmy_pvn(PL_parser.tokenbuf, tokenbuf_len,
//						UTF ? SVf_UTF8 : 0);
//			if (tmp != NOT_IN_PAD) {
//            /* might be an "our" variable" */
//				if (PAD_COMPNAME_FLAGS_isOUR(tmp)) {
//                /* build ops for a bareword */
//					HV *  const stash = PAD_COMPNAME_OURSTASH(tmp);
//					HEK * const stashname = HvNAME_HEK(stash);
//					SV *  const sym = newSVhek(stashname);
//					sv_catpvs(sym, "::");
//					sv_catpvn_flags(sym, PL_parser.tokenbuf+1, tokenbuf_len - 1, (UTF ? SV_CATUTF8 : SV_CATBYTES ));
//					PL_parser.yylval.opval = new OP(OP_CONST, 0, sym);
//					PL_parser.yylval.opval.op_private = OPpCONST_ENTERED;
//					if (pit != '&')
//						gv_fetchsv(sym,
//								GV_ADDMULTI,
//								((PL_parser.tokenbuf[0] == '$') ? SVt_PV
//										: (PL_parser.tokenbuf[0] == '@') ? SVt_PVAV
//										: SVt_PVHV));
//					return WORD;
//				}
//
//				PL_parser.yylval.opval = newOP(OP_PADANY, 0);
//				PL_parser.yylval.opval.op_targ = tmp;
//				return PRIVATEREF;
//			}
//		}
//
//    /*
//       Whine if they've said @foo in a doublequoted string,
//       and @foo isn't a variable we can find in the symbol
//       table.
//    */
//		if (ckWARN(WARN_AMBIGUOUS) &&
//				pit == '@' && PL_lex_state != LEX_NORMAL && !PL_lex_brackets) {
//			GV *const gv = gv_fetchpvn_flags(PL_parser.tokenbuf + 1, tokenbuf_len - 1,
//					( UTF ? SVf_UTF8 : 0 ), SVt_PVAV);
//			if ((!gv || ((PL_parser.tokenbuf[0] == '@') ? !GvAV(gv) : !GvHV(gv)))
//		/* DO NOT warn for @- and @+ */
//					&& !( PL_parser.tokenbuf[2] == '\0' &&
//					( PL_parser.tokenbuf[1] == '-' || PL_parser.tokenbuf[1] == '+' ))
//					)
//			{
//            /* Downgraded from fatal to warning 20000522 mjd */
//				Perl_warner(aTHX_ packWARN(WARN_AMBIGUOUS),
//						"Possible unintended interpolation of %"UTF8f
//						" in string",
//						UTF8fARG(UTF, tokenbuf_len, PL_parser.tokenbuf));
//			}
//		}
//
//    /* build ops for a bareword */
//		PL_parser.yylval.opval = new OP(OP_CONST, 0,
//			newSVpvn_flags(PL_parser.tokenbuf + 1,
//					tokenbuf_len - 1,
//					UTF ? SVf_UTF8 : 0 ));
//		PL_parser.yylval.opval.op_private = OPpCONST_ENTERED;
//		if (pit != '&')
//			gv_fetchpvn_flags(PL_parser.tokenbuf+1, tokenbuf_len - 1,
//					(PL_in_eval ? GV_ADDMULTI : GV_ADD)
//							| ( UTF ? SVf_UTF8 : 0 ),
//					((PL_parser.tokenbuf[0] == '$') ? SVt_PV
//							: (PL_parser.tokenbuf[0] == '@') ? SVt_PVAV
//							: SVt_PVHV));
		return WORD;
	}
}