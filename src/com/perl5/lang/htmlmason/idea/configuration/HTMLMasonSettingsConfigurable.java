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

package com.perl5.lang.htmlmason.idea.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.FormBuilder;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by hurricup on 05.03.2016.
 */
public class HTMLMasonSettingsConfigurable extends AbstractMasonSettingsConfigurable
{
	final HTMLMasonSettings mySettings;


	public HTMLMasonSettingsConfigurable(Project myProject)
	{
		this(myProject, "HTML::Mason");
	}

	public HTMLMasonSettingsConfigurable(Project myProject, String windowTitile)
	{
		super(myProject, windowTitile);
		mySettings = HTMLMasonSettings.getInstance(myProject);
	}

	@Nullable
	@Override
	public JComponent createComponent()
	{
		FormBuilder builder = FormBuilder.createFormBuilder();
		builder.getPanel().setLayout(new VerticalFlowLayout());

		createRootsListComponent(builder);
		createGlobalsComponent(builder);

		return builder.getPanel();
	}

	@Override
	public boolean isModified()
	{
		return
				!mySettings.componentRoots.equals(rootsModel.getItems()) ||
						!mySettings.globalVariables.equals(globalsModel.getItems())
				;
	}

	@Override
	public void apply() throws ConfigurationException
	{
		mySettings.componentRoots.clear();
		mySettings.componentRoots.addAll(rootsModel.getItems());

		mySettings.globalVariables.clear();
		for (VariableDescription variableDescription : new ArrayList<VariableDescription>(globalsModel.getItems()))
		{
			if (StringUtil.isNotEmpty(variableDescription.variableName))
			{
				mySettings.globalVariables.add(variableDescription);
			}
			else
			{
				globalsModel.removeRow(globalsModel.indexOf(variableDescription));
			}
		}
		mySettings.settingsUpdated();
	}


	@Override
	public void reset()
	{
		rootsModel.removeAll();
		rootsModel.add(mySettings.componentRoots);

		globalsModel.setItems(new ArrayList<VariableDescription>());
		for (VariableDescription variableDescription : mySettings.globalVariables)
		{
			globalsModel.addRow(variableDescription.clone());
		}
	}

}
