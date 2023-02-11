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

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.actions.PerlDumbAwareAction;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class CpanminusAdapter extends PackageManagerAdapter {
  public static final @NlsSafe String PACKAGE_NAME = "App::cpanminus";
  public static final @NlsSafe String SCRIPT_NAME = "cpanm";

  public CpanminusAdapter(@NotNull Sdk sdk, @Nullable Project project) {
    super(sdk, project);
  }

  @Override
  public @NotNull String getPresentableName() {
    return "cpanminus";
  }

  @Override
  protected @NotNull String getManagerScriptName() {
    return SCRIPT_NAME;
  }

  @Override
  protected @NotNull String getManagerPackageName() {
    return PACKAGE_NAME;
  }

  @Override
  protected @NotNull List<String> getInstallParameters(@NotNull Collection<String> packageNames) {
    return ContainerUtil.prepend(super.getInstallParameters(packageNames), "-v");
  }

  /**
   * Installs a cpanminus into {@code sdk} using {@code cpan}
   *
   * @see CpanAdapter
   */
  public static void install(@Nullable Project project, @Nullable Sdk sdk) {
    if (project == null || sdk == null) {
      return;
    }
    new CpanAdapter(sdk, project).install(PACKAGE_NAME);
  }

  public static void install(@Nullable Project project) {
    install(project, PerlProjectManager.getSdk(project));
  }

  /**
   * @return true iff App::cpanminus is available in sdk classpath
   */
  @Contract("null->false")
  public static boolean isAvailable(@Nullable Project project) {
    ApplicationManager.getApplication().assertReadAccessAllowed();
    return PerlRunUtil.findScript(project, SCRIPT_NAME) != null;
  }

  public static @Nullable AnAction createInstallAction(@NotNull Sdk sdk,
                                                       @Nullable Project project,
                                                       @NotNull Collection<String> libraryNames,
                                                       @Nullable Runnable actionCallback) {
    ApplicationManager.getApplication().assertReadAccessAllowed();
    return !isAvailable(project) ? null : new PerlDumbAwareAction(CpanAdapter.createInstallActionTitle(SCRIPT_NAME)) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        new CpanminusAdapter(sdk, project).install(libraryNames);
        if (actionCallback != null) {
          actionCallback.run();
        }
      }
    };
  }

  @Contract("null->null")
  public static @Nullable CpanminusAdapter create(@Nullable Project project) {
    Sdk sdk = PerlProjectManager.getSdk(project);
    return isAvailable(project) ? new CpanminusAdapter(sdk, project) : null;
  }
}
