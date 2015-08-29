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

package com.perl5.lang.perl.idea.modules;

import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ExcludeFolder;
import com.intellij.openapi.roots.SourceFolder;
import com.intellij.openapi.roots.ui.configuration.ContentEntryEditor;
import com.intellij.openapi.roots.ui.configuration.ContentRootPanel;
import com.intellij.openapi.roots.ui.configuration.ModuleSourceRootEditHandler;
import com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * Created by hurricup on 29.08.2015.
 */
public abstract class PerlContentEntryEditor extends ContentEntryEditor
{
	private final CompilerModuleExtension myCompilerExtension;

	public PerlContentEntryEditor(final String contentEntryUrl, List<ModuleSourceRootEditHandler<?>> moduleSourceRootEditHandlers)
	{
		super(contentEntryUrl, moduleSourceRootEditHandlers);
		myCompilerExtension = getModel().getModuleExtension(CompilerModuleExtension.class);
	}

	@Override
	protected ContentRootPanel createContentRootPane()
	{
		return new ContentRootPanel(this, getEditHandlers())
		{
			@Nullable
			@Override
			protected ContentEntry getContentEntry()
			{
				return PerlContentEntryEditor.this.getContentEntry();
			}

			@Nullable
			@Override
			protected JComponent createRootPropertiesEditor(ModuleSourceRootEditHandler<?> editor, SourceFolder folder)
			{
				return editor.createPropertiesEditor(folder, this, myCallback);
			}
		};
	}

	@Override
	protected ExcludeFolder doAddExcludeFolder(@NotNull final VirtualFile file)
	{
		final boolean isCompilerOutput = isCompilerOutput(file.getUrl());
		if (isCompilerOutput)
		{
			myCompilerExtension.setExcludeOutput(true);
			return null;
		}
		return super.doAddExcludeFolder(file);
	}

	@Override
	protected void doRemoveExcludeFolder(@NotNull final String excludeRootUrl)
	{
		if (isCompilerOutput(excludeRootUrl))
		{
			myCompilerExtension.setExcludeOutput(false);
		}
		super.doRemoveExcludeFolder(excludeRootUrl);
	}

	private boolean isCompilerOutput(@NotNull final String fileUrl)
	{
		if (fileUrl.equals(myCompilerExtension.getCompilerOutputUrl()))
		{
			return true;
		}

		if (fileUrl.equals(myCompilerExtension.getCompilerOutputUrlForTests()))
		{
			return true;
		}

		if (myCompilerExtension.isCompilerOutputPathInherited())
		{
			final ProjectStructureConfigurable instance = ProjectStructureConfigurable.getInstance(getModel().getModule().getProject());
			final String compilerOutputUrl = instance.getProjectConfig().getCompilerOutputUrl();
			if (fileUrl.equals(compilerOutputUrl))
			{
				return true;
			}
		}

		return false;
	}
}
