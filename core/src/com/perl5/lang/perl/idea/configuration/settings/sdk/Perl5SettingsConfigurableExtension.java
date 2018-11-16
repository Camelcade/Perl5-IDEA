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

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.modules.PerlSourceRootType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Perl5SettingsConfigurableExtension {
  ExtensionPointName<Perl5SettingsConfigurableExtension> EP_NAME = ExtensionPointName.create("com.perl5.settings.configurable.extension");

  @NotNull
  default List<PerlSourceRootType> getSourceRootTypes() {
    return Collections.emptyList();
  }

  @Nullable
  default Configurable createProjectConfigurable(@NotNull Project project) {return null;}

  static Stream<Perl5SettingsConfigurableExtension> stream() {
    return Arrays.stream(EP_NAME.getExtensions());
  }

  static void forEach(Consumer<Perl5SettingsConfigurableExtension> consumer) {
    for (Perl5SettingsConfigurableExtension extension : EP_NAME.getExtensions()) {
      consumer.accept(extension);
    }
  }
}
