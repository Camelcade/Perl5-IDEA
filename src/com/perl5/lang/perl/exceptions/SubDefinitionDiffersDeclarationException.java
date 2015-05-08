package com.perl5.lang.perl.exceptions;

/**
 * Created by hurricup on 08.05.2015.
 */
public class SubDefinitionDiffersDeclarationException extends PerlParsingException
{
	public SubDefinitionDiffersDeclarationException(String message)
	{
		super(message);
	}
	public SubDefinitionDiffersDeclarationException(String format, Object ... str)
	{
		super(String.format(format, str));
	}

}
