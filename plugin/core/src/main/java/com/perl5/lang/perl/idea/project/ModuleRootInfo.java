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

package com.perl5.lang.perl.idea.project;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

class ModuleRootInfo {
  private final @NotNull Set<VirtualFile> myLibRoots = new LinkedHashSet<>();
  private final @NotNull Set<VirtualFile> myExcludedRoots = new LinkedHashSet<>();
  private final @NotNull Set<VirtualFile> myTestRoots = new LinkedHashSet<>();

  void addLibRoot(@Nullable VirtualFile root) {
    if (root != null) {
      myLibRoots.add(root);
    }
  }

  void addExcludedRoot(@Nullable VirtualFile root) {
    if (root != null) {
      myExcludedRoots.add(root);
    }
  }

  void addTestRoot(@Nullable VirtualFile root) {
    if (root != null) {
      myTestRoots.add(root);
    }
  }

  @NotNull Set<VirtualFile> getLibRoots() {
    return myLibRoots;
  }

  @NotNull Set<VirtualFile> getExcludedRoots() {
    return myExcludedRoots;
  }

  @NotNull Set<VirtualFile> getTestRoots() {
    return myTestRoots;
  }
}
