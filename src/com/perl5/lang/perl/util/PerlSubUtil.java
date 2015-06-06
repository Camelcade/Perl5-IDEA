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
import com.perl5.lang.perl.psi.PsiPerlSubDeclaration;
import com.perl5.lang.perl.psi.PsiPerlSubDefinition;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationStubIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionsStubIndex;

import java.util.*;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlSubUtil implements PerlElementTypes, PerlSubUtilBuiltIn
{
	protected static final HashMap<String,IElementType> knownFunctions = new HashMap<String,IElementType>();

	static{
		for( String functionName: BUILT_IN )
		{
			knownFunctions.put(functionName, PERL_FUNCTION);
		}
	}

	/**
	 * Checks if provided function is built in
	 * @param function function name
	 * @return checking result
	 */
	public static boolean isBuiltIn(String function)
	{
		return knownFunctions.containsKey(function);
	}

	/**
	 * Returns token type for specific function name
	 * @param function function name
	 * @return token type
	 */
	public static IElementType getFunctionType(String function)
	{
		return isBuiltIn(function) ? PERL_FUNCTION_BUILT_IN : PERL_FUNCTION;
	}

	/**
	 * Searching project files for sub definitions by specific package and function name
	 * @param project	project to search in
	 * @param canonicalName	canonical function name package::name
	 * @return	Collection of found definitions
	 */
	public static Collection<PsiPerlSubDefinition> findSubDefinitions(Project project, String canonicalName)
	{
		assert canonicalName != null;
		return StubIndex.getElements(PerlSubDefinitionsStubIndex.KEY, canonicalName, project, GlobalSearchScope.projectScope(project), PsiPerlSubDefinition.class);
	}

	/**
	 * Returns list of defined subs names
	 * @param project project to search in
	 * @return collection of sub names
	 */
	public static Collection<String> getDefinedSubsNames(Project project)
	{
		return StubIndex.getInstance().getAllKeys(PerlSubDefinitionsStubIndex.KEY, project);
	}


	/**
	 * Searching project files for sub declarations by specific package and function name
	 * @param project	project to search in
	 * @param canonicalName	canonical function name package::name
	 * @return	Collection of found definitions
	 */
	public static Collection<PsiPerlSubDeclaration> findSubDeclarations(Project project, String canonicalName)
	{
		assert canonicalName != null;
		return StubIndex.getElements(PerlSubDeclarationStubIndex.KEY, canonicalName, project, GlobalSearchScope.projectScope(project), PsiPerlSubDeclaration.class);
	}

	/**
	 * Returns list of declared subs names
	 * @param project project to search in
	 * @return collection of sub names
	 */
	public static Collection<String> getDeclaredSubsNames(Project project)
	{
		return StubIndex.getInstance().getAllKeys(PerlSubDeclarationStubIndex.KEY, project);
	}

}
