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

package com.perl5.lang.htmlmason.filetypes;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectLocator;
import com.intellij.openapi.util.io.ByteSequence;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 12.03.2016.
 */
public class HTMLMasonFileTypeDetector implements FileTypeRegistry.FileTypeDetector
{
	private static final int VERSION = 0;

	@Nullable
	@Override
	public FileType detect(@NotNull VirtualFile file, @NotNull ByteSequence firstBytes, @Nullable CharSequence firstCharsIfText)
	{
		Project project = ProjectLocator.getInstance().guessProjectForFile(file);
		if (project != null)
		{
			HTMLMasonSettings settings = HTMLMasonSettings.getInstance(project);
			if (settings != null && (StringUtil.equals(settings.autoHandlerName, file.getName()) || StringUtil.equals(settings.defaultHandlerName, file.getName())))
			{
				return HTMLMasonFileType.INSTANCE;
			}
		}
		return null;
	}

	@Override
	public int getVersion()
	{
		return VERSION;
	}
}
