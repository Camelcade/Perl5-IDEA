package com.perl5.lang.lexer.ported;

/**
 * Created by hurricup on 18.04.2015.
 * Made from l1_char_class_tab.h, ASCII part
 * handy.h
 */
public class CharClass
{
	// @todo make a proper validation in WE encoding
	static boolean FITS_IN_8_BITS(char c)
	{
		return c < 0xFF;
	}

	static int _CC_mask(int classnum)
	{
		return (1 << classnum);
	}


	static boolean _generic_isCC(char c, int classnum)
	{
		return FITS_IN_8_BITS(c)
			&& ((PL_charclass[c] & _CC_mask(classnum)) > 0);
	}

    /* The mask for the _A versions of the macros; it just adds in the bit for
     * ASCII. */
	static int _CC_mask_A(int classnum)
	{
		return (_CC_mask(classnum) | _CC_mask(_CC_ASCII));
	}

    /* For internal core Perl use only: the base macro for defining macros like
     * isALPHA_A.  The foo_A version makes sure that both the desired bit and
     * the ASCII bit are present */
	static boolean _generic_isCC_A(char c, int classnum){
		return (FITS_IN_8_BITS(c)
			&& ((PL_charclass[c] & _CC_mask_A(classnum))
				== _CC_mask_A(classnum)));
	}

	static boolean isALPHA_A(char c){ return  _generic_isCC_A(c, _CC_ALPHA);}
	static boolean isALPHANUMERIC_A(char c){ return _generic_isCC_A(c, _CC_ALPHANUMERIC);}
	static boolean isBLANK_A(char c){ return  _generic_isCC_A(c, _CC_BLANK);}
	static boolean isCNTRL_A(char c){ return  _generic_isCC_A(c, _CC_CNTRL);}
	static boolean isDIGIT_A(char c){ return  _generic_isCC(c, _CC_DIGIT);} /* No non-ASCII digits */
	static boolean isGRAPH_A(char c){ return  _generic_isCC_A(c, _CC_GRAPH);}
	static boolean isLOWER_A(char c){ return  _generic_isCC_A(c, _CC_LOWER);}
	static boolean isPRINT_A(char c){ return  _generic_isCC_A(c, _CC_PRINT);}
	static boolean isPSXSPC_A(char c){ return _generic_isCC_A(c, _CC_PSXSPC);}
	static boolean isPUNCT_A(char c){ return  _generic_isCC_A(c, _CC_PUNCT);}
	static boolean isSPACE_A(char c){ return  _generic_isCC_A(c, _CC_SPACE);}
	static boolean isUPPER_A(char c){ return  _generic_isCC_A(c, _CC_UPPER);}
	static boolean isWORDCHAR_A(char c){ return _generic_isCC_A(c, _CC_WORDCHAR);}
	static boolean isXDIGIT_A(char c){ return  _generic_isCC(c, _CC_XDIGIT);} /* No non-ASCII xdigits */
	static boolean isIDFIRST_A(char c){ return _generic_isCC_A(c, _CC_IDFIRST);}
	static boolean isALPHA_L1(char c){ return  _generic_isCC(c, _CC_ALPHA);}
	static boolean isALPHANUMERIC_L1(char c){ return _generic_isCC(c, _CC_ALPHANUMERIC);}
	static boolean isBLANK_L1(char c){ return  _generic_isCC(c, _CC_BLANK);}

/* continuation character for legal NAME in \N{NAME} */
	static boolean isCHARNAME_CONT(char c){ return _generic_isCC(c, _CC_CHARNAME_CONT);}

	static boolean isCNTRL_L1(char c){ return  _generic_isCC(c, _CC_CNTRL);}
	static boolean isGRAPH_L1(char c){ return  _generic_isCC(c, _CC_GRAPH);}
	static boolean isLOWER_L1(char c){ return  _generic_isCC(c, _CC_LOWER);}
	static boolean isPRINT_L1(char c){ return  _generic_isCC(c, _CC_PRINT);}
	static boolean isPSXSPC_L1(char c){ return _generic_isCC(c, _CC_PSXSPC);}
	static boolean isPUNCT_L1(char c){ return  _generic_isCC(c, _CC_PUNCT);}
	static boolean isSPACE_L1(char c){ return  _generic_isCC(c, _CC_SPACE);}
	static boolean isUPPER_L1(char c){ return  _generic_isCC(c, _CC_UPPER);}
	static boolean isWORDCHAR_L1(char c){ return _generic_isCC(c, _CC_WORDCHAR);}
	static boolean isIDFIRST_L1(char c){ return _generic_isCC(c, _CC_IDFIRST);}


	/* Character class numbers.  For internal core Perl use only.  The ones less
	 * than 32 are used in PL_charclass[] and the ones up through the one that
	 * corresponds to <_HIGHEST_REGCOMP_DOT_H_SYNC> are used by regcomp.h and
	 * related files.  PL_charclass ones use names used in l1_char_class_tab.h but
	 * their actual definitions are here.  If that file has a name not used here,
	 * it won't compile.
	 *
	 * The first group of these is ordered in what I (khw) estimate to be the
	 * frequency of their use.  This gives a slight edge to exiting a loop earlier
	 * (in reginclass() in regexec.c) */
	static final int _CC_WORDCHAR           = 0;      /* \w and [:word:] */
	static final int _CC_DIGIT              = 1;      /* \d and [:digit:] */
	static final int _CC_ALPHA              = 2;      /* [:alpha:] */
	static final int _CC_LOWER              = 3;      /* [:lower:] */
	static final int _CC_UPPER              = 4;      /* [:upper:] */
	static final int _CC_PUNCT              = 5;      /* [:punct:] */
	static final int _CC_PRINT              = 6;      /* [:print:] */
	static final int _CC_ALPHANUMERIC       = 7;      /* [:alnum:] */
	static final int _CC_GRAPH              = 8;      /* [:graph:] */
	static final int _CC_CASED              = 9;      /* [:lower:] and [:upper:] under /i */

	static final int _FIRST_NON_SWASH_CC     = 10;
/* The character classes above are implemented with swashes.  The second group
 * (just below) contains the ones implemented without.  These are also sorted
 * in rough order of the frequency of their use, except that \v should be last,
 * as it isn't a real Posix character class, and some (small) inefficiencies in
 * regular expression handling would be introduced by putting it in the middle
 * of those that are.  Also, cntrl and ascii come after the others as it may be
 * useful to group these which have no members that match above Latin1, (or
 * above ASCII in the latter case) */

	static final int _CC_SPACE             = 10;      /* \s */
	static final int _CC_BLANK             = 11;      /* [:blank:] */
	static final int _CC_XDIGIT            = 12;      /* [:xdigit:] */
	static final int _CC_PSXSPC            = 13;      /* [:space:] */
	static final int _CC_CNTRL             = 14;      /* [:cntrl:] */
	static final int _CC_ASCII             = 15;      /* [:ascii:] */
	static final int _CC_VERTSPACE         = 16;      /* \v */

	static final int _HIGHEST_REGCOMP_DOT_H_SYNC = _CC_VERTSPACE;

	/* The members of the third group below do not need to be coordinated with data
	 * structures in regcomp.[ch] and regexec.c. */
	static final int _CC_IDFIRST                  = 17;
	static final int _CC_CHARNAME_CONT            = 18;
	static final int _CC_NONLATIN1_FOLD           = 19;
	static final int _CC_NONLATIN1_SIMPLE_FOLD    = 20;
	static final int _CC_QUOTEMETA                = 21;
	static final int _CC_NON_FINAL_FOLD           = 22;
	static final int _CC_IS_IN_SOME_FOLD          = 23;
	static final int _CC_MNEMONIC_CNTRL           = 24;
	/* Unused: 25-31
	 * If more bits are needed, one could add a second word for non-64bit
	 * QUAD_IS_INT systems, using some #ifdefs to distinguish between having a 2nd
	 * word or not.  The IS_IN_SOME_FOLD bit is the most easily expendable, as it
	 * is used only for optimization (as of this writing), and differs in the
	 * Latin1 range from the ALPHA bit only in two relatively unimportant
	 * characters: the masculine and feminine ordinal indicators, so removing it
	 * would just cause /i regexes which match them to run less efficiently */
	static final int[] PL_charclass ={
	/* U+00 NUL */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+01 SOH */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+02 STX */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+03 ETX */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+04 EOT */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+05 ENQ */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+06 ACK */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+07 BEL */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA)|(1<<_CC_MNEMONIC_CNTRL),
	/* U+08 BS */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA)|(1<<_CC_MNEMONIC_CNTRL),
	/* U+09 HT */ (1<<_CC_ASCII)|(1<<_CC_BLANK)|(1<<_CC_CNTRL)|(1<<_CC_PSXSPC)|(1<<_CC_QUOTEMETA)|(1<<_CC_SPACE)|(1<<_CC_MNEMONIC_CNTRL),
	/* U+0A LF */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_PSXSPC)|(1<<_CC_QUOTEMETA)|(1<<_CC_SPACE)|(1<<_CC_VERTSPACE)|(1<<_CC_MNEMONIC_CNTRL),
	/* U+0B VT */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_PSXSPC)|(1<<_CC_QUOTEMETA)|(1<<_CC_SPACE)|(1<<_CC_VERTSPACE),
	/* U+0C FF */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_PSXSPC)|(1<<_CC_QUOTEMETA)|(1<<_CC_SPACE)|(1<<_CC_VERTSPACE)|(1<<_CC_MNEMONIC_CNTRL),
	/* U+0D CR */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_PSXSPC)|(1<<_CC_QUOTEMETA)|(1<<_CC_SPACE)|(1<<_CC_VERTSPACE)|(1<<_CC_MNEMONIC_CNTRL),
	/* U+0E SO */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+0F SI */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+10 DLE */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+11 DC1 */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+12 DC2 */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+13 DC3 */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+14 DC4 */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+15 NAK */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+16 SYN */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+17 ETB */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+18 CAN */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+19 EOM */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+1A SUB */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+1B ESC */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA)|(1<<_CC_MNEMONIC_CNTRL),
	/* U+1C FS */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+1D GS */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+1E RS */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+1F US */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+20 SP */ (1<<_CC_ASCII)|(1<<_CC_BLANK)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_PRINT)|(1<<_CC_PSXSPC)|(1<<_CC_QUOTEMETA)|(1<<_CC_SPACE),
	/* U+21 '!' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+22 '"' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+23 '#' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+24 '$' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+25 '%' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+26 '&' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+27 "'" */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+28 '(' */ (1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+29 ')' */ (1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+2A '*' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+2B '+' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+2C ',' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+2D '-' */ (1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+2E '.' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+2F '/' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+30 '0' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_DIGIT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT),
	/* U+31 '1' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_DIGIT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT),
	/* U+32 '2' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_DIGIT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT),
	/* U+33 '3' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_DIGIT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT),
	/* U+34 '4' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_DIGIT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT),
	/* U+35 '5' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_DIGIT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT),
	/* U+36 '6' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_DIGIT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT),
	/* U+37 '7' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_DIGIT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT),
	/* U+38 '8' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_DIGIT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT),
	/* U+39 '9' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_DIGIT)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT),
	/* U+3A ':' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+3B ';' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+3C '<' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+3D '=' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+3E '>' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+3F '?' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+40 '@' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+41 'A' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+42 'B' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+43 'C' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+44 'D' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+45 'E' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+46 'F' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+47 'G' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+48 'H' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+49 'I' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+4A 'J' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+4B 'K' */ (1<<_CC_NONLATIN1_SIMPLE_FOLD)|(1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+4C 'L' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+4D 'M' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+4E 'N' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+4F 'O' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+50 'P' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+51 'Q' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+52 'R' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+53 'S' */ (1<<_CC_NONLATIN1_SIMPLE_FOLD)|(1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+54 'T' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+55 'U' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+56 'V' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+57 'W' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+58 'X' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+59 'Y' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+5A 'Z' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+5B '[' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+5C '\' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+5D ']' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+5E '^' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+5F '_' */ (1<<_CC_ASCII)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_WORDCHAR),
	/* U+60 '`' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+61 'a' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+62 'b' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+63 'c' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+64 'd' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+65 'e' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+66 'f' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_XDIGIT)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+67 'g' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+68 'h' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+69 'i' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+6A 'j' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+6B 'k' */ (1<<_CC_NONLATIN1_SIMPLE_FOLD)|(1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+6C 'l' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+6D 'm' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+6E 'n' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+6F 'o' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+70 'p' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+71 'q' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+72 'r' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+73 's' */ (1<<_CC_NONLATIN1_SIMPLE_FOLD)|(1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+74 't' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+75 'u' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+76 'v' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+77 'w' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+78 'x' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+79 'y' */ (1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_NON_FINAL_FOLD)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+7A 'z' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_ASCII)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+7B '{' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+7C '|' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+7D '}' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+7E '~' */ (1<<_CC_ASCII)|(1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+7F DEL */ (1<<_CC_ASCII)|(1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+80 PAD */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+81 HOP */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+82 BPH */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+83 NBH */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+84 IND */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+85 NEL */ (1<<_CC_CNTRL)|(1<<_CC_PSXSPC)|(1<<_CC_QUOTEMETA)|(1<<_CC_SPACE)|(1<<_CC_VERTSPACE),
	/* U+86 SSA */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+87 ESA */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+88 HTS */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+89 HTJ */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+8A VTS */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+8B PLD */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+8C PLU */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+8D RI */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+8E SS2 */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+8F SS3 */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+90 DCS */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+91 PU1 */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+92 PU2 */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+93 STS */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+94 CCH */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+95 MW */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+96 SPA */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+97 EPA */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+98 SOS */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+99 SGC */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+9A SCI */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+9B CSI */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+9C ST */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+9D OSC */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+9E PM */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+9F APC */ (1<<_CC_CNTRL)|(1<<_CC_QUOTEMETA),
	/* U+A0 NBSP */ (1<<_CC_BLANK)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_PRINT)|(1<<_CC_PSXSPC)|(1<<_CC_QUOTEMETA)|(1<<_CC_SPACE),
	/* U+A1 INVERTED '!' */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+A2 CENT */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+A3 POUND */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+A4 CURRENCY */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+A5 YEN */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+A6 BROKEN BAR */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+A7 SECTION */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+A8 DIAERESIS */ (1<<_CC_GRAPH)|(1<<_CC_PRINT),
	/* U+A9 COPYRIGHT */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+AA FEMININE ORDINAL */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR),
	/* U+AB LEFT-POINTING DOUBLE ANGLE QUOTE */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+AC NOT */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+AD SOFT HYPHEN */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+AE REGISTERED */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+AF MACRON */ (1<<_CC_GRAPH)|(1<<_CC_PRINT),
	/* U+B0 DEGREE */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+B1 PLUS-MINUS */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+B2 SUPERSCRIPT 2 */ (1<<_CC_GRAPH)|(1<<_CC_PRINT),
	/* U+B3 SUPERSCRIPT 3 */ (1<<_CC_GRAPH)|(1<<_CC_PRINT),
	/* U+B4 ACUTE ACCENT */ (1<<_CC_GRAPH)|(1<<_CC_PRINT),
	/* U+B5 MICRO */ (1<<_CC_NONLATIN1_SIMPLE_FOLD)|(1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+B6 PILCROW */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+B7 MIDDLE DOT */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT),
	/* U+B8 CEDILLA */ (1<<_CC_GRAPH)|(1<<_CC_PRINT),
	/* U+B9 SUPERSCRIPT 1 */ (1<<_CC_GRAPH)|(1<<_CC_PRINT),
	/* U+BA MASCULINE ORDINAL */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR),
	/* U+BB RIGHT-POINTING DOUBLE ANGLE QUOTE */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+BC 1/4 */ (1<<_CC_GRAPH)|(1<<_CC_PRINT),
	/* U+BD 1/2 */ (1<<_CC_GRAPH)|(1<<_CC_PRINT),
	/* U+BE 3/4 */ (1<<_CC_GRAPH)|(1<<_CC_PRINT),
	/* U+BF INVERTED '?' */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_PUNCT)|(1<<_CC_QUOTEMETA),
	/* U+C0 A with GRAVE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+C1 A with ACUTE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+C2 A with '^' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+C3 A with '~' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+C4 A with DIAERESIS */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+C5 A with RING */ (1<<_CC_NONLATIN1_SIMPLE_FOLD)|(1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+C6 AE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+C7 C with CEDILLA */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+C8 E with GRAVE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+C9 E with ACUTE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+CA E with '^' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+CB E with DIAERESIS */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+CC I with GRAVE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+CD I with ACUTE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+CE I with '^' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+CF I with DIAERESIS */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+D0 ETH */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+D1 N with '~' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+D2 O with GRAVE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+D3 O with ACUTE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+D4 O with '^' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+D5 O with '~' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+D6 O with DIAERESIS */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+D7 MULTIPLICATION */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+D8 O with '/' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+D9 U with GRAVE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+DA U with ACUTE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+DB U with '^' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+DC U with DIAERESIS */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+DD Y with ACUTE */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+DE THORN */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_PRINT)|(1<<_CC_UPPER)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+DF sharp s */ (1<<_CC_NONLATIN1_SIMPLE_FOLD)|(1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+E0 a with grave */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+E1 a with acute */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+E2 a with '^' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+E3 a with '~' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+E4 a with diaeresis */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+E5 a with ring */ (1<<_CC_NONLATIN1_SIMPLE_FOLD)|(1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+E6 ae */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+E7 c with cedilla */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+E8 e with grave */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+E9 e with acute */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+EA e with '^' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+EB e with diaeresis */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+EC i with grave */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+ED i with acute */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+EE i with '^' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+EF i with diaeresis */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+F0 eth */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+F1 n with '~' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+F2 o with grave */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+F3 o with acute */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+F4 o with '^' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+F5 o with '~' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+F6 o with diaeresis */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+F7 DIVISION */ (1<<_CC_GRAPH)|(1<<_CC_PRINT)|(1<<_CC_QUOTEMETA),
	/* U+F8 o with '/' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+F9 u with grave */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+FA u with acute */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+FB u with '^' */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+FC u with diaeresis */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+FD y with acute */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+FE thorn */ (1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	/* U+FF y with diaeresis */ (1<<_CC_NONLATIN1_SIMPLE_FOLD)|(1<<_CC_NONLATIN1_FOLD)|(1<<_CC_ALPHANUMERIC)|(1<<_CC_ALPHA)|(1<<_CC_CASED)|(1<<_CC_CHARNAME_CONT)|(1<<_CC_GRAPH)|(1<<_CC_IDFIRST)|(1<<_CC_LOWER)|(1<<_CC_PRINT)|(1<<_CC_WORDCHAR)|(1<<_CC_IS_IN_SOME_FOLD),
	};
}
