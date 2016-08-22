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

package com.perl5.lang.perl.idea.configuration.paths;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.configuration.DefaultModulesProvider;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.Computable;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 29.08.2015.
 */
public class PerlPlatformContentEntriesConfigurable implements Configurable
{
	private final Project myProject;
	private final JpsModuleSourceRootType<?>[] myRootTypes = new JpsModuleSourceRootType[]{JpsPerlLibrarySourceRootType.INSTANCE};

	private JPanel myTopPanel;
	private JPanel myRightPanel;
	@SuppressWarnings("Since15")
	private CollectionListModel<Module> myModuleCollectionListModel;
	private JBList myModulesList;
	private ListSelectionListener mySelectionListener;

	private final List<PerlModuleConfigurationState> myStatesList = new ArrayList<PerlModuleConfigurationState>();

	public PerlPlatformContentEntriesConfigurable(Project project)
	{
		myProject = project;
	}

	@Override
	public String getDisplayName()
	{
		return "Project Structure";
	}

	@Override
	public String getHelpTopic()
	{
		return null;
	}

	@Override
	public JComponent createComponent()
	{
		myTopPanel = new JPanel(new BorderLayout());
		createEditor();
		return myTopPanel;
	}

	private void createEditor()
	{
		Module[] modules = ModuleManager.getInstance(myProject).getModules();
		if (modules.length > 1)
		{
			Splitter splitter = new Splitter(false, 0.25f);
			myTopPanel.add(splitter);
			JBScrollPane JBScrollPane = new JBScrollPane();
			splitter.setFirstComponent(JBScrollPane);
			myRightPanel = new JPanel(new VerticalFlowLayout());
			splitter.setSecondComponent(myRightPanel);
			//noinspection Since15
			myModuleCollectionListModel = new CollectionListModel<Module>();
			myModulesList = new JBList(myModuleCollectionListModel);
			mySelectionListener = new ListSelectionListener()
			{
				@Override
				public void valueChanged(ListSelectionEvent e)
				{
					if (!e.getValueIsAdjusting())
					{
						int lastIndex = e.getLastIndex();
						if (myModuleCollectionListModel.getSize() > lastIndex)
						{
							Module selectedModule = myModuleCollectionListModel.getElementAt(lastIndex);
							for (PerlModuleConfigurationState perlModuleConfigurationState : myStatesList)
							{
								perlModuleConfigurationState.getEditorComponent().setVisible(selectedModule.equals(perlModuleConfigurationState.getModule()));
							}
							myRightPanel.repaint();
						}
					}
				}
			};
			myModulesList.addListSelectionListener(mySelectionListener);
			myModulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			JBScrollPane.getViewport().add(myModulesList);
		}
		else
		{
			//noinspection SuspiciousNameCombination
			myRightPanel = myTopPanel;
		}

		myStatesList.clear();
		DefaultModulesProvider defaultModulesProvider = new DefaultModulesProvider(myProject);
		for (Module module : modules)
		{
			final PerlModuleConfigurationState perlModuleConfigurationState = new PerlModuleConfigurationState(module, defaultModulesProvider, myRootTypes);
			JComponent component = ApplicationManager.getApplication().runReadAction(new Computable<JComponent>()
			{
				@Override
				public JComponent compute()
				{
					return perlModuleConfigurationState.getEditor().createComponent();
				}
			});
			myRightPanel.add(component);
			myStatesList.add(perlModuleConfigurationState);

			if (myModuleCollectionListModel != null)
			{
				myModuleCollectionListModel.add(module);
			}
		}
	}

	@Override
	public boolean isModified()
	{
		for (PerlModuleConfigurationState perlModuleConfigurationState : myStatesList)
		{
			if (perlModuleConfigurationState.getEditor().isModified())
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void apply() throws ConfigurationException
	{
		for (PerlModuleConfigurationState perlModuleConfigurationState : myStatesList)
		{
			perlModuleConfigurationState.apply();
		}
		disposeUIResources();
		createEditor();
	}

	@Override
	public void reset()
	{
		for (PerlModuleConfigurationState perlModuleConfigurationState : myStatesList)
		{
			perlModuleConfigurationState.reset();
		}
		if (myStatesList.size() > 1)
		{
			myModulesList.setSelectedIndex(0);
		}
	}

	@Override
	public void disposeUIResources()
	{
		for (PerlModuleConfigurationState perlModuleConfigurationState : myStatesList)
		{
			if (myRightPanel != null)
			{
				myRightPanel.remove(perlModuleConfigurationState.getEditorComponent());
			}
			perlModuleConfigurationState.dispose();
		}

		if (myModulesList != null && mySelectionListener != null)
		{
			myModulesList.removeListSelectionListener(mySelectionListener);
			mySelectionListener = null;
		}

		myModulesList = null;
		myRightPanel = null;
	}
}
