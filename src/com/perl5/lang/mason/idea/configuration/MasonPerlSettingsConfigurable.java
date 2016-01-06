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

package com.perl5.lang.mason.idea.configuration;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.Consumer;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * Created by hurricup on 03.01.2016.
 */
public class MasonPerlSettingsConfigurable implements Configurable
{
	final Project myProject;
	final String windowTitile;
	final MasonPerlSettings mySettings;

	CollectionListModel<String> rootsModel;
	CollectionListModel<String> autobaseModel;

	JBList rootsList;
	JBList autobaseList;

	public MasonPerlSettingsConfigurable(Project myProject)
	{
		this(myProject, "Mason");
	}

	public MasonPerlSettingsConfigurable(Project myProject, String windowTitile)
	{
		this.myProject = myProject;
		this.windowTitile = windowTitile;
		mySettings = MasonPerlSettings.getInstance(myProject);
	}

	@Nls
	@Override
	public String getDisplayName()
	{
		return windowTitile;
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

		rootsModel = new CollectionListModel<String>();
		rootsList = new JBList(rootsModel);
		builder.addLabeledComponent(new JLabel("Components roots:"), ToolbarDecorator
				.createDecorator(rootsList)
				.setAddAction(new AnActionButtonRunnable()
				{
					@Override
					public void run(AnActionButton anActionButton)
					{
						//rootsModel.add("New element");
						FileChooserFactory.getInstance().createPathChooser(
								FileChooserDescriptorFactory.
										createMultipleFoldersDescriptor().
										withRoots(myProject.getBaseDir()).
										withTreeRootVisible(true).
										withTitle("Select Mason Component Roots"),
								myProject,
								rootsList
						).choose(null, new Consumer<List<VirtualFile>>()
						{
							@Override
							public void consume(List<VirtualFile> virtualFiles)
							{
								String rootPath = myProject.getBasePath();
								if (rootPath != null)
								{
									VirtualFile rootFile = VfsUtil.findFileByIoFile(new File(rootPath), true);

									if (rootFile != null)
									{
										for (VirtualFile file : virtualFiles)
										{
											String relativePath = VfsUtil.getRelativePath(file, rootFile);
											if (!rootsModel.getItems().contains(relativePath))
											{
												rootsModel.add(relativePath);
											}
										}
									}
								}
							}
						});
					}
				}).createPanel());

		autobaseModel = new CollectionListModel<String>();
		autobaseList = new JBList(autobaseModel);
		builder.addLabeledComponent(new JLabel("Autobase names (order is important, later components inherited from early):"), ToolbarDecorator
				.createDecorator(autobaseList)
				.setAddAction(new AnActionButtonRunnable()
				{
					@Override
					public void run(AnActionButton anActionButton)
					{
						String fileName = Messages.showInputDialog(
								myProject,
								"Type new Autobase filename?",
								"New Autobase Filename",
								Messages.getQuestionIcon(),
								"",
								null);
						if (StringUtil.isNotEmpty(fileName) && !autobaseModel.getItems().contains(fileName))
						{
							autobaseModel.add(fileName);
						}
					}
				}).createPanel());

		return builder.getPanel();
	}

	@Override
	public boolean isModified()
	{
		return
				!mySettings.componentRoots.equals(rootsModel.getItems()) ||
						!mySettings.autobaseNames.equals(autobaseModel.getItems());
	}

	@Override
	public void apply() throws ConfigurationException
	{
		mySettings.componentRoots.clear();
		mySettings.componentRoots.addAll(rootsModel.getItems());

		mySettings.autobaseNames.clear();
		mySettings.autobaseNames.addAll(autobaseModel.getItems());

		mySettings.settingsUpdated();
	}

	@Override
	public void reset()
	{
		rootsModel.removeAll();
		rootsModel.add(mySettings.componentRoots);
		autobaseModel.removeAll();
		autobaseModel.add(mySettings.autobaseNames);
	}

	@Override
	public void disposeUIResources()
	{
		rootsModel = null;
		rootsList = null;
		autobaseList = null;
		autobaseModel = null;
	}
}
