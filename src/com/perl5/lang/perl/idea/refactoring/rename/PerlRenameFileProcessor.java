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

package com.perl5.lang.perl.idea.refactoring.rename;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenamePsiFileProcessor;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlRenameFileProcessor extends RenamePsiFileProcessor
{
	@Override
	public boolean canProcessElement(@NotNull PsiElement element)
	{
		return element instanceof PerlFileImpl && ((PerlFileImpl) element).getName().endsWith(".pm");
	}

	@Override
	public void renameElement(PsiElement element, String newName, UsageInfo[] usages, @Nullable RefactoringElementListener listener) throws IncorrectOperationException
	{
		if (!newName.endsWith(".pm")) // suppose it's a package name
		{
			String newPackageName = PerlPackageUtil.getCanonicalPackageName(newName);
			List<String> newPackageChunks = Arrays.asList(newPackageName.split("::"));
			newName = newPackageChunks.get(newPackageChunks.size() - 1) + ".pm";
		}

		super.renameElement(element, newName, usages, listener);
	}

	// repeated from PerlRenamePolyReferencedElementProcessor, cause inherited from RenamePsiFileProcessor
	@Override
	public void prepareRenaming(PsiElement element, String newName, Map<PsiElement, String> allRenames, SearchScope scope)
	{
		for (PsiReference reference : ReferencesSearch.search(element, element.getUseScope()).findAll())
		{
			if (reference instanceof PsiPolyVariantReference)
			{
				for (ResolveResult resolveResult : ((PsiPolyVariantReference) reference).multiResolve(false))
				{
					PsiElement resolveResultElement = resolveResult.getElement();
					if (!allRenames.containsKey(resolveResultElement))
					{
						allRenames.put(resolveResultElement, newName);
					}
				}
			}
		}
	}


//	@Nullable
//	@Override
//	public Runnable getPostRenameCallback(PsiElement element, String newName, RefactoringElementListener elementListener)
//	{
//		Runnable postProcessor = null;
//
//		if (newName.endsWith(".pm"))
//		{
//			assert element instanceof PerlFileImpl;
//			final Project project = element.getProject();
//			VirtualFile currentVirtualFile = ((PerlFileImpl) element).getVirtualFile();
//			final String currentPacakgeName = ((PerlFileImpl) element).getFilePackageName();
//
//			if (currentPacakgeName != null)
//			{
//				String currentFileName = currentVirtualFile.getNameWithoutExtension();
//				String newFileName = newName.replaceFirst("\\.pm$", "");
//
//				final String newFilePath = currentVirtualFile.getPath().replaceFirst(currentVirtualFile.getName() + "$", newName);
//				final String newPackageName = currentPacakgeName.replaceFirst(currentFileName + "$", newFileName);
//
//				postProcessor = new Runnable()
//				{
//					@Override
//					public void run()
//					{
//						VirtualFile newVirtualFile = LocalFileSystem.getInstance().findFileByIoFile(new File(newFilePath));
//
//						if (newVirtualFile != null)
//						{
//							PsiFile newPsiFile = PsiManager.getInstance(project).findFile(newVirtualFile);
//
//							if (newPsiFile != null)
//							{
//								RenameRefactoringQueue queue = new RenameRefactoringQueue(project);
//
//								for (PsiPerlNamespaceDefinition namespaceDefinition : PsiTreeUtil.findChildrenOfType(newPsiFile, PsiPerlNamespaceDefinition.class))
//									if (currentPacakgeName.equals(namespaceDefinition.getPackageName()))
//										queue.addElement(namespaceDefinition, newPackageName);
//
//								queue.run();
//							}
//						}
//					}
//				};
//			}
//		}
//		return postProcessor;
//	}
}
