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

package com.perl5.lang.perl.profiler.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.util.ClearableLazyValue;
import com.perl5.lang.perl.profiler.PerlProfilerBundle;
import com.perl5.lang.perl.profiler.run.PerlProfilerStartupMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

class PerlProfilerConfigurationEditorConfigurable implements UnnamedConfigurable {
  private final ClearableLazyValue<PerlProfilerConfigurationEditor> myEditorProvider =
    ClearableLazyValue.create(PerlProfilerConfigurationEditor::new);
  private final @NotNull PerlProfilerConfigurationState myState;

  public PerlProfilerConfigurationEditorConfigurable(@NotNull PerlProfilerConfigurationState state) {
    myState = state;
  }

  @Override
  public @Nullable JComponent createComponent() {
    return getEditor().myPanel;
  }

  private @NotNull PerlProfilerConfigurationEditor getEditor() {
    return myEditorProvider.getValue();
  }

  @Override
  public boolean isModified() {
    var editor = getEditor();
    return myState.isOptimizerDisabled() != editor.myOptimizerCheckBox.isSelected() ||
           myState.getStartupMode() != editor.myStartModeComboBox.getSelectedItem();
  }

  @Override
  public void apply() throws ConfigurationException {
    var editor = getEditor();
    var startMode = editor.myStartModeComboBox.getSelectedItem();
    if (startMode == null) {
      throw new ConfigurationException(PerlProfilerBundle.message("dialog.message.please.select.startup.mode"));
    }
    myState.setStartupMode((PerlProfilerStartupMode)startMode);
    myState.setOptimizerDisabled(editor.myOptimizerCheckBox.isSelected());
  }

  @Override
  public void reset() {
    var editor = getEditor();
    editor.myStartModeComboBox.setSelectedItem(myState.getStartupMode());
    editor.myOptimizerCheckBox.setSelected(myState.isOptimizerDisabled());
  }
}
