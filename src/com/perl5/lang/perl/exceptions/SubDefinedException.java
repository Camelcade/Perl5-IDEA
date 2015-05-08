package com.perl5.lang.perl.exceptions;

/**
 * Created by hurricup on 08.05.2015.
 */
public class SubDefinedException extends PerlParsingException
{
	public SubDefinedException(String message)
	{
		super(message);
	}

	public SubDefinedException(String format, Object ... str)
	{
		super(String.format(format, str));
	}
}
