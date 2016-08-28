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

package com.perl5.lang.perl.idea.configuration.module;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleOrderEntry;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.*;
import com.intellij.ui.table.JBTable;
import com.intellij.ui.table.TableView;
import com.intellij.util.Consumer;
import com.intellij.util.containers.HashSet;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 28.08.2016.
 */
public class PerlDependenciesConfigurable implements UnnamedConfigurable
{
	private ListTableModel<PerlDependencyWrapper> myDependenciesTableModel;
	private JBTable myDependenciesTable;
	private ModuleConfigurationState myConfigurationState;

	public PerlDependenciesConfigurable(ModuleConfigurationState configurationState)
	{
		myConfigurationState = configurationState;
	}

	@Nullable
	@Override
	public JComponent createComponent()
	{
		myDependenciesTableModel = new ListTableModel<PerlDependencyWrapper>(
				new myDependencyExportableColumnInfo(),
				new myDependencyTypeColumnInfo(),
				new myDependencyNameColumnInfo()
		)
		{
			@Override
			public void removeRow(int idx)
			{
				myConfigurationState.getRootModel().removeOrderEntry(getItem(idx).getOrderEntry());
				super.removeRow(idx);
			}

			@Override
			public void exchangeRows(int idx1, int idx2)
			{
				OrderEntry item1 = getItem(idx1).getOrderEntry();
				OrderEntry item2 = getItem(idx2).getOrderEntry();

				ModifiableRootModel rootModel = myConfigurationState.getRootModel();
				OrderEntry[] orderEntries = rootModel.getOrderEntries();

				List<OrderEntry> newOrderEntries = new ArrayList<OrderEntry>();
				for (OrderEntry orderEntry : orderEntries)
				{
					if (orderEntry.equals(item1))
					{
						newOrderEntries.add(item2);
					}
					else if (orderEntry.equals(item2))
					{
						newOrderEntries.add(item1);
					}
					else
					{
						newOrderEntries.add(orderEntry);
					}
				}
				rootModel.rearrangeOrderEntries(newOrderEntries.toArray(new OrderEntry[newOrderEntries.size()]));
				super.exchangeRows(idx1, idx2);
			}
		};

		myDependenciesTable = new TableView<PerlDependencyWrapper>(myDependenciesTableModel);
		setFixedColumnWidth(0);
		setFixedColumnWidth(1);

		return ToolbarDecorator
				.createDecorator(myDependenciesTable)
				.setAddAction(new AnActionButtonRunnable()
				{
					@Override
					public void run(AnActionButton anActionButton)
					{
						DataContext dataContext = DataManager.getInstance().getDataContext(myDependenciesTable);

						DefaultActionGroup defaultActionGroup = new DefaultActionGroup();
						final ModifiableRootModel rootModel = myConfigurationState.getRootModel();
						Module currentModule = rootModel.getModule();
						HashSet<Module> linkedModules = new HashSet<Module>();
						for (OrderEntry orderEntry : rootModel.getOrderEntries())
						{
							if (orderEntry instanceof ModuleOrderEntry)
							{
								linkedModules.add(((ModuleOrderEntry) orderEntry).getModule());
							}
						}

						for (final Module module : ModuleManager.getInstance(myConfigurationState.getProject()).getModules())
						{
							if (currentModule.equals(module) || linkedModules.contains(module))
							{
								continue;
							}
							defaultActionGroup.add(new AnAction(PerlBundle.message("perl.settings.dependency.type.module") + ": " + module.getName())
							{
								@Override
								public void actionPerformed(AnActionEvent e)
								{
									ModuleOrderEntry moduleOrderEntry = rootModel.addModuleOrderEntry(module);
									myDependenciesTableModel.addRow(PerlDependencyWrapperFactory.getWrapper(moduleOrderEntry));
								}
							});
						}

						defaultActionGroup.add(new AnAction(PerlBundle.message("perl.settings.dependency.type.library") + "...")
						{
							@Override
							public void actionPerformed(AnActionEvent e)
							{
								FileChooserFactory.getInstance().createPathChooser(
										FileChooserDescriptorFactory.createSingleFolderDescriptor(),
										myConfigurationState.getProject(),
										myDependenciesTable
								).choose(null, new Consumer<List<VirtualFile>>()
								{
									@Override
									public void consume(List<VirtualFile> virtualFiles)
									{
										Library library = rootModel.getModuleLibraryTable().createLibrary();
										Library.ModifiableModel modifiableModel = library.getModifiableModel();
										modifiableModel.addRoot(virtualFiles.get(0), OrderRootType.CLASSES);
										modifiableModel.commit();
										myDependenciesTableModel.addRow(PerlDependencyWrapperFactory.getWrapper(rootModel.findLibraryOrderEntry(library)));
									}
								});
							}
						});

						JBPopupFactory.getInstance().createActionGroupPopup(
								PerlBundle.message("perl.settings.dependency.choose"),
								defaultActionGroup,
								dataContext,
								JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
								true
						).show(anActionButton.getPreferredPopupPoint());
					}
				})
				.createPanel();
	}

	private void setFixedColumnWidth(final int columnIndex)
	{
		final TableColumn column = myDependenciesTable.getTableHeader().getColumnModel().getColumn(columnIndex);
		column.setResizable(false);
		column.setMaxWidth(column.getPreferredWidth());
	}

	@Override
	public boolean isModified()
	{
		return false;
	}

	@Override
	public void apply() throws ConfigurationException
	{

	}

	@Override
	public void reset()
	{
		myDependenciesTableModel.setItems(new ArrayList<PerlDependencyWrapper>());
		for (OrderEntry orderEntry : myConfigurationState.getRootModel().getOrderEntries())
		{
			myDependenciesTableModel.addRow(PerlDependencyWrapperFactory.getWrapper(orderEntry));
		}
	}

	@Override
	public void disposeUIResources()
	{
	}

	private static class myDependencyExportableColumnInfo extends ColumnInfo<PerlDependencyWrapper, Boolean>
	{
		public myDependencyExportableColumnInfo()
		{
			super(PerlBundle.message("perl.settings.dependency.exportable"));
		}

		@Nullable
		@Override
		public Boolean valueOf(PerlDependencyWrapper perlDependencyWrapper)
		{
			return perlDependencyWrapper.isExported();
		}

		@Override
		public boolean isCellEditable(PerlDependencyWrapper perlDependencyWrapper)
		{
			return perlDependencyWrapper.isExportable();
		}

		@Override
		public void setValue(PerlDependencyWrapper perlDependencyWrapper, Boolean value)
		{
			perlDependencyWrapper.setExported(value);
		}

		@Nullable
		@Override
		public TableCellEditor getEditor(PerlDependencyWrapper perlDependencyWrapper)
		{
			return new BooleanTableCellEditor();
		}

		@Nullable
		@Override
		public TableCellRenderer getRenderer(final PerlDependencyWrapper perlDependencyWrapper)
		{
			if (perlDependencyWrapper.isExportable())
			{
				return new BooleanTableCellRenderer();
			}

			return new TableCellRenderer()
			{
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
				{
					JPanel jPanel = new JPanel();
					jPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
					return jPanel;
				}
			};
		}
	}

	private static class myDependencyNameColumnInfo extends ColumnInfo<PerlDependencyWrapper, String>
	{
		public myDependencyNameColumnInfo()
		{
			super(PerlBundle.message("perl.settings.dependency.name"));
		}

		@Nullable
		@Override
		public String valueOf(PerlDependencyWrapper perlDependencyWrapper)
		{
			return perlDependencyWrapper.getName();
		}

		@Nullable
		@Override
		public TableCellRenderer getRenderer(final PerlDependencyWrapper perlDependencyWrapper)
		{
			return new ColoredTableCellRenderer()
			{
				@Override
				protected void customizeCellRenderer(JTable table, Object value, boolean selected, boolean hasFocus, int row, int column)
				{
					setTransparentIconBackground(true);
					setIcon(perlDependencyWrapper.getIcon());
					append(perlDependencyWrapper.getName());
				}
			};
		}
	}

	private static class myDependencyTypeColumnInfo extends ColumnInfo<PerlDependencyWrapper, String>
	{
		public myDependencyTypeColumnInfo()
		{
			super(PerlBundle.message("perl.settings.dependency.type"));
		}

		@Nullable
		@Override
		public String valueOf(PerlDependencyWrapper perlDependencyWrapper)
		{
			return perlDependencyWrapper.getType();
		}
	}


}
