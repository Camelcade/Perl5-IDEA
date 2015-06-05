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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileReference;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 28.05.2015.
 */
public class PerlNamespaceFileReference extends PerlReferencePoly implements PsiFileReference

{
	private final String packageName;

	public PerlNamespaceFileReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
		assert element instanceof PerlNamespaceElement;
		packageName = ((PerlNamespaceElement) element).getName();
		if( element.getText().endsWith("::"))
			setRangeInElement(new TextRange(0, element.getTextLength()-2));
	}

	@NotNull
	@Override
	public Object[] getVariants()
	{
		return new Object[0];
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		List<ResolveResult> result = new ArrayList<ResolveResult>();

		// resolves to a psi file
		String properPath = PerlPackageUtil.getPackagePathByName(packageName);
		Project project = myElement.getProject();

		for (VirtualFile sourceRoot : ProjectRootManager.getInstance(myElement.getProject()).getContentSourceRoots())
		{
			VirtualFile packageFile = sourceRoot.findFileByRelativePath(properPath);
			if( packageFile != null)
			{
				PsiFile packagePsiFile = PsiManager.getInstance(project).findFile(packageFile);
				if( packagePsiFile != null)
				{
					result.add(new PsiElementResolveResult(packagePsiFile));
					break;
				}
			}

		}

		return result.toArray(new ResolveResult[result.size()]);
	}

	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
	{
		assert myElement instanceof PerlNamespaceElement;
		String currentName = ((PerlNamespaceElement) myElement).getName();
		if( currentName != null && newElementName.endsWith(".pm") )
		{
			String[] nameChunks = currentName.split("::");
			nameChunks[nameChunks.length-1] = newElementName.replaceFirst("\\.pm$", "");
			newElementName = StringUtils.join(nameChunks, "::");

			return super.handleElementRename(newElementName);

		}

		throw new IncorrectOperationException("Can't bind package use/require to a non-pm file: " + newElementName);
	}

	@Override
	public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException
	{
		VirtualFile newFile = element.getContainingFile().getVirtualFile();

		if( "pm".equals(newFile.getExtension()))
		{
			VirtualFile innerMostRoot = PerlUtil.findInnermostSourceRoot(myElement.getProject(), newFile);

			if (innerMostRoot != null)
			{
				String newPath = VfsUtil.getRelativePath(newFile, innerMostRoot);
				return super.handleElementRename(PerlPackageUtil.getPackageNameByPath(newPath));
			}
			// todo this is not being handled on rename
//			else
//			{
//				throw new IncorrectOperationException("Failed attempt to move package file outside of the one of the source roots: " + newFile.getPath());
//			}
			return null;
		}
		throw new IncorrectOperationException("Unable to rebind package use/require to a non-pm file " + newFile.getName());
	}

	@Override
	public TextRange getRangeInElement()
	{
		return super.getRangeInElement();
	}
}
