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
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.table.JBTable;
import com.intellij.util.Consumer;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.ListTableModel;
import com.perl5.lang.perl.lexer.PerlBaseLexer;
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 03.01.2016.
 */
public class MasonSettingsConfigurable implements Configurable
{
	private static final Pattern VARIABLE_CHECK_PATTERN = Pattern.compile(
			"[$@%]" + PerlLexer.IDENTIFIER_PATTERN
	);


	final Project myProject;
	final String windowTitile;
	final MasonSettings mySettings;

	CollectionListModel<String> rootsModel;
	JBList rootsList;

	CollectionListModel<String> autobaseModel;
	JBList autobaseList;

	ListTableModel<VariableDescription> globalsModel;
	JBTable globalsTable;

	public MasonSettingsConfigurable(Project myProject)
	{
		this(myProject, "Mason2");
	}

	public MasonSettingsConfigurable(Project myProject, String windowTitile)
	{
		this.myProject = myProject;
		this.windowTitile = windowTitile;
		mySettings = MasonSettings.getInstance(myProject);
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
		builder.addLabeledComponent(new JLabel("Components roots (relative to project's root):"), ToolbarDecorator
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
		builder.addLabeledComponent(new JLabel("Autobase names (autobase_names option. Order is important, later components may be inherited from early):"), ToolbarDecorator
				.createDecorator(autobaseList)
				.setAddAction(new AnActionButtonRunnable()
				{
					@Override
					public void run(AnActionButton anActionButton)
					{
						String fileName = Messages.showInputDialog(
								myProject,
								"Type new Autobase filename:",
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


		globalsModel = new ListTableModel<VariableDescription>(
				new myVariableNameColumnInfo(),
				new myVariableTypeColumnInfo()
		);
		globalsTable = new JBTable(globalsModel);
		builder.addLabeledComponent(new JLabel("Components global variables (allow_globals option):"), ToolbarDecorator
				.createDecorator(globalsTable)
				.setAddAction(new AnActionButtonRunnable()
				{
					@Override
					public void run(AnActionButton anActionButton)
					{
						final TableCellEditor cellEditor = globalsTable.getCellEditor();
						if (cellEditor != null)
						{
							cellEditor.stopCellEditing();
						}
						final TableModel model = globalsTable.getModel();

						int indexToEdit = -1;

						for (VariableDescription variableDescription : globalsModel.getItems())
						{
							if (StringUtil.isEmpty(variableDescription.variableName))
							{
								indexToEdit = globalsModel.indexOf(variableDescription);
								break;
							}
						}

						if (indexToEdit == -1)
						{
							globalsModel.addRow(new VariableDescription());
							indexToEdit = model.getRowCount() - 1;
						}

						TableUtil.editCellAt(globalsTable, indexToEdit, 0);
					}
				})
				.createPanel());

		return builder.getPanel();
	}

	@Override
	public boolean isModified()
	{
		return
				!mySettings.componentRoots.equals(rootsModel.getItems()) ||
						!mySettings.globalVariables.equals(globalsModel.getItems()) ||
						!mySettings.autobaseNames.equals(autobaseModel.getItems())
				;
	}

	@Override
	public void apply() throws ConfigurationException
	{
		mySettings.componentRoots.clear();
		mySettings.componentRoots.addAll(rootsModel.getItems());

		mySettings.autobaseNames.clear();
		mySettings.autobaseNames.addAll(autobaseModel.getItems());

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

		autobaseModel.removeAll();
		autobaseModel.add(mySettings.autobaseNames);

		globalsModel.setItems(new ArrayList<VariableDescription>());
		for (VariableDescription variableDescription : mySettings.globalVariables)
		{
			globalsModel.addRow(variableDescription.clone());
		}
	}

	@Override
	public void disposeUIResources()
	{
		rootsModel = null;
		rootsList = null;
		autobaseList = null;
		autobaseModel = null;
		globalsTable = null;
		globalsModel = null;
	}

	public static abstract class myStringColumnInfo extends ColumnInfo<VariableDescription, String>
	{
		public myStringColumnInfo(String name)
		{
			super(name);
		}

		@Override
		public boolean isCellEditable(VariableDescription variableDescription)
		{
			return true;
		}
	}

	public class myVariableNameColumnInfo extends myStringColumnInfo
	{

		public myVariableNameColumnInfo()
		{
			super("Variable name");
		}

		@Nullable
		@Override
		public String valueOf(VariableDescription variableDescription)
		{
			return variableDescription.variableName;
		}

		@Override
		public void setValue(VariableDescription variableDescription, String value)
		{
			if (StringUtil.isNotEmpty(value) && !containsVariableName(value))
			{
				if (VARIABLE_CHECK_PATTERN.matcher(value).matches())
				{
					variableDescription.variableName = value;
					if (value.charAt(0) != '$')
					{
						variableDescription.variableType = "";
					}
				}
				else
				{
					Messages.showErrorDialog("Incorrect variable name: " + value, "Incorrect Variable Name");
				}
			}
		}

		protected boolean containsVariableName(String variableName)
		{
			for (VariableDescription variableDescription : globalsModel.getItems())
			{
				if (variableName.equals(variableDescription.variableName))
				{
					return true;
				}
			}
			return false;
		}
	}

	public class myVariableTypeColumnInfo extends myStringColumnInfo
	{
		public myVariableTypeColumnInfo()
		{
			super("Variable type");
		}

		@Nullable
		@Override
		public String valueOf(VariableDescription variableDescription)
		{
			return variableDescription.variableType;
		}

		@Override
		public boolean isCellEditable(VariableDescription variableDescription)
		{
			return StringUtil.isNotEmpty(variableDescription.variableName) && variableDescription.variableName.charAt(0) == '$';
		}

		@Override
		public void setValue(VariableDescription variableDescription, String value)
		{
			if (StringUtil.isNotEmpty(value))
			{
				if (PerlBaseLexer.AMBIGUOUS_PACKAGE_PATTERN.matcher(value).matches())
				{
					variableDescription.variableType = value;
				}
				else
				{
					Messages.showErrorDialog("Incorrect package name: " + value, "Incorrect Package Name");
				}
			}
		}
	}


}
