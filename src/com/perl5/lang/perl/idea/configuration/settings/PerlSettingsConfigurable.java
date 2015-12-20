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

package com.perl5.lang.perl.idea.configuration.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PlatformUtils;
import com.intellij.util.ui.FormBuilder;
import com.perl5.lang.perl.idea.project.PerlMicroIdeSettingsLoader;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.settings.Perl5Settings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 30.08.2015.
 */
public class PerlSettingsConfigurable implements Configurable
{
	Project myProject;
	Perl5Settings mySettings;
	TextFieldWithBrowseButton perlPathInputField;
	JCheckBox simpleMainCheckbox;

	private PerlSettingsConfigurable()
	{
	}

	public PerlSettingsConfigurable(Project myProject)
	{
		this.myProject = myProject;
		mySettings = Perl5Settings.getInstance(myProject);
	}

	@Nls
	@Override
	public String getDisplayName()
	{
		return "Perl5 settings";
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

		if (!PlatformUtils.isIntelliJ())
		{
			createMicroIdeComponents(builder);
		}

		simpleMainCheckbox = new JCheckBox("Use simple main:: subs resolution (many scripts with same named subs in main:: namespace)");
		simpleMainCheckbox.setSelected(mySettings.SIMPLE_MAIN_RESOLUTION);
		builder.addComponent(simpleMainCheckbox);

		return builder.getPanel();
	}

	protected void createMicroIdeComponents(FormBuilder builder)
	{
		perlPathInputField = new TextFieldWithBrowseButton();
		perlPathInputField.setText(mySettings.perlPath);

		FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false)
		{
			@Override
			public void validateSelectedFiles(VirtualFile[] files) throws Exception
			{
				PerlSdkType sdkType = PerlSdkType.getInstance();
				if (!sdkType.isValidSdkHome(files[0].getCanonicalPath()))
					throw new ConfigurationException("Unable to locate perl5 executable");
			}
		};

		perlPathInputField.addBrowseFolderListener(
				"Perl5 Interpreter",
				"Choose a directory with perl5 executable:",
				null, // project
				descriptor
		);

		builder.addLabeledComponent("Perl5 interpreter path: ", perlPathInputField, 1);
	}

	@Override
	public boolean isModified()
	{
		return isMicroIdeModified() ||
				mySettings.SIMPLE_MAIN_RESOLUTION != simpleMainCheckbox.isSelected();
	}

	protected boolean isMicroIdeModified()
	{
		return !PlatformUtils.isIntelliJ() &&
				(
						!mySettings.perlPath.equals(perlPathInputField.getText())
				);
	}

	@Override
	public void apply() throws ConfigurationException
	{
		mySettings.SIMPLE_MAIN_RESOLUTION = simpleMainCheckbox.isSelected();

		if (!PlatformUtils.isIntelliJ())
		{
			applyMicroIdeSettings();
		}
	}

	public void applyMicroIdeSettings()
	{
		mySettings.perlPath = perlPathInputField.getText();

		ApplicationManager.getApplication().runWriteAction(
				new Runnable()
				{
					@Override
					public void run()
					{
						ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(ModuleManager.getInstance(myProject).getModules()[0]).getModifiableModel();
						PerlMicroIdeSettingsLoader.applyClassPaths(modifiableModel);
						modifiableModel.commit();
					}
				}
		);
	}

	@Override
	public void reset()
	{

	}

	@Override
	public void disposeUIResources()
	{
		perlPathInputField = null;
		simpleMainCheckbox = null;
	}
}
