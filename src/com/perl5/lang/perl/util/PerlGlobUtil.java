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
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlPerlGlob;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.stubs.globs.PerlGlobsStubIndex;
import com.perl5.lang.perl.stubs.subs.definitions.PerlSubDefinitionsStubIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlGlobUtil implements PerlElementTypes
{
	public static final ArrayList<String> BUILT_IN = new ArrayList<String>( Arrays.asList(
			"*ARGV",
			"*STDERR",
			"*STDOUT",
			"*ARGVOUT",
			"*STDIN"
	));

	/**
	 * Searching project files for sub definitions by specific package and function name
	 * @param project	project to search in
	 * @param canonicalName	canonical function name package::name
	 * @return	ArrayList of found definitions
	 */
	public static List<PerlPerlGlob> findGlobsDefinitions(Project project, String canonicalName)
	{
		assert canonicalName != null;

		Collection<PerlPerlGlob> result = StubIndex.getElements(PerlGlobsStubIndex.KEY, canonicalName, project, GlobalSearchScope.projectScope(project), PerlPerlGlob.class);

		return new ArrayList<PerlPerlGlob>(result);
	}


	/**
	 * Returns list of known stubbed globs
	 * @param project project to search in
	 * @return list of globs
	 */
	public static List<String> getDefinedGlobsNames(Project project)
	{
		return new ArrayList<String>(StubIndex.getInstance().getAllKeys(PerlGlobsStubIndex.KEY, project));
	}

}
