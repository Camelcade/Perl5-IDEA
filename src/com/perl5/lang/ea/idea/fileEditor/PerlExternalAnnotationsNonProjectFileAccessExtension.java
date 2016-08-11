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

package com.perl5.lang.ea.idea.fileEditor;

import com.intellij.openapi.fileEditor.impl.NonProjectFileWritingAccessExtension;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.ea.fileTypes.PerlExternalAnnotationsFileType;
import com.perl5.lang.perl.util.PerlAnnotationsUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 11.08.2016.
 */
public class PerlExternalAnnotationsNonProjectFileAccessExtension implements NonProjectFileWritingAccessExtension
{
	@Override
	public boolean isWritable(@NotNull VirtualFile file)
	{
		FileType fileType = file.getFileType();
		if (fileType == PerlExternalAnnotationsFileType.INSTANCE)
		{
			VirtualFile pluginAnnotationsRoot = PerlAnnotationsUtil.getPluginAnnotationsRoot();
			return pluginAnnotationsRoot == null || !VfsUtil.isAncestor(pluginAnnotationsRoot, file, true);
		}

		return false;
	}
}
