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

package com.perl5.lang.perl.extensions.packageprocessor;

import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.util.PerlPackageUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 18.08.2015.
 */
public abstract class PerlPackageProcessorBase implements IPerlPackageProcessor
{
	@Override
	public boolean isPragma()
	{
		return false;
	}

	@Override
	public void use(PerlUseStatement useStatement)
	{

	}

	@Override
	public void no(PerlUseStatement noStatement)
	{

	}

	@Override
	public List<String> getImportedSubs(PerlUseStatement useStatement)
	{
		List<String> result = new ArrayList<String>();
		Project project = useStatement.getProject();
		String packageName = useStatement.getPackageName();
		if (packageName != null)
		{
			List<String> parameters = useStatement.getImportParameters();
//			System.err.println("Import parameters for " + packageName + " are " + parameters);
			Set<String> packageExport = new HashSet<String>();
			Set<String> packageExportOk = new HashSet<String>();

			// fixme handle tags
			for (PerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, packageName))
			{
				packageExport.addAll(namespaceDefinition.getEXPORT());
				packageExportOk.addAll(namespaceDefinition.getEXPORT_OK());
			}
			packageExportOk.addAll(packageExport);

			if (parameters == null)    // default import
				result.addAll(packageExport);
			else
				for (String parameter : parameters)
					if (packageExportOk.contains(parameter))
						result.add(parameter);
		}

//		System.err.println("Imported from " + packageName + ": " + result);

		return result;
	}
}
