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

package com.perl5.lang.mojolicious;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public final class MojoUtil {
  public static final String MOJO_PACKAGE_NAME = "Mojolicious";
  public static final String MOJO_PLUGIN_PACKAGE_NAME = "Mojolicious::Plugin";
  private static final String MOJO_SCRIPT_NAME = "mojo";
  public static final String DEFAULT_TEMPLATES_DIR_NAME = "templates";

  private MojoUtil() {
  }

  /**
   * @return a mojo script virtual file for {@code sdk} if any
   */
  @Contract("null->null")
  public static @Nullable VirtualFile getMojoScript(@Nullable Project project) {
    return PerlRunUtil.findScript(project, MOJO_SCRIPT_NAME);
  }

  /**
   * @return a mojo script virtual file for {@code event} if any
   */
  @Contract("null->null")
  public static @Nullable VirtualFile getMojoScript(@Nullable AnActionEvent event) {
    return getMojoScript(event.getProject());
  }

  /**
   * @return a mojo script virtual file for {@code module} if any
   */
  @Contract("null->null")
  public static @Nullable VirtualFile getMojoScript(@Nullable Module module) {
    return getMojoScript(module.getProject());
  }

  /**
   * @return iff mojolicious is available in the context of {@code event}
   */
  @Contract("null->false")
  public static boolean isMojoAvailable(@Nullable AnActionEvent event) {
    return getMojoScript(event) != null;
  }

  /**
   * @return iff mojolicious is available in the context of {@code module}
   */
  @Contract("null->false")
  public static boolean isMojoAvailable(@Nullable Module module) {
    return getMojoScript(module) != null;
  }

  /**
   * @return iff mojolicious is available in the context of {@code project}
   */
  @Contract("null->false")
  public static boolean isMojoAvailable(@Nullable Project project) {
    return getMojoScript(project) != null;
  }

  /**
   * @return iff mojolicious is available in the context of {@code dataContext}
   */
  @Contract("null->false")
  public static boolean isMojoAvailable(@Nullable DataContext dataContext) {
    return dataContext != null && getMojoScript(CommonDataKeys.PROJECT.getData(dataContext)) != null;
  }
}
