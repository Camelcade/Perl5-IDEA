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
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFileHandler;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.impl.PerlFileElementImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 05.06.2015.
 *
 */
public class PerlMoveFileHandler extends MoveFileHandler
{
	private static final Key<String> ORIGINAL_PACKAGE_NAME = Key.create("PERL_ORIGINAL_PACKAGE_NAME");

	@Override
	public boolean canProcessElement(PsiFile element)
	{
		// todo assign proper type to the pm files
		return element instanceof PerlFileElementImpl && element.getName() != null && element.getName().endsWith(".pm");
	}

	@Override
	public void prepareMovedFile(PsiFile file, PsiDirectory moveDestination, Map<PsiElement, PsiElement> oldToNewMap)
	{
		file.putUserData(ORIGINAL_PACKAGE_NAME, ((PerlFileElementImpl) file).getFilePackageName());
	}

	@Nullable
	@Override
	public List<UsageInfo> findUsages(PsiFile psiFile, PsiDirectory newParent, boolean searchInComments, boolean searchInNonJavaFiles)
	{
		return null;
	}

	@Override
	public void retargetUsages(List<UsageInfo> usageInfos, Map<PsiElement, PsiElement> oldToNewMap)
	{
	}

	@Override
	public void updateMovedFile(PsiFile file) throws IncorrectOperationException
	{
		String originalPackageName = file.getUserData(ORIGINAL_PACKAGE_NAME);
		Project project = file.getProject();
		VirtualFile virtualFile = file.getVirtualFile();
		VirtualFile newInnermostRoot = PerlUtil.findInnermostSourceRoot(project, virtualFile);

		if (newInnermostRoot != null && originalPackageName != null)
		{
			String newRelativePath = VfsUtil.getRelativePath(virtualFile, newInnermostRoot);
			String newPackageName = PerlPackageUtil.getPackageNameByPath(newRelativePath);

			RenameRefactoringQueue queue = new RenameRefactoringQueue(project);

			for (PsiPerlNamespaceDefinition namespaceDefinition : PsiTreeUtil.findChildrenOfType(file, PsiPerlNamespaceDefinition.class))
				if (originalPackageName.equals(namespaceDefinition.getPackageName()))
					queue.addElement(namespaceDefinition, newPackageName);

			queue.run();
		}
	}
}
