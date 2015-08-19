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

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
	@NotNull
	public List<String> getImports(PerlUseStatement useStatement)
	{
		HashSet<String> result = new HashSet<String>();
		String packageName = useStatement.getPackageName();
		if (packageName != null)
		{
			PsiElement expr = useStatement.getExpr();
			HashSet<String> possibleExports = new HashSet<String>();
			for (PerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(useStatement.getProject(), packageName))
				possibleExports.addAll(namespaceDefinition.getEXPORT());

			if (expr == null) // default imports
				result = possibleExports;
			else
			{
				for (PerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(useStatement.getProject(), packageName))
					possibleExports.addAll(namespaceDefinition.getEXPORT_OK());

				// fixme implement tags import

				for (PerlStringContentElement stringContentElement : PerlPsiUtil.findStringElments(expr))
					if (possibleExports.contains(stringContentElement.getName()))
						result.add(stringContentElement.getName());
			}
		}

//		System.err.println("Imported from " + packageName + ": " + result);

		return new ArrayList<String>(result);
	}
}
