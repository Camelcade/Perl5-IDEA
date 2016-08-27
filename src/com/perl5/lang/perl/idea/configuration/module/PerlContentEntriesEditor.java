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

import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ui.configuration.CommonContentEntriesEditor;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.TabbedPaneWrapper;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import javax.swing.*;

/**
 * Created by hurricup on 07.06.2015.
 */
public abstract class PerlContentEntriesEditor extends CommonContentEntriesEditor
{
	public PerlContentEntriesEditor(String moduleName, ModuleConfigurationState state, JpsModuleSourceRootType<?>... rootTypes)
	{
		super(moduleName, state, rootTypes);
	}

	@Override
	public void apply() throws ConfigurationException
	{
		// fixme here we should remove classroots from reset and add new ones;
		// fixme also, we should serialize our library roots
//		PerlLibUtil.updatePerlLibsForModel(getModel());
	}

	@Override
	public void reset()
	{
		// fixme here we should remember library roots
		super.reset();
	}

	@Override
	public JPanel createComponentImpl()
	{

		JPanel componentImpl = super.createComponentImpl();

		Disposable disposable = Disposer.newDisposable();
		registerDisposable(disposable);
		TabbedPaneWrapper tabbedPaneWrapper = new TabbedPaneWrapper(disposable);
		tabbedPaneWrapper.addTab("Structure", componentImpl);

		return (JPanel) tabbedPaneWrapper.getComponent();
	}

}
