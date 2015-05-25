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
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlSubDefinitionImpl;
import com.perl5.lang.perl.stubs.PerlSubDefinitionStub;
import com.perl5.lang.perl.stubs.PerlSubDefinitionStubIndex;

import java.util.*;

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
	 * @return	ArrayList of found definitions
	 */
	public static List<PerlSubDefinition> findSubDefinitions(Project project, String canonicalName)
	{
		assert canonicalName != null;

		Collection<PerlSubDefinition> result = StubIndex.getElements(PerlSubDefinitionStubIndex.SUB_DEFINITION, canonicalName, project, GlobalSearchScope.projectScope(project), PerlSubDefinition.class);

		return new ArrayList<PerlSubDefinition>(result);
	}

	public static List<String> getDefinedSubsNames(Project project)
	{
		return new ArrayList<String>(StubIndex.getInstance().getAllKeys(PerlSubDefinitionStubIndex.SUB_DEFINITION, project));
	}


}
