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
import com.perl5.lang.perl.idea.stubs.strings.PerlConstantsStubIndex;
import com.perl5.lang.perl.idea.stubs.subsdeclarations.PerlSubDeclarationStubIndex;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionsStubIndex;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlSubUtil implements PerlElementTypes, PerlSubUtilBuiltIn
{
	protected static final HashMap<String, IElementType> knownFunctions = new HashMap<String, IElementType>();

	static
	{
		for (String functionName : BUILT_IN)
		{
			knownFunctions.put(functionName, SUB);
		}
	}

	/**
	 * Checks if provided function is built in
	 *
	 * @param function function name
	 * @return checking result
	 */
	public static boolean isBuiltIn(String function)
	{
		return knownFunctions.containsKey(function);
	}


	/**
	 * Checks if sub defined as unary with ($) proto
	 *
	 * @param packageName package name
	 * @param subName     sub name
	 * @return check result
	 */
	public static boolean isUnary(@Nullable String packageName, @NotNull String subName)
	{
		// todo implement checking
		return false;
	}

	/**
	 * Searching project files for sub definitions by specific package and function name
	 *
	 * @param project       project to search in
	 * @param canonicalName canonical function name package::name
	 * @return Collection of found definitions
	 */
	public static Collection<PsiPerlSubDefinition> getSubDefinitions(Project project, String canonicalName)
	{
		assert canonicalName != null;
		return StubIndex.getElements(PerlSubDefinitionsStubIndex.KEY, canonicalName, project, GlobalSearchScope.allScope(project), PsiPerlSubDefinition.class);
	}

	/**
	 * Returns list of defined subs names
	 *
	 * @param project project to search in
	 * @return collection of sub names
	 */
	public static Collection<String> getDefinedSubsNames(Project project)
	{
		return StubIndex.getInstance().getAllKeys(PerlSubDefinitionsStubIndex.KEY, project);
	}


	/**
	 * Searching project files for sub declarations by specific package and function name
	 *
	 * @param project       project to search in
	 * @param canonicalName canonical function name package::name
	 * @return Collection of found definitions
	 */
	public static Collection<PsiPerlSubDeclaration> getSubDeclarations(Project project, String canonicalName)
	{
		assert canonicalName != null;
		return StubIndex.getElements(PerlSubDeclarationStubIndex.KEY, canonicalName, project, GlobalSearchScope.allScope(project), PsiPerlSubDeclaration.class);
	}

	/**
	 * Returns list of declared subs names
	 *
	 * @param project project to search in
	 * @return collection of sub names
	 */
	public static Collection<String> getDeclaredSubsNames(Project project)
	{
		return StubIndex.getInstance().getAllKeys(PerlSubDeclarationStubIndex.KEY, project);
	}

	/**
	 * Searching project files for constants definitions by specific package and name
	 *
	 * @param project       project to search in
	 * @param canonicalName canonical function name package::name
	 * @return Collection of found definitions
	 */
	public static Collection<PerlString> getConstantsDefinitions(Project project, String canonicalName)
	{
		assert canonicalName != null;
		return StubIndex.getElements(PerlConstantsStubIndex.KEY, canonicalName, project, GlobalSearchScope.allScope(project), PerlString.class);
	}

	/**
	 * Returns list of defined constants
	 *
	 * @param project project to search in
	 * @return collection of constants names
	 */
	public static Collection<String> getDefinedConstantsNames(Project project)
	{
		return StubIndex.getInstance().getAllKeys(PerlConstantsStubIndex.KEY, project);
	}

	/**
	 * Detects return value of method container
	 *
	 * @param methodContainer method container inspected
	 * @return package name or null
	 */
	public static String getMethodReturnValue(PerlMethodContainer methodContainer)
	{
		if (methodContainer.getMethod() != null && methodContainer.getMethod().getSubNameElement() != null)
		{
			// fixme this should be moved to a method
			PerlMethod methodElement = methodContainer.getMethod();
			PerlSubNameElement subNameElement = methodElement.getSubNameElement();

			if ("new".equals(subNameElement.getName()))
				return methodElement.getPackageName();
			for (PerlSubDefinition subDefinition : subNameElement.getSubDefinitions())
				if (subDefinition.getSubAnnotations().getReturns() != null)
					return subDefinition.getSubAnnotations().getReturns();
			for (PerlSubDeclaration subDeclaration : subNameElement.getSubDeclarations())
				if (subDeclaration.getSubAnnotations().getReturns() != null)
					return subDeclaration.getSubAnnotations().getReturns();
		}

		return null;
	}
}
