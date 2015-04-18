package com.perl5.lang.lexer.ported;

/**
 * Created by hurricup on 18.04.2015.
 * Perl.h
 */
public interface Perl
{
	enum expectation {
		XOPERATOR,
		XTERM,
		XREF,
		XSTATE,
		XBLOCK,
		XATTRBLOCK,
		XATTRTERM,
		XTERMBLOCK,
		XBLOCKTERM,
		XPOSTDEREF,
		XTERMORDORDOR /* evil hack */
    /* update exp_name[] in toke.c if adding to this enum */
	};

}
