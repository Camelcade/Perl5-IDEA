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

package com.perl5.lang.perl.idea.configuration.settings.sdk;


import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableProvider;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Perl5SettingsConfigurableProvider extends ConfigurableProvider {
  private final @NotNull Project myProject;

  public Perl5SettingsConfigurableProvider(@NotNull Project project) {
    myProject = project;
  }

  @Override
  public @Nullable Configurable createConfigurable() {
    return new Perl5SettingsConfigurable(myProject);
  }

  @Override
  public boolean canCreateConfigurable() {
    return !myProject.isDefault();
  }
}
