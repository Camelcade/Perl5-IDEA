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

package com.perl5.lang.perl.idea.sdk.versionManager.plenv;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlInstallFormOptions;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.idea.sdk.versionManager.PerlInstallForm.configureThreadsCombobox;

class PlenvInstallPerlForm extends PerlInstallFormOptions {
  private JComboBox myThreads;
  private JPanel myRootPanel;
  private JTextField myTargetName;
  private JCheckBox mySkipTestingCheckBox;
  private JCheckBox myNoManpagesCheckBox;
  private JTextPane mySetOptionsBelowIfTextPane;
  private JCheckBox myInstallDevel;

  public PlenvInstallPerlForm() {
    configureThreadsCombobox(myThreads);
  }

  @NotNull
  @Override
  public JPanel getRootPanel() {
    return myRootPanel;
  }

  @NotNull
  @Override
  public String getTargetName(@NotNull String distributionId) {
    String userText = StringUtil.notNullize(myTargetName.getText()).trim();
    return StringUtil.isEmpty(userText) ? distributionId : userText;
  }

  @NotNull
  @Override
  public List<String> buildParametersList() {
    List<String> buildParams = new ArrayList<>();

    Integer threads = ObjectUtils.tryCast(myThreads.getSelectedItem(), Integer.class);
    if (threads != null && threads > 1) {
      buildParams.add("-j");
      buildParams.add(threads.toString());
    }

    if (!mySkipTestingCheckBox.isSelected()) {
      buildParams.add("--test");
    }

    if (myNoManpagesCheckBox.isSelected()) {
      buildParams.add("--noman");
    }

    String targetName = myTargetName.getText().trim();
    if (StringUtil.isNotEmpty(targetName)) {
      buildParams.add("--as=" + targetName);
    }

    if (myInstallDevel.isSelected()) {
      buildParams.add("-Dusedevel");
    }

    return buildParams;
  }
}
