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
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlInstallFormOptions;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.idea.sdk.versionManager.PerlInstallForm.configureThreadsCombobox;

class PerlBrewInstallPerlForm extends PerlInstallFormOptions {
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

  public PerlBrewInstallPerlForm() {
    configureThreadsCombobox(myThreadsComboBox);
  }

  @NotNull
  public JPanel getRootPanel() {
    return myRootPanel;
  }

  @NotNull
  public String getTargetName(@NotNull String distributionId) {
    String userText = StringUtil.notNullize(myNameTextField.getText()).trim();
    return StringUtil.isEmpty(userText) ? distributionId : userText;
  }

  @NotNull
  public List<String> buildParametersList() {
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
}
