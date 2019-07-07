/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.configuration.module;

import com.intellij.ide.util.projectWizard.ModuleNameLocationSettings;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class MojoProjectGeneratorPeer extends PerlProjectGeneratorPeerBase<MojoProjectGenerationSettings> {
  private JBTextField myNameField;

  public MojoProjectGeneratorPeer() {
    super(new MojoProjectGenerationSettings());
  }

  @Override
  public void buildUI(@NotNull SettingsStep settingsStep) {
    super.buildUI(settingsStep);
    myNameField = new JBTextField();
    settingsStep.addSettingsField(getEntityLabel(), myNameField);
    ModuleNameLocationSettings moduleNameLocationSettings = settingsStep.getModuleNameLocationSettings();
    if (moduleNameLocationSettings != null) {
      myNameField.setText(moduleNameLocationSettings.getModuleName());
    }
  }

  protected abstract String getEntityLabel();

  @Nullable
  @Override
  public final ValidationInfo validate() {
    return isNameValid(myNameField.getText()) ? null : new ValidationInfo("Invalid name", myNameField);
  }

  protected abstract boolean isNameValid(@NotNull String name);

  @Override
  public void apply() {
    super.apply();
    getSettings().setEntityName(myNameField.getText());
  }

  @Override
  public void reset() {
    super.reset();
    myNameField.setText(getSettings().getEntityName());
  }
}
