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

package com.perl5.lang.mason2;

import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.FileBasedIndexImpl;
import com.perl5.compat.PerlStubIndex;
import com.perl5.lang.htmlmason.MasonCoreUtil;
import com.perl5.lang.mason2.filetypes.MasonFileType;
import com.perl5.lang.mason2.idea.configuration.MasonSettings;
import com.perl5.lang.mason2.psi.MasonNamespaceDefinition;
import com.perl5.lang.mason2.psi.stubs.MasonNamespaceDefitnitionsStubIndex;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 06.01.2016.
 */
public class Mason2Util
{
	@NotNull
	public static String getClassnameFromPath(@NotNull String path)
	{
		return "/MC0::" + path.replaceAll("[^\\p{L}\\d_\\/]", "_").replaceAll("" + VfsUtil.VFS_SEPARATOR_CHAR, PerlPackageUtil.PACKAGE_SEPARATOR);
	}

	@Nullable
	public static String getVirtualFileClassName(@NotNull Project project, @Nullable VirtualFile componentFile)
	{
		if (componentFile != null && componentFile.isValid())
		{
			VirtualFile componentRoot = getComponentRoot(project, componentFile);
			if (componentRoot != null)
			{
				//noinspection ConstantConditions
				return getClassnameFromPath(VfsUtil.getRelativePath(componentFile, componentRoot));
			}
		}

		return null;
	}

	@Nullable
	public static VirtualFile getComponentRoot(@NotNull Project project, @Nullable VirtualFile file)
	{
		return MasonCoreUtil.getComponentRoot(MasonSettings.getInstance(project), file);
	}

	public static List<PerlNamespaceDefinition> getMasonNamespacesByAbsolutePath(@NotNull Project project, @NotNull String absolutePath)
	{
		return new ArrayList<PerlNamespaceDefinition>(
				PerlStubIndex.getElements(
						MasonNamespaceDefitnitionsStubIndex.KEY,
						absolutePath,
						project,
						GlobalSearchScope.projectScope(project),
						MasonNamespaceDefinition.class
				)
		);
	}

	@NotNull
	public static List<PerlNamespaceDefinition> collectComponentNamespacesByPaths(@NotNull Project project, @NotNull List<String> componentPaths, @NotNull VirtualFile anchorDir)
	{
		List<PerlNamespaceDefinition> result = new ArrayList<PerlNamespaceDefinition>();
		MasonSettings masonSettings = MasonSettings.getInstance(project);

		for (String componentPath : componentPaths)
		{
			VirtualFile componentFile = null;
			if (componentPath.startsWith("" + VfsUtil.VFS_SEPARATOR_CHAR)) // abs path relative to mason roots, see the Mason::Interp::_determine_parent_compc
			{
				for (VirtualFile componentRoot : masonSettings.getComponentsRootsVirtualFiles())
				{
					componentFile = componentRoot.findFileByRelativePath(componentPath.substring(1));
					if (componentFile != null)
					{
						break;
					}
				}
			}
			else // relative path
			{
				componentFile = anchorDir.findFileByRelativePath(componentPath);
			}

			if (componentFile != null)
			{
				String absolutePath = VfsUtil.getRelativePath(componentFile, project.getBaseDir());
				if (absolutePath != null)
				{
					result.addAll(Mason2Util.getMasonNamespacesByAbsolutePath(project, absolutePath));
				}
			}
		}

		return result;
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
		{
			return;
		}

		PsiDocumentManager.getInstance(project).commitAllDocuments();

		VirtualFile projectRoot = project.getBaseDir();

		if (projectRoot != null)
		{
			final FileBasedIndex index = FileBasedIndex.getInstance();

			for (String root : rootsToReindex)
			{
				VirtualFile componentRoot = VfsUtil.findRelativeFile(root, projectRoot);
				if (componentRoot != null)
				{
					for (VirtualFile file : VfsUtil.collectChildrenRecursively(componentRoot))
					{
						if (file.getFileType() instanceof MasonFileType)
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
