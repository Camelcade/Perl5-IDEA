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

package com.perl5.lang.perl.psi.utils;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.exceptions.PerlParsingException;
import com.perl5.lang.perl.parser.*;
import com.perl5.lang.perl.util.PerlGlobUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by hurricup on 04.05.2015.
 * This wrapper created to be able to store per-parsing data like pragmas, warnings and variables ?
 */
public class PerlBuilder extends GeneratedParserUtilBase.Builder
{
	protected final HashSet<String> REGISTERED_HANDLES = new HashSet<>();

	public PerlBuilder(PsiBuilder builder, GeneratedParserUtilBase.ErrorState state, PsiParser parser) {
		super(builder, state, parser);
		REGISTERED_HANDLES.addAll(PerlGlobUtil.BUILT_IN);
	}

	/**
	 * Return token ahead of current, skips spaces and comments
	 * @param steps	positive or negative steps number to get token
	 * @return	token data: type and text
	 */
	public PerlTokenData lookupToken(int steps)
	{
		assert steps != 0;
		int rawStep = 0;
		int step = steps / Math.abs(steps);

		IElementType rawTokenType = null;

		while( steps != 0 )
		{
			rawStep += step;
			rawTokenType = rawLookup(rawStep);

			// reached end
			if( rawTokenType == null )
				return null;

			if( !PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(rawTokenType))
			{
				steps-=step;
			}
		}

		return new PerlTokenData(rawTokenType, getOriginalText().subSequence(rawTokenTypeStart(rawStep), rawTokenTypeStart(rawStep+1)).toString());
	}

	public void registerHandle(String handle)
	{
		REGISTERED_HANDLES.add(handle);
	}

	public boolean isRegisteredHandle(String handle)
	{
		return REGISTERED_HANDLES.contains(handle);
	}

}
