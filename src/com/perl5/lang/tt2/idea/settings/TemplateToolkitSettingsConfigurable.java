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

package com.perl5.lang.tt2.idea.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.FormBuilder;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.util.PerlConfigurationUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 05.06.2016.
 */
public class TemplateToolkitSettingsConfigurable implements Configurable
{
	private final TemplateToolkitSettings mySettings;
	private final Project myProject;
	@SuppressWarnings("Since15")
	protected CollectionListModel<String> rootsModel;
	protected JBList rootsList;
	@SuppressWarnings("Since15")
	protected CollectionListModel<String> substitutedExtensionsModel;
	protected JBList substitutedExtensionsList;
	protected JPanel substitutedExtensionsPanel;
	private JTextField startTagField;
	private JTextField endTagField;
	private JTextField outlineTagField;
	private JCheckBox enableAnycaseCheckBox;
	private JCheckBox enableRelativePathsCheckBox;


	public TemplateToolkitSettingsConfigurable(Project project)
	{
		mySettings = TemplateToolkitSettings.getInstance(project);
		myProject = project;
	}

	@Nls
	@Override
	public String getDisplayName()
	{
		return "Template Toolkit 2";
	}

	@Nullable
	@Override
	public String getHelpTopic()
	{
		return null;
	}

	@Nullable
	@Override
	public JComponent createComponent()
	{
		FormBuilder builder = FormBuilder.createFormBuilder();
		builder.getPanel().setLayout(new VerticalFlowLayout());

		builder.addLabeledComponent(
				PerlBundle.message("ttk2.label.opentag"),
				startTagField = new JTextField()
		);

		builder.addLabeledComponent(
				PerlBundle.message("ttk2.label.closetag"),
				endTagField = new JTextField()
		);

		builder.addLabeledComponent(
				PerlBundle.message("ttk2.label.outlinetag"),
				outlineTagField = new JTextField()
		);

		builder.addComponent(
				enableAnycaseCheckBox = new JCheckBox(PerlBundle.message("ttk2.label.enableanycase"))
		);

		builder.addComponent(
				enableRelativePathsCheckBox = new JCheckBox(PerlBundle.message("ttk2.label.enablerelative"))
		);

		//noinspection Since15
		rootsModel = new CollectionListModel<String>();
		rootsList = new JBList(rootsModel);
		builder.addLabeledComponent(
				PerlBundle.message("ttk2.configuration.template.dirs.label"),
				PerlConfigurationUtil.createProjectPathsSelection(
						myProject,
						rootsList,
						rootsModel,
						PerlBundle.message("ttk2.configuration.choose.template.dir.title")
				));


		//noinspection Since15
		substitutedExtensionsModel = new CollectionListModel<String>();
		substitutedExtensionsList = new JBList(substitutedExtensionsModel);
		substitutedExtensionsPanel = PerlConfigurationUtil.createSubstituteExtensionPanel(substitutedExtensionsModel, substitutedExtensionsList);
		builder.addLabeledComponent(new JLabel(PerlBundle.message("ttk2.configuration.extension")), substitutedExtensionsPanel);

		return builder.getPanel();
	}

	@Override
	public boolean isModified()
	{
		return mySettings.ENABLE_ANYCASE != enableAnycaseCheckBox.isSelected() ||
				mySettings.ENABLE_RELATIVE != enableAnycaseCheckBox.isSelected() ||
				!StringUtil.equals(mySettings.START_TAG, startTagField.getText()) ||
				!StringUtil.equals(mySettings.END_TAG, endTagField.getText()) ||
				!StringUtil.equals(mySettings.OUTLINE_TAG, outlineTagField.getText()) ||
				!mySettings.substitutedExtensions.equals(substitutedExtensionsModel.getItems()) ||
				!mySettings.TEMPLATE_DIRS.equals(rootsModel.getItems())
				;
	}

	@Override
	public void apply() throws ConfigurationException
	{
		if (StringUtil.isEmpty(startTagField.getText()))
		{
			throw new ConfigurationException(PerlBundle.message("ttk2.error.emptyopentag"));
		}
		if (StringUtil.isEmpty(endTagField.getText()))
		{
			throw new ConfigurationException(PerlBundle.message("ttk2.error.emptyclosetag"));
		}

		mySettings.START_TAG = startTagField.getText();
		mySettings.END_TAG = endTagField.getText();
		mySettings.OUTLINE_TAG = outlineTagField.getText();
		mySettings.ENABLE_ANYCASE = enableAnycaseCheckBox.isSelected();
		mySettings.ENABLE_RELATIVE = enableRelativePathsCheckBox.isSelected();

		mySettings.TEMPLATE_DIRS.clear();
		mySettings.TEMPLATE_DIRS.addAll(rootsModel.getItems());

		mySettings.substitutedExtensions.clear();
		mySettings.substitutedExtensions.addAll(substitutedExtensionsModel.getItems());

		mySettings.settingsUpdated();
	}

	@Override
	public void reset()
	{
		startTagField.setText(mySettings.START_TAG);
		endTagField.setText(mySettings.END_TAG);
		outlineTagField.setText(mySettings.OUTLINE_TAG);
		enableAnycaseCheckBox.setSelected(mySettings.ENABLE_ANYCASE);
		enableRelativePathsCheckBox.setSelected(mySettings.ENABLE_RELATIVE);
		rootsModel.removeAll();
		rootsModel.add(mySettings.TEMPLATE_DIRS);
		substitutedExtensionsModel.removeAll();
		substitutedExtensionsModel.add(mySettings.substitutedExtensions);
	}

	@Override
	public void disposeUIResources()
	{
	}
}
