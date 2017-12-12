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

package com.perl5.lang.perl.idea.run;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.InsertPathAction;
import com.intellij.ui.PanelWithAnchor;
import com.intellij.ui.TextFieldWithHistory;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.PathUtil;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @see com.intellij.execution.ui.AlternativeJREPanel
 * @since 17-Sep-15
 */
public class PerlAlternativeSdkPanel extends JPanel implements PanelWithAnchor {
  private final ComponentWithBrowseButton<TextFieldWithHistory> myPathField;
  private final JBCheckBox myCbEnabled;
  private final TextFieldWithHistory myFieldWithHistory;
  private JComponent myAnchor;

  public PerlAlternativeSdkPanel() {
    myCbEnabled = new JBCheckBox("Use alternative Perl");

    myFieldWithHistory = new TextFieldWithHistory();
    myFieldWithHistory.setHistorySize(-1);
    final ArrayList<String> foundSdks = new ArrayList<>();
    final List<Sdk> perlSdks = ProjectJdkTable.getInstance().getSdksOfType(PerlSdkType.INSTANCE);

    for (Sdk sdk : perlSdks) {
      if (sdk.getSdkType() == PerlSdkType.INSTANCE) {
        foundSdks.add(sdk.getName());
      }
    }

    for (Sdk sdk : perlSdks) {
      String homePath = sdk.getHomePath();

      if (!foundSdks.contains(homePath)) {
        foundSdks.add(homePath);
      }
    }
    myFieldWithHistory.setHistory(foundSdks);
    myPathField = new ComponentWithBrowseButton<>(myFieldWithHistory, null);
    myPathField.addBrowseFolderListener("Select Perl",
                                        "Select Perl SDK",
                                        null, new FileChooserDescriptor(false, true, false, false, false, false) {
        @Override
        public boolean isFileSelectable(VirtualFile file) {
          return super.isFileSelectable(file) && PerlSdkType.INSTANCE.isValidSdkHome(file.getPath());
        }
      },
                                        TextComponentAccessor.TEXT_FIELD_WITH_HISTORY_WHOLE_TEXT);

    setLayout(new MigLayout("ins 0, gap 10, fill, flowx"));
    add(myCbEnabled, "shrinkx");
    add(myPathField, "growx, pushx");

    InsertPathAction.addTo(myFieldWithHistory.getTextEditor());

    myCbEnabled.addActionListener(e -> enabledChanged());
    enabledChanged();

    setAnchor(myCbEnabled);

    updateUI();
  }

  private void enabledChanged() {
    final boolean pathEnabled = isPathEnabled();
    GuiUtils.enableChildren(myPathField, pathEnabled);
    myFieldWithHistory.invalidate(); //need to revalidate inner component
  }

  public String getPath() {
    return FileUtil.toSystemIndependentName(myPathField.getChildComponent().getText().trim());
  }

  private void setPath(@Nullable String path) {
    myPathField.getChildComponent().setText(StringUtil.notNullize(PathUtil.toSystemDependentName(path)));
  }

  public boolean isPathEnabled() {
    return myCbEnabled.isSelected();
  }

  private void setPathEnabled(boolean b) {
    myCbEnabled.setSelected(b);
    enabledChanged();
  }

  public void reset(@Nullable String path, boolean isEnabled) {
    setPathEnabled(isEnabled);
    setPath(path);
  }

  @Override
  public JComponent getAnchor() {
    return myAnchor;
  }

  @Override
  public void setAnchor(JComponent anchor) {
    myAnchor = anchor;
    myCbEnabled.setAnchor(anchor);
  }
}
