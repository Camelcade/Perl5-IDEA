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

package com.perl5.lang.perl.idea.configuration.module;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.impl.RootModelImpl;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import java.util.List;

/**
 * Created by hurricup on 27.08.2016.
 */
public class PerlMicroIdeContentEntriesEditor extends PerlContentEntriesEditor
{
	private static final JpsModuleSourceRootType<?>[] ourRootTypes = new JpsModuleSourceRootType[]{
			JavaSourceRootType.SOURCE,
			JavaSourceRootType.TEST_SOURCE,
			JpsPerlLibrarySourceRootType.INSTANCE
	};

	public PerlMicroIdeContentEntriesEditor(String moduleName, ModuleConfigurationState state)
	{
		super(moduleName, state, ourRootTypes);
	}

	@Override
	public void apply() throws ConfigurationException
	{
		final ModifiableRootModel model = getModel();
		if (model.isChanged())
		{
			ApplicationManager.getApplication().runWriteAction(new Runnable()
			{
				@Override
				public void run()
				{
					((RootModelImpl) model).docommit();
				}
			});
		}
		super.apply();
	}

	@Override
	protected List<ContentEntry> addContentEntries(VirtualFile[] files)
	{
		List<ContentEntry> entries = super.addContentEntries(files);
		addContentEntryPanels(entries.toArray(new ContentEntry[entries.size()]));
		return entries;
	}

	@Override
	public void reset()
	{
		super.reset();
	}

	@Override
	public void disposeUIResources()
	{
		super.disposeUIResources();
		ModifiableRootModel rootModel = getState().getRootModel();
		if (rootModel.isWritable() && !rootModel.isDisposed())
		{
			rootModel.dispose();
		}
	}

}
