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

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.FormBuilder;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Perl5ProjectConfigurable extends Perl5StructureConfigurable {
  @NotNull
  private final Project myProject;

  protected JBList<VirtualFile> myLibsList;
  private CollectionListModel<VirtualFile> myLibsModel;

  public Perl5ProjectConfigurable(@NotNull Project project) {
    myProject = project;
  }

  @Override
  protected JComponent getAdditionalPanel() {
    FormBuilder builder = FormBuilder.createFormBuilder();
    builder.getPanel().setLayout(new VerticalFlowLayout());

    myLibsModel = new CollectionListModel<>();
    myLibsList = new JBList<>(myLibsModel);
    myLibsList.setCellRenderer(new ColoredListCellRenderer<VirtualFile>() {
      @Override
      protected void customizeCellRenderer(@NotNull JList<? extends VirtualFile> list,
                                           VirtualFile value,
                                           int index,
                                           boolean selected,
                                           boolean hasFocus) {
        setIcon(PerlIcons.PERL_LANGUAGE_ICON);
        append(FileUtil.toSystemDependentName(value.getPath()));
      }
    });
    builder.addLabeledComponent(
      PerlBundle.message("perl.settings.external.libs"),
      ToolbarDecorator.createDecorator(myLibsList)
        .setAddAction(this::doAddExternalLibrary)
        .createPanel()
    );
    return builder.getPanel();
  }

  private void doAddExternalLibrary(AnActionButton button) {
    FileChooserFactory.getInstance().createPathChooser(
      FileChooserDescriptorFactory.
        createMultipleFoldersDescriptor().
        withTreeRootVisible(true).
        withTitle(PerlBundle.message("perl.settings.select.libs")),
      myProject,
      myLibsList
    ).choose(null, virtualFiles -> {
      Ref<Boolean> notifyInternals = new Ref<>(false);

      List<VirtualFile> rootsToAdd = new ArrayList<>();

      for (VirtualFile virtualFile : virtualFiles) {
        if (!virtualFile.isValid()) {
          continue;
        }
        if (!virtualFile.isDirectory()) {
          virtualFile = virtualFile.getParent();
          if (virtualFile == null || !virtualFile.isValid()) {
            continue;
          }
        }

        if (ModuleUtilCore.findModuleForFile(virtualFile, myProject) != null) {
          notifyInternals.set(true);
          continue;
        }
        rootsToAdd.add(virtualFile);
      }

      myLibsModel.add(rootsToAdd);

      if (notifyInternals.get()) {
        Messages.showErrorDialog(
          myLibsList,
          PerlBundle.message("perl.settings.external.libs.internal.text"),
          PerlBundle.message("perl.settings.external.libs.internal.title")
        );
      }
    });
  }

  @Override
  public boolean isModified() {
    return super.isModified() || isLibsModified();
  }

  private boolean isLibsModified() {
    return !myLibsModel.getItems().equals(PerlProjectManager.getInstance(myProject).getExternalLibraryRoots());
  }

  @Override
  public void reset() {
    // fixme sdk is not reset
    myLibsModel.removeAll();
    myLibsModel.add(PerlProjectManager.getInstance(myProject).getExternalLibraryRoots());
  }

  @Override
  public void apply() throws ConfigurationException {
    PerlProjectManager.getInstance(myProject).setProjectSdk(getSelectedSdk()); // fixme this should be invoked from superclass
    if (isLibsModified()) {
      PerlProjectManager.getInstance(myProject).setExternalLibraries(myLibsModel.getItems());
    }
  }

  @Override
  protected List<Perl5SdkWrapper> getSdkItems() {
    List<Perl5SdkWrapper> defaultItems = new ArrayList<>(super.getSdkItems());
    defaultItems.add(0, DISABLE_PERL_ITEM);
    return defaultItems;
  }

  @Override
  public void disposeUIResources() {
    myLibsList = null;
    super.disposeUIResources();
  }

  @Nullable
  @Override
  protected Perl5SdkWrapper getDefaultSelectedItem() {
    Sdk projectSdk = PerlProjectManager.getInstance(myProject).getProjectSdk();
    return projectSdk == null ? DISABLE_PERL_ITEM : new Perl5RealSdkWrapper(projectSdk);
  }
}
