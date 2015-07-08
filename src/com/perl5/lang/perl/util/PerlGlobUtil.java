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

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.idea.stubs.globs.PerlGlobsStubIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlGlobUtil implements PerlElementTypes
{
	public static final HashSet<String> BUILT_IN = new HashSet<>( Arrays.asList(
			"ARGV",
			"STDERR",
			"STDOUT",
			"ARGVOUT",
			"STDIN"
	));

	static
	{
		BUILT_IN.addAll(PerlScalarUtilBuiltIn.BUILT_IN);
		BUILT_IN.addAll(PerlSubUtilBuiltIn.BUILT_IN);
		BUILT_IN.addAll(PerlArrayUtil.BUILT_IN);
		BUILT_IN.addAll(PerlHashUtil.BUILT_IN);
	}

	/**
	 * Searching project files for sub definitions by specific package and function name
	 * @param project	project to search in
	 * @param canonicalName	canonical function name package::name
	 * @return collection of found definitions
	 */
	public static Collection<PsiPerlGlobVariable> findGlobsDefinitions(Project project, String canonicalName)
	{
		assert canonicalName != null;

		return StubIndex.getElements(PerlGlobsStubIndex.KEY, canonicalName, project, GlobalSearchScope.allScope(project), PsiPerlGlobVariable.class);
	}

	/**
	 * Returns list of known stubbed globs
	 * @param project project to search in
	 * @return collection of globs names
	 */
	public static Collection<String> getDefinedGlobsNames(Project project)
	{
		return StubIndex.getInstance().getAllKeys(PerlGlobsStubIndex.KEY, project);
	}

}
