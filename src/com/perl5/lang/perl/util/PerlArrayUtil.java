package com.perl5.lang.perl.util;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlArrayUtil implements PerlElementTypes
{
	protected static final HashMap<String,IElementType> BUILT_IN_MAP = new HashMap<String,IElementType>();

	public static final ArrayList<String> BUILT_IN = new ArrayList<String>( Arrays.asList(
			"@+",
			"@-",
			"@_",
			"@ARGV",
			"@INC",
			"@LAST_MATCH_START",
			"@EXPORT",
			"@ISA",
			"@EXPORT_OK",
			"@EXPORT_TAGS",

			// hash slices
			"@!",
			"@^H",
			"@ENV",
			"@INC",
			"@OVERLOAD",
			"@SIG"
	));

	static{
		for( String builtIn: BUILT_IN )
		{
			BUILT_IN_MAP.put(builtIn, ARRAY_VARIABLE);
		}
	}

	public static boolean isBuiltIn(String variable)
	{
		return BUILT_IN_MAP.containsKey(variable);
	}


}
