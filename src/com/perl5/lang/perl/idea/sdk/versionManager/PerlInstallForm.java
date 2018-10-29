/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.versionManager;

import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PerlInstallForm {
  @NotNull
  private final InstallFormHelper myHelper;

  private JComboBox<String> myDistributionsComboBox;
  private JPanel myRootPanel;
  private JPanel myOptionsPanel;
  private JCheckBox myAddInstalledPerl5ToCheckBox;
  private JCheckBox mySetInstalledPerl5ForCheckBox;

  public PerlInstallForm(@Nullable PerlInstallFormOptions optionsForm,
                         @NotNull InstallFormHelper helper,
                         @NotNull List<String> distributionsList) {
    if (optionsForm != null) {
      myOptionsPanel.add(optionsForm.getRootPanel(), BorderLayout.CENTER);
    }
    myHelper = helper;

    distributionsList.sort(helper);

    myDistributionsComboBox.setModel(new CollectionComboBoxModel<>(distributionsList));
    myDistributionsComboBox.addActionListener(e -> updateState());
    myDistributionsComboBox.setRenderer(new ColoredListCellRenderer<String>() {
      @Override
      protected void customizeCellRenderer(@NotNull JList<? extends String> list,
                                           String value,
                                           int index,
                                           boolean selected,
                                           boolean hasFocus) {
        if (myHelper.isInstalled(value)) {
          append(myHelper.cleanDistributionItem(value)).append(" (installed)", SimpleTextAttributes.GRAY_ATTRIBUTES);
        }
        else {
          append(value);
        }
      }
    });
    myAddInstalledPerl5ToCheckBox.addChangeListener(e -> updateState());
    updateState();
  }

  protected void updateState() {
    myAddInstalledPerl5ToCheckBox.setEnabled(isAddInstalledPerl());
    myHelper.changed(this);
  }

  public boolean isAddInstalledPerl() {
    return myAddInstalledPerl5ToCheckBox.isSelected();
  }

  public boolean isChooseInstalledPerl() {
    return mySetInstalledPerl5ForCheckBox.isSelected();
  }

  @NotNull
  public String getSelectedDistributionId() {
    return myHelper.cleanDistributionItem(ObjectUtils.notNull((String)myDistributionsComboBox.getSelectedItem(), ""));
  }

  public JPanel getRootPanel() {
    return myRootPanel;
  }

  public static void configureThreadsCombobox(@NotNull JComboBox<Integer> threadsComboBox) {
    List<Integer> threadsList = new ArrayList<>(16);
    for (int i = 1; i <= 16; i++) {
      threadsList.add(i);
    }
    threadsComboBox.setModel(new CollectionComboBoxModel<>(threadsList));
    threadsComboBox.setRenderer(new ColoredListCellRenderer<Integer>() {
      @Override
      protected void customizeCellRenderer(@NotNull JList<? extends Integer> list,
                                           Integer value,
                                           int index,
                                           boolean selected,
                                           boolean hasFocus) {
        if (value == 1) {
          append("Single thread");
        }
        else {
          append(Integer.toString(value)).append(" threads");
        }
      }
    });
    threadsComboBox.setSelectedIndex(0);
  }

  public interface InstallFormHelper extends Comparator<String> {
    /**
     * Invoked on sdk combobox change
     */
    void changed(@NotNull PerlInstallForm form);

    /**
     * Cleans {@code rawItem} from tech information, like {@code '(installed)'}
     */
    String cleanDistributionItem(@NotNull String rawItem);

    /**
     * @return true iff rawItem is marked as installed
     */
    boolean isInstalled(@NotNull String rawItem);
  }
}
