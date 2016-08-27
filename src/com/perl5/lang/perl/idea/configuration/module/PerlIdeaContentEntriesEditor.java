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

import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hurricup on 27.08.2016.
 */
public class PerlIdeaContentEntriesEditor extends PerlContentEntriesEditor
{
	private static final JpsModuleSourceRootType<?>[] ourRootTypes = new JpsModuleSourceRootType[]{
			JavaSourceRootType.SOURCE, JavaSourceRootType.TEST_SOURCE, JpsPerlLibrarySourceRootType.INSTANCE
	};

	public PerlIdeaContentEntriesEditor(String moduleName, ModuleConfigurationState state)
	{
		super(moduleName, state, ourRootTypes);
	}

	@Override
	protected void addAdditionalSettingsToPanel(JPanel mainPanel)
	{
		PerlModuleSdkConfigurable perlModuleSdkConfigurable = new PerlModuleSdkConfigurable(ProjectStructureConfigurable.getInstance(myProject).getProjectJdksModel())
		{
			@Override
			protected ModifiableRootModel getRootModel()
			{
				return getState().getRootModel();
			}
		};
		registerDisposable(perlModuleSdkConfigurable);
		mainPanel.add(perlModuleSdkConfigurable.createComponent(), BorderLayout.NORTH);
	}
}
