/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.*;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.fields.ExpandableTextField;
import com.intellij.util.ObjectUtils;
import com.intellij.util.ui.UIUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkConfigurable;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkManipulator;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration.PREREQUISITES_JOINER;
import static com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration.PREREQUISITES_PARSER;

public abstract class GenericPerlRunConfigurationEditorPanel<Configuration extends GenericPerlRunConfiguration>
  extends CommonProgramParametersPanel implements Perl5SdkManipulator {
  private @Nullable Sdk mySdkProxy;

  private final @NotNull Project myProject;

  private LabeledComponent<?> myScriptLabeledField;
  private JPanel myScriptField;

  private LabeledComponent<?> myLabeledConsoleCharset;
  private ComboBox<String> myConsoleCharset;

  private LabeledComponent<RawCommandLineEditor> myLabeledPerlArgumentsPanel;
  private RawCommandLineEditor myPerlArgumentsPanel;

  private JBCheckBox myAlternativeSdkCheckbox;
  private Perl5SdkConfigurable mySdkConfigurable;

  private LabeledComponent<ExpandableTextField> myRequiredModulesLabeledField;

  public GenericPerlRunConfigurationEditorPanel(@NotNull Project project) {
    myProject = project;
    mySdkConfigurable.setProject(project);
  }

  @Override
  protected void addComponents() {
    createLabeledComponents();
    add(myScriptLabeledField);
    super.addComponents();
    getLabeledComponents().forEach(this::add);
    add(createAlternativeSdkPanel());

    setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 5, true, false));
    setProgramParametersLabel(getProgramArgumentsLabel());
  }

  protected void createLabeledComponents() {
    createConsoleEncodingField();
    createPerlArgumentsField();
    createScriptField();
    createPrerequisitesField();
  }

  protected void createPrerequisitesField() {
    myRequiredModulesLabeledField = createLabeledComponent(
      new ExpandableTextField(PREREQUISITES_PARSER, PREREQUISITES_JOINER),
      PerlBundle.message("label.required.modules"),
      PerlBundle.message("tooltip.comma.separated.list.modules.required.to.run.configuration"));
  }

  protected static @NotNull <T extends JComponent> LabeledComponent<T> createLabeledComponent(@NotNull T component,
                                                                                              @NotNull @NlsContexts.Label String label,
                                                                                              @Nullable @NlsContexts.Tooltip String tooltip) {
    var labeledComponent = LabeledComponent.create(component, label, BorderLayout.WEST);
    if (tooltip != null) {
      labeledComponent.setToolTipText(tooltip);
      component.setToolTipText(tooltip);
    }
    return labeledComponent;
  }

  protected @NotNull List<LabeledComponent<?>> getLabeledComponents() {
    return Arrays.asList(myLabeledPerlArgumentsPanel, myLabeledConsoleCharset, myRequiredModulesLabeledField);
  }

  protected @Nls @NotNull String getProgramArgumentsLabel() {
    return PerlBundle.message("perl.run.option.script.arguments");
  }

  protected void createPerlArgumentsField() {
    myPerlArgumentsPanel = new RawCommandLineEditor();
    myLabeledPerlArgumentsPanel = LabeledComponent.create(myPerlArgumentsPanel, PerlBundle.message("perl.run.option.perl.arguments"));
    myLabeledPerlArgumentsPanel.setLabelLocation(BorderLayout.WEST);
    copyDialogCaption(myLabeledPerlArgumentsPanel);
  }

  private void createConsoleEncodingField() {
    myConsoleCharset = new ComboBox<>(new CollectionComboBoxModel<>(new ArrayList<>(Charset.availableCharsets().keySet())));
    myLabeledConsoleCharset = LabeledComponent.create(myConsoleCharset, PerlBundle.message("perl.run.option.output.encoding"));
    myLabeledConsoleCharset.setLabelLocation(BorderLayout.WEST);
  }

  protected @NotNull TextFieldWithBrowseButton createTextFieldForScript() {
    TextFieldWithBrowseButton fieldWithBrowseButton = new TextFieldWithBrowseButton();
    fieldWithBrowseButton.addBrowseFolderListener(
      myProject,
      FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor()
        .withFileFilter(getRunConfigurationProducer()::isOurFile)
        .withTitle(PerlBundle.message("perl.run.config.select.script.header"))
        .withDescription(PerlBundle.message("perl.run.config.select.script.prompt")),
      TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
    return fieldWithBrowseButton;
  }

  protected @NotNull JPanel doCreateScriptField() {
    TextFieldWithBrowseButton scriptField = createTextFieldForScript();

    scriptField.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
      @Override
      protected void textChanged(@NotNull DocumentEvent documentEvent) {
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(scriptField.getText());
        if (file != null) {
          myConsoleCharset.setSelectedItem(file.getCharset().displayName());
        }
        else {
          myConsoleCharset.setSelectedItem(null);
        }
      }
    });
    return scriptField;
  }

  protected @NlsContexts.Label @NotNull String getScriptFieldLabelText() {
    return PerlBundle.message("perl.run.option.script");
  }

  private void createScriptField() {
    myScriptField = doCreateScriptField();
    if (!(myScriptField instanceof TextAccessor)) {
      throw new RuntimeException("Script field must be a TextAccessor");
    }
    myScriptLabeledField = LabeledComponent.create(myScriptField, getScriptFieldLabelText());
    myScriptLabeledField.setLabelLocation(BorderLayout.WEST);
  }

  private @NotNull JPanel createAlternativeSdkPanel() {
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
  protected final void setupAnchor() {
    super.setupAnchor();
    List<PanelWithAnchor> components = new ArrayList<>(getLabeledComponents());
    components.addFirst(myScriptLabeledField);
    components.addFirst(this);
    myAnchor = UIUtil.mergeComponentsWithAnchor(components);
  }

  protected void reset(Configuration runConfiguration) {
    ((TextAccessor)myScriptField).setText(runConfiguration.getScriptPath());
    myRequiredModulesLabeledField.getComponent().setText(runConfiguration.getRequiredModules());
    myConsoleCharset.setSelectedItem(runConfiguration.getConsoleCharset());
    myPerlArgumentsPanel.setText(runConfiguration.getPerlArguments());
    myAlternativeSdkCheckbox.setSelected(runConfiguration.isUseAlternativeSdk());
    mySdkConfigurable.setEnabled(runConfiguration.isUseAlternativeSdk());
    mySdkProxy = PerlSdkTable.getInstance().findJdk(runConfiguration.getAlternativeSdkName());
    mySdkConfigurable.reset();
    super.reset(runConfiguration);
  }

  protected void applyTo(Configuration runConfiguration) {
    runConfiguration.setRequiredModules(myRequiredModulesLabeledField.getComponent().getText());
    runConfiguration.setScriptPath(((TextAccessor)myScriptField).getText());
    runConfiguration.setConsoleCharset(StringUtil.nullize((String)myConsoleCharset.getSelectedItem(), true));
    runConfiguration.setPerlArguments(myPerlArgumentsPanel.getText());
    runConfiguration.setUseAlternativeSdk(myAlternativeSdkCheckbox.isSelected());
    mySdkConfigurable.apply();
    runConfiguration.setAlternativeSdkName(ObjectUtils.doIfNotNull(mySdkProxy, Sdk::getName));
    super.applyTo(runConfiguration);
  }

  @Override
  public final void reset(@NotNull CommonProgramRunConfigurationParameters configuration) {
    throw new RuntimeException("Should not be invoked");
  }

  @Override
  public final void applyTo(@NotNull CommonProgramRunConfigurationParameters configuration) {
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
  protected abstract @NotNull GenericPerlRunConfigurationProducer<? extends Configuration> getRunConfigurationProducer();
}
