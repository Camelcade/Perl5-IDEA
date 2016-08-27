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

import com.intellij.application.options.ModuleAwareProjectConfigurable;
import com.intellij.facet.impl.DefaultFacetsProvider;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.impl.ModuleConfigurationStateImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.DefaultModulesProvider;
import com.intellij.openapi.roots.ui.configuration.FacetsProvider;
import com.intellij.openapi.util.Computable;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.08.2016.
 */
public class PerlModuleAwareProjectConfigurable extends ModuleAwareProjectConfigurable<PerlContentEntriesEditor>
{
	public PerlModuleAwareProjectConfigurable(@NotNull Project project)
	{
		super(project, PerlBundle.message("perl.project.settings"), null);
	}

	@NotNull
	@Override
	protected PerlContentEntriesEditor createModuleConfigurable(final Module module)
	{
		final ModifiableRootModel myModifiableRootModel = ApplicationManager.getApplication().runReadAction(new Computable<ModifiableRootModel>()
		{

			@Override
			public ModifiableRootModel compute()
			{
				return ModuleRootManager.getInstance(module).getModifiableModel();
			}
		});

		final ModuleConfigurationStateImpl moduleConfigurationState =
				new ModuleConfigurationStateImpl(module.getProject(), new DefaultModulesProvider(module.getProject()))
				{
					@Override
					public ModifiableRootModel getRootModel()
					{
						return myModifiableRootModel;
					}

					@Override
					public FacetsProvider getFacetsProvider()
					{
						return DefaultFacetsProvider.INSTANCE;
					}
				};

		return new PerlMicroIdeContentEntriesEditor(module.getName(), moduleConfigurationState);
	}


}
