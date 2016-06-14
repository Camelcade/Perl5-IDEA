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

package com.perl5.lang.htmlmason.idea.lang;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.impl.FilePropertyPusher;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.FileAttribute;
import com.intellij.psi.LanguageSubstitutors;
import com.intellij.util.io.DataInputOutputUtil;
import com.intellij.util.messages.MessageBus;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by hurricup on 08.03.2016.
 */
public class HTMLMasonFilePropertyPusher implements FilePropertyPusher<Boolean>
{
	public static final Key<Boolean> KEY = new Key<Boolean>("perl5.html.mason.handling");
	public static final FilePropertyPusher<Boolean> INSTANCE = new HTMLMasonFilePropertyPusher();
	private static final FileAttribute PERSISTENCE = new FileAttribute("html_mason_handling_persistence", 1, true);

	@Override
	public void initExtra(@NotNull Project project, @NotNull MessageBus bus, @NotNull Engine languageLevelUpdater)
	{

	}

	@NotNull
	@Override
	public Key<Boolean> getFileDataKey()
	{
		return KEY;
	}

	@Override
	public boolean pushDirectoriesOnly()
	{
		return false;
	}

	@NotNull
	@Override
	public Boolean getDefaultValue()
	{
		return false;
	}

	@Nullable
	@Override
	public Boolean getImmediateValue(@NotNull Project project, @Nullable VirtualFile file)
	{
		if (file == null)
		{
			return false;
		}

		FileType fileType = file.getFileType();
		if (fileType instanceof LanguageFileType)
		{
			return LanguageSubstitutors.INSTANCE.substituteLanguage(((LanguageFileType) fileType).getLanguage(), file, project) == HTMLMasonLanguage.INSTANCE;
		}
		return false;

	}

	@Nullable
	@Override
	public Boolean getImmediateValue(@NotNull Module module)
	{
		return null;
	}

	@Override
	public boolean acceptsFile(@NotNull VirtualFile file)
	{
		return true;
	}

	@Override
	public boolean acceptsDirectory(@NotNull VirtualFile file, @NotNull Project project)
	{
		return false;
	}

	@Override
	public void persistAttribute(@NotNull Project project, @NotNull VirtualFile fileOrDir, @NotNull Boolean value) throws IOException
	{
		final DataInputStream iStream = PERSISTENCE.readAttribute(fileOrDir);
		if (iStream != null)
		{
			try
			{
				if ((DataInputOutputUtil.readINT(iStream) == 1) == value)
				{
					return;
				}
			}
			finally
			{
				iStream.close();
			}
		}

//		System.err.println("Setting "  + value + " to " + fileOrDir);
		final DataOutputStream oStream = PERSISTENCE.writeAttribute(fileOrDir);
		DataInputOutputUtil.writeINT(oStream, value ? 1 : 0);
		oStream.close();
	}

	@Override
	public void afterRootsChanged(@NotNull Project project)
	{
	}
}
