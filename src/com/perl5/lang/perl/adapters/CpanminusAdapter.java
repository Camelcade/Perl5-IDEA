/*
 * Copyright 2015-2018 Alexandr Evstigneev
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
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class CpanminusAdapter extends PackageManagerAdapter {
  public static final String PACKAGE_NAME = "App::cpanminus";
  private static final String PACKAGE_PATH = PerlPackageUtil.getPackagePathByName(PACKAGE_NAME);
  public static final String SCRIPT_NAME = "cpanm";

  public CpanminusAdapter(@NotNull Sdk sdk, @NotNull Project project) {
    super(sdk, project);
  }

  @NotNull
  @Override
  public String getPresentableName() {
    return "cpanminus";
  }

  @NotNull
  @Override
  protected String getManagerScriptName() {
    return SCRIPT_NAME;
  }

  @NotNull
  @Override
  protected String getManagerPackageName() {
    return PACKAGE_NAME;
  }

  @NotNull
  @Override
  protected List<String> getInstallParameters(@NotNull Collection<String> packageNames) {
    return ContainerUtil.prepend(super.getInstallParameters(packageNames), "-v");
  }

  /**
   * Installs a cpanminus into {@code sdk} using {@code cpan}
   *
   * @see CpanAdapter
   */
  public static void install(@NotNull Project project, @NotNull Sdk sdk) {
    new CpanAdapter(sdk, project).install(PACKAGE_NAME);
  }

  /**
   * @return true iff App::cpanminus is available in sdk classpath
   */
  public static boolean isAvailable(@NotNull Sdk sdk) {
    ApplicationManager.getApplication().assertReadAccessAllowed();
    return PerlRunUtil.findScript(sdk, SCRIPT_NAME) != null;
  }

  @Nullable
  public static AnAction createInstallAction(@NotNull Sdk sdk,
                                             @NotNull Project project,
                                             @NotNull Collection<String> libraryNames,
                                             @Nullable Runnable actionCallback) {
    ApplicationManager.getApplication().assertReadAccessAllowed();
    return !isAvailable(sdk) ? null : new DumbAwareAction(PerlBundle.message("perl.quickfix.install.family", SCRIPT_NAME)) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        new CpanminusAdapter(sdk, project).install(libraryNames);
        if (actionCallback != null) {
          actionCallback.run();
        }
      }
    };
  }
}
