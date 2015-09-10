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
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.parser.PerlTokenData;
import com.perl5.lang.perl.util.PerlPackageUtil;

import java.util.Set;

/**
 * Created by hurricup on 04.05.2015.
 * This wrapper created to be able to store per-parsing data like pragmas, warnings and variables ?
 */
public class PerlBuilder extends GeneratedParserUtilBase.Builder
{
	protected Set<String> KNOWN_SUBS;
	protected Set<String> KNOWN_PACKAGES;
	protected boolean recoveringStatement = false;
	protected int bracesLevel = 0;
	protected boolean currentStringState = false;

	// flag forces stringification of -identifiers, required for use Package -option;
	boolean stringify = false;

	// flag shows that SQ strings should be re-parsed as QQ strings. Used in use vars expr
	boolean reparseSQString = false;

	boolean allowAllSigils = false;

	Project myProject = getProject();

	public PerlBuilder(PsiBuilder builder, GeneratedParserUtilBase.ErrorState state, PsiParser parser, boolean reuseIndex)
	{
		super(builder, state, parser);
		KNOWN_SUBS = PerlNamesCache.getSubsNamesSet(myProject, reuseIndex);
		KNOWN_PACKAGES = PerlNamesCache.getPackagesNamesSet(myProject, reuseIndex);
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
				return null;

			if (!PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(rawTokenType))
				steps -= step;
		}

		// fixme crushes on quick s typing
		return new PerlTokenData(rawTokenType, getOriginalText().subSequence(rawTokenTypeStart(rawStep), rawTokenTypeStart(rawStep + 1)).toString());
	}

	/**
	 * Checks if sub is indexed.
	 *
	 * @param subName canonical sub name Foo::somesub
	 * @return checking result
	 */

	public boolean isKnownSub(String subName)
	{
		return KNOWN_SUBS.contains(subName);
	}


	/**
	 * Checks if package is indexed.
	 *
	 * @return checking result
	 */

	public boolean isKnownPackage(String packageName)
	{
		return KNOWN_PACKAGES.contains(PerlPackageUtil.getCanonicalPackageName(packageName));
	}

	public void startRecovery()
	{
		recoveringStatement = true;
		bracesLevel = 0;
	}

	public void stopRecovery()
	{
		recoveringStatement = false;
	}

	public boolean isRecoveringStatement()
	{
		return recoveringStatement;
	}

	public int getBracesLevel()
	{
		return bracesLevel;
	}

	public void openBrace()
	{
		bracesLevel++;
	}

	public void closeBrace()
	{
		bracesLevel--;
	}

	public boolean getCurrentStringState()
	{
		return currentStringState;
	}

	public void setCurrentStringState(boolean currentStringState)
	{
		this.currentStringState = currentStringState;
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

	public boolean isReparseSQString()
	{
		return reparseSQString;
	}

	public boolean setReparseSQString(boolean newState)
	{
		boolean currentState = isReparseSQString();
		reparseSQString = newState;
		return currentState;
	}

	public boolean isAllowAllSigils()
	{
		return allowAllSigils;
	}

	public boolean setAllowSigils(boolean newState)
	{
		boolean currentState = isAllowAllSigils();
		allowAllSigils = newState;
		return currentState;
	}
}
