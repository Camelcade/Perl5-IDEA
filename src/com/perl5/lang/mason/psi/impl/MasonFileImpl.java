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

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.indexing.IndexingDataKeys;
import com.perl5.lang.mason.MasonLanguage;
import com.perl5.lang.mason.MasonUtils;
import com.perl5.lang.mason.filetypes.MasonPurePerlComponentFileType;
import com.perl5.lang.mason.idea.configuration.MasonSettings;
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 20.12.2015.
 */
public class MasonFileImpl extends PerlFileImpl implements PerlImplicitVariablesProvider
{
	public MasonFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, MasonLanguage.INSTANCE);
	}

	public MasonFileImpl(@NotNull FileViewProvider viewProvider, Language language)
	{
		super(viewProvider, language);
	}

	@Override
	public String toString()
	{
		return "Mason2 pure perl file";
	}

	@Override
	protected FileType getDefaultFileType()
	{
		return MasonPurePerlComponentFileType.INSTANCE;
	}

	/**
	 * Returns VFS object representing component root
	 *
	 * @return component root
	 */
	@Nullable
	public VirtualFile getComponentRoot()
	{
		return MasonUtils.getComponentRoot(getProject(), getContainingVirtualFile());
	}

	/**
	 * Returns real containing virtual file, not the Light one
	 *
	 * @return virtual file or null
	 */
	@Nullable
	public VirtualFile getContainingVirtualFile()
	{
		VirtualFile originalFile = getViewProvider().getVirtualFile();

		if (originalFile instanceof LightVirtualFile)
		{
			if (getUserData(IndexingDataKeys.VIRTUAL_FILE) != null)
			{
				originalFile = getUserData(IndexingDataKeys.VIRTUAL_FILE);
			}
			else if (((LightVirtualFile) originalFile).getOriginalFile() != null)
			{
				originalFile = ((LightVirtualFile) originalFile).getOriginalFile();
			}
		}
		return originalFile instanceof LightVirtualFile || originalFile == null || !originalFile.exists() ? null : originalFile;
	}

	@Override
	public boolean isKnownVariable(@NotNull PerlVariable variable)
	{
		return MasonSettings.getInstance(getProject()).isGlobalVariable(variable);
	}

	@NotNull
	@Override
	public List<String> getFullQualifiedVariablesList()
	{
		return MasonSettings.getInstance(getProject()).getGlobalVariables();
	}
}
