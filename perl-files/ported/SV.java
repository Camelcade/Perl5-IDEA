package com.perl5.lang.lexer.ported;

/**
 * Created by hurricup on 18.04.2015.
 */
public interface SV
{
	public static final int SVf_UTF8        = 0x20000000;  /* SvPV is UTF-8 encoded
                       This is also set on RVs whose overloaded
                       stringification is UTF-8. This might
                       only happen as a side effect of SvPV() */

}
