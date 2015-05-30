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

package com.perl5.lang.perl.idea;

import com.intellij.psi.PsiElement;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlElementFactory;
import com.perl5.lang.perl.psi.PerlNamespace;
import com.perl5.lang.perl.util.PerlPackageUtil;

/**
 * Created by hurricup on 29.05.2015.
 */
public class PerlRenameNamespaceProcessor extends RenamePsiElementProcessor
{
	@Override
	public boolean canProcessElement(PsiElement element)
	{
		return element instanceof PerlNamespace;
	}

	@Override
	public void renameElement(PsiElement element, String newName, UsageInfo[] usages, RefactoringElementListener listener) throws IncorrectOperationException
	{
		boolean packageNameInvalid;

		if( !"".equals(newName))
		{
			try
			{
				String canonicalName = PerlPackageUtil.canonicalPackageName(newName);
				PerlNamespace newNamespace = PerlElementFactory.createPackageName(element.getProject(), canonicalName);
				packageNameInvalid = (newNamespace == null || !canonicalName.equals(newNamespace.getName()));
			} catch (Exception any)	{
				packageNameInvalid = true;
			}
		}
		else
			throw new IncorrectOperationException("It's not allowed to rename to the empty/main package");

		if (packageNameInvalid) {
			throw new IncorrectOperationException("Invalid package name: "+newName);
		}

		super.renameElement(element, newName, usages, listener);
	}
}
