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

package com.perl5.lang.perl.idea.sdk.versionManager.perlbrew;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ObjectUtils;
import com.intellij.util.text.VersionComparatorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

class PerlBrewInstallPerlForm {
  @NotNull
  private final ChangeListener myListener;

  private JComboBox<String> myDistributionsComboBox;
  private JComboBox<Integer> myThreadsComboBox;
  private JTextField myNameTextField;
  private JCheckBox myForceInstallationCheckBox;
  private JCheckBox mySkipTestingCheckBox;
  private JCheckBox myNoManpagesCheckBox;
  private JCheckBox myWithUsethreadsCheckBox;
  private JCheckBox myWithUsemultiplicityCheckBox;
  private JCheckBox myWithUse64bitintCheckBox;
  private JCheckBox myWithUse64bitallCheckBox;
  private JCheckBox myWithUselongdoubleCheckBox;
  private JCheckBox myWithDEBUGGINGEnabledCheckBox;
  private JCheckBox myUseClangCheckBox;
  private JCheckBox myDonTCallPatchperlCheckBox;
  private JPanel myRootPanel;
  private JCheckBox myAddInstalledPerl5ToCheckBox;
  private JCheckBox myChooseForCurrentProjectCheckBox;

  public PerlBrewInstallPerlForm(@NotNull ChangeListener listener, @NotNull List<String> distributionsList) {
    myListener = listener;
    List<Integer> threadsList = new ArrayList<>(16);
    for (int i = 1; i <= 16; i++) {
      threadsList.add(i);
    }
    myThreadsComboBox.setModel(new CollectionComboBoxModel<>(threadsList));
    myThreadsComboBox.setRenderer(new ColoredListCellRenderer<Integer>() {
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
    myThreadsComboBox.setSelectedIndex(0);

    distributionsList.sort((a, b) -> {
      int wordIndex = a.indexOf("-");
      a = wordIndex == -1 ? a : a.substring(wordIndex + 1);
      wordIndex = b.indexOf("-");
      b = wordIndex == -1 ? a : b.substring(wordIndex + 1);
      return VersionComparatorUtil.compare(b, a);
    });

    myDistributionsComboBox.setModel(new CollectionComboBoxModel<>(distributionsList));
    myDistributionsComboBox.addActionListener(e -> updateState());
    myDistributionsComboBox.setRenderer(new ColoredListCellRenderer<String>() {
      @Override
      protected void customizeCellRenderer(@NotNull JList<? extends String> list,
                                           String value,
                                           int index,
                                           boolean selected,
                                           boolean hasFocus) {
        if (value.startsWith("i ")) {
          append(cleanDistributionItem(value)).append(" (installed)", SimpleTextAttributes.GRAY_ATTRIBUTES);
        }
        else {
          append(value);
        }
      }
    });
    myAddInstalledPerl5ToCheckBox.addChangeListener(e -> updateState());
    updateState();
  }

  private void updateState() {
    myChooseForCurrentProjectCheckBox.setEnabled(myAddInstalledPerl5ToCheckBox.isSelected());
    myListener.changed(this);
  }

  public JPanel getRootPanel() {
    return myRootPanel;
  }

  public JCheckBox getAddInstalledPerl5ToCheckBox() {
    return myAddInstalledPerl5ToCheckBox;
  }

  public JCheckBox getChooseForCurrentProjectCheckBox() {
    return myChooseForCurrentProjectCheckBox;
  }

  public JComboBox<String> getDistributionsComboBox() {
    return myDistributionsComboBox;
  }

  @NotNull
  public String getSelectedDistributionId() {
    return cleanDistributionItem(ObjectUtils.notNull((String)getDistributionsComboBox().getSelectedItem(), ""));
  }

  @Nullable
  public String getTargetName() {
    return StringUtil.notNullize(myNameTextField.getText()).trim();
  }

  @NotNull
  List<String> buildParametersList() {
    List<String> buildParams = new ArrayList<>();
    if (myForceInstallationCheckBox.isSelected()) {
      buildParams.add("--force");
    }
    Integer threads = ObjectUtils.tryCast(myThreadsComboBox.getSelectedItem(), Integer.class);
    if (threads != null && threads > 1) {
      buildParams.add("-j");
      buildParams.add(threads.toString());
    }
    if (mySkipTestingCheckBox.isSelected()) {
      buildParams.add("--notest");
    }

    String targetName = myNameTextField.getText();
    if (StringUtil.isNotEmpty(targetName)) {
      buildParams.add("--as");
      buildParams.add(targetName);
    }

    if (myNoManpagesCheckBox.isSelected()) {
      buildParams.add("--noman");
    }

    if (myWithUsethreadsCheckBox.isSelected()) {
      buildParams.add("--thread");
    }

    if (myWithUsemultiplicityCheckBox.isSelected()) {
      buildParams.add("--multi");
    }

    if (myWithUse64bitintCheckBox.isSelected()) {
      buildParams.add("--64int");
    }

    if (myWithUse64bitallCheckBox.isSelected()) {
      buildParams.add("--64all");
    }

    if (myWithUselongdoubleCheckBox.isSelected()) {
      buildParams.add("--ld");
    }

    if (myWithDEBUGGINGEnabledCheckBox.isSelected()) {
      buildParams.add("--debug");
    }

    if (myUseClangCheckBox.isSelected()) {
      buildParams.add("--clang");
    }

    if (myDonTCallPatchperlCheckBox.isSelected()) {
      buildParams.add("--no-patchperl");
    }
    return buildParams;
  }

  @NotNull
  private static String cleanDistributionItem(@NotNull String original) {
    return StringUtil.trimStart(original, "i ").trim();
  }

  interface ChangeListener {
    void changed(@NotNull PerlBrewInstallPerlForm form);
  }
}
