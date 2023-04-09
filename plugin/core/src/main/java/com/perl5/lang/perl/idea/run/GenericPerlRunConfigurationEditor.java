/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class GenericPerlRunConfigurationEditor<Configuration extends GenericPerlRunConfiguration>
  extends PerlConfigurationEditorBase<Configuration> {
  private GenericPerlRunConfigurationEditorPanel<Configuration> myArgumentsPanel;

  public GenericPerlRunConfigurationEditor(Project project) {
    super(project);
  }

  @Override
  protected void resetEditorFrom(@NotNull Configuration perlRunConfiguration) {
    myArgumentsPanel.reset(perlRunConfiguration);
    super.resetEditorFrom(perlRunConfiguration);
  }

  @Override
  protected void applyEditorTo(@NotNull Configuration perlRunConfiguration) throws ConfigurationException {
    myArgumentsPanel.applyTo(perlRunConfiguration);
    super.applyEditorTo(perlRunConfiguration);
  }

  @Override
  protected final @NotNull JComponent getGeneralComponent() {
    return myArgumentsPanel = createCommonArgumentsPanel();
  }

  protected abstract @NotNull GenericPerlRunConfigurationEditorPanel<Configuration> createCommonArgumentsPanel();

  @Override
  protected void disposeEditor() {
    super.disposeEditor();
    myArgumentsPanel.disposeUIResources();
  }
}
