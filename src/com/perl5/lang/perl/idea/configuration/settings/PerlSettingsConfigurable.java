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

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.util.FileContentUtil;
import com.intellij.util.PlatformUtils;
import com.intellij.util.ui.FormBuilder;
import com.perl5.lang.perl.idea.actions.PerlFormatWithPerlTidyAction;
import com.perl5.lang.perl.idea.annotators.PerlCriticAnnotator;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.util.PerlLibUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import com.perl5.lang.perl.xsubs.PerlXSubsState;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by hurricup on 30.08.2015.
 */
public class PerlSettingsConfigurable implements Configurable
{
	Project myProject;
	PerlSharedSettings mySharedSettings;
	PerlLocalSettings myLocalSettings;
	TextFieldWithBrowseButton perlCriticPathInputField;
	RawCommandLineEditor perlCriticArgsInputField;
	TextFieldWithBrowseButton perlTidyPathInputField;
	RawCommandLineEditor perlTidyArgsInputField;
	TextFieldWithBrowseButton perlPathInputField;

	TextFieldWithBrowseButton applicationExternalAnnotationsPath;
	TextFieldWithBrowseButton projectExternalAnnotationsPath;

	JTextField deparseArgumentsTextField;
	JPanel regeneratePanel;
	JButton regenerateButton;
	JCheckBox simpleMainCheckbox;
	JCheckBox autoInjectionCheckbox;
	JCheckBox perlCriticCheckBox;
	JCheckBox perlTryCatchCheckBox;
	JCheckBox perlAnnotatorCheckBox;
	JCheckBox allowInjectionWithInterpolation;
	@SuppressWarnings("Since15")
	CollectionListModel<String> selfNamesModel;
	JBList selfNamesList;
	private PerlApplicationSettings myAppSettings;


	public PerlSettingsConfigurable(Project myProject)
	{
		this.myProject = myProject;
		mySharedSettings = PerlSharedSettings.getInstance(myProject);
		myLocalSettings = PerlLocalSettings.getInstance(myProject);
		myAppSettings = PerlApplicationSettings.getInstance();
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

		addAnnotationsPaths(builder);

		simpleMainCheckbox = new JCheckBox("Use simple main:: subs resolution (many scripts with same named subs in main:: namespace)");
		builder.addComponent(simpleMainCheckbox);

		autoInjectionCheckbox = new JCheckBox("Automatically inject other languages in here-docs by marker text");
		builder.addComponent(autoInjectionCheckbox);

		allowInjectionWithInterpolation = new JCheckBox("Allow injections in QQ here-docs with interpolated entities");
		builder.addComponent(allowInjectionWithInterpolation);

		perlTryCatchCheckBox = new JCheckBox("Enable TryCatch syntax extension");
		builder.addComponent(perlTryCatchCheckBox);

		perlAnnotatorCheckBox = new JCheckBox("Enable perl -cw annotations [NYI]");
//		builder.addComponent(perlAnnotatorCheckBox);

		perlCriticCheckBox = new JCheckBox("Enable Perl::Critic annotations (should be installed)");
		builder.addComponent(perlCriticCheckBox);

		perlCriticPathInputField = new TextFieldWithBrowseButton();
		perlCriticPathInputField.setEditable(false);
		FileChooserDescriptor perlCriticDescriptor = new FileChooserDescriptor(true, false, false, false, false, false)
		{
			@Override
			public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles)
			{
				if (!super.isFileVisible(file, showHiddenFiles))
				{
					return false;
				}

				return file.isDirectory() || StringUtil.equals(file.getNameWithoutExtension(), PerlCriticAnnotator.PERL_CRITIC_LINUX_NAME);
			}
		};

		perlCriticPathInputField.addBrowseFolderListener(
				"Select File",
				"Choose a Perl::Critic Executable",
				null, // project
				perlCriticDescriptor
		);
		builder.addLabeledComponent(new JLabel("Path to PerlCritic executable:"), perlCriticPathInputField);


		perlCriticArgsInputField = new RawCommandLineEditor();
		builder.addComponent(copyDialogCaption(LabeledComponent.create(perlCriticArgsInputField, "Perl::Critic command line arguments:"), "Perl::Critic command line arguments:"));

		perlTidyPathInputField = new TextFieldWithBrowseButton();
		perlTidyPathInputField.setEditable(false);
		FileChooserDescriptor perlTidyDescriptor = new FileChooserDescriptor(true, false, false, false, false, false)
		{
			@Override
			public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles)
			{
				if (!super.isFileVisible(file, showHiddenFiles))
				{
					return false;
				}

				return file.isDirectory() || StringUtil.equals(file.getNameWithoutExtension(), PerlFormatWithPerlTidyAction.PERL_TIDY_LINUX_NAME);
			}
		};

		perlTidyPathInputField.addBrowseFolderListener(
				"Select File",
				"Choose a Perl::Tidy Executable",
				null, // project
				perlTidyDescriptor
		);
		builder.addLabeledComponent(new JLabel("Path to PerlTidy executable:"), perlTidyPathInputField);
		perlTidyArgsInputField = new RawCommandLineEditor();
		builder.addComponent(
				copyDialogCaption(
						LabeledComponent.create(perlTidyArgsInputField, "Perl::Tidy command line arguments (-st -se arguments will be added automatically):"),
						"Perl::Tidy command line arguments:"
				));

		regeneratePanel = new JPanel(new BorderLayout());
		regenerateButton = new JButton("Re-generate XSubs declarations");
		regenerateButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				PerlXSubsState.getInstance(myProject).reparseXSubs();
			}
		});
		regeneratePanel.add(regenerateButton, BorderLayout.WEST);
		builder.addComponent(regeneratePanel);

		deparseArgumentsTextField = new JTextField();
		builder.addLabeledComponent("Comma-separated B::Deparse options for deparse action", deparseArgumentsTextField);

		//noinspection Since15
		selfNamesModel = new CollectionListModel<String>();
		selfNamesList = new JBList(selfNamesModel);
		builder.addLabeledComponent(new JLabel("Scalar names considered as an object self-reference (without a $):"), ToolbarDecorator
				.createDecorator(selfNamesList)
				.setAddAction(new AnActionButtonRunnable()
				{
					@Override
					public void run(AnActionButton anActionButton)
					{
						String variableName = Messages.showInputDialog(
								myProject,
								"Type variable name:",
								"New Self-Reference Variable Name",
								Messages.getQuestionIcon(),
								"",
								null);
						if (StringUtil.isNotEmpty(variableName))
						{
							while (variableName.startsWith("$"))
							{
								variableName = variableName.substring(1);
							}

							if (StringUtil.isNotEmpty(variableName) && !selfNamesModel.getItems().contains(variableName))
							{
								selfNamesModel.add(variableName);
							}
						}
					}
				}).createPanel());


		return builder.getPanel();
	}

	protected void addAnnotationsPaths(FormBuilder builder)
	{
		applicationExternalAnnotationsPath = new TextFieldWithBrowseButton();
		applicationExternalAnnotationsPath.setEditable(false);
		FileChooserDescriptor applicationLevelAnnotationsDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
		applicationExternalAnnotationsPath.addBrowseFolderListener(
				"Select Directory",
				"Choose an Application Level Annotations Directory:",
				myProject,
				applicationLevelAnnotationsDescriptor
		);
		builder.addLabeledComponent(new JLabel("Application-level annotations path:"), applicationExternalAnnotationsPath);


		projectExternalAnnotationsPath = new TextFieldWithBrowseButton();
		projectExternalAnnotationsPath.setEditable(false);
		FileChooserDescriptor projectLevelAnnotationsDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
		projectLevelAnnotationsDescriptor.setRoots(myProject.getBaseDir());

		projectExternalAnnotationsPath.addBrowseFolderListener(
				"Select Directory",
				"Choose a Project Level Annotations Directory:",
				myProject,
				projectLevelAnnotationsDescriptor,
				new TextComponentAccessor<JTextField>()
				{
					@Override
					public String getText(JTextField component)
					{
						VirtualFile baseDir = myProject.getBaseDir();
						VirtualFile annotationsPath = baseDir.findFileByRelativePath(component.getText());
						return annotationsPath == null ? baseDir.getCanonicalPath() : annotationsPath.getCanonicalPath();
					}

					@Override
					public void setText(JTextField component, @NotNull String text)
					{
						VirtualFile fileByIoFile = VfsUtil.findFileByIoFile(new File(text), true);
						String result = "";
						if (fileByIoFile != null)
						{
							result = VfsUtil.getRelativePath(fileByIoFile, myProject.getBaseDir());
						}

						component.setText(result);
					}
				}
		);
		builder.addLabeledComponent(new JLabel("Project-level annotations path:"), projectExternalAnnotationsPath);
	}

	protected void createMicroIdeComponents(FormBuilder builder)
	{
		perlPathInputField = new TextFieldWithBrowseButton()
		{

			@Override
			public void addBrowseFolderListener(@Nullable String title, @Nullable String description, @Nullable Project project, FileChooserDescriptor fileChooserDescriptor, TextComponentAccessor<JTextField> accessor, boolean autoRemoveOnHide)
			{
				addBrowseFolderListener(project, new BrowseFolderActionListener<JTextField>(title, description, this, project, fileChooserDescriptor, accessor)
				{
					@Nullable
					@Override
					protected VirtualFile getInitialFile()
					{
						VirtualFile virtualFile = super.getInitialFile();
						if (virtualFile == null)
						{
							String directoryName = PerlRunUtil.getPathFromPerl();
							if (StringUtil.isNotEmpty(directoryName))
							{
								directoryName = FileUtil.toSystemIndependentName(directoryName);
								VirtualFile path = LocalFileSystem.getInstance().findFileByPath(expandPath(directoryName));
								while (path == null && directoryName.length() > 0)
								{
									int pos = directoryName.lastIndexOf('/');
									if (pos <= 0)
									{
										break;
									}
									directoryName = directoryName.substring(0, pos);
									path = LocalFileSystem.getInstance().findFileByPath(directoryName);
								}
								return path;
							}
						}
						return virtualFile;
					}
				}, autoRemoveOnHide);
			}
		};

		perlPathInputField.getTextField().setEditable(false);

		FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false)
		{
			@Override
			public void validateSelectedFiles(VirtualFile[] files) throws Exception
			{
				PerlSdkType sdkType = PerlSdkType.getInstance();
				if (!sdkType.isValidSdkHome(files[0].getCanonicalPath()))
				{
					throw new ConfigurationException("Unable to locate perl5 executable");
				}
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
				!StringUtil.equals(applicationExternalAnnotationsPath.getText(), myAppSettings.getAnnotationsPath()) ||
				!StringUtil.equals(projectExternalAnnotationsPath.getText(), mySharedSettings.getAnnotationsPath()) ||
				mySharedSettings.SIMPLE_MAIN_RESOLUTION != simpleMainCheckbox.isSelected() ||
				mySharedSettings.AUTOMATIC_HEREDOC_INJECTIONS != autoInjectionCheckbox.isSelected() ||
				mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION != allowInjectionWithInterpolation.isSelected() ||
				mySharedSettings.PERL_ANNOTATOR_ENABLED != perlAnnotatorCheckBox.isSelected() ||
				mySharedSettings.PERL_CRITIC_ENABLED != perlCriticCheckBox.isSelected() ||
				mySharedSettings.PERL_TRY_CATCH_ENABLED != perlTryCatchCheckBox.isSelected() ||
				!StringUtil.equals(mySharedSettings.PERL_DEPARSE_ARGUMENTS, deparseArgumentsTextField.getText()) ||
				!StringUtil.equals(myLocalSettings.PERL_CRITIC_PATH, perlCriticPathInputField.getText()) ||
				!StringUtil.equals(mySharedSettings.PERL_CRITIC_ARGS, perlCriticArgsInputField.getText()) ||
				!StringUtil.equals(myLocalSettings.PERL_TIDY_PATH, perlTidyPathInputField.getText()) ||
				!StringUtil.equals(mySharedSettings.PERL_TIDY_ARGS, perlTidyArgsInputField.getText()) ||
				!mySharedSettings.selfNames.equals(selfNamesModel.getItems());
	}

	protected boolean isMicroIdeModified()
	{
		return !PlatformUtils.isIntelliJ() &&
				(
						!myLocalSettings.PERL_PATH.equals(perlPathInputField.getText())
				);
	}

	@Override
	public void apply() throws ConfigurationException
	{
		myAppSettings.setAnnotationsPath(applicationExternalAnnotationsPath.getText());
		mySharedSettings.setAnnotationsPath(projectExternalAnnotationsPath.getText());

		mySharedSettings.SIMPLE_MAIN_RESOLUTION = simpleMainCheckbox.isSelected();
		mySharedSettings.AUTOMATIC_HEREDOC_INJECTIONS = autoInjectionCheckbox.isSelected();
		mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION = allowInjectionWithInterpolation.isSelected();
		mySharedSettings.PERL_ANNOTATOR_ENABLED = perlAnnotatorCheckBox.isSelected();
		mySharedSettings.setDeparseOptions(deparseArgumentsTextField.getText());

		boolean needReparse = mySharedSettings.PERL_TRY_CATCH_ENABLED != perlTryCatchCheckBox.isSelected();
		mySharedSettings.PERL_TRY_CATCH_ENABLED = perlTryCatchCheckBox.isSelected();

		mySharedSettings.PERL_CRITIC_ENABLED = perlCriticCheckBox.isSelected();
		myLocalSettings.PERL_CRITIC_PATH = perlCriticPathInputField.getText();
		mySharedSettings.PERL_CRITIC_ARGS = perlCriticArgsInputField.getText();

		myLocalSettings.PERL_TIDY_PATH = perlTidyPathInputField.getText();
		mySharedSettings.PERL_TIDY_ARGS = perlTidyArgsInputField.getText();

		mySharedSettings.selfNames.clear();
		mySharedSettings.selfNames.addAll(selfNamesModel.getItems());

		if (!PlatformUtils.isIntelliJ())
		{
			applyMicroIdeSettings();
		}
		PerlLibUtil.applyClassPaths(myProject);
		mySharedSettings.settingsUpdated();

		if (needReparse)
		{
			FileContentUtil.reparseOpenedFiles();
		}
	}

	public void applyMicroIdeSettings()
	{
		myLocalSettings.PERL_PATH = perlPathInputField.getText();
	}

	@Override
	public void reset()
	{
		selfNamesModel.removeAll();
		selfNamesModel.add(mySharedSettings.selfNames);

		applicationExternalAnnotationsPath.setText(myAppSettings.getAnnotationsPath());
		projectExternalAnnotationsPath.setText(mySharedSettings.getAnnotationsPath());

		simpleMainCheckbox.setSelected(mySharedSettings.SIMPLE_MAIN_RESOLUTION);
		autoInjectionCheckbox.setSelected(mySharedSettings.AUTOMATIC_HEREDOC_INJECTIONS);
		allowInjectionWithInterpolation.setSelected(mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION);
		perlAnnotatorCheckBox.setSelected(mySharedSettings.PERL_ANNOTATOR_ENABLED);
		perlTryCatchCheckBox.setSelected(mySharedSettings.PERL_TRY_CATCH_ENABLED);
		deparseArgumentsTextField.setText(mySharedSettings.PERL_DEPARSE_ARGUMENTS);

		perlCriticCheckBox.setSelected(mySharedSettings.PERL_CRITIC_ENABLED);
		perlCriticPathInputField.setText(myLocalSettings.PERL_CRITIC_PATH);
		perlCriticArgsInputField.setText(mySharedSettings.PERL_CRITIC_ARGS);

		perlTidyPathInputField.setText(myLocalSettings.PERL_TIDY_PATH);
		perlTidyArgsInputField.setText(mySharedSettings.PERL_TIDY_ARGS);

		if (!PlatformUtils.isIntelliJ())
		{
			resetMicroIdeSettings();
		}
	}

	protected void resetMicroIdeSettings()
	{
		perlPathInputField.setText(myLocalSettings.PERL_PATH);
	}

	@Override
	public void disposeUIResources()
	{
	}

	protected LabeledComponent<RawCommandLineEditor> copyDialogCaption(final LabeledComponent<RawCommandLineEditor> component, String text)
	{
		final RawCommandLineEditor rawCommandLineEditor = component.getComponent();
		rawCommandLineEditor.setDialogCaption(text);
		component.getLabel().setLabelFor(rawCommandLineEditor.getTextField());
		return component;
	}

}
