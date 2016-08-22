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

package com.perl5.lang.perl.idea.configuration.paths;

import com.intellij.facet.impl.DefaultFacetsProvider;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.impl.ModuleConfigurationStateImpl;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.CommonContentEntriesEditor;
import com.intellij.openapi.roots.ui.configuration.DefaultModulesProvider;
import com.intellij.openapi.roots.ui.configuration.FacetsProvider;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import javax.swing.*;

/**
 * Created by hurricup on 17.08.2016.
 */
public class PerlModuleConfigurationState extends ModuleConfigurationStateImpl implements Disposable
{
	private final Module myModule;
	private final ModifiableRootModel myModifiableModel;
	private final CommonContentEntriesEditor myEditor;

	public PerlModuleConfigurationState(@NotNull Module module, @NotNull ModulesProvider provider, JpsModuleSourceRootType<?>[] rootTypes)
	{
		super(module.getProject(), provider);
		myModule = module;

		myModifiableModel = ApplicationManager.getApplication().runReadAction(new Computable<ModifiableRootModel>()
		{

			@Override
			public ModifiableRootModel compute()
			{
				return ModuleRootManager.getInstance(myModule).getModifiableModel();
			}
		});

		final ModuleConfigurationStateImpl moduleConfigurationState =
				new ModuleConfigurationStateImpl(myModule.getProject(), new DefaultModulesProvider(myModule.getProject()))
				{
					@Override
					public ModifiableRootModel getRootModel()
					{
						return myModifiableModel;
					}

					@Override
					public FacetsProvider getFacetsProvider()
					{
						return DefaultFacetsProvider.INSTANCE;
					}
				};
		myEditor = new PerlContentEntriesEditor(myModule.getName(), moduleConfigurationState, rootTypes)
		{
			@Override
			protected java.util.List<ContentEntry> addContentEntries(VirtualFile[] files)
			{
				java.util.List<ContentEntry> entries = super.addContentEntries(files);
				addContentEntryPanels(entries.toArray(new ContentEntry[entries.size()]));
				return entries;
			}
		};
	}

	public Module getModule()
	{
		return myModule;
	}

	public ModifiableRootModel getModifiableModel()
	{
		return myModifiableModel;
	}

	@NotNull
	public CommonContentEntriesEditor getEditor()
	{
		return myEditor;
	}

	@NotNull
	public JComponent getEditorComponent()
	{
		return getEditor().getComponent();
	}

	public void apply() throws ConfigurationException
	{
		getEditor().apply();
		final ModifiableRootModel modifiableModel = getModifiableModel();
		if (modifiableModel.isChanged())
		{
			ApplicationManager.getApplication().runWriteAction(new Runnable()
			{
				@Override
				public void run()
				{
					modifiableModel.commit();
				}
			});
		}
	}

	public void reset()
	{
		getEditor().reset();
	}

	@Override
	public void dispose()
	{
		if (myEditor != null)
		{
			myEditor.disposeUIResources();
		}
		if (myModifiableModel != null && !myModifiableModel.isDisposed())
		{
			myModifiableModel.dispose();
		}
	}
}
