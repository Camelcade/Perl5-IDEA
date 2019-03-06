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

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.util.ObjectUtils;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;

public class PerlTestRunConfigurationType extends ConfigurationTypeBase {
  public PerlTestRunConfigurationType() {
    super("PerlTestRunConfiguration", "Perl5 Test", "Runs perl5 tests using prove utility", PerlIcons.PERL_TEST_CONFIGURATION);
    addFactory(new ConfigurationFactory(this) {
      @NotNull
      @Override
      public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new PerlTestRunConfiguration(project, this, "Unnamed");
      }
    });
  }

  @NotNull
  public static PerlTestRunConfigurationType getInstance() {
    return ObjectUtils.notNull(CONFIGURATION_TYPE_EP.findExtension(PerlTestRunConfigurationType.class));
  }
}
