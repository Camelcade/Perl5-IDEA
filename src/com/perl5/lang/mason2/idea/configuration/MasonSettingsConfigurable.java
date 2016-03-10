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

package com.perl5.lang.mason2.idea.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.perl5.lang.htmlmason.idea.configuration.AbstractMasonSettingsConfigurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by hurricup on 03.01.2016.
 */
public class MasonSettingsConfigurable extends AbstractMasonSettingsConfigurable
{
	protected final MasonSettings mySettings;

	protected CollectionListModel<String> autobaseModel;
	protected JBList autobaseList;

	public MasonSettingsConfigurable(Project myProject)
	{
		this(myProject, "Mason2");
	}

	public MasonSettingsConfigurable(Project myProject, String windowTitile)
	{
		super(myProject, windowTitile);
		mySettings = MasonSettings.getInstance(myProject);
	}


	@Nullable
	@Override
	public JComponent createComponent()
	{
		FormBuilder builder = FormBuilder.createFormBuilder();
		builder.getPanel().setLayout(new VerticalFlowLayout());

		createRootsListComponent(builder);
		createGlobalsComponent(builder);
		createAutobaseNamesComponent(builder);

		return builder.getPanel();
	}

	@Override
	public boolean isModified()
	{
		return
				!mySettings.componentRoots.equals(rootsModel.getItems()) ||
						!mySettings.globalVariables.equals(globalsModel.getItems()) ||
						!mySettings.autobaseNames.equals(autobaseModel.getItems())
				;
	}

	@Override
	public void apply() throws ConfigurationException
	{
		mySettings.componentRoots.clear();
		mySettings.componentRoots.addAll(rootsModel.getItems());

		mySettings.autobaseNames.clear();
		mySettings.autobaseNames.addAll(autobaseModel.getItems());

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

		autobaseModel.removeAll();
		autobaseModel.add(mySettings.autobaseNames);

		globalsModel.setItems(new ArrayList<VariableDescription>());
		for (VariableDescription variableDescription : mySettings.globalVariables)
		{
			globalsModel.addRow(variableDescription.clone());
		}
	}

	@Override
	public void disposeUIResources()
	{
		super.disposeUIResources();
		autobaseList = null;
		autobaseModel = null;
	}

	protected void createAutobaseNamesComponent(FormBuilder builder)
	{
		autobaseModel = new CollectionListModel<String>();
		autobaseList = new JBList(autobaseModel);
		builder.addLabeledComponent(new JLabel("Autobase names (autobase_names option. Order is important, later components may be inherited from early):"), ToolbarDecorator
				.createDecorator(autobaseList)
				.setAddAction(new AnActionButtonRunnable()
				{
					@Override
					public void run(AnActionButton anActionButton)
					{
						String fileName = Messages.showInputDialog(
								myProject,
								"Type new Autobase filename:",
								"New Autobase Filename",
								Messages.getQuestionIcon(),
								"",
								null);
						if (StringUtil.isNotEmpty(fileName) && !autobaseModel.getItems().contains(fileName))
						{
							autobaseModel.add(fileName);
						}
					}
				})
				.setPreferredSize(JBUI.size(0, 100))
				.createPanel());
	}

}
