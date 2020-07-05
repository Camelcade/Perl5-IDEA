/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.run;

import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfigurationEditor;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfigurationEditorPanel;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfigurationProducer;
import org.jetbrains.annotations.NotNull;

class PerlRunConfigurationEditor extends GenericPerlRunConfigurationEditor<PerlRunConfiguration> {
  public PerlRunConfigurationEditor(Project project) {
    super(project);
  }

  @Override
  protected @NotNull GenericPerlRunConfigurationEditorPanel<PerlRunConfiguration> createCommonParametersPanel() {
    return new ParametersPanel(myProject);
  }

  private static class ParametersPanel extends GenericPerlRunConfigurationEditorPanel<PerlRunConfiguration> {
    public ParametersPanel(@NotNull Project project) {
      super(project);
    }

    @Override
    protected @NotNull GenericPerlRunConfigurationProducer<PerlRunConfiguration> getRunConfigurationProducer() {
      return PerlRunConfigurationProducer.getInstance();
    }
  }
}
