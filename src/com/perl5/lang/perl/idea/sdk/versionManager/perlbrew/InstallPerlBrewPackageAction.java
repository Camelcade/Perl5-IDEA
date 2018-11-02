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

package com.perl5.lang.perl.idea.sdk.versionManager.perlbrew;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;

public abstract class InstallPerlBrewPackageAction extends PerlBrewActionBase {
  private final String myPackageName;

  protected InstallPerlBrewPackageAction(@NotNull String packageName) {
    myPackageName = packageName;
    Presentation templatePresentation = getTemplatePresentation();
    templatePresentation.setText(PerlBundle.message("perl.vm.perlbrew.install.action", packageName));
  }

  @Override
  protected boolean alwaysHideDisabled() {
    return true;
  }

  @NotNull
  protected abstract String getScriptName();

  @NotNull
  protected abstract String getInstallCommand();

  @Override
  protected boolean isEnabled(AnActionEvent event) {
    return super.isEnabled(event) && PerlRunUtil.findScript(getEventProject(event), getScriptName()) == null;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Project project = getEventProject(e);
    PerlBrewAdapter perlBrewAdapter = PerlBrewAdapter.create(project);
    if (perlBrewAdapter == null) {
      return;
    }
    perlBrewAdapter.runInstallInConsole(project, myPackageName, getInstallCommand());
  }
}
