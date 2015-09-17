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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.encoding.EncodingManager;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.TextFieldWithAutoCompletion;
import com.intellij.ui.TextFieldWithAutoCompletionListProvider;
import com.perl5.lang.perl.PerlFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * @author VISTALL
 * @since 16-Sep-15
 */
public class PerlConfigurationEditor extends SettingsEditor<PerlConfiguration>
{
	private TextFieldWithBrowseButton myScriptField;
	private TextFieldWithAutoCompletion<Charset> myCharsetField;
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
		myCharsetField.setText(perlConfiguration.getCharset());
	}

	@Override
	protected void applyEditorTo(PerlConfiguration perlConfiguration) throws ConfigurationException
	{
		perlConfiguration.setScriptPath(myScriptField.getText());
		myParametersPanel.applyTo(perlConfiguration);
		perlConfiguration.setCharset(StringUtil.nullize(myCharsetField.getText(), true));
	}

	@NotNull
	@Override
	protected JComponent createEditor()
	{
		myScriptField = new TextFieldWithBrowseButton();
		myScriptField.addBrowseFolderListener("Select Perl Script", "Please select perl script file", myProject, FileChooserDescriptorFactory.createSingleFileDescriptor(PerlFileType.INSTANCE), TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);

		Collection<Charset> favorites = EncodingManager.getInstance().getFavorites();
		myCharsetField = new TextFieldWithAutoCompletion<Charset>(myProject, new TextFieldWithAutoCompletionListProvider<Charset>(favorites)
		{
			@Nullable
			@Override
			protected Icon getIcon(@NotNull Charset charset)
			{
				return null;
			}

			@NotNull
			@Override
			protected String getLookupString(@NotNull Charset charset)
			{
				return charset.displayName();
			}

			@Nullable
			@Override
			protected String getTailText(@NotNull Charset charset)
			{
				return null;
			}

			@Nullable
			@Override
			protected String getTypeText(@NotNull Charset charset)
			{
				return null;
			}

			@Override
			public int compare(Charset t1, Charset t2)
			{
				return t1.displayName().compareTo(t2.displayName());
			}
		}, false, null);

		myScriptField.getTextField().getDocument().addDocumentListener(new DocumentAdapter()
		{
			@Override
			protected void textChanged(DocumentEvent documentEvent)
			{
				VirtualFile file = LocalFileSystem.getInstance().findFileByPath(myScriptField.getText());
				if(file != null)
				{
					myCharsetField.setPlaceholder(file.getCharset().displayName());
				}
				else
				{
					myCharsetField.setPlaceholder(null);
				}
			}
		});

		myCharsetField.setPlaceholder("file encoding");

		myParametersPanel = new CommonProgramParametersPanel()
		{
			@Override
			protected void addComponents()
			{
				LabeledComponent<TextFieldWithBrowseButton> scriptLabel = LabeledComponent.create(myScriptField, "Script");
				scriptLabel.setLabelLocation(BorderLayout.WEST);
				add(scriptLabel);
				LabeledComponent<TextFieldWithAutoCompletion<Charset>> consoleEncoding = LabeledComponent.create(myCharsetField, "Console Encoding");
				consoleEncoding.setLabelLocation(BorderLayout.WEST);
				add(consoleEncoding);
				super.addComponents();
			}
		};
		return myParametersPanel;
	}
}
