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

package com.perl5.lang.perl.idea.run.prove;

import com.intellij.openapi.project.Project;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfigurationEditor;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfigurationProducer;
import org.jetbrains.annotations.NotNull;

class PerlTestRunConfigurationEditor extends GenericPerlRunConfigurationEditor<PerlTestRunConfiguration> {
  public PerlTestRunConfigurationEditor(Project project) {
    super(project);
  }

  @NotNull
  @Override
  protected GenericPerlRunConfigurationProducer<PerlTestRunConfiguration> getRunConfigurationProducer() {
    return PerlTestRunConfigurationProducer.getInstance();
  }

  @Override
  protected CommonParametersPanel createCommonParametersPanel() {
    return new FormPanel();
  }

  private class FormPanel extends CommonParametersPanel {
    @NotNull
    @Override
    protected String getProgramParametersLabel() {
      return PerlBundle.message("perl.run.option.prove.parameters");
    }
  }
}
