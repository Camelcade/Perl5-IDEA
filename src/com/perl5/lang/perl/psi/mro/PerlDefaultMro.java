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
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PsiPerlSubDeclaration;
import com.perl5.lang.perl.psi.PsiPerlSubDefinition;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
		assert packageName != null: "Null package name for " + subName;
		assert subName != null: "Null sub name for " + packageName;


		Collection<PsiPerlSubDefinition> result = new ArrayList<>();

		if (!checkedPackages.contains(packageName))
		{
			checkedPackages.add(packageName);

			if (!noCheckCurrent)    // suppress for resolving SUPER::
				result.addAll(PerlSubUtil.findSubDefinitions(project, packageName + "::" + subName));

			if (result.size() == 0)    // not found, need to check parents
			{
				for (PsiPerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.findNamespaceDefinitions(project, packageName))
				{
					List<String> parentNamespaces = namespaceDefinition.getParentNamespaces();

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
		assert packageName != null: "Null package name for " + subName;
		assert subName != null: "Null sub name for " + packageName;

		Collection<PsiPerlSubDeclaration> result = new ArrayList<>();

		if (!checkedPackages.contains(packageName))
		{
			checkedPackages.add(packageName);

			if (!noCheckCurrent)    // suppress for resolving SUPER::
				result.addAll(PerlSubUtil.findSubDeclarations(project, packageName + "::" + subName));

			if (result.size() == 0)    // not found, need to check parents
			{
				for (PsiPerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.findNamespaceDefinitions(project, packageName))
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
		assert packageName != null: "Null package name for " + subName;
		assert subName != null: "Null sub name for " + packageName;

		Collection<PsiPerlGlobVariable> result = new ArrayList<>();

		if (!checkedPackages.contains(packageName))
		{
			checkedPackages.add(packageName);

			if (!noCheckCurrent)    // suppress for resolving SUPER::
				result.addAll(PerlGlobUtil.findGlobsDefinitions(project, packageName + "::" + subName));

			if (result.size() == 0)    // not found, need to check parents
			{
				for (PsiPerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.findNamespaceDefinitions(project, packageName))
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


}
