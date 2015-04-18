package com.perl5.lang.lexer.ported;

/**
 * Created by hurricup on 18.04.2015.
 */
public class lex_fake_of
{
	enum fake_of{
		LEX_FAKEEOF_NEVER,      /* don't fake EOF */
		LEX_FAKEEOF_CLOSING,    /* fake EOF at unmatched closing punctuation */
		LEX_FAKEEOF_NONEXPR,    /* ... and at token that can't be in expression */
		LEX_FAKEEOF_LOWLOGIC,   /* ... and at low-precedence logic operator */
		LEX_FAKEEOF_COMMA,      /* ... and at comma */
		LEX_FAKEEOF_ASSIGN,     /* ... and at assignment operator */
		LEX_FAKEEOF_IFELSE,     /* ... and at ?: operator */
		LEX_FAKEEOF_RANGE,      /* ... and at range operator */
		LEX_FAKEEOF_LOGIC,      /* ... and at logic operator */
		LEX_FAKEEOF_BITWISE,    /* ... and at bitwise operator */
		LEX_FAKEEOF_COMPARE,    /* ... and at comparison operator */
		LEX_FAKEEOF_MAX
	};
}
