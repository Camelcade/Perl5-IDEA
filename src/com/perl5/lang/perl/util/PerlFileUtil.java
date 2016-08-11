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

package com.perl5.lang.perl.util;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Created by hurricup on 11.08.2016.
 */
public class PerlFileUtil
{
	@Nullable
	public static VirtualFile findOrCreateRelativeFile(@NotNull VirtualFile root, @NotNull String relativePath) throws IOException
	{
		VirtualFile targetFile = VfsUtil.findRelativeFile(root, relativePath);
		if (targetFile != null)
		{
			return targetFile;
		}

		int filenamePosition = relativePath.lastIndexOf('/');
		String fileName;

		if (filenamePosition > -1)
		{
			String directoryRelativePath = relativePath.substring(0, filenamePosition);
			fileName = relativePath.substring(filenamePosition + 1);
			if (StringUtil.isNotEmpty(directoryRelativePath))
			{
				root = VfsUtil.createDirectoryIfMissing(root, directoryRelativePath);
			}
		}
		else
		{
			fileName = relativePath;
		}
		return root.findOrCreateChildData(null, fileName);
	}
}
