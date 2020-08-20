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

import com.intellij.icons.AllIcons;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.profiler.api.configurations.ProfilerAttacher;
import com.intellij.profiler.api.configurations.ProfilerConfigurationTypeBase;
import com.intellij.profiler.api.configurations.ProfilerStarter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PerlProfilerConfigurationType extends ProfilerConfigurationTypeBase<PerlProfilerConfigurationState> {
  @NonNls static final @NotNull String ID = "com.perl5.profiler.nytprof";
  @NonNls static final String PERL_PROFILER_LANGUAGE_SETTINGS_GROUP = "profiler.perl5.group";
  private static final String CONFIGURATION_TYPE_NAME = "NYTProf";

  @Override
  public @NotNull String getDisplayName() {
    return CONFIGURATION_TYPE_NAME;
  }

  @Override
  public @Nullable String getHelpTopic() {
    return null;
  }

  @Override
  public @NotNull Icon getIcon() {
    return AllIcons.Actions.Profile;
  }

  @Override
  public @NotNull String getId() {
    return ID;
  }

  @Override
  public @Nullable String getLanguageSettingsGroup() {
    return PERL_PROFILER_LANGUAGE_SETTINGS_GROUP;
  }

  @Override
  public @Nullable ProfilerAttacher createAttacher(@NotNull PerlProfilerConfigurationState state) {
    return null;
  }

  @Override
  public @NotNull UnnamedConfigurable createConfigurable(@NotNull PerlProfilerConfigurationState state) {
    return new PerlProfilerConfigurationEditorConfigurable(state);
  }

  @Override
  public @Nullable ProfilerStarter createStarter(@NotNull PerlProfilerConfigurationState profilerConfigurationState) {
    return new PerlProfilerStarter(profilerConfigurationState);
  }

  @Override
  public @NotNull PerlProfilerConfigurationState getTemplateState() {
    var state = new PerlProfilerConfigurationState();
    state.setDisplayName(CONFIGURATION_TYPE_NAME);
    return state;
  }
}
