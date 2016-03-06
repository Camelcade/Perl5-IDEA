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

package com.perl5.lang.htmlmason.idea.configuration;

import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.FormBuilder;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 05.03.2016.
 */
public class HTMLMasonSettingsConfigurable extends AbstractMasonSettingsConfigurable
{
	final HTMLMasonSettings mySettings;

	protected CollectionListModel<String> substitutedExtensionsModel;
	protected JBList substitutedExtensionsList;
	protected JPanel substitutedExtensionsPanel;

	public HTMLMasonSettingsConfigurable(Project myProject)
	{
		this(myProject, "HTML::Mason");
	}

	public HTMLMasonSettingsConfigurable(Project myProject, String windowTitile)
	{
		super(myProject, windowTitile);
		mySettings = HTMLMasonSettings.getInstance(myProject);
	}

	@Nullable
	@Override
	public JComponent createComponent()
	{
		FormBuilder builder = FormBuilder.createFormBuilder();
		builder.getPanel().setLayout(new VerticalFlowLayout());

		createRootsListComponent(builder);
		createGlobalsComponent(builder);
		createSubstitutedExtensionsComponent(builder);

		return builder.getPanel();
	}

	@Override
	public boolean isModified()
	{
		return
				!mySettings.componentRoots.equals(rootsModel.getItems()) ||
						!mySettings.globalVariables.equals(globalsModel.getItems()) ||
						!mySettings.substitutedExtensions.equals(substitutedExtensionsModel.getItems())
				;
	}

	@Override
	public void apply() throws ConfigurationException
	{
		mySettings.componentRoots.clear();
		mySettings.componentRoots.addAll(rootsModel.getItems());

		mySettings.substitutedExtensions.clear();
		mySettings.substitutedExtensions.addAll(substitutedExtensionsModel.getItems());

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

		substitutedExtensionsModel.removeAll();
		substitutedExtensionsModel.add(mySettings.substitutedExtensions);

		globalsModel.setItems(new ArrayList<VariableDescription>());
		for (VariableDescription variableDescription : mySettings.globalVariables)
		{
			globalsModel.addRow(variableDescription.clone());
		}
	}

	protected void createSubstitutedExtensionsComponent(FormBuilder builder)
	{
		substitutedExtensionsModel = new CollectionListModel<String>();
		substitutedExtensionsList = new JBList(substitutedExtensionsModel);
		substitutedExtensionsPanel = ToolbarDecorator
				.createDecorator(substitutedExtensionsList)
				.setAddAction(new AnActionButtonRunnable()
				{
					@Override
					public void run(AnActionButton anActionButton)
					{
						FileTypeManager fileTypeManager = FileTypeManager.getInstance();
						final List<String> currentItems = substitutedExtensionsModel.getItems();
						List<FileNameMatcher> possibleItems = new ArrayList<FileNameMatcher>();
						List<Icon> itemsIcons = new ArrayList<Icon>();

						for (FileType fileType : fileTypeManager.getRegisteredFileTypes())
						{
							if (fileType instanceof LanguageFileType)
							{
								for (FileNameMatcher matcher : fileTypeManager.getAssociations(fileType))
								{
									String presentableString = matcher.getPresentableString();
									if (!currentItems.contains(presentableString))
									{
										possibleItems.add(matcher);
										itemsIcons.add(fileType.getIcon());
									}
								}
							}
						}

						BaseListPopupStep<FileNameMatcher> fileNameMatcherBaseListPopupStep =
								new BaseListPopupStep<FileNameMatcher>("Select Extension", possibleItems, itemsIcons)
								{
									@Override
									public PopupStep onChosen(FileNameMatcher selectedValue, boolean finalChoice)
									{
										substitutedExtensionsModel.add(selectedValue.getPresentableString());
										return super.onChosen(selectedValue, finalChoice);
									}
								};

						JBPopupFactory.getInstance().createListPopup(fileNameMatcherBaseListPopupStep, 20).showInCenterOf(substitutedExtensionsPanel);
					}
				}).createPanel();
		builder.addLabeledComponent(new JLabel("Extensions that should be handled as HTML::Mason components (only under roots configured above):"), substitutedExtensionsPanel);
	}

	@Override
	public void disposeUIResources()
	{
		super.disposeUIResources();
		substitutedExtensionsPanel = null;
		substitutedExtensionsModel = null;
		substitutedExtensionsList = null;
	}
}
