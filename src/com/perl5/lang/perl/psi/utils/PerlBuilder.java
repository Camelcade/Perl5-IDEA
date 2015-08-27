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
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.parser.PerlTokenData;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;

import java.util.HashSet;

/**
 * Created by hurricup on 04.05.2015.
 * This wrapper created to be able to store per-parsing data like pragmas, warnings and variables ?
 */
public class PerlBuilder extends GeneratedParserUtilBase.Builder
{
	protected HashSet<String> KNOWN_SUBS = new HashSet<String>();
	protected HashSet<String> KNOWN_PACKAGES = new HashSet<String>(PerlPackageUtil.BUILT_IN_ALL);
	protected boolean recoveringStatement = false;
	protected int bracesLevel = 0;
	protected boolean currentStringState = false;
	boolean indexSnapshotDone = false;
	boolean stringify = false;
	Project myProject = getProject();

	public PerlBuilder(PsiBuilder builder, GeneratedParserUtilBase.ErrorState state, PsiParser parser)
	{
		super(builder, state, parser);
	}

	/**
	 * Makes index snapshot hashsets
	 *
	 * @return result
	 */
	public synchronized boolean makeIndexSnapshot()
	{
		if (!indexSnapshotDone && !DumbService.isDumb(myProject))
		{
			KNOWN_SUBS.addAll(PerlSubUtil.getDeclaredSubsNames(myProject));
			KNOWN_SUBS.addAll(PerlSubUtil.getDefinedSubsNames(myProject));
			KNOWN_SUBS.addAll(PerlGlobUtil.getDefinedGlobsNames(myProject));

			KNOWN_PACKAGES.addAll(PerlPackageUtil.getDefinedPackageNames(myProject));
			indexSnapshotDone = true;
		}
		return indexSnapshotDone;
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
		if (!indexSnapshotDone)
			makeIndexSnapshot();

		return KNOWN_SUBS.contains(subName);
	}

	/**
	 * Checks if package is indexed.
	 *
	 * @param packageName package name
	 * @return checking result
	 */
	public boolean isKnownPackage(String packageName)
	{
		String canonicalPackageName = PerlPackageUtil.getCanonicalPackageName(packageName);

		if (!indexSnapshotDone)
			makeIndexSnapshot();

		return KNOWN_PACKAGES.contains(canonicalPackageName);
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
}
