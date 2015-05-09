package com.perl5.lang.perl.parser;

/**
 * Created by hurricup on 09.05.2015.
 * Represents perl version number
 */
public class PerlVersion
{
	protected String version;

	public PerlVersion(String version)
	{
		this.version = version;
	}

	public String toString()
	{
		return version;
	}
}
