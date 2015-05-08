package com.perl5.lang.perl.exceptions;

/**
 * Created by hurricup on 08.05.2015.
 */
public class PerlParsingException extends Exception
{
	public PerlParsingException(String message)
	{
		super(message);
	}

	public PerlParsingException(String format, Object ... str)
	{
		super(String.format(format, str));
	}
}
