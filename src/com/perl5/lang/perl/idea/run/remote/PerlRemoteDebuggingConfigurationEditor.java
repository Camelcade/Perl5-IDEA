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
import com.intellij.ui.ColoredListCellRenderer;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptionsSets;
import org.jdesktop.swingx.combobox.MapComboBoxModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by hurricup on 09.05.2016.
 */
public class PerlRemoteDebuggingConfigurationEditor extends SettingsEditor<PerlRemoteDebuggingConfiguration>
{
	private Project myProject;

	private JTextField myWorkingDirectoryComponent;
	private JTextField myScriptCharset;
	private ComboBox myStartMode;
	private ComboBox myPerlRole;
	private JTextField myDebuggingHost;
	private JFormattedTextField myDebuggingPort;
	private JCheckBox myIsNonInteractiveModeEnabled;
	private JCheckBox myIsCompileTimeBreakpointsEnabled;


	public PerlRemoteDebuggingConfigurationEditor(Project project)
	{
		myProject = project;
	}

	@Override
	protected void resetEditorFrom(PerlRemoteDebuggingConfiguration perlConfiguration)
	{
		myWorkingDirectoryComponent.setText(perlConfiguration.getRemoteProjectRoot());
		myStartMode.setSelectedItem(perlConfiguration.getStartMode());
		myPerlRole.setSelectedItem(perlConfiguration.getPerlRole());
		myDebuggingHost.setText(perlConfiguration.getDebugHost());
		myDebuggingPort.setText(String.valueOf(perlConfiguration.getDebugPort()));
		myScriptCharset.setText(perlConfiguration.getScriptCharset());
		myIsCompileTimeBreakpointsEnabled.setSelected(perlConfiguration.isCompileTimeBreakpointsEnabled());
		myIsNonInteractiveModeEnabled.setSelected(perlConfiguration.isNonInteractiveModeEnabled());
	}

	@Override
	protected void applyEditorTo(PerlRemoteDebuggingConfiguration perlConfiguration) throws ConfigurationException
	{
		perlConfiguration.setRemoteProjectRoot(myWorkingDirectoryComponent.getText());
		perlConfiguration.setDebugHost(myDebuggingHost.getText());
		String debuggingPort = myDebuggingPort.getText();
		if (StringUtil.isNotEmpty(debuggingPort))
		{
			perlConfiguration.setDebugPort(Integer.parseInt(myDebuggingPort.getText()));
		}
		perlConfiguration.setScriptCharset(myScriptCharset.getText());
		perlConfiguration.setPerlRole(myPerlRole.getSelectedItem().toString());
		perlConfiguration.setStartMode(myStartMode.getSelectedItem().toString());
		perlConfiguration.setNonInteractiveModeEnabled(myIsNonInteractiveModeEnabled.isSelected());
		perlConfiguration.setCompileTimeBreakpointsEnabled(myIsCompileTimeBreakpointsEnabled.isSelected());
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
				myWorkingDirectoryComponent = new JTextField();
				LabeledComponent<JTextField> workingDirectory = LabeledComponent.create(myWorkingDirectoryComponent, PerlBundle.message("perl.run.option.remote.root"));
				workingDirectory.setLabelLocation(BorderLayout.WEST);
				add(workingDirectory);

				myScriptCharset = new JTextField();
				LabeledComponent<JTextField> scriptCharset = LabeledComponent.create(myScriptCharset, PerlBundle.message("perl.run.option.script.encoding"));
				scriptCharset.setLabelLocation(BorderLayout.WEST);
				add(scriptCharset);

				//noinspection Since15
				myStartMode = new ComboBox(new MapComboBoxModel<String, String>(PerlDebugOptionsSets.STARTUP_OPTIONS))
				{
					@Override
					public void setRenderer(ListCellRenderer renderer)
					{
						super.setRenderer(new ColoredListCellRenderer<String>()
						{
							@Override
							protected void customizeCellRenderer(JList list, String value, int index, boolean selected, boolean hasFocus)
							{
								append(PerlDebugOptionsSets.STARTUP_OPTIONS.get(value));
							}
						});
					}
				};
				;
				LabeledComponent<?> startMode = LabeledComponent.create(myStartMode, PerlBundle.message("perl.run.option.debugger.startup.mode"));
				startMode.setLabelLocation(BorderLayout.WEST);
				add(startMode);

				myIsNonInteractiveModeEnabled = new JCheckBox(PerlBundle.message("perl.run.option.debugger.noninteractive.mode"));
				add(myIsNonInteractiveModeEnabled);

				myIsCompileTimeBreakpointsEnabled = new JCheckBox(PerlBundle.message("perl.run.option.debugger.compile.time.breakpoints"));
				add(myIsCompileTimeBreakpointsEnabled);


				//noinspection Since15
				myPerlRole = new ComboBox(new MapComboBoxModel<String, String>(PerlDebugOptionsSets.ROLE_OPTIONS))
				{
					@Override
					public void setRenderer(ListCellRenderer renderer)
					{
						super.setRenderer(new ColoredListCellRenderer<String>()
						{
							@Override
							protected void customizeCellRenderer(JList list, String value, int index, boolean selected, boolean hasFocus)
							{
								append(PerlDebugOptionsSets.ROLE_OPTIONS.get(value));
							}
						});
					}
				};
				;
				LabeledComponent<?> perlRole = LabeledComponent.create(myPerlRole, PerlBundle.message("perl.run.option.debugger.connection.mode"));
				perlRole.setLabelLocation(BorderLayout.WEST);
				add(perlRole);

				myDebuggingHost = new JTextField();
				LabeledComponent<JTextField> debuggingHost = LabeledComponent.create(myDebuggingHost, PerlBundle.message("perl.run.option.debugger.host"));
				debuggingHost.setLabelLocation(BorderLayout.WEST);
				add(debuggingHost);

				NumberFormat numberFormat = NumberFormat.getInstance();
				numberFormat.setMaximumIntegerDigits(6);
				numberFormat.setGroupingUsed(false);

				NumberFormatter formatter = new NumberFormatter(numberFormat);
				formatter.setAllowsInvalid(false);
				formatter.setMaximum(65535);
				formatter.setMinimum(0);

				myDebuggingPort = new JFormattedTextField(formatter);
				LabeledComponent<JFormattedTextField> debuggingPort = LabeledComponent.create(myDebuggingPort, PerlBundle.message("perl.run.option.debugger.port"));
				debuggingPort.setLabelLocation(BorderLayout.WEST);
				add(debuggingPort);
			}
		};
	}
}
