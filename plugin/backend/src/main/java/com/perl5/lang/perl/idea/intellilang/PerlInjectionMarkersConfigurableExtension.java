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

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurableExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlInjectionMarkersConfigurableExtension implements Perl5SettingsConfigurableExtension {
  @Override
  public @Nullable Configurable createProjectConfigurable(@NotNull Project project) {
    return new PerlInjectionMarkersTable(project);
  }
}
