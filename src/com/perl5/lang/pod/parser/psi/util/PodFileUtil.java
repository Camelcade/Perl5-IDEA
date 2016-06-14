/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.pod.parser.psi.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.util.Processor;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlUtil;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.filetypes.PodFileType;
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 27.03.2016.
 */
public class PodFileUtil
{
	public static final String PM_OR_POD_EXTENSION_PATTERN = ".(" + PodFileType.EXTENSION + "|" + PerlFileTypePackage.EXTENSION + ")$";

	private static final Set<String> myClassLikeExtensions = new THashSet<String>(Arrays.asList(
			PodFileType.EXTENSION,
			PerlFileTypePackage.EXTENSION
	));

	@Nullable
	public static String getPackageName(PsiFile file)
	{
		VirtualFile virtualFile = file.getVirtualFile();
		VirtualFile classRoot = PerlUtil.getFileClassRoot(file);

		if (virtualFile != null && myClassLikeExtensions.contains(virtualFile.getExtension()))
		{
			if (classRoot == null)
			{
				return virtualFile.getName();
			}
			else
			{
				return getPackageNameFromVirtualFile(virtualFile, classRoot);
			}
		}
		return null;
	}

	/**
	 * Searching perl file related to specified pod file
	 *
	 * @param podFile pod psi file
	 * @return perl psi file if found
	 */
	@Nullable
	public static PsiFile getTargetPerlFile(PsiFile podFile)
	{
		if (podFile == null)
		{
			return null;
		}

		final PsiFile baseFile = podFile.getViewProvider().getStubBindingRoot();
		if (baseFile != podFile)
		{
			return baseFile;
		}

		String packageName = getPackageName(baseFile);
		if (StringUtil.isNotEmpty(packageName))
		{
			return PerlPackageUtil.getPackagePsiFileByPackageName(baseFile.getProject(), packageName);
		}
		return null;
	}

	@Nullable
	public static String getPackageNameFromVirtualFile(VirtualFile file, VirtualFile classRoot)
	{
		String relativePath = VfsUtil.getRelativePath(file, classRoot);
		if (relativePath != null)
		{
			return StringUtil.join(relativePath.replaceAll(PM_OR_POD_EXTENSION_PATTERN, "").split("/"), PerlPackageUtil.PACKAGE_SEPARATOR);
		}
		return null;
	}

	public static String getFilenameFromPackage(@NotNull String packageName)
	{
		return StringUtil.join(PerlPackageUtil.getCanonicalPackageName(packageName).split(PerlPackageUtil.PACKAGE_SEPARATOR), "/") + "." + PodFileType.EXTENSION;
	}

	@Nullable
	public static PsiFile getPodOrPackagePsiByDescriptor(Project project, PodLinkDescriptor descriptor)
	{
		final List<PsiFile> result = new ArrayList<PsiFile>();

		PodFileUtil.processPodFilesByDescriptor(project, descriptor, new Processor<PsiFile>()
		{
			@Override
			public boolean process(PsiFile psiFile)
			{
				if (psiFile != null)
				{
					if (psiFile.getFileType() == PodFileType.INSTANCE)
					{
						result.clear();
						result.add(psiFile);
						return false;
					}
					else if ((psiFile = psiFile.getViewProvider().getPsi(PodLanguage.INSTANCE)) != null)
					{
						result.add(psiFile);
					}
				}
				return true;
			}
		});

		return result.isEmpty() ? null : result.get(0);
	}

	public static void processPodFilesByDescriptor(Project project, PodLinkDescriptor descriptor, Processor<PsiFile> processor)
	{
		if (descriptor.getFileId() != null)
		{
			// seek file
			String fileId = descriptor.getFileId();

			if (fileId.contains(PerlPackageUtil.PACKAGE_SEPARATOR) || !StringUtil.startsWith(fileId, "perl")) // can be Foo/Bar.pod or Foo/Bar.pm
			{
				final PsiManager psiManager = PsiManager.getInstance(project);
				String podRelativePath = PodFileUtil.getFilenameFromPackage(fileId);
				String packageRelativePath = PerlPackageUtil.getPackagePathByName(fileId);

				for (VirtualFile classRoot : ProjectRootManager.getInstance(project).orderEntries().getClassesRoots())
				{
					VirtualFile targetVirtualFile = classRoot.findFileByRelativePath(podRelativePath);
					if (targetVirtualFile != null)
					{
						if (!processor.process(psiManager.findFile(targetVirtualFile)))
						{
							return;
						}
					}

					targetVirtualFile = classRoot.findFileByRelativePath(packageRelativePath);
					if (targetVirtualFile != null)
					{
						if (!processor.process(psiManager.findFile(targetVirtualFile)))
						{
							return;
						}
					}
				}
			}
			else // top level file perl.*
			{
				fileId += "." + PodFileType.EXTENSION;

				for (PsiFile podFile : FilenameIndex.getFilesByName(project, fileId, PerlScopes.getProjectAndLibrariesScope(project)))
				{
					if (!processor.process(podFile))
					{
						return;
					}
				}
			}
		}
	}
}
