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

package com.perl5.lang.perl.idea.run;

import com.intellij.execution.ui.CommonProgramParametersPanel;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.perl5.lang.perl.PerlFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @author VISTALL
 * @since 16-Sep-15
 */
public class PerlConfigurationEditor extends SettingsEditor<PerlConfiguration>
{
	private TextFieldWithBrowseButton myScriptField;
	private CommonProgramParametersPanel myParametersPanel;
	private Project myProject;

	public PerlConfigurationEditor(Project project)
	{
		myProject = project;
	}

	@Override
	protected void resetEditorFrom(PerlConfiguration perlConfiguration)
	{
		myScriptField.setText(perlConfiguration.getScriptPath());
		myParametersPanel.reset(perlConfiguration);
	}

	@Override
	protected void applyEditorTo(PerlConfiguration perlConfiguration) throws ConfigurationException
	{
		perlConfiguration.setScriptPath(myScriptField.getText());
		myParametersPanel.applyTo(perlConfiguration);
	}

	@NotNull
	@Override
	protected JComponent createEditor()
	{
		myScriptField = new TextFieldWithBrowseButton();
		myScriptField.addBrowseFolderListener("Select Perl Script", "Please select perl script file", myProject, FileChooserDescriptorFactory.createSingleFileDescriptor(PerlFileType.INSTANCE), TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);

		myParametersPanel = new CommonProgramParametersPanel()
		{
			@Override
			protected void addComponents()
			{
				LabeledComponent<TextFieldWithBrowseButton> scriptLabel = LabeledComponent.create(myScriptField, "Script");
				scriptLabel.setLabelLocation(BorderLayout.WEST);
				add(scriptLabel);
				super.addComponents();
			}
		};
		return myParametersPanel;
	}
}
