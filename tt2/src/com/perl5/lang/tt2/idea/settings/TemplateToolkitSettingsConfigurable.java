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

package com.perl5.lang.tt2.idea.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.FormBuilder;
import com.perl5.lang.perl.util.PerlConfigurationUtil;
import com.perl5.lang.tt2.TemplateToolkitBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 05.06.2016.
 */
public class TemplateToolkitSettingsConfigurable implements Configurable {
  private final TemplateToolkitSettings mySettings;
  private final Project myProject;
  @SuppressWarnings("Since15")
  protected CollectionListModel<String> substitutedExtensionsModel;
  protected JBList<String> substitutedExtensionsList;
  protected JPanel substitutedExtensionsPanel;
  private JTextField startTagField;
  private JTextField endTagField;
  private JTextField outlineTagField;
  private JCheckBox enableAnycaseCheckBox;


  public TemplateToolkitSettingsConfigurable(Project project) {
    mySettings = TemplateToolkitSettings.getInstance(project);
    myProject = project;
  }

  @Nls
  @Override
  public String getDisplayName() {
    return "Template Toolkit 2";
  }

  @Nullable
  @Override
  public String getHelpTopic() {
    return null;
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    FormBuilder builder = FormBuilder.createFormBuilder();
    builder.getPanel().setLayout(new VerticalFlowLayout());

    builder.addLabeledComponent(
      TemplateToolkitBundle.message("ttk2.label.opentag"),
      startTagField = new JTextField()
    );

    builder.addLabeledComponent(
      TemplateToolkitBundle.message("ttk2.label.closetag"),
      endTagField = new JTextField()
    );

    builder.addLabeledComponent(
      TemplateToolkitBundle.message("ttk2.label.outlinetag"),
      outlineTagField = new JTextField()
    );

    builder.addComponent(
      enableAnycaseCheckBox = new JCheckBox(TemplateToolkitBundle.message("ttk2.label.enableanycase"))
    );

    //noinspection Since15
    substitutedExtensionsModel = new CollectionListModel<>();
    substitutedExtensionsList = new JBList<>(substitutedExtensionsModel);
    substitutedExtensionsPanel =
      PerlConfigurationUtil.createSubstituteExtensionPanel(substitutedExtensionsModel, substitutedExtensionsList);
    builder.addLabeledComponent(new JLabel(TemplateToolkitBundle.message("ttk2.configuration.extension")), substitutedExtensionsPanel);

    return builder.getPanel();
  }

  @Override
  public boolean isModified() {
    return mySettings.ENABLE_ANYCASE != enableAnycaseCheckBox.isSelected() ||
           !StringUtil.equals(mySettings.START_TAG, startTagField.getText()) ||
           !StringUtil.equals(mySettings.END_TAG, endTagField.getText()) ||
           !StringUtil.equals(mySettings.OUTLINE_TAG, outlineTagField.getText()) ||
           !mySettings.substitutedExtensions.equals(substitutedExtensionsModel.getItems())
      ;
  }

  @Override
  public void apply() throws ConfigurationException {
    if (StringUtil.isEmpty(startTagField.getText())) {
      throw new ConfigurationException(TemplateToolkitBundle.message("ttk2.error.emptyopentag"));
    }
    if (StringUtil.isEmpty(endTagField.getText())) {
      throw new ConfigurationException(TemplateToolkitBundle.message("ttk2.error.emptyclosetag"));
    }
    if (!isModified()) {
      return;
    }
    mySettings.START_TAG = startTagField.getText();
    mySettings.END_TAG = endTagField.getText();
    mySettings.OUTLINE_TAG = outlineTagField.getText();
    mySettings.ENABLE_ANYCASE = enableAnycaseCheckBox.isSelected();

    mySettings.substitutedExtensions.clear();
    mySettings.substitutedExtensions.addAll(substitutedExtensionsModel.getItems());

    mySettings.settingsUpdated();
  }

  @Override
  public void reset() {
    startTagField.setText(mySettings.START_TAG);
    endTagField.setText(mySettings.END_TAG);
    outlineTagField.setText(mySettings.OUTLINE_TAG);
    enableAnycaseCheckBox.setSelected(mySettings.ENABLE_ANYCASE);
    substitutedExtensionsModel.removeAll();
    substitutedExtensionsModel.add(mySettings.substitutedExtensions);
  }

  @Override
  public void disposeUIResources() {
  }
}
