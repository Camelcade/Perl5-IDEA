package com.perl5.lang.lexer.ported;

/**
 * Created by hurricup on 18.04.2015.
 */
public class OP
{
	public int type;
	public int flags;
	public String sv;

	public OP( int type, int flags, String sv )
	{
		this.type = type;
		this.flags = flags;
		this.sv = sv;
	}

}
