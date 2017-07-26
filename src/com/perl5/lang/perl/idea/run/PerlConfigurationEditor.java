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

import com.intellij.execution.ui.CommonProgramParametersPanel;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.RawCommandLineEditor;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * @author VISTALL
 * @since 16-Sep-15
 */
public class PerlConfigurationEditor extends PerlConfigurationEditorBase<PerlConfiguration> {
  private TextFieldWithBrowseButton myScriptField;
  private CommonProgramParametersPanel myParametersPanel;
  private ComboBox myConsoleCharset;
  private RawCommandLineEditor myPerlParametersPanel;
  private PerlAlternativeSdkPanel myAlternativeSdkPanel;

  public PerlConfigurationEditor(Project project) {
    super(project);
  }

  @Override
  protected void resetEditorFrom(@NotNull PerlConfiguration perlConfiguration) {
    myScriptField.setText(perlConfiguration.getScriptPath());
    myParametersPanel.reset(perlConfiguration);
    myConsoleCharset.setSelectedItem(perlConfiguration.getConsoleCharset());
    myPerlParametersPanel.setText(perlConfiguration.getPerlParameters());
    myAlternativeSdkPanel.reset(perlConfiguration.getAlternativeSdkPath(), perlConfiguration.isUseAlternativeSdk());
    super.resetEditorFrom(perlConfiguration);
  }

  @Override
  protected void applyEditorTo(@NotNull PerlConfiguration perlConfiguration) throws ConfigurationException {
    perlConfiguration.setScriptPath(myScriptField.getText());
    myParametersPanel.applyTo(perlConfiguration);
    perlConfiguration.setConsoleCharset(StringUtil.nullize((String)myConsoleCharset.getSelectedItem(), true));
    perlConfiguration.setPerlParameters(myPerlParametersPanel.getText());
    perlConfiguration.setUseAlternativeSdk(myAlternativeSdkPanel.isPathEnabled());
    perlConfiguration.setAlternativeSdkPath(myAlternativeSdkPanel.getPath());
    super.applyEditorTo(perlConfiguration);
  }

  @Nullable
  @Override
  protected JComponent getGeneralComponent() {
    myScriptField = new TextFieldWithBrowseButton();
    myScriptField.addBrowseFolderListener(
      PerlBundle.message("perl.run.config.select.script.header"),
      PerlBundle.message("perl.run.config.select.script.prompt"),
      myProject,
      FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor().withFileFilter(
        PerlConfigurationProducer::isExecutableFile), TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);

    myConsoleCharset = new ComboBox(new CollectionComboBoxModel(new ArrayList<>(Charset.availableCharsets().keySet())));

    myScriptField.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
      @Override
      protected void textChanged(DocumentEvent documentEvent) {
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(myScriptField.getText());
        if (file != null) {
          myConsoleCharset.setSelectedItem(file.getCharset().displayName());
        }
        else {
          myConsoleCharset.setSelectedItem(null);
        }
      }
    });

    myAlternativeSdkPanel = new PerlAlternativeSdkPanel();

    myParametersPanel = new CommonProgramParametersPanel() {
      @Override
      protected void addComponents() {

        LabeledComponent<?> scriptLabel = LabeledComponent.create(myScriptField, PerlBundle.message("perl.run.option.script"));
        scriptLabel.setLabelLocation(BorderLayout.WEST);
        add(scriptLabel);

        LabeledComponent<?> consoleEncoding =
          LabeledComponent.create(myConsoleCharset, PerlBundle.message("perl.run.option.output.encoding"));
        consoleEncoding.setLabelLocation(BorderLayout.WEST);
        add(consoleEncoding);

        myPerlParametersPanel = new RawCommandLineEditor();
        LabeledComponent<RawCommandLineEditor> perlParametersPanel =
          LabeledComponent.create(myPerlParametersPanel, PerlBundle.message("perl.run.option.perl.parameters"));
        perlParametersPanel.setLabelLocation(BorderLayout.WEST);
        copyDialogCaption(perlParametersPanel);
        add(perlParametersPanel);

        super.addComponents();
        add(myAlternativeSdkPanel);

        setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 5, true, false));
      }
    };
    myParametersPanel.setProgramParametersLabel(PerlBundle.message("perl.run.option.script.parameters"));
    return myParametersPanel;
  }
}
