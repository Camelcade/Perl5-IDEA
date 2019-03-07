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

package com.perl5.lang.perl.idea.run;

import com.intellij.execution.CommonProgramRunConfigurationParameters;
import com.intellij.execution.ui.CommonProgramParametersPanel;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.RawCommandLineEditor;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ObjectUtils;
import com.intellij.util.ui.UIUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkConfigurable;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkManipulator;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public abstract class GenericPerlRunConfigurationEditorPanel<Configuration extends GenericPerlRunConfiguration>
  extends CommonProgramParametersPanel implements Perl5SdkManipulator {
  @Nullable
  private Sdk mySdkProxy;

  @NotNull
  private Project myProject;

  private LabeledComponent<?> myScriptLabeledField;
  private TextFieldWithBrowseButton myScriptField;

  private LabeledComponent<?> myLabeledConsoleCharset;
  private ComboBox myConsoleCharset;

  private LabeledComponent<RawCommandLineEditor> myLabeledPerlParametersPanel;
  private RawCommandLineEditor myPerlParametersPanel;

  private JBCheckBox myAlternativeSdkCheckbox;
  private Perl5SdkConfigurable mySdkConfigurable;

  public GenericPerlRunConfigurationEditorPanel(@NotNull Project project) {
    myProject = project;
    mySdkConfigurable.setProject(project);
  }

  @Override
  protected void addComponents() {
    createConsoleEncodingField();
    createPerlParametersField();
    createScriptField();

    add(myScriptLabeledField);
    add(myLabeledConsoleCharset);
    add(myLabeledPerlParametersPanel);
    super.addComponents();
    add(createAlternativeSdkPanel());

    setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 5, true, false));
    setProgramParametersLabel(getProgramParametersLabel());
  }

  @NotNull
  protected String getProgramParametersLabel() {
    return PerlBundle.message("perl.run.option.script.parameters");
  }

  protected void createPerlParametersField() {
    myPerlParametersPanel = new RawCommandLineEditor();
    myLabeledPerlParametersPanel = LabeledComponent.create(myPerlParametersPanel, PerlBundle.message("perl.run.option.perl.parameters"));
    myLabeledPerlParametersPanel.setLabelLocation(BorderLayout.WEST);
    copyDialogCaption(myLabeledPerlParametersPanel);
  }

  private void createConsoleEncodingField() {
    myConsoleCharset = new ComboBox(new CollectionComboBoxModel(new ArrayList<>(Charset.availableCharsets().keySet())));
    myLabeledConsoleCharset = LabeledComponent.create(myConsoleCharset, PerlBundle.message("perl.run.option.output.encoding"));
    myLabeledConsoleCharset.setLabelLocation(BorderLayout.WEST);
  }

  private void createScriptField() {
    myScriptField = new TextFieldWithBrowseButton();
    myScriptField.addBrowseFolderListener(
      PerlBundle.message("perl.run.config.select.script.header"),
      PerlBundle.message("perl.run.config.select.script.prompt"),
      myProject,
      FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor().withFileFilter(
        getRunConfigurationProducer()::isOurFile), TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);

    myScriptField.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
      @Override
      protected void textChanged(@NotNull DocumentEvent documentEvent) {
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(myScriptField.getText());
        if (file != null) {
          myConsoleCharset.setSelectedItem(file.getCharset().displayName());
        }
        else {
          myConsoleCharset.setSelectedItem(null);
        }
      }
    });
    myScriptLabeledField = LabeledComponent.create(myScriptField, PerlBundle.message("perl.run.option.script"));
    myScriptLabeledField.setLabelLocation(BorderLayout.WEST);
  }

  @NotNull
  private JPanel createAlternativeSdkPanel() {
    JPanel alternativeSdkPanel = new JPanel(new MigLayout("ins 0, gap 10, fill, flowx"));
    myAlternativeSdkCheckbox = new JBCheckBox();
    mySdkConfigurable = new Perl5SdkConfigurable(this, null);
    alternativeSdkPanel.add(myAlternativeSdkCheckbox, "shrinkx");
    JComponent sdkComponent = mySdkConfigurable.createComponent();
    mySdkConfigurable.setLabelText(PerlBundle.message("perl.run.config.use.alternative.label"));
    alternativeSdkPanel.add(sdkComponent, "growx, pushx");
    myAlternativeSdkCheckbox.addChangeListener(e -> mySdkConfigurable.setEnabled(myAlternativeSdkCheckbox.isSelected()));
    return alternativeSdkPanel;
  }

  @Override
  protected void setupAnchor() {
    super.setupAnchor();
    myAnchor = UIUtil.mergeComponentsWithAnchor(this, myScriptLabeledField, myLabeledPerlParametersPanel, myLabeledConsoleCharset);
  }

  protected void reset(Configuration runConfiguration) {
    myScriptField.setText(runConfiguration.getScriptPath());
    myConsoleCharset.setSelectedItem(runConfiguration.getConsoleCharset());
    myPerlParametersPanel.setText(runConfiguration.getPerlParameters());
    myAlternativeSdkCheckbox.setSelected(runConfiguration.isUseAlternativeSdk());
    mySdkConfigurable.setEnabled(runConfiguration.isUseAlternativeSdk());
    mySdkProxy = PerlSdkTable.getInstance().findJdk(runConfiguration.getAlternativeSdkName());
    mySdkConfigurable.reset();
    super.reset(runConfiguration);
  }

  protected void applyTo(Configuration runConfiguration) {
    runConfiguration.setScriptPath(myScriptField.getText());
    runConfiguration.setConsoleCharset(StringUtil.nullize((String)myConsoleCharset.getSelectedItem(), true));
    runConfiguration.setPerlParameters(myPerlParametersPanel.getText());
    runConfiguration.setUseAlternativeSdk(myAlternativeSdkCheckbox.isSelected());
    mySdkConfigurable.apply();
    runConfiguration.setAlternativeSdkName(ObjectUtils.doIfNotNull(mySdkProxy, Sdk::getName));
    super.applyTo(runConfiguration);
  }

  @Override
  public final void reset(CommonProgramRunConfigurationParameters configuration) {
    throw new RuntimeException("Should not be invoked");
  }

  @Override
  public final void applyTo(CommonProgramRunConfigurationParameters configuration) {
    throw new RuntimeException("Should not be invoked");
  }

  public void disposeUIResources() {
    mySdkConfigurable.disposeUIResources();
  }

  @Override
  public Perl5SdkWrapper getCurrentSdkWrapper() {
    return mySdkProxy == null ? null : new Perl5RealSdkWrapper(mySdkProxy);
  }

  @Override
  public void setSdk(@Nullable Sdk sdk) {
    mySdkProxy = sdk;
  }

  /**
   * @return a run configuration producer for the run configuration
   */
  @NotNull
  protected abstract GenericPerlRunConfigurationProducer<Configuration> getRunConfigurationProducer();
}
