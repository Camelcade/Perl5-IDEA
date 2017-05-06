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

package com.perl5.lang.perl.util;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.Consumer;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 05.06.2016.
 */
public class PerlConfigurationUtil {
  public static final int WIDGET_HEIGHT = 90;

  public static JPanel createProjectPathsSelection(
    @NotNull final Project myProject,
    @NotNull final JBList rootsList,
    @SuppressWarnings("Since15") @NotNull final CollectionListModel<String> rootsModel,
    @NotNull @Nls final String dialogTitle
  ) {
    return ToolbarDecorator
      .createDecorator(rootsList)
      .setAddAction(new AnActionButtonRunnable() {
        @Override
        public void run(AnActionButton anActionButton) {
          //rootsModel.add("New element");
          FileChooserFactory.getInstance().createPathChooser(
            FileChooserDescriptorFactory.
              createMultipleFoldersDescriptor().
              withRoots(myProject.getBaseDir()).
              withTreeRootVisible(true).
              withTitle(dialogTitle),
            myProject,
            rootsList
          ).choose(null, new Consumer<List<VirtualFile>>() {
            @Override
            public void consume(List<VirtualFile> virtualFiles) {
              String rootPath = myProject.getBasePath();
              if (rootPath != null) {
                VirtualFile rootFile = VfsUtil.findFileByIoFile(new File(rootPath), true);

                if (rootFile != null) {
                  for (VirtualFile file : virtualFiles) {
                    String relativePath = VfsUtil.getRelativePath(file, rootFile);
                    if (!rootsModel.getItems().contains(relativePath)) {
                      rootsModel.add(relativePath);
                    }
                  }
                }
              }
            }
          });
        }
      })
      .setPreferredSize(JBUI.size(0, WIDGET_HEIGHT))
      .createPanel();
  }

  public static JPanel createSubstituteExtensionPanel(
    @SuppressWarnings("Since15") @NotNull final CollectionListModel<String> substitutedExtensionsModel,
    @NotNull final JBList substitutedExtensionsList

  ) {
    return ToolbarDecorator
      .createDecorator(substitutedExtensionsList)
      .setAddAction(new AnActionButtonRunnable() {
        @Override
        public void run(AnActionButton anActionButton) {
          FileTypeManager fileTypeManager = FileTypeManager.getInstance();
          final List<String> currentItems = substitutedExtensionsModel.getItems();
          List<FileNameMatcher> possibleItems = new ArrayList<FileNameMatcher>();
          List<Icon> itemsIcons = new ArrayList<Icon>();

          for (FileType fileType : fileTypeManager.getRegisteredFileTypes()) {
            if (fileType instanceof LanguageFileType) {
              for (FileNameMatcher matcher : fileTypeManager.getAssociations(fileType)) {
                if (matcher instanceof ExtensionFileNameMatcher) {
                  String presentableString = matcher.getPresentableString();
                  if (!currentItems.contains(presentableString)) {
                    possibleItems.add(matcher);
                    itemsIcons.add(fileType.getIcon());
                  }
                }
              }
            }
          }

          BaseListPopupStep<FileNameMatcher> fileNameMatcherBaseListPopupStep =
            new BaseListPopupStep<FileNameMatcher>("Select Extension", possibleItems, itemsIcons) {
              @Override
              public PopupStep onChosen(FileNameMatcher selectedValue, boolean finalChoice) {
                substitutedExtensionsModel.add(selectedValue.getPresentableString());
                return super.onChosen(selectedValue, finalChoice);
              }
            };

          JBPopupFactory.getInstance().createListPopup(fileNameMatcherBaseListPopupStep).show(anActionButton.getPreferredPopupPoint());
        }
      })
      .disableDownAction()
      .disableUpAction()
      .setPreferredSize(JBUI.size(0, PerlConfigurationUtil.WIDGET_HEIGHT))
      .createPanel();
  }
}
