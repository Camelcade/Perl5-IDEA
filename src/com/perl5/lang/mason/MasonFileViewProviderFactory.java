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

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.FileViewProviderFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.SingleRootFileViewProvider;
import com.intellij.testFramework.LightVirtualFile;
import com.perl5.lang.mason.filetypes.MasonPurePerlComponentFileType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.12.2015.
 */
public class MasonFileViewProviderFactory implements FileViewProviderFactory
{
	@NotNull
	@Override
	public FileViewProvider createFileViewProvider(@NotNull VirtualFile file, Language language, @NotNull PsiManager manager, boolean eventSystemEnabled)
	{
		FileType fileType;

		if (file instanceof LightVirtualFile && ((LightVirtualFile) file).getOriginalFile() != null)
		{
			fileType = ((LightVirtualFile) file).getOriginalFile().getFileType();
		}
		else
		{
			// fixme dirty hack for stupid LightVirtualFiles, we should check something like registered extensions, but duunno how yet
			if ("mp".equals(file.getExtension()))
			{
				fileType = MasonPurePerlComponentFileType.INSTANCE;
			}
			else
			{
				fileType = file.getFileType();
			}
		}

		if (fileType == MasonPurePerlComponentFileType.INSTANCE)
		{
			return new SingleRootFileViewProvider(manager, file, eventSystemEnabled, MasonPurePerlComponentFileType.INSTANCE);
		}
		return new MasonFileViewProvider(manager, file, eventSystemEnabled);
	}
}
