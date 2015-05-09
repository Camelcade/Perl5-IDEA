package com.perl5.lang.perl.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by hurricup on 09.05.2015.
 */
public class PerlPackagePragma extends PerlPackage
{
	public PerlPackagePragma(String name){super(name);}

	public PerlPackagePragma(PerlPackagePragma original)
	{
		super(original.getName());
	}

}
