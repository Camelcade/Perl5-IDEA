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

import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5TextSdkWrapper;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public abstract class Perl5StructureConfigurable implements UnnamedConfigurable {
  protected static final Perl5SdkWrapper DISABLE_PERL_ITEM =
    new Perl5TextSdkWrapper(PerlBundle.message("perl.settings.disable.perl.support"));
  protected static final Perl5SdkWrapper NOT_SELECTED_ITEM = new Perl5TextSdkWrapper(PerlBundle.message("perl.settings.sdk.not.selected"));
  private Perl5StructurePanel myPanel;

  @Nullable
  @Override
  public JComponent createComponent() {
    if (myPanel == null) {
      initPanel();
    }
    return myPanel.getMainPanel();
  }

  public Perl5StructurePanel getPanel() {
    return myPanel;
  }

  @Nullable
  public Perl5SdkWrapper getSelectedItem() {
    return (Perl5SdkWrapper)myPanel.getSdkComboBox().getSelectedItem();
  }


  protected void initPanel() {
    myPanel = new Perl5StructurePanel();

    JComboBox<Perl5SdkWrapper> sdkComboBox = myPanel.getSdkComboBox();
    //noinspection unchecked
    sdkComboBox.setModel(new ListComboBoxModel<Perl5SdkWrapper>(getSdkItems()));
    sdkComboBox.setRenderer(new ColoredListCellRenderer<Perl5SdkWrapper>() {
      @Override
      protected void customizeCellRenderer(@NotNull JList<? extends Perl5SdkWrapper> list,
                                           Perl5SdkWrapper wrapper,
                                           int index,
                                           boolean selected,
                                           boolean hasFocus) {
        if (wrapper != null) {
          wrapper.customizeRenderer(this);
        }
      }
    });
    sdkComboBox.setSelectedItem(getDefaultSelectedItem());
  }

  @Nullable
  protected abstract Perl5SdkWrapper getDefaultSelectedItem();

  protected List<Perl5SdkWrapper> getSdkItems() {
    return ContainerUtil.map(ProjectJdkTable.getInstance().getAllJdks(), Perl5RealSdkWrapper::new);
  }

  @Override
  public boolean isModified() {
    Perl5SdkWrapper defaultItem = getDefaultSelectedItem();
    Perl5SdkWrapper selectedItem = getSelectedItem();
    if (selectedItem == defaultItem || selectedItem == null) {
      return false;
    }

    return !selectedItem.equals(defaultItem);
  }

  @Nullable
  public Sdk getSelectedSdk() {
    Perl5SdkWrapper selectedItem = getSelectedItem();
    return selectedItem instanceof Perl5RealSdkWrapper ? ((Perl5RealSdkWrapper)selectedItem).getSdk() : null;
  }
}
