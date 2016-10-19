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

package com.perl5.lang.perl.parser.builder;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.PerlParserImpl;
import com.perl5.lang.perl.parser.PerlTokenData;

/**
 * Created by hurricup on 04.05.2015.
 * This wrapper created to be able to store per-parsing data like pragmas, warnings and variables ?
 */
public class PerlBuilder extends GeneratedParserUtilBase.Builder implements PerlElementTypes
{
	private final PerlParserImpl perlParser;

	// flag forces stringification of -identifiers, required for use Package -option;
	boolean stringify = false;
	// flags that sq strings should be converted to the use_vars_lazy_parsable_strings
	boolean isUseVarsContent = false;
	// flag shows that we are in the interpolated string. Involves additional checkings like space between $var and {hash_key}
	boolean isInterpolated = false;
	// flag set if we are inside of regexp. Safe parsing for array indexes
	boolean isRegex = false;
	// flag allowes special variable names
	boolean isSpecialVariableNamesAllowed = true;
	/**
	 * This element may be set to make an additional wrapping for strings, like constants and so on
	 */
	PerlStringWrapper stringWrapper = null;

	public PerlBuilder(PsiBuilder builder, GeneratedParserUtilBase.ErrorState state, PsiParser parser)
	{
		super(builder, state, parser);
		perlParser = (PerlParserImpl) parser;
	}

	/**
	 * Return token ahead of current, skips spaces and comments
	 *
	 * @param steps positive or negative steps number to get token
	 * @return token data: type and text
	 */
	public PerlTokenData lookupToken(int steps)
	{
		assert steps != 0;
		int rawStep = 0;
		int step = steps / Math.abs(steps);

		IElementType rawTokenType = null;

		while (steps != 0)
		{
			rawStep += step;
			rawTokenType = rawLookup(rawStep);

			// reached end
			if (rawTokenType == null)
			{
				return null;
			}

			if (!PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(rawTokenType))
			{
				steps -= step;
			}
		}

		// fixme crushes on quick s typing
		return new PerlTokenData(rawTokenType, getOriginalText().subSequence(rawTokenTypeStart(rawStep), rawTokenTypeStart(rawStep + 1)).toString());
	}


	public boolean isStringify()
	{
		return stringify;
	}

	public boolean setStringify(boolean stringify)
	{
		boolean oldState = this.stringify;
		this.stringify = stringify;
		return oldState;
	}

	public boolean isUseVarsContent()
	{
		return isUseVarsContent;
	}

	public void setUseVarsContent(boolean newState)
	{
		isUseVarsContent = newState;
	}

	public boolean isInterpolated()
	{
		return isInterpolated;
	}

	public boolean isRegex()
	{
		return isRegex;
	}

	public boolean setIsRegex(boolean newState)
	{
		boolean currentState = isRegex();
		isRegex = newState;
		return currentState;
	}


	public boolean setSpecialVariableNamesAllowed(boolean specialVariableNamesAllowed)
	{
		boolean oldValue = isSpecialVariableNamesAllowed;
		isSpecialVariableNamesAllowed = specialVariableNamesAllowed;
		return oldValue;
	}

	public PerlStringWrapper getStringWrapper()
	{
		return stringWrapper;
	}

	public PerlStringWrapper setStringWrapper(PerlStringWrapper stringWrapper)
	{
		PerlStringWrapper currentValue = this.stringWrapper;
		this.stringWrapper = stringWrapper;
		return currentValue;
	}

	public PerlParserImpl getPerlParser()
	{
		return perlParser;
	}

	public void setNextSubElementType(IElementType subElement)
	{
		// fixme implement
	}

}
