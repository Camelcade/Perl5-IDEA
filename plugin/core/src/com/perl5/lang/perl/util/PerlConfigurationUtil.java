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

package com.perl5.lang.perl.util;

import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PerlConfigurationUtil {
  public static final int WIDGET_HEIGHT = 90;

  public static JPanel createSubstituteExtensionPanel(final @NotNull CollectionListModel<String> substitutedExtensionsModel,
                                                      final @NotNull JBList<String> substitutedExtensionsList
  ) {
    return ToolbarDecorator
      .createDecorator(substitutedExtensionsList)
      .setAddAction(new AnActionButtonRunnable() {
        @Override
        public void run(AnActionButton anActionButton) {
          FileTypeManager fileTypeManager = FileTypeManager.getInstance();
          final List<String> currentItems = substitutedExtensionsModel.getItems();
          List<FileNameMatcher> possibleItems = new ArrayList<>();
          List<Icon> itemsIcons = new ArrayList<>();

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
            new BaseListPopupStep<>("Select Extension", possibleItems, itemsIcons) {
              @Override
              public PopupStep<?> onChosen(FileNameMatcher selectedValue, boolean finalChoice) {
                substitutedExtensionsModel.add(selectedValue.getPresentableString());
                //noinspection unchecked
                return super.onChosen(selectedValue, finalChoice);
              }
            };

          JBPopupFactory.getInstance()
            .createListPopup(fileNameMatcherBaseListPopupStep)
            .show(Objects.requireNonNull(anActionButton.getPreferredPopupPoint()));
        }
      })
      .disableDownAction()
      .disableUpAction()
      .setPreferredSize(JBUI.size(0, PerlConfigurationUtil.WIDGET_HEIGHT))
      .createPanel();
  }
}
