package com.perl5.lang.perl.util;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlFunctionUtil implements PerlElementTypes, PerlFunctionUtilBuiltIn
{
	protected static final HashMap<String,IElementType> knownFunctions = new HashMap<String,IElementType>();

	static{
		for( String functionName: BUILT_IN )
		{
			knownFunctions.put(functionName, PERL_FUNCTION);
		}
	}

	public static boolean isBuiltIn(String function)
	{
		return knownFunctions.containsKey(function);
	}

	public static IElementType getFunctionType(String function)
	{
		return isBuiltIn(function) ? PERL_FUNCTION_BUILT_IN : PERL_FUNCTION;
	}

}
