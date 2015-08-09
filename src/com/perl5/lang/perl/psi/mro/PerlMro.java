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

package com.perl5.lang.perl.psi.mro;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;

import java.util.*;

/**
 * Created by hurricup on 08.08.2015.
 */
public abstract class PerlMro
{
	public static final SubDefinitionGetter SUB_DEFINITIONS_GETTER = new SubDefinitionGetter();
	public static final SubDeclarationsGetter SUB_DECLARATIONS_GETTER = new SubDeclarationsGetter();
	public static final ConstantsGetter CONSTANTS_GETTER = new ConstantsGetter();
	public static final GlobVariablesGetter GLOBS_GETTER = new GlobVariablesGetter();

	protected PerlMro()
	{
	}

	// fixme do we need theese methods?
	public static Collection<PsiPerlSubDefinition> getSubDefinitions(Project project, String packageName, String subName)
	{
		return SUB_DEFINITIONS_GETTER.resolveSub(project, packageName, subName, false);
	}

	public static Collection<PsiPerlSubDefinition> getSuperSubDefinitions(Project project, String packageName, String subName)
	{
		return SUB_DEFINITIONS_GETTER.resolveSub(project, packageName, subName, true);
	}

	public static Collection<PsiPerlSubDeclaration> getSubDeclarations(Project project, String packageName, String subName)
	{
		return SUB_DECLARATIONS_GETTER.resolveSub(project, packageName, subName, false);
	}

	public static Collection<PsiPerlSubDeclaration> getSuperSubDeclarations(Project project, String packageName, String subName)
	{
		return SUB_DECLARATIONS_GETTER.resolveSub(project, packageName, subName, true);
	}

	public static Collection<PsiPerlGlobVariable> getSubAliases(Project project, String packageName, String subName)
	{
		return GLOBS_GETTER.resolveSub(project, packageName, subName, false);
	}

	public static Collection<PsiPerlGlobVariable> getSuperSubAliases(Project project, String packageName, String subName)
	{
		return GLOBS_GETTER.resolveSub(project, packageName, subName, true);
	}

	public static Collection<PerlConstant> getConstants(Project project, String packageName, String subName)
	{
		return CONSTANTS_GETTER.resolveSub(project, packageName, subName, false);
	}

	public static Collection<PerlConstant> getSuperConstants(Project project, String packageName, String subName)
	{
		return CONSTANTS_GETTER.resolveSub(project, packageName, subName, true);
	}

	/**
	 * Resolving method with current MRO; fixme on every iteration mro can change, so we need to stub it
	 *
	 * @param project     current Project
	 * @param packageName package name
	 * @param subName     sub name
	 * @param isSuper     super flag
	 * @return collection of first encountered super subs declarations, definitions, constants and typeglobs
	 */
	public static Collection<PsiElement> resolveSub(Project project, String packageName, String subName, boolean isSuper)
	{
		Collection<PsiElement> result = new ArrayList<PsiElement>();
		if (packageName == null || subName == null)
			return result;

		for (String currentPackageName : getLinearISA(project, packageName, isSuper))
		{
			String fullName = currentPackageName + "::" + subName;
			result.addAll(PerlSubUtil.getSubDefinitions(project, fullName));
			result.addAll(PerlSubUtil.getSubDeclarations(project, fullName));
			result.addAll(PerlSubUtil.getConstantsDefinitions(project, fullName));
			result.addAll(PerlGlobUtil.getGlobsDefinitions(project, fullName));

			if (result.size() > 0)
				break;
		}

		return result;

	}

	/**
	 * Returns collection of Sub Definitions of class and it's superclasses according perl's default MRO
	 *
	 * @param project         Current project
	 * @param basePackageName base project
	 * @param isSuper         flag for SUPER resolutions
	 * @return collection of definitions
	 */
	public static Collection<PsiElement> getVariants(Project project, String basePackageName, boolean isSuper)
	{
		HashMap<String, PsiElement> methods = new HashMap<String, PsiElement>();

		for (String packageName : getLinearISA(project, basePackageName, isSuper))
		{
			for (PerlSubDefinition subDefinition : PerlSubUtil.getSubDefinitions(project, "*" + packageName))
				if (!methods.containsKey(subDefinition.getSubName()))
					methods.put(subDefinition.getSubName(), subDefinition);
			for (PerlSubDeclaration subDeclaration : PerlSubUtil.getSubDeclarations(project, "*" + packageName))
				if (!methods.containsKey(subDeclaration.getSubName()))
					methods.put(subDeclaration.getSubName(), subDeclaration);
			for (PerlConstant constant : PerlSubUtil.getConstantsDefinitions(project, "*" + packageName))
				if (!methods.containsKey(constant.getName()))
					methods.put(constant.getName(), constant);
			for (PerlGlobVariable globVariable : PerlGlobUtil.getGlobsDefinitions(project, "*" + packageName))
				if (!methods.containsKey(globVariable.getName()))
					methods.put(globVariable.getName(), globVariable);
		}

		return new ArrayList<PsiElement>(methods.values());
	}

	/**
	 * Building linear @ISA list
	 *
	 * @param project     current project
	 * @param packageName current package name
	 * @param isSuper     if false - we include current package into the list, true - otherwise
	 * @return list of linear @ISA
	 */
	public static ArrayList<String> getLinearISA(Project project, String packageName, boolean isSuper)
	{
		HashSet<String> recursionMap = new HashSet<String>();
		ArrayList<String> result = new ArrayList<String>();

		if (!isSuper)
		{
			recursionMap.add(packageName);
			result.add(packageName);
		}

		getPackageParents(project, packageName, recursionMap, result);

		if (!recursionMap.contains("UNIVERSAL"))
			result.add("UNIVERSAL");

		return result;
	}

	public static void getPackageParents(Project project, String packageName, HashSet<String> recursionMap, ArrayList<String> result)
	{
		// at the moment we are checking all definitions available
		// fixme we should check only those, which are used in currrent file
		for (PerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, packageName))
			namespaceDefinition.getLinearISA(recursionMap, result);
	}

	/**
	 * Method should return a sequence of packages. See http://perldoc.perl.org/mro.html#mro%3a%3aget_linear_isa(%24classname%5b%2c-%24type%5d)
	 * Method should not add package itself or UNIVERSAL, only parents structure. Package itself and UNIVERSAL being added by calee
	 *
	 * @param project      current project
	 * @param packageNames list of package names to populate
	 * @param recursionMap map for controlling recursive inheritance
	 */
	public abstract void getLinearISA(Project project, List<String> packageNames, HashSet<String> recursionMap, ArrayList<String> result);

	// fixme shouldn't we move this to separate class for reuse?
	abstract static class EntitiesGetter<T>
	{
		public Collection<T> resolveSub(Project project, String packageName, String subName, boolean isSuper)
		{
			Collection<T> result = new ArrayList<T>();
			if (packageName == null || subName == null)
				return result;

			for (String currentPackageName : getLinearISA(project, packageName, isSuper))
			{
				result.addAll(getEntities(project, currentPackageName + "::" + subName));
				if (result.size() > 0)
					break;
			}

			return result;
		}

		public abstract Collection<T> getEntities(Project project, String name);
	}

	static class SubDefinitionGetter extends EntitiesGetter<PsiPerlSubDefinition>
	{
		@Override
		public Collection<PsiPerlSubDefinition> getEntities(Project project, String name)
		{
			return PerlSubUtil.getSubDefinitions(project, name);
		}
	}

	static class SubDeclarationsGetter extends EntitiesGetter<PsiPerlSubDeclaration>
	{
		@Override
		public Collection<PsiPerlSubDeclaration> getEntities(Project project, String name)
		{
			return PerlSubUtil.getSubDeclarations(project, name);
		}
	}

	static class GlobVariablesGetter extends EntitiesGetter<PsiPerlGlobVariable>
	{
		@Override
		public Collection<PsiPerlGlobVariable> getEntities(Project project, String name)
		{
			return PerlGlobUtil.getGlobsDefinitions(project, name);
		}
	}

	static class ConstantsGetter extends EntitiesGetter<PerlConstant>
	{
		@Override
		public Collection<PerlConstant> getEntities(Project project, String name)
		{
			return PerlSubUtil.getConstantsDefinitions(project, name);
		}
	}
}
