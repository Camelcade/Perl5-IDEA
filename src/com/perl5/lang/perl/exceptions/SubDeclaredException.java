package com.perl5.lang.perl.exceptions;

/**
 * Created by hurricup on 08.05.2015.
 */
public class SubDeclaredException extends PerlParsingException
{
	public SubDeclaredException(String message)
	{
		super(message);
	}
	public SubDeclaredException(String format, Object ... str)
	{
		super(String.format(format, str));
	}
}
