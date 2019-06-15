/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public final class MojoUtil {
  public static final String MOJO_PACKAGE_NAME = "Mojolicious";
  private static final String MOJO_SCRIPT_NAME = "mojo";

  private MojoUtil() {
  }

  /**
   * @return a mojo script virtual file for {@code sdk} if any
   */
  @Contract("null->null")
  @Nullable
  public static VirtualFile getMojoScript(@Nullable Sdk sdk) {
    return PerlRunUtil.findScript(sdk, MOJO_SCRIPT_NAME);
  }

  /**
   * @return a mojo script virtual file for {@code event} if any
   */
  @Contract("null->null")
  @Nullable
  public static VirtualFile getMojoScript(@Nullable AnActionEvent event) {
    return getMojoScript(PerlProjectManager.getSdk(event));
  }

  /**
   * @return a mojo script virtual file for {@code project} if any
   */
  @Contract("null->null")
  @Nullable
  public static VirtualFile getMojoScript(@Nullable Project project) {
    return getMojoScript(PerlProjectManager.getSdk(project));
  }

  /**
   * @return iff mojolicious is available in the context of {@code event}
   */
  @Contract("null->false")
  public static boolean isMojoAvailable(@Nullable AnActionEvent event) {
    return getMojoScript(event) != null;
  }

  /**
   * @return iff mojolicious is available in the context of {@code dataContext}
   */
  @Contract("null->false")
  public static boolean isMojoAvailable(@Nullable DataContext dataContext) {
    return dataContext != null && getMojoScript(CommonDataKeys.PROJECT.getData(dataContext)) != null;
  }
}
