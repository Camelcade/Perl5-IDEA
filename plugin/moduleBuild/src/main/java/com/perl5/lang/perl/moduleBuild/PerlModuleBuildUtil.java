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

package com.perl5.lang.perl.moduleBuild;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PerlModuleBuildUtil {
  public static final @NotNull @NonNls String MODULE_BUILD = "Module::Build";
  public static final @NotNull @NonNls String BUILD_PL = "Build.PL";
  public static final @NotNull @NonNls String BUILD = "Build";

  private PerlModuleBuildUtil() {
  }

  @Contract("null->null")
  public static @Nullable VirtualFile findBuildPLFile(@Nullable Project project) {
    if (project == null) {
      return null;
    }
    for (Module module : ModuleManager.getInstance(project).getModules()) {
      VirtualFile buildPlFile = findBuildPLFile(module);
      if (buildPlFile != null) {
        return buildPlFile;
      }
    }
    return null;
  }

  @Contract("null->null")
  private static @Nullable VirtualFile findBuildPLFile(@Nullable Module module) {
    if (module == null) {
      return null;
    }
    for (VirtualFile contentRoot : ModuleRootManager.getInstance(module).getContentRoots()) {
      var buildPlFile = contentRoot.findChild(BUILD_PL);
      if (buildPlFile != null) {
        return buildPlFile;
      }
    }
    return null;
  }

  public static boolean isModuleBuildAvailable(@Nullable Project project) {
    return PerlProjectManager.isPerlEnabled(project) && findBuildPLFile(project) != null;
  }

  public static boolean isModuleBuildAvailable(@Nullable Module module) {
    return PerlProjectManager.isPerlEnabled(module) && findBuildPLFile(module) != null;
  }
}
