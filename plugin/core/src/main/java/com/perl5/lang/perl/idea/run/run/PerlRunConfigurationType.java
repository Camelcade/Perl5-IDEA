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

package com.perl5.lang.perl.idea.run.run;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.run.PerlRunConfigurationFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PerlRunConfigurationType extends ConfigurationTypeBase {
  private final @NotNull PerlRunConfigurationFactory myScriptConfigurationFactory;

  public PerlRunConfigurationType() {
    super("#PerlConfigurationType", PerlBundle.message("perl"), PerlBundle.message("runs.perl5.scripts"), PerlIcons.PERL_LANGUAGE_ICON);

    myScriptConfigurationFactory = new PerlRunConfigurationFactory(this) {
      @Override
      public @NotNull String getId() {
        return "Perl";
      }

      @Override
      public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new PerlRunConfiguration(project, this, "Unnamed");
      }
    };
    addFactory(myScriptConfigurationFactory);
  }

  public @NotNull PerlRunConfigurationFactory getScriptConfigurationFactory() {
    return myScriptConfigurationFactory;
  }

  public static @NotNull PerlRunConfigurationType getInstance() {
    return Objects.requireNonNull(CONFIGURATION_TYPE_EP.findExtension(PerlRunConfigurationType.class));
  }
}
