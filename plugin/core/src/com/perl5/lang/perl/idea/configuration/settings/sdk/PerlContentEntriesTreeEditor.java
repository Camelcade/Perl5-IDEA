/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.ide.util.treeView.NodeRenderer;
import com.intellij.idea.ActionsBundle;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.CustomComponentAction;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileElement;
import com.intellij.openapi.fileChooser.actions.NewFolderAction;
import com.intellij.openapi.fileChooser.ex.FileNodeDescriptor;
import com.intellij.openapi.fileChooser.ex.FileSystemTreeImpl;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.projectRoots.impl.PerlModuleExtension;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.actions.IconWithTextAction;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.tree.TreeUtil;
import com.perl5.lang.perl.idea.modules.PerlSourceRootType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PerlContentEntriesTreeEditor implements UnnamedConfigurable, Disposable {
  private static final Logger LOG = Logger.getInstance(PerlContentEntriesTreeEditor.class);
  private JPanel myTreePanel;
  private Tree myTree = new Tree();
  private FileSystemTreeImpl myFileSystemTree;
  private Module myModule;
  private DefaultActionGroup myEditingActionsGroup = new DefaultActionGroup();
  private ModifiableRootModel myModifiableRootModel;
  private PerlModuleExtension myModifiableModel;

  public PerlContentEntriesTreeEditor(@NotNull Module module, @NotNull Disposable parentDisposable) {
    Disposer.register(parentDisposable, this);


    List<PerlSourceRootType> types = new ArrayList<>();
    Perl5SettingsConfigurableExtension.forEach(extension -> types.addAll(extension.getSourceRootTypes()));

    for (PerlSourceRootType type : types) {
      myEditingActionsGroup.add(new PerlToggleSourceRootAction(this, type.getEditHandler()));
    }


    myModule = module;

    VirtualFile[] contentRoots = ModuleRootManager.getInstance(myModule).getContentRoots();
    myTree.setRootVisible(contentRoots.length < 2);
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
    FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createMultipleFoldersDescriptor();
    descriptor.setShowFileSystemRoots(false);
    descriptor.setRoots(contentRoots);

    myFileSystemTree = new FileSystemTreeImpl(module.getProject(),
                                              descriptor,
                                              myTree,
                                              new MyTreeCellRenderer(),
                                              () -> {
                                                myFileSystemTree.updateTree();
                                                //myFileSystemTree.select(file, null);
                                              },
                                              null) {

      @Override
      protected boolean useNewAsyncModel() {
        return true;
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

  public void repaint() {
    myTree.repaint();
  }

  @Override
  public @NotNull JComponent createComponent() {
    JPanel panel = new JPanel(new VerticalFlowLayout());
    panel.add(myTreePanel);
    return panel;
  }

  @Override
  public boolean isModified() {
    return myModifiableModel.isChanged();
  }

  @Override
  public void apply() {
    LOG.assertTrue(myModifiableRootModel != null);
    LOG.assertTrue(myModifiableModel != null);
    WriteAction.run(() -> myModifiableRootModel.commit());
  }

  @Override
  public void reset() {
    if (myModifiableRootModel != null) {
      myModifiableRootModel.dispose();
      myModifiableRootModel = null;
      myModifiableModel = null;
    }
    myModifiableRootModel = ModuleRootManager.getInstance(myModule).getModifiableModel();
    myModifiableModel = myModifiableRootModel.getModuleExtension(PerlModuleExtension.class);
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
    if (myModifiableRootModel != null) {
      myModifiableRootModel.dispose();
    }
  }

  @NotNull
  VirtualFile[] getSelectedFiles() {
    final TreePath[] selectionPaths = myTree.getSelectionPaths();
    if (selectionPaths == null) {
      return VirtualFile.EMPTY_ARRAY;
    }
    final List<VirtualFile> selected = new ArrayList<>();
    for (TreePath treePath : selectionPaths) {
      final DefaultMutableTreeNode node = (DefaultMutableTreeNode)treePath.getLastPathComponent();
      final Object nodeDescriptor = node.getUserObject();
      if (!(nodeDescriptor instanceof FileNodeDescriptor)) {
        return VirtualFile.EMPTY_ARRAY;
      }
      final FileElement fileElement = ((FileNodeDescriptor)nodeDescriptor).getElement();
      final VirtualFile file = fileElement.getFile();
      if (file != null) {
        selected.add(file);
      }
    }
    return selected.toArray(VirtualFile.EMPTY_ARRAY);
  }

  @NotNull
  PerlModuleExtension getModifiableModel() {
    return myModifiableModel;
  }

  private static class MyNewFolderAction extends NewFolderAction implements CustomComponentAction {
    private MyNewFolderAction() {
      super(ActionsBundle.message("action.FileChooser.NewFolder.text"),
            ActionsBundle.message("action.FileChooser.NewFolder.description"),
            AllIcons.Actions.NewFolder);
    }

    @Override
    public @NotNull JComponent createCustomComponent(@NotNull Presentation presentation, @NotNull String place) {
      return IconWithTextAction.createCustomComponentImpl(this, presentation, place);
    }
  }

  private class MyTreeCellRenderer extends NodeRenderer {
    @Override
    public void customizeCellRenderer(@NotNull JTree tree,
                                      Object value,
                                      boolean selected,
                                      boolean expanded,
                                      boolean leaf,
                                      int row,
                                      boolean hasFocus) {
      super.customizeCellRenderer(tree, value, selected, expanded, leaf, row, hasFocus);
      final Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
      if (!(userObject instanceof NodeDescriptor)) {
        return;
      }
      final Object element = ((NodeDescriptor<?>)userObject).getElement();
      if (!(element instanceof FileElement)) {
        return;
      }
      final VirtualFile file = ((FileElement)element).getFile();
      if (file == null || !file.isDirectory()) {
        return;
      }
      PerlSourceRootType rootType = myModifiableModel.getRootType(file);
      if (rootType == null) {
        return;
      }
      /*
      final ContentEntry contentEntry = editor.getContentEntry();
      if (contentEntry != null) {
        final String prefix = getPresentablePrefix(contentEntry, file);
        if (!prefix.isEmpty()) {
          append(" (" + prefix + ")", new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, JBColor.GRAY));
        }
        setIcon(updateIcon(contentEntry, file, getIcon()));
      }
      */
      setIcon(rootType.getEditHandler().getRootIcon());
    }
  }
}
