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
import com.intellij.openapi.project.DumbModeTask;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.impl.PushedFilePropertiesUpdater;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.FileContentUtil;
import com.intellij.util.Processor;
import com.intellij.util.indexing.FileBasedIndexProjectHandler;
import com.intellij.util.ui.FormBuilder;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import gnu.trove.THashSet;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
		Set<String> rootsDiff = getDiff(mySettings.componentRoots, rootsModel.getItems());
		mySettings.componentRoots.clear();
		mySettings.componentRoots.addAll(rootsModel.getItems());

		Set<String> extDiff = getDiff(mySettings.substitutedExtensions, substitutedExtensionsModel.getItems());
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

		mySettings.updateSubstitutors();
		mySettings.settingsUpdated();

		if (rootsDiff.size() > 0 || extDiff.size() > 0)
		{
			updateFileProperties(rootsDiff, extDiff);
		}
	}

	protected void updateFileProperties(final Set<String> rootsDiff, Set<String> extDiff)
	{
		boolean rootsChanged = rootsDiff.size() > 0;
		boolean extChanged = extDiff.size() > 0;

		if (rootsChanged)
			extDiff.addAll(mySettings.substitutedExtensions);

		if (extChanged)
			rootsDiff.addAll(mySettings.componentRoots);

		// collecting matchers
		final List<FileNameMatcher> matchers = new ArrayList<FileNameMatcher>();
		FileTypeManager fileTypeManager = FileTypeManager.getInstance();
		for (FileType fileType : fileTypeManager.getRegisteredFileTypes())
		{
			if (fileType instanceof LanguageFileType)
			{
				for (FileNameMatcher matcher : fileTypeManager.getAssociations(fileType))
				{
					if (extDiff.contains(matcher.getPresentableString()))
					{
						matchers.add(matcher);
					}
				}
			}
		}

		// processing files
		final PushedFilePropertiesUpdater pushedFilePropertiesUpdater = PushedFilePropertiesUpdater.getInstance(myProject);
		VirtualFile projectRoot = myProject.getBaseDir();
		if (projectRoot != null)
		{
			for (String root : rootsDiff)
			{
				VirtualFile componentRoot = VfsUtil.findRelativeFile(projectRoot, root);
				if (componentRoot != null)
				{
					VfsUtil.processFilesRecursively(componentRoot, new Processor<VirtualFile>()
					{
						@Override
						public boolean process(VirtualFile virtualFile)
						{
							if (!virtualFile.isDirectory())
							{
								for (FileNameMatcher matcher : matchers)
								{
									if (matcher.accept(virtualFile.getName()))
									{
//										pushedFilePropertiesUpdater.findAndUpdateValue(virtualFile, HTMLMasonFilePropertyPusher.INSTANCE, false);
										pushedFilePropertiesUpdater.filePropertiesChanged(virtualFile);
										break;
									}
								}
							}
							return true;
						}
					});
				}
			}
		}

		FileContentUtil.reparseOpenedFiles();

		// taken from 16 version of platform, dumbmode reindexing
		DumbModeTask dumbTask = FileBasedIndexProjectHandler.createChangedFilesIndexingTask(myProject);
		if (dumbTask != null)
		{
			DumbService.getInstance(myProject).queueTask(dumbTask);
		}
	}

	protected Set<String> getDiff(List<String> first, List<String> second)
	{
		Set<String> diff = new THashSet<String>(first);
		diff.removeAll(second);

		Set<String> temp = new THashSet<String>(second);
		temp.removeAll(first);
		diff.addAll(temp);

		return diff;
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
