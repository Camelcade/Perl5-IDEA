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

package com.perl5.lang.perl.idea.run.prove;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDialog;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.RawCommandLineEditor;
import com.intellij.ui.components.fields.ExpandableTextField;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfigurationEditor;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfigurationEditorPanel;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfigurationProducer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration.FILES_JOINER;
import static com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration.FILES_PARSER;

class PerlTestRunConfigurationEditor extends GenericPerlRunConfigurationEditor<PerlAbstractTestRunConfiguration> {
  private final @NotNull PerlAbstractTestRunConfigurationProducer<PerlAbstractTestRunConfiguration> myProducer;

  public PerlTestRunConfigurationEditor(@NotNull Project project,
                                        @NotNull PerlAbstractTestRunConfigurationProducer<PerlAbstractTestRunConfiguration> producer) {
    super(project);
    myProducer = producer;
  }

  @Override
  protected @NotNull PerlTestRunConfigurationEditor.ArgumentsPanel createCommonArgumentsPanel() {
    return new ArgumentsPanel(myProject);
  }


  protected class ArgumentsPanel extends GenericPerlRunConfigurationEditorPanel<PerlAbstractTestRunConfiguration> {
    private JComboBox<Integer> myJobsCombobox;
    private LabeledComponent<JComboBox<Integer>> myLabeledJobsCombobox;

    private RawCommandLineEditor myTestScriptArgumentsEditor;
    private LabeledComponent<RawCommandLineEditor> myLabeledTestScriptArgumentsEditor;

    protected ArgumentsPanel(@NotNull Project project) {
      super(project);
    }

    @Override
    protected @NotNull String getProgramArgumentsLabel() {
      return PerlBundle.message("perl.run.test.runner.arguemnts");
    }

    @Override
    protected void createLabeledComponents() {
      super.createLabeledComponents();

      ArrayList<Integer> jobs = new ArrayList<>();
      for (int i = 1; i <= 32; i++) {
        jobs.add(i);
      }

      myJobsCombobox = new JComboBox<>(jobs.toArray(new Integer[0]));
      myLabeledJobsCombobox = LabeledComponent.create(myJobsCombobox, PerlBundle.message("perl.run.option.jobs.number"));
      myLabeledJobsCombobox.setLabelLocation(BorderLayout.WEST);

      myTestScriptArgumentsEditor = new RawCommandLineEditor();
      myLabeledTestScriptArgumentsEditor =
        LabeledComponent.create(myTestScriptArgumentsEditor, PerlBundle.message("perl.run.option.test.script.arguments"));
      myLabeledTestScriptArgumentsEditor.setLabelLocation(BorderLayout.WEST);
    }

    @Override
    protected void reset(PerlAbstractTestRunConfiguration runConfiguration) {
      super.reset(runConfiguration);
      myJobsCombobox.setSelectedItem(runConfiguration.getJobsNumber());
      myTestScriptArgumentsEditor.setText(runConfiguration.getTestScriptParameters());
    }

    @Override
    protected void applyTo(PerlAbstractTestRunConfiguration runConfiguration) {
      super.applyTo(runConfiguration);
      Object item = myJobsCombobox.getSelectedItem();
      runConfiguration.setJobsNumber(item instanceof Integer integer ? integer : PerlTestRunConfiguration.DEFAULT_JOBS_NUMBER);
      runConfiguration.setTestScriptParameters(myTestScriptArgumentsEditor.getText());
    }

    @Override
    protected @NotNull List<LabeledComponent<?>> getLabeledComponents() {
      List<LabeledComponent<?>> parentComponents = new ArrayList<>(super.getLabeledComponents());
      parentComponents.add(myLabeledJobsCombobox);
      parentComponents.add(myLabeledTestScriptArgumentsEditor);
      return parentComponents;
    }

    @Override
    protected @NotNull String getScriptFieldLabelText() {
      return PerlBundle.message("perl.run.prove.option.script.label");
    }

    @Override
    protected @NotNull TextFieldWithBrowseButton createTextFieldForScript() {
      TextFieldWithBrowseButton fieldWithBrowseButton = new TextFieldWithBrowseButton(new ExpandableTextField(FILES_PARSER, FILES_JOINER));
      fieldWithBrowseButton.addActionListener(e -> {
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(true, true, false, false, false, true) {
          @Override
          public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
            return getRunConfigurationProducer().isOurFile(file);
          }
        };
        fileChooserDescriptor.setTitle(PerlBundle.message("perl.run.prove.config.select.script.header"));
        fileChooserDescriptor.setDescription(PerlBundle.message("perl.run.prove.config.select.script.prompt"));

        List<VirtualFile> filesToSelect = GenericPerlRunConfiguration.computeVirtualFilesFromPaths(fieldWithBrowseButton.getText());
        final FileChooserDialog chooser =
          FileChooserFactory.getInstance().createFileChooser(fileChooserDescriptor, myProject, fieldWithBrowseButton);
        VirtualFile[] choosenFiles = chooser.choose(myProject, filesToSelect.toArray(VirtualFile.EMPTY_ARRAY));
        fieldWithBrowseButton.setText(StringUtil.join(ContainerUtil.map(choosenFiles, VirtualFile::getPath), " "));
      });
      return fieldWithBrowseButton;
    }

    @Override
    protected final @NotNull GenericPerlRunConfigurationProducer<PerlAbstractTestRunConfiguration> getRunConfigurationProducer() {
      return myProducer;
    }
  }
}
