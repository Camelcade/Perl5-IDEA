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
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import com.perl5.lang.perl.util.PerlUtil;

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
		return getSubDefinitions(project, packageName, subName, new HashSet<String>());
	}

	public static Collection<PsiPerlSubDefinition> getSubDefinitions(Project project, String packageName, String subName, HashSet<String> checkedPackages)
	{
		Collection<PsiPerlSubDefinition> result = new ArrayList<>();

		if( !checkedPackages.contains(packageName))
		{
			checkedPackages.add(packageName);

			result.addAll(PerlSubUtil.findSubDefinitions(project, packageName + "::" + subName));

			if( result.size() == 0)	// not found, need to check parents
			{
				for( PsiPerlNamespaceDefinition namespaceDefinition: PerlPackageUtil.findNamespaceDefinitions(project, packageName) )
					for( String parentNamespace: namespaceDefinition.getParentNamespaces() )
					{
						result.addAll(getSubDefinitions(project, parentNamespace, subName, checkedPackages));
						if( result.size() > 0)
							break;
					}
			}
		}

		return result;
	}


}
