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
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.ui.JBUI;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5TextSdkWrapper;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import org.apache.batik.ext.swing.GridBagConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public abstract class Perl5StructureConfigurable implements UnnamedConfigurable, ProjectJdkTable.Listener {
  protected static final Perl5SdkWrapper DISABLE_PERL_ITEM =
    new Perl5TextSdkWrapper(PerlBundle.message("perl.settings.disable.perl.support"));
  protected static final Perl5SdkWrapper NOT_SELECTED_ITEM = new Perl5TextSdkWrapper(PerlBundle.message("perl.settings.sdk.not.selected"));
  private Perl5StructurePanel myPanel;
  private boolean myChange = false;
  private final MessageBusConnection myConnection;

  public Perl5StructureConfigurable() {
    myConnection = ApplicationManager.getApplication().getMessageBus().connect();
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    if (myPanel == null) {
      initPanel();
    }
    return myPanel.getMainPanel();
  }

  @Nullable
  public Perl5SdkWrapper getSelectedSdkWrapper() {
    ComboBox<Perl5SdkWrapper> sdkComboBox = myPanel.getSdkComboBox();
    return sdkComboBox == null ? null : (Perl5SdkWrapper)sdkComboBox.getSelectedItem();
  }


  private void initPanel() {
    myPanel = new Perl5StructurePanel();

    // combo box
    JComboBox<Perl5SdkWrapper> sdkComboBox = myPanel.getSdkComboBox();
    //noinspection unchecked
    sdkComboBox.setModel(new CollectionComboBoxModel<>(getAllSdkWrappers()));
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
    sdkComboBox.addItemListener(e -> {
      if (e.getStateChange() != ItemEvent.SELECTED) {
        return;
      }
      updateSdkButtons();
    });

    myConnection.subscribe(PerlSdkTable.PERL_TABLE_TOPIC, this);

    // add sdk button
    myPanel.getAddButton().addActionListener(e -> SdkConfigurationUtil.selectSdkHome(PerlSdkType.INSTANCE, this::addSdk));
    myPanel.getDeleteButton().addActionListener(this::removeSdk);
    myPanel.getEditButton().addActionListener(this::renameSdk);
    myPanel.getSdkPanel().setVisible(isSdkPanelVisible());

    myPanel.getMainPanel().add(
      getAdditionalPanel(),
      new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstants.BOTH, JBUI.emptyInsets(), 0, 0));
  }

  private void updateSdkButtons() {
    boolean buttonsState = getSelectedSdk() != null;
    myPanel.getDeleteButton().setEnabled(buttonsState);
    myPanel.getEditButton().setEnabled(buttonsState);
  }

  protected abstract JComponent getAdditionalPanel();

  protected boolean isSdkPanelVisible() {
    return true;
  }

  protected void renameSdk(ActionEvent e) {
    Sdk selectedSdk = getSelectedSdk();
    if (selectedSdk == null) {
      return;
    }
    PerlSdkTable perlSdkTable = PerlSdkTable.getInstance();
    Messages.showInputDialog(
      myPanel.getSdkComboBox(),
      PerlBundle.message("perl.rename.sdk.text"),
      PerlBundle.message("perl.rename.sdk.title"),
      PerlIcons.PERL_LANGUAGE_ICON,
      selectedSdk.getName(),
      new InputValidator() {
        @Override
        public boolean checkInput(String inputString) {
          if (StringUtil.isEmpty(inputString)) {
            return false;
          }
          Sdk perlSdk = perlSdkTable.findJdk(inputString);
          return perlSdk == null || perlSdk.equals(selectedSdk);
        }

        @Override
        public boolean canClose(String inputString) {
          if (StringUtil.equals(selectedSdk.getName(), inputString)) {
            return true;
          }
          SdkModificator modificator = selectedSdk.getSdkModificator();
          modificator.setName(inputString);
          modificator.commitChanges();
          myPanel.getSdkComboBox().repaint();
          return true;
        }
      }
    );
  }

  protected void removeSdk(ActionEvent e) {
    Sdk selectedSdk = getSelectedSdk();
    if (selectedSdk != null) {
      PerlSdkTable.getInstance().removeJdk(selectedSdk);
    }
  }

  private void updateSdkModel(@Nullable Perl5SdkWrapper itemToSelect) {
    JComboBox<Perl5SdkWrapper> sdkComboBox = myPanel.getSdkComboBox();
    List<Perl5SdkWrapper> allItems = getAllSdkWrappers();
    if (itemToSelect == null || !allItems.contains(itemToSelect)) {
      itemToSelect = allItems.isEmpty() ? null : allItems.get(0);
    }
    sdkComboBox.setModel(new CollectionComboBoxModel<>(allItems, itemToSelect));
    updateSdkButtons();
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

  @NotNull
  protected abstract Perl5SdkWrapper getCurrentSdkWrapper();

  protected List<Perl5SdkWrapper> getAllSdkWrappers() {
    return ContainerUtil.map(PerlSdkTable.getInstance().getAllJdks(), Perl5RealSdkWrapper::new);
  }

  @Override
  public boolean isModified() {
    return isSdkModified();
  }

  private boolean isSdkModified() {
    return !getCurrentSdkWrapper().equals(getSelectedSdkWrapper());
  }

  @Nullable
  public Sdk getSelectedSdk() {
    Perl5SdkWrapper selectedWrapper = getSelectedSdkWrapper();
    return selectedWrapper instanceof Perl5RealSdkWrapper ? ((Perl5RealSdkWrapper)selectedWrapper).getSdk() : null;
  }

  @Override
  public void jdkAdded(@NotNull Sdk sdk) {
    Perl5SdkWrapper itemToSelect = getSelectedSdkWrapper();
    if (myChange) {
      myChange = false;
      itemToSelect = new Perl5RealSdkWrapper(sdk);
    }
    updateSdkModel(itemToSelect);
  }

  @Override
  public void jdkRemoved(@NotNull Sdk sdk) {
    updateSdkModel(getSelectedSdkWrapper());
  }

  @Override
  public void jdkNameChanged(@NotNull Sdk jdk, @NotNull String previousName) {
  }

  @Override
  public void reset() {
    myPanel.getSdkComboBox().setSelectedItem(getCurrentSdkWrapper());
    updateSdkButtons();
  }

  @Override
  public void apply() throws ConfigurationException {
    if (isSdkModified()) {
      setSdk(getSelectedSdk());
    }
  }

  protected abstract void setSdk(@Nullable Sdk sdk);

  @Override
  public void disposeUIResources() {
    myConnection.deliverImmediately();
    myConnection.disconnect();
    myPanel = null;
  }
}
