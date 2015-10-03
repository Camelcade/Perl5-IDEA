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
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenamePsiFileProcessor;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.idea.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.apache.commons.lang.StringUtils;
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
		return element instanceof PerlFileImpl && ((PerlFileImpl) element).getVirtualFile().getFileType() == PerlFileTypePackage.INSTANCE;
	}

	@Nullable
	@Override
	public Runnable getPostRenameCallback(final PsiElement element, String newName, RefactoringElementListener elementListener)
	{
		if (newName.endsWith(".pm"))
		{
			final Project project = element.getProject();
			final String currentPackageName = ((PerlFileImpl) element).getFilePackageName();
			String[] nameChunks = currentPackageName.split("::");
			nameChunks[nameChunks.length - 1] = newName.replaceFirst("\\.pm$", "");
			final String newPackageName = StringUtils.join(nameChunks, "::");

			final String newFileName = ((PerlFileImpl) element).getVirtualFile().getParent().getPath() + '/' + newName;

			return new Runnable()
			{
				@Override
				public void run()
				{
					VirtualFile newFile = LocalFileSystem.getInstance().findFileByPath(newFileName);

					if (newFile != null)
					{
						PsiFile psiFile = PsiManager.getInstance(project).findFile(newFile);

						if (psiFile != null)
						{
							RenameRefactoring refactoring = null;

							for (PerlNamespaceDefinition namespaceDefinition : PsiTreeUtil.findChildrenOfType(psiFile, PerlNamespaceDefinition.class))
							{
								if (currentPackageName.equals(namespaceDefinition.getName()))
								{
									if (refactoring == null)
									{
										refactoring = RefactoringFactory.getInstance(psiFile.getProject()).createRename(namespaceDefinition, newPackageName);
									} else
									{
										refactoring.addElement(namespaceDefinition, newPackageName);
									}
								}
							}

							if (refactoring != null)
							{
								refactoring.run();
							}
						}
					}
				}
			};
		}
		return super.getPostRenameCallback(element, newName, elementListener);
	}

}
