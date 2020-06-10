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

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author VISTALL
 * @since 16-Sep-15
 */
public class PerlRunConfigurationType extends ConfigurationTypeBase {
  public PerlRunConfigurationType() {
    super("#PerlConfigurationType", "Perl", "Runs perl5 scripts", PerlIcons.PERL_LANGUAGE_ICON);

    addFactory(new ConfigurationFactory(this) {
      @Override
      public @NotNull String getId() {
        return "Perl";
      }

      @Override
      public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new PerlRunConfiguration(project, this, "Unnamed");
      }
    });
  }

  public static @NotNull PerlRunConfigurationType getInstance() {
    return Objects.requireNonNull(CONFIGURATION_TYPE_EP.findExtension(PerlRunConfigurationType.class));
  }
}
