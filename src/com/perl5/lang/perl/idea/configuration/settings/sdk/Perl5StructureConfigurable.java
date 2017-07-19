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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5TextSdkWrapper;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public abstract class Perl5StructureConfigurable implements UnnamedConfigurable, ProjectJdkTable.Listener {
  protected static final Perl5SdkWrapper DISABLE_PERL_ITEM =
    new Perl5TextSdkWrapper(PerlBundle.message("perl.settings.disable.perl.support"));
  protected static final Perl5SdkWrapper NOT_SELECTED_ITEM = new Perl5TextSdkWrapper(PerlBundle.message("perl.settings.sdk.not.selected"));
  private Perl5StructurePanel myPanel;
  private boolean myChange = false;

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

    // combo box
    JComboBox<Perl5SdkWrapper> sdkComboBox = myPanel.getSdkComboBox();
    //noinspection unchecked
    sdkComboBox.setModel(new CollectionComboBoxModel<>(getSdkItems()));
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

    ApplicationManager.getApplication().getMessageBus().connect().subscribe(PerlSdkTable.PERL_TABLE_TOPIC, this);

    // add sdk button
    myPanel.getAddButton().addActionListener(e -> SdkConfigurationUtil.selectSdkHome(PerlSdkType.getInstance(), this::addSdk));
  }

  private void updateSdkModel(@Nullable Perl5SdkWrapper selectedItem) {
    JComboBox<Perl5SdkWrapper> sdkComboBox = myPanel.getSdkComboBox();
    List<Perl5SdkWrapper> allItems = getSdkItems();
    if (selectedItem == null || !allItems.contains(selectedItem)) {
      selectedItem = getDefaultSelectedItem();
    }
    sdkComboBox.setModel(new CollectionComboBoxModel<>(allItems, selectedItem));
  }

  private void addSdk(@NotNull String home) {
    File perlDir = new File(home);
    if (perlDir.exists() && perlDir.isFile()) {
      home = perlDir.getParent();
    }
    PerlSdkType sdkType = PerlSdkType.getInstance();
    String newSdkName = SdkConfigurationUtil.createUniqueSdkName(sdkType,
                                                                 home,
                                                                 Arrays.asList(PerlSdkTable.getInstance().getAllJdks()));
    final ProjectJdkImpl newSdk = new ProjectJdkImpl(newSdkName, sdkType);
    newSdk.setHomePath(home);
    sdkType.setupSdkPaths(newSdk);
    // should we check for version string?
    myChange = true;
    PerlSdkTable.getInstance().addJdk(newSdk);
  }

  @Nullable
  protected abstract Perl5SdkWrapper getDefaultSelectedItem();

  protected List<Perl5SdkWrapper> getSdkItems() {
    return ContainerUtil.map(PerlSdkTable.getInstance().getAllJdks(), Perl5RealSdkWrapper::new);
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

  @Override
  public void jdkAdded(Sdk sdk) {
    Perl5SdkWrapper itemToSelect = getSelectedItem();
    if (myChange) {
      myChange = false;
      itemToSelect = new Perl5RealSdkWrapper(sdk);
    }
    updateSdkModel(itemToSelect);
  }

  @Override
  public void jdkRemoved(Sdk sdk) {
    updateSdkModel(getSelectedItem());
  }

  @Override
  public void jdkNameChanged(Sdk jdk, String previousName) {

  }
}
