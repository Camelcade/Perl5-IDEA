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

package com.perl5.lang.perl.profiler.configuration;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableProvider;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.profiler.configurations.ui.EditProfilerConfigurationsComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlProfilerConfigurationEditorConfigurableProvider extends ConfigurableProvider {
  public static final String PERL_PROFILER_CONFIGURABLE_ID = "profiler.perl5";

  @Override
  public @Nullable Configurable createConfigurable() {
    return new SearchableConfigurable.Delegate(new EditProfilerConfigurationsComponent(
      PerlProfilerConfigurationType.PERL_PROFILER_LANGUAGE_SETTINGS_GROUP, null)) {
      @Override
      public @NotNull String getId() {
        return PERL_PROFILER_CONFIGURABLE_ID;
      }
    };
  }
}
