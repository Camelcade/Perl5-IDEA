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
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.util.PerlPackageUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by hurricup on 15.06.2015.
 * Class represents default Perl's MRO.
 * In other words, it knows how to find sub definition and/or declaration
 */
public class PerlMroDfs extends PerlMro
{
	public final PerlMro INSTANCE = new PerlMroDfs();

	/**
	 * Builds list of inheritance path for DFS mro (Perl5 default): http://perldoc.perl.org/mro.html
	 *
	 * @param project      project
	 * @param packageName  current package name
	 * @param recursionMap recursion protection map
	 * @param result       list to populate
	 */
	@Override
	public void getLinearISA(Project project, String packageName, HashSet<String> recursionMap, ArrayList<String> result)
	{
		// at the moment we are checking all definitions available
		for (PerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, packageName))
		{
			Collection<String> parentPackageNames = namespaceDefinition.getParentNamespaces();

			for (String parentPackageName : parentPackageNames)
				if (!recursionMap.contains(parentPackageName))
				{
					recursionMap.add(parentPackageName);
					result.add(parentPackageName);
					getLinearISA(project, parentPackageName, recursionMap, result);
				}
		}
	}
}
