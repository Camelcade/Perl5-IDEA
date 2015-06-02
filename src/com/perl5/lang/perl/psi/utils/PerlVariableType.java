package com.perl5.lang.perl.psi.utils;

/**
 * Created by hurricup on 01.06.2015.
 */
public enum PerlVariableType{
	SCALAR,
	ARRAY,
	HASH;

	public char getSigil()
	{
		if( this == SCALAR)
			return '$';
		else if( this == ARRAY )
			return '@';
		else if( this == HASH )
			return '%';
		return ' ';
	}

};
