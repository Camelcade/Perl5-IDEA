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

package com.perl5.lang.perl.idea.run;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBTabbedPane;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptionsSets;
import org.jdesktop.swingx.combobox.MapComboBoxModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hurricup on 22.06.2016.
 */
public abstract class PerlConfigurationEditorBase<Settings extends PerlDebugOptions> extends SettingsEditor<Settings>
{
	protected Project myProject;

	private JTextField myScriptCharset;
	private ComboBox myStartMode;
	private JCheckBox myIsNonInteractiveModeEnabled;
	private JCheckBox myIsCompileTimeBreakpointsEnabled;
	private EditorTextField myInitCodeTextField;

	public PerlConfigurationEditorBase(Project project)
	{
		myProject = project;
	}

	@Override
	protected void resetEditorFrom(Settings perlConfiguration)
	{
		myScriptCharset.setText(perlConfiguration.getScriptCharset());
		myIsCompileTimeBreakpointsEnabled.setSelected(perlConfiguration.isCompileTimeBreakpointsEnabled());
		myIsNonInteractiveModeEnabled.setSelected(perlConfiguration.isNonInteractiveModeEnabled());
		myInitCodeTextField.setText(perlConfiguration.getInitCode());
		myStartMode.setSelectedItem(perlConfiguration.getStartMode());
	}

	@Override
	protected void applyEditorTo(Settings perlConfiguration) throws ConfigurationException
	{
		perlConfiguration.setScriptCharset(myScriptCharset.getText());
		perlConfiguration.setStartMode(myStartMode.getSelectedItem().toString());
		perlConfiguration.setNonInteractiveModeEnabled(myIsNonInteractiveModeEnabled.isSelected());
		perlConfiguration.setCompileTimeBreakpointsEnabled(myIsCompileTimeBreakpointsEnabled.isSelected());
		perlConfiguration.setInitCode(myInitCodeTextField.getText());

	}

	@Nullable
	protected JComponent getGeneralComponent()
	{
		return null;
	}

	@Nullable
	protected JComponent getDebuggingComponent()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 5, true, false));

		myScriptCharset = new JTextField();
		LabeledComponent<JTextField> scriptCharset = LabeledComponent.create(myScriptCharset, PerlBundle.message("perl.run.option.script.encoding"));
		scriptCharset.setLabelLocation(BorderLayout.WEST);
		panel.add(scriptCharset);

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

		LabeledComponent<?> startMode = LabeledComponent.create(myStartMode, PerlBundle.message("perl.run.option.debugger.startup.mode"));
		startMode.setLabelLocation(BorderLayout.WEST);
		panel.add(startMode);

		myIsNonInteractiveModeEnabled = new JCheckBox(PerlBundle.message("perl.run.option.debugger.noninteractive.mode"));
		panel.add(myIsNonInteractiveModeEnabled);

		myIsCompileTimeBreakpointsEnabled = new JCheckBox(PerlBundle.message("perl.run.option.debugger.compile.time.breakpoints"));
		panel.add(myIsCompileTimeBreakpointsEnabled);

		PsiFile fileFromText = PsiFileFactory.getInstance(myProject).createFileFromText("file.dummy", PerlFileType.INSTANCE, "", 0, true);
		Document document = PsiDocumentManager.getInstance(myProject).getDocument(fileFromText);
		myInitCodeTextField = new EditorTextField(document, myProject, PerlFileType.INSTANCE);
		myInitCodeTextField.setOneLineMode(false);
		myInitCodeTextField.setPreferredSize(new Dimension(0, 100));
		LabeledComponent<EditorTextField> initCode = LabeledComponent.create(myInitCodeTextField, PerlBundle.message("perl.run.option.debugger.init.code"));
		initCode.setLabelLocation(BorderLayout.NORTH);
		panel.add(initCode);

		return panel;
	}


	@NotNull
	@Override
	protected JComponent createEditor()
	{
		JComponent generalComponent = getGeneralComponent();
		JComponent debuggingComponent = getDebuggingComponent();

		if (generalComponent != null && debuggingComponent != null)
		{
			JBTabbedPane tabbedPaneWrapper = new JBTabbedPane();
			tabbedPaneWrapper.addTab(PerlBundle.message("perl.run.option.tab.general"), generalComponent);
			tabbedPaneWrapper.addTab(PerlBundle.message("perl.run.option.tab.debugging"), debuggingComponent);
			return tabbedPaneWrapper;
		}
		else if (generalComponent != null)
		{
			return generalComponent;
		}
		else if (debuggingComponent != null)
		{
			return debuggingComponent;
		}

		throw new RuntimeException("No components created for settings editor");
	}
}
