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

package com.perl5.lang.mason.psi.impl;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.roots.ui.configuration.JavaVfsSourceRootDetectionUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.mason.MasonPerlLanguage;
import com.perl5.lang.mason.filetypes.MasonTopLevelComponentFileType;
import com.perl5.lang.mason.idea.configuration.MasonPerlSettings;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by hurricup on 20.12.2015.
 */
public class MasonPerlFileImpl extends PerlFileImpl
{
	private String filePackage;

	public MasonPerlFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, MasonPerlLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType()
	{
		return MasonTopLevelComponentFileType.INSTANCE;
	}

	@Override
	public String toString()
	{
		return "Mason component";
	}

	@Override
	public String getPackageName()
	{
//		if (filePackage != null)
//			return filePackage;

		MasonPerlSettings masonSettings = MasonPerlSettings.getInstance(getProject());
		VirtualFile originalFile = getViewProvider().getVirtualFile();

		for (String relativeRoot : masonSettings.componentRoots)
		{
			VirtualFile rootFile = VfsUtil.findRelativeFile(getProject().getBaseDir(), relativeRoot);
			if (rootFile != null && VfsUtil.isAncestor(rootFile, originalFile, true))
			{
				String componentPath = VfsUtil.getRelativePath(originalFile, rootFile);

				if (componentPath != null)
				{
					return filePackage = "MC0::" + StringUtils.join(
							componentPath.
									replaceFirst("\\.[^" + VfsUtil.VFS_SEPARATOR_CHAR + "]*$", "").
									split("" + VfsUtil.VFS_SEPARATOR_CHAR),
							PerlPackageUtil.PACKAGE_SEPARATOR
					);
				}
			}
		}

		return filePackage = super.getPackageName();
	}
}
