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
 * Created by hurricup on 15.06.2015.
 * Class represents default Perl's MRO.
 * In other words, it knows how to find sub definition and/or declaration
 */
public class PerlDefaultMro
{
	public static Collection<PsiPerlSubDefinition> getSubDefinitions(Project project, String packageName, String subName)
	{
		return getSubDefinitions(project, packageName, subName, new HashSet<String>(), false);
	}

	public static Collection<PsiPerlSubDeclaration> getSubDeclarations(Project project, String packageName, String subName)
	{
		return getSubDeclarations(project, packageName, subName, new HashSet<String>(), false);
	}

	public static Collection<PsiPerlGlobVariable> getSubAliases(Project project, String packageName, String subName)
	{
		return getSubAliases(project, packageName, subName, new HashSet<String>(), false);
	}

	public static Collection<PsiPerlSubDefinition> getSuperSubDefinitions(Project project, String packageName, String subName)
	{
		return getSubDefinitions(project, packageName, subName, new HashSet<String>(), true);
	}

	public static Collection<PsiPerlSubDeclaration> getSuperSubDeclarations(Project project, String packageName, String subName)
	{
		return getSubDeclarations(project, packageName, subName, new HashSet<String>(), true);
	}

	public static Collection<PsiPerlGlobVariable> getSuperSubAliases(Project project, String packageName, String subName)
	{
		return getSubAliases(project, packageName, subName, new HashSet<String>(), true);
	}

	/**
	 * Resolving sub definitions according to the Perl's MRO; fixme not dry
	 *
	 * @param project         current project
	 * @param packageName     current package name
	 * @param subName         current sub name
	 * @param checkedPackages recursion control hashset
	 * @return collection of definitions
	 */
	public static Collection<PsiPerlSubDefinition> getSubDefinitions(Project project, String packageName, String subName, HashSet<String> checkedPackages, boolean noCheckCurrent)
	{
		Collection<PsiPerlSubDefinition> result = new ArrayList<>();
		if (packageName == null || subName == null)
			return result;

		if (!checkedPackages.contains(packageName))
		{
			checkedPackages.add(packageName);

			if (!noCheckCurrent)    // suppress for resolving SUPER::
				result.addAll(PerlSubUtil.getSubDefinitions(project, packageName + "::" + subName));

			if (result.size() == 0)    // not found, need to check parents
			{
				for (PsiPerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, packageName))
				{
					List<String> parentNamespaces = namespaceDefinition.getParentNamespaces();

					// fixme this should be returned by getParentNamespaces
					if (parentNamespaces.size() == 0 && !"UNIVERSAL".equals(packageName))
						parentNamespaces.add("UNIVERSAL");

					for (String parentNamespace : parentNamespaces)
					{
						result.addAll(getSubDefinitions(project, parentNamespace, subName, checkedPackages, false));
						if (result.size() > 0)
							break;
					}
				}
			}
		}

		return result;
	}

	/**
	 * Resolving sub declarations according to the Perl's MRO; fixme not dry
	 *
	 * @param project         current project
	 * @param packageName     current package name
	 * @param subName         current sub name
	 * @param checkedPackages recursion control hashset
	 * @return collection of definitions
	 */
	public static Collection<PsiPerlSubDeclaration> getSubDeclarations(Project project, String packageName, String subName, HashSet<String> checkedPackages, boolean noCheckCurrent)
	{
		Collection<PsiPerlSubDeclaration> result = new ArrayList<>();
		if (packageName == null || subName == null)
			return result;

		if (!checkedPackages.contains(packageName))
		{
			checkedPackages.add(packageName);

			if (!noCheckCurrent)    // suppress for resolving SUPER::
				result.addAll(PerlSubUtil.getSubDeclarations(project, packageName + "::" + subName));

			if (result.size() == 0)    // not found, need to check parents
			{
				for (PsiPerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, packageName))
				{
					List<String> parentNamespaces = namespaceDefinition.getParentNamespaces();

					if (parentNamespaces.size() == 0 && !"UNIVERSAL".equals(packageName))
						parentNamespaces.add("UNIVERSAL");

					for (String parentNamespace : parentNamespaces)
					{
						result.addAll(getSubDeclarations(project, parentNamespace, subName, checkedPackages, false));
						if (result.size() > 0)
							break;
					}
				}
			}
		}

		return result;
	}

	/**
	 * Resolving sub aliases according to the Perl's MRO; fixme not dry; not sure globs works this way
	 *
	 * @param project         current project
	 * @param packageName     current package name
	 * @param subName         current sub name
	 * @param checkedPackages recursion control hashset
	 * @return collection of definitions
	 */
	public static Collection<PsiPerlGlobVariable> getSubAliases(Project project, String packageName, String subName, HashSet<String> checkedPackages, boolean noCheckCurrent)
	{
		Collection<PsiPerlGlobVariable> result = new ArrayList<>();
		if (packageName == null || subName == null)
			return result;

		if (!checkedPackages.contains(packageName))
		{
			checkedPackages.add(packageName);

			if (!noCheckCurrent)    // suppress for resolving SUPER::
				result.addAll(PerlGlobUtil.getGlobsDefinitions(project, packageName + "::" + subName));

			if (result.size() == 0)    // not found, need to check parents
			{
				for (PsiPerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, packageName))
				{
					List<String> parentNamespaces = namespaceDefinition.getParentNamespaces();

					if (parentNamespaces.size() == 0 && !"UNIVERSAL".equals(packageName))
						parentNamespaces.add("UNIVERSAL");

					for (String parentNamespace : parentNamespaces)
					{
						result.addAll(getSubAliases(project, parentNamespace, subName, checkedPackages, false));
						if (result.size() > 0)
							break;
					}
				}
			}
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
	public static Collection<PsiElement> getPackagePossibleMethods(Project project, String basePackageName, boolean isSuper)
	{
		HashMap<String, PsiElement> methods = new HashMap<>();

		for (String packageName : getPackageParentsSequence(project, basePackageName, isSuper, new HashSet<String>()))
		{
			for (PerlSubDefinition subDefinition : PerlSubUtil.getSubDefinitions(project, "*" + packageName))
				if (!methods.containsKey(subDefinition.getSubName()))
					methods.put(subDefinition.getSubName(), subDefinition);
			for (PerlSubDeclaration subDeclaration: PerlSubUtil.getSubDeclarations(project, "*" + packageName))
				if (!methods.containsKey(subDeclaration.getSubName()))
					methods.put(subDeclaration.getSubName(), subDeclaration);
			for (PerlGlobVariable globVariable : PerlGlobUtil.getGlobsDefinitions(project, "*" + packageName))
				if (!methods.containsKey(globVariable.getName()))
					methods.put(globVariable.getName(), globVariable);
		}

		return new ArrayList<>(methods.values());
	}

	/**
	 * Builds list of inheritance path. Basically, this should be re-implemented for other MROs
	 *
	 * @param project      project
	 * @param packageName  current package name
	 * @param isSuper      flag if it's SUPER resolution
	 * @param recursionMap recursion protection map
	 * @return list of package names
	 */
	public static ArrayList<String> getPackageParentsSequence(Project project, String packageName, boolean isSuper, HashSet<String> recursionMap)
	{
		ArrayList<String> result = new ArrayList<>();

		if (!recursionMap.contains(packageName))
		{
			recursionMap.add(packageName);

			if (!isSuper)
				result.add(packageName);

			for (PerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, packageName))
			{
				Collection<String> parentPackageNames = namespaceDefinition.getParentNamespaces();

				if( parentPackageNames.size() == 0 && !recursionMap.contains("UNIVERSAL"))
					parentPackageNames.add("UNIVERSAL");

				for (String parentPackageName : parentPackageNames)
					result.addAll(getPackageParentsSequence(project, parentPackageName, false, recursionMap));
			}
		}

		return result;
	}


}
