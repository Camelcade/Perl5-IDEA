/*
 * Copyright 2015 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
