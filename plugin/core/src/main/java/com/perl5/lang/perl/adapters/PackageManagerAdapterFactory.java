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

package com.perl5.lang.perl.adapters;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface PackageManagerAdapterFactory<T extends PackageManagerAdapter> {
  ExtensionPointName<PackageManagerAdapterFactory<?>> EP_NAME = ExtensionPointName.create("com.perl5.packageManagerAdapterFactory");

  @Nullable T createAdapter(@NotNull Sdk sdk, @Nullable Project project);

  /**
   * Creates an adapter, prefers cpanminus over cpan
   */
  static @NotNull PackageManagerAdapter create(@NotNull Sdk sdk, @Nullable Project project) {
    for (PackageManagerAdapterFactory<?> factory : EP_NAME.getExtensionList()) {
      PackageManagerAdapter adapter = factory.createAdapter(sdk, project);
      if (adapter != null) {
        return adapter;
      }
    }
    throw new RuntimeException("CPAN adapter expected to be always available");
  }

  static <F extends PackageManagerAdapterFactory<? extends PackageManagerAdapter>> @NotNull F findInstance(@NotNull Class<F> cls) {
    return Objects.requireNonNull(EP_NAME.findFirstAssignableExtension(cls));
  }
}
