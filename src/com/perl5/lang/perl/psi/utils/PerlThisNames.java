package com.perl5.lang.perl.psi.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hurricup on 01.06.2015.
 */
public abstract class PerlThisNames
{
	public final static Set<String> NAMES_SET = new HashSet<>();
	static{
		NAMES_SET.add("this");
		NAMES_SET.add("self");
		NAMES_SET.add("proto");
	}
}
