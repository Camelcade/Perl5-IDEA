/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.configuration.settings.sdk;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.treeView.AbstractTreeBuilder;
import com.intellij.ide.util.treeView.AbstractTreeStructure;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.ide.util.treeView.NodeRenderer;
import com.intellij.idea.ActionsBundle;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.CustomComponentAction;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.actions.NewFolderAction;
import com.intellij.openapi.fileChooser.ex.FileSystemTreeImpl;
import com.intellij.openapi.fileChooser.impl.FileTreeBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.ModuleSourceRootEditHandler;
import com.intellij.openapi.roots.ui.configuration.actions.IconWithTextAction;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.tree.TreeUtil;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.JpsDummyElement;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by hurricup on 07.06.2015.
 */
public class PerlContentEntriesEditor implements UnnamedConfigurable, Disposable {
  private final FileChooserDescriptor myDescriptor;
  private JPanel myTreePanel;
  private Tree myTree = new Tree();
  private FileSystemTreeImpl myFileSystemTree;
  private Module myModule;
  private DefaultActionGroup myEditingActionsGroup = new DefaultActionGroup();

  public PerlContentEntriesEditor(@NotNull Module module, @NotNull Disposable parentDisposable) {
    Disposer.register(parentDisposable, this);

    for (ModuleSourceRootEditHandler<JpsDummyElement> handler : Collections
      .singletonList(ModuleSourceRootEditHandler.getEditHandler(JpsPerlLibrarySourceRootType.INSTANCE))) {

      ToggleAction toggleAction = new ToggleAction() {


        @Override
        public boolean isSelected(AnActionEvent e) {
          return false;
        }

        @Override
        public void setSelected(AnActionEvent e, boolean state) {

        }

        @Override
        public boolean displayTextInToolbar() {
          return true;
        }
      };

      Presentation presentation = toggleAction.getTemplatePresentation();
      presentation.setText(handler.getMarkRootButtonText());
      presentation.setDescription(ProjectBundle.message("module.toggle.sources.action.description",
                                                        handler.getFullRootTypeName().toLowerCase(Locale.getDefault())));
      presentation.setIcon(handler.getRootIcon());

      myEditingActionsGroup.add(toggleAction);
    }


    myModule = module;

    myTree.setRootVisible(true);
    myTree.setShowsRootHandles(true);
    TreeUtil.installActions(myTree);
    new TreeSpeedSearch(myTree);
    //myTree.setPreferredSize(new Dimension(250, -1));

    myTreePanel = new JPanel(new GridBagLayout());

    ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("PerlContentEntries", myEditingActionsGroup, true);
    myTreePanel.add(new JLabel("Mark as:"), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, 0, JBUI.insets(0, 10), 0, 0));
    myTreePanel.add(actionToolbar.getComponent(),
                    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                                           JBUI.emptyInsets(), 0, 0));

    final JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(myTree, true);
    myTreePanel.add(scrollPane,
                    new GridBagConstraints(0, GridBagConstraints.RELATIVE, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                                           JBUI.emptyInsets(), 0, 0));

    myTreePanel.setVisible(true);
    myDescriptor = FileChooserDescriptorFactory.createMultipleFoldersDescriptor();
    myDescriptor.setShowFileSystemRoots(false);
    myDescriptor.setRoots(ModuleRootManager.getInstance(myModule).getContentRoots());

    myFileSystemTree = new FileSystemTreeImpl(module.getProject(),
                                              myDescriptor,
                                              myTree,
                                              new MyTreeCellRenderer(),
                                              () -> {
                                                myFileSystemTree.updateTree();
                                                //myFileSystemTree.select(file, null);
                                              },
                                              null) {
      @Override
      protected AbstractTreeBuilder createTreeBuilder(JTree tree,
                                                      DefaultTreeModel treeModel,
                                                      AbstractTreeStructure treeStructure,
                                                      Comparator<NodeDescriptor> comparator,
                                                      FileChooserDescriptor descriptor,
                                                      @Nullable Runnable onInitialized) {
        return new MyFileTreeBuilder(tree, treeModel, treeStructure, comparator, descriptor, onInitialized);
      }
    };
    myFileSystemTree.showHiddens(true);
    Disposer.register(this, myFileSystemTree);

    final NewFolderAction newFolderAction = new MyNewFolderAction();
    final DefaultActionGroup mousePopupGroup = new DefaultActionGroup();
    mousePopupGroup.add(myEditingActionsGroup);
    mousePopupGroup.addSeparator();
    mousePopupGroup.add(newFolderAction);
    myFileSystemTree.registerMouseListener(mousePopupGroup);
  }

  @NotNull
  @Override
  public JComponent createComponent() {
    return myTreePanel;
  }

  @Override
  public boolean isModified() {
    return false;
  }

  @Override
  public void apply() throws ConfigurationException {

  }

  @Override
  public void disposeUIResources() {
    myTreePanel = null;
    myTree = null;
    myFileSystemTree = null;
    myEditingActionsGroup = null;
    myModule = null;
  }

  @Override
  public void dispose() {
    disposeUIResources();
  }

  private static class MyNewFolderAction extends NewFolderAction implements CustomComponentAction {
    private MyNewFolderAction() {
      super(ActionsBundle.message("action.FileChooser.NewFolder.text"),
            ActionsBundle.message("action.FileChooser.NewFolder.description"),
            AllIcons.Actions.NewFolder);
    }

    @Override
    public JComponent createCustomComponent(Presentation presentation) {
      return IconWithTextAction.createCustomComponentImpl(this, presentation);
    }
  }

  private static class MyFileTreeBuilder extends FileTreeBuilder {
    public MyFileTreeBuilder(JTree tree,
                             DefaultTreeModel treeModel,
                             AbstractTreeStructure treeStructure,
                             Comparator<NodeDescriptor> comparator,
                             FileChooserDescriptor descriptor,
                             @Nullable Runnable onInitialized) {
      super(tree, treeModel, treeStructure, comparator, descriptor, onInitialized);
    }

    @Override
    protected boolean isAlwaysShowPlus(NodeDescriptor nodeDescriptor) {
      return false; // need this in order to not show plus for empty directories
    }
  }

  private class MyTreeCellRenderer extends NodeRenderer {

  }
}
