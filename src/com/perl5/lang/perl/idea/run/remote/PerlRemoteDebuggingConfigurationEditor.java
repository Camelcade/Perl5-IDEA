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

package com.perl5.lang.perl.idea.run.remote;

import com.intellij.execution.ui.CommonProgramParametersPanel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.CollectionComboBoxModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by hurricup on 09.05.2016.
 */
public class PerlRemoteDebuggingConfigurationEditor extends SettingsEditor<PerlRemoteDebuggingConfiguration>
{
	private Project myProject;

	private LabeledComponent<JTextField> myWorkingDirectoryComponent;
	private ComboBox myScriptCharset;
	private LabeledComponent<JCheckBox> myIsPerlServer;
	private LabeledComponent<JCheckBox> myStopOnCompilation;
	private LabeledComponent<JTextField> myDebuggingHost;
	private LabeledComponent<JFormattedTextField> myDebuggingPort;

	public PerlRemoteDebuggingConfigurationEditor(Project project)
	{
		myProject = project;
	}

	@Override
	protected void resetEditorFrom(PerlRemoteDebuggingConfiguration configuration)
	{
		myWorkingDirectoryComponent.getComponent().setText(configuration.getRemoteProjectRoot());
		myIsPerlServer.getComponent().setSelected(configuration.isPerlServer());
		myDebuggingHost.getComponent().setText(configuration.getDebugHost());
		myDebuggingPort.getComponent().setText(configuration.getDebugPort());
		myStopOnCompilation.getComponent().setSelected(configuration.isStopOnCompilation());
		myScriptCharset.setSelectedItem(configuration.getClientCharset());

	}

	@Override
	protected void applyEditorTo(PerlRemoteDebuggingConfiguration configuration) throws ConfigurationException
	{
		configuration.setRemoteProjectRoot(myWorkingDirectoryComponent.getComponent().getText());
		configuration.setPerlServer(myIsPerlServer.getComponent().isSelected());
		configuration.setDebugHost(myDebuggingHost.getComponent().getText());
		configuration.setDebugPort(myDebuggingPort.getComponent().getText());
		configuration.setStopOnCompilation(myStopOnCompilation.getComponent().isSelected());
		configuration.setClientCharset(StringUtil.nullize((String) myScriptCharset.getSelectedItem(), true));
	}

	@NotNull
	@Override
	protected JComponent createEditor()
	{
		return new CommonProgramParametersPanel()
		{

			@Override
			protected void addComponents()
			{
				myWorkingDirectoryComponent = LabeledComponent.create(new JTextField(), "Remote project root");
				myWorkingDirectoryComponent.setLabelLocation(BorderLayout.WEST);
				add(myWorkingDirectoryComponent);

				myScriptCharset = new ComboBox(new CollectionComboBoxModel(new ArrayList<String>(Charset.availableCharsets().keySet())));
				LabeledComponent<?> consoleEncoding = LabeledComponent.create(myScriptCharset, "Script's text encoding");
				consoleEncoding.setLabelLocation(BorderLayout.WEST);
				add(consoleEncoding);

				myStopOnCompilation = LabeledComponent.create(new JCheckBox(), "Stop debugger ASAP");
				myStopOnCompilation.setLabelLocation(BorderLayout.WEST);
				add(myStopOnCompilation);

				myIsPerlServer = LabeledComponent.create(new JCheckBox(), "Perl acts as a server");
				myIsPerlServer.setLabelLocation(BorderLayout.WEST);
				add(myIsPerlServer);

				myDebuggingHost = LabeledComponent.create(new JTextField(), "Server Hostname");
				myDebuggingHost.setLabelLocation(BorderLayout.WEST);
				add(myDebuggingHost);

				NumberFormat numberFormat = NumberFormat.getInstance();
				numberFormat.setMaximumIntegerDigits(6);
				numberFormat.setGroupingUsed(false);

				NumberFormatter formatter = new NumberFormatter(numberFormat);
				formatter.setAllowsInvalid(false);
				formatter.setMaximum(65535);
				formatter.setMinimum(0);

				myDebuggingPort = LabeledComponent.create(new JFormattedTextField(formatter), "Server Port");
				myDebuggingPort.setLabelLocation(BorderLayout.WEST);
				add(myDebuggingPort);
			}
		};
	}
}
