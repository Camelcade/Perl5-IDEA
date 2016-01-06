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

package com.perl5.lang.perl.idea.hierarchy.namespace;

import com.intellij.ide.hierarchy.HierarchyBrowser;
import com.intellij.ide.hierarchy.HierarchyProvider;
import com.intellij.ide.hierarchy.TypeHierarchyBrowserBase;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 16.08.2015.
 */
public class PerlPackageHierarchyProvider implements HierarchyProvider
{
	@Nullable
	@Override
	public PsiElement getTarget(@NotNull DataContext dataContext)
	{
		PsiElement element = CommonDataKeys.PSI_ELEMENT.getData(dataContext);
		if (element == null)
		{
			final Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
			final PsiFile file = CommonDataKeys.PSI_FILE.getData(dataContext);

			if (editor != null && file != null)
				element = file.findElementAt(editor.getCaretModel().getOffset());
		}
		if (!(element instanceof PerlNamespaceDefinition))
			element = PsiTreeUtil.getParentOfType(element, PerlNamespaceDefinition.class);

		return element;
	}

	@NotNull
	@Override
	public HierarchyBrowser createHierarchyBrowser(PsiElement target)
	{
		return new PerlPackageHierarchyBrowser(target);
	}

	@Override
	public void browserActivated(@NotNull HierarchyBrowser hierarchyBrowser)
	{
		((PerlPackageHierarchyBrowser) hierarchyBrowser).changeView(TypeHierarchyBrowserBase.SUPERTYPES_HIERARCHY_TYPE);
	}
}
