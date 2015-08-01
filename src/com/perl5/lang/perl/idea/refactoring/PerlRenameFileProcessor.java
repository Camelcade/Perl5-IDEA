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

package com.perl5.lang.perl.idea.refactoring;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenamePsiFileProcessor;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.impl.PerlFileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlRenameFileProcessor extends RenamePsiFileProcessor
{
	@Override
	public boolean canProcessElement(@NotNull PsiElement element)
	{
		return element instanceof PerlFileElement && "pm".equals(element.getContainingFile().getVirtualFile().getExtension());
	}

	@Nullable
	@Override
	public Runnable getPostRenameCallback(PsiElement element, String newName, RefactoringElementListener elementListener)
	{
		Runnable postProcessor = null;

		if (newName.endsWith(".pm"))
		{
			assert element instanceof PerlFileElement;
			final Project project = element.getProject();
			VirtualFile currentVirtualFile = ((PerlFileElement) element).getVirtualFile();
			final String currentPacakgeName = ((PerlFileElement) element).getFilePackageName();

			if (currentPacakgeName != null)
			{
				String currentFileName = currentVirtualFile.getNameWithoutExtension();
				String newFileName = newName.replaceFirst("\\.pm$", "");

				final String newFilePath = currentVirtualFile.getPath().replaceFirst(currentVirtualFile.getName() + "$", newName);
				final String newPackageName = currentPacakgeName.replaceFirst(currentFileName + "$", newFileName);

				postProcessor = new Runnable()
				{
					@Override
					public void run()
					{
						VirtualFile newVirtualFile = LocalFileSystem.getInstance().findFileByIoFile(new File(newFilePath));

						if (newVirtualFile != null)
						{
							PsiFile newPsiFile = PsiManager.getInstance(project).findFile(newVirtualFile);

							if (newPsiFile != null)
							{
								RenameRefactoringQueue queue = new RenameRefactoringQueue(project);

								for (PsiPerlNamespaceDefinition namespaceDefinition : PsiTreeUtil.findChildrenOfType(newPsiFile, PsiPerlNamespaceDefinition.class))
									if (currentPacakgeName.equals(namespaceDefinition.getPackageName()))
										queue.addElement(namespaceDefinition, newPackageName);

								queue.run();
							}
						}
					}
				};
			}
		}

		return postProcessor;
	}
}
