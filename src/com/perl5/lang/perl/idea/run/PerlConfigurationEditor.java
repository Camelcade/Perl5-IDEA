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
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.RawCommandLineEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * @author VISTALL
 * @since 16-Sep-15
 */
public class PerlConfigurationEditor extends SettingsEditor<PerlConfiguration>
{
	private TextFieldWithBrowseButton myScriptField;
	private ComboBox myConsoleCharset;
	private CommonProgramParametersPanel myParametersPanel;
	private LabeledComponent<RawCommandLineEditor> myPerlParametersPanel;
	private PerlAlternativeSdkPanel myAlternativeSdkPanel;
	private Project myProject;
	private LabeledComponent<JCheckBox> myStopOnCompilation;
	private LabeledComponent<JTextField> myScriptCharset;

	public PerlConfigurationEditor(Project project)
	{
		myProject = project;
	}

	@Override
	protected void resetEditorFrom(PerlConfiguration perlConfiguration)
	{
		myScriptField.setText(perlConfiguration.getScriptPath());
		myParametersPanel.reset(perlConfiguration);
		myConsoleCharset.setSelectedItem(perlConfiguration.getConsoleCharset());
		myAlternativeSdkPanel.reset(perlConfiguration.getAlternativeSdkPath(), perlConfiguration.isUseAlternativeSdk());
		myPerlParametersPanel.getComponent().setText(perlConfiguration.getPERL_PARAMETERS());
		myStopOnCompilation.getComponent().setSelected(perlConfiguration.isStopOnCompilation());
		myScriptCharset.getComponent().setText(perlConfiguration.getScriptCharset());
	}

	@Override
	protected void applyEditorTo(PerlConfiguration perlConfiguration) throws ConfigurationException
	{
		perlConfiguration.setPERL_PARAMETERS(myPerlParametersPanel.getComponent().getText());
		perlConfiguration.setScriptPath(myScriptField.getText());
		myParametersPanel.applyTo(perlConfiguration);
		perlConfiguration.setConsoleCharset(StringUtil.nullize((String) myConsoleCharset.getSelectedItem(), true));
		perlConfiguration.setAlternativeSdkPath(myAlternativeSdkPanel.getPath());
		perlConfiguration.setUseAlternativeSdk(myAlternativeSdkPanel.isPathEnabled());
		perlConfiguration.setStopOnCompilation(myStopOnCompilation.getComponent().isSelected());
		perlConfiguration.setScriptCharset(myScriptCharset.getComponent().getText());
	}

	@NotNull
	@Override
	protected JComponent createEditor()
	{
		myScriptField = new TextFieldWithBrowseButton();
		myScriptField.addBrowseFolderListener("Select Perl Script", "Please select perl script file", myProject, FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor().withFileFilter(new Condition<VirtualFile>()
		{
			@Override
			public boolean value(VirtualFile virtualFile)
			{
				return PerlConfigurationProducer.isExecutableFile(virtualFile);
			}
		}), TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);

		myConsoleCharset = new ComboBox(new CollectionComboBoxModel(new ArrayList<String>(Charset.availableCharsets().keySet())));

		myScriptField.getTextField().getDocument().addDocumentListener(new DocumentAdapter()
		{
			@Override
			protected void textChanged(DocumentEvent documentEvent)
			{
				VirtualFile file = LocalFileSystem.getInstance().findFileByPath(myScriptField.getText());
				if (file != null)
				{
					myConsoleCharset.setSelectedItem(file.getCharset().displayName());
				}
				else
				{
					myConsoleCharset.setSelectedItem(null);
				}
			}
		});

		myAlternativeSdkPanel = new PerlAlternativeSdkPanel();

		myParametersPanel = new CommonProgramParametersPanel()
		{
			@Override
			protected void addComponents()
			{
				LabeledComponent<?> scriptLabel = LabeledComponent.create(myScriptField, "Script");
				scriptLabel.setLabelLocation(BorderLayout.WEST);
				add(scriptLabel);

				LabeledComponent<?> consoleEncoding = LabeledComponent.create(myConsoleCharset, "Script output encoding");
				consoleEncoding.setLabelLocation(BorderLayout.WEST);
				add(consoleEncoding);

				myScriptCharset = LabeledComponent.create(new JTextField(), "Text scalars encoding");
				myScriptCharset.setLabelLocation(BorderLayout.WEST);
				add(myScriptCharset);

				myStopOnCompilation = LabeledComponent.create(new JCheckBox(), "Stop debugger ASAP");
				myStopOnCompilation.setLabelLocation(BorderLayout.WEST);
				add(myStopOnCompilation);

				myPerlParametersPanel = LabeledComponent.create(new RawCommandLineEditor(), "Perl5 arguments");
				myPerlParametersPanel.setLabelLocation(BorderLayout.WEST);
//				add(myPerlParametersPanel);

				super.addComponents();
				add(myAlternativeSdkPanel);
			}
		};
		return myParametersPanel;
	}
}
