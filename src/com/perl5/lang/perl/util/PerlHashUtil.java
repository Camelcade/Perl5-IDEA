package com.perl5.lang.perl.util;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlHashUtil implements PerlElementTypes
{
	protected static final HashMap<String,IElementType> BUILT_IN_MAP = new HashMap<String,IElementType>();

	public static final ArrayList<String> BUILT_IN = new ArrayList<String>( Arrays.asList(
			"%!",
			"%+",
			"%-",
			"%^H",
			"%ENV",
			"%INC",
			"%OVERLOAD",
			"%SIG",
			"%EXPORT_TAGS"

	));

	static{
		for( String builtIn: BUILT_IN )
		{
			BUILT_IN_MAP.put(builtIn, PERL_SCALAR);
		}
	}

	public static boolean isBuiltIn(String variable)
	{
		return BUILT_IN_MAP.containsKey(variable);
	}

}
