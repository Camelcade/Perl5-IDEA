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

package com.perl5.lang.perl.idea.sdk.host.docker;

import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.RawCommandLineEditor;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UIUtil;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class PerlDockerProjectSettingsConfigurable implements UnnamedConfigurable {
  @NotNull
  private final PerlDockerProjectSettings mySettings;

  private RawCommandLineEditor myArgumentsEditor;
  private RawCommandLineEditor myPreviewEditor;

  public PerlDockerProjectSettingsConfigurable(@NotNull Project project) {
    mySettings = PerlDockerProjectSettings.getInstance(project);
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    FormBuilder builder = FormBuilder.createFormBuilder();

    myArgumentsEditor = new RawCommandLineEditor();
    builder.addLabeledComponent(PerlDockerBundle.message("command.line.option.label"), myArgumentsEditor);

    myPreviewEditor = new RawCommandLineEditor();
    JTextField previewEditorTextField = myPreviewEditor.getTextField();
    previewEditorTextField.setForeground(UIUtil.getInactiveTextColor());
    previewEditorTextField.setEditable(false);
    builder.addLabeledComponent(PerlDockerBundle.message("command.line.preview.label"), myPreviewEditor);

    myArgumentsEditor.getTextField().getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        updatePreview();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        updatePreview();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        updatePreview();
      }
    });
    updatePreview();

    return builder.getPanel();
  }

  private void updatePreview() {
    PerlCommandLine dockerCommandLine = PerlDockerAdapter.buildBaseProcessCommandLine(new PerlCommandLine());
    dockerCommandLine.addParameters(StringUtil.split(myArgumentsEditor.getText(), " "));
    dockerCommandLine.addParameters("<project_and_helpers_volumes>", "<image_name>", "sh", "<shell_script_with_commands>");
    myPreviewEditor.setText(dockerCommandLine.getCommandLineString());
  }

  @Override
  public boolean isModified() {
    return !StringUtil.equals(mySettings.getAdditionalDockerParameters(), myArgumentsEditor.getText());
  }

  @Override
  public void apply() {
    if (!isModified()) {
      return;
    }
    mySettings.setAdditionalDockerParameters(myArgumentsEditor.getText());
  }

  @Override
  public void reset() {
    myArgumentsEditor.setText(mySettings.getAdditionalDockerParameters());
  }
}
