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

package com.perl5.lang.mason;

import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.FileBasedIndexImpl;
import com.perl5.lang.mason.filetypes.MasonPurePerlComponentFileType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 06.01.2016.
 */
public class MasonUtils
{
	@NotNull
	public static String getClassnameFromPath(@NotNull String path)
	{
		return "MC0::" + path.replaceAll("[^\\w\\/]", "_").replaceAll("" + VfsUtil.VFS_SEPARATOR_CHAR, PerlPackageUtil.PACKAGE_SEPARATOR);
	}

	public static void reindexProjectFile(Project project, VirtualFile virtualFile)
	{
		if (VfsUtil.isAncestor(project.getBaseDir(), virtualFile, false))
		{
			reindexProjectRoots(project, Collections.singletonList(VfsUtil.getRelativePath(virtualFile, project.getBaseDir())));
		}
	}

	public static void reindexProjectRoots(Project project, List<String> rootsToReindex)
	{
		if (rootsToReindex.isEmpty())
			return;

		PsiDocumentManager.getInstance(project).commitAllDocuments();

		VirtualFile projectRoot = project.getBaseDir();

		if (projectRoot != null)
		{
			final FileBasedIndex index = FileBasedIndex.getInstance();

			for (String root : rootsToReindex)
			{
				VirtualFile componentRoot = VfsUtil.findRelativeFile(projectRoot, root);
				if (componentRoot != null)
				{
					for (VirtualFile file : VfsUtil.collectChildrenRecursively(componentRoot))
					{
						if (file.getFileType() instanceof MasonPurePerlComponentFileType)
						{
							index.requestReindex(file);
						}
					}
				}
			}
			if (index instanceof FileBasedIndexImpl)
			{
				new Task.Backgroundable(project, "Reindexing Files", false, PerformInBackgroundOption.ALWAYS_BACKGROUND)
				{
					@Override
					public void run(@NotNull ProgressIndicator indicator)
					{
						double totalFiles = ((FileBasedIndexImpl) index).getChangedFileCount();
						double filesLeft;
						while ((filesLeft = ((FileBasedIndexImpl) index).getChangedFileCount()) > 0)
						{
							indicator.setFraction((totalFiles - filesLeft) / totalFiles);
						}
					}
				}.queue();
			}
		}
	}

}
