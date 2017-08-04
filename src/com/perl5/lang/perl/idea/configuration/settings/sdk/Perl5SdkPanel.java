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

import com.intellij.openapi.ui.ComboBox;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;

import javax.swing.*;

public class Perl5SdkPanel {
  private ComboBox<Perl5SdkWrapper> mySdkComboBox;
  private JButton myAddButton;
  private JButton myEditButton;
  private JButton myDeleteButton;
  private JPanel myMainPanel;

  public ComboBox<Perl5SdkWrapper> getSdkComboBox() {
    return mySdkComboBox;
  }

  public JPanel getMainPanel() {
    return myMainPanel;
  }

  public JButton getAddButton() {
    return myAddButton;
  }

  public JButton getEditButton() {
    return myEditButton;
  }

  public JButton getDeleteButton() {
    return myDeleteButton;
  }

  private void createUIComponents() {
    // TODO: place custom component creation code here
    mySdkComboBox = new ComboBox<>(375);
  }
}
