/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.modules.PerlLibrarySourceRootType;
import com.perl5.lang.perl.idea.modules.PerlSourceRootType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class Perl5SettingsConfigurableExtensionImpl implements Perl5SettingsConfigurableExtension {
  @NotNull
  @Override
  public List<PerlSourceRootType> getSourceRootTypes() {
    return Collections.singletonList(PerlLibrarySourceRootType.INSTANCE);
  }

  @Nullable
  @Override
  public Configurable createProjectConfigurable(@NotNull Project project) {
    return new Perl5ProjectConfigurable(project);
  }
}
