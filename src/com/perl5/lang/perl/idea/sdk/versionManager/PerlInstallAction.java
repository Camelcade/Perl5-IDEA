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

package com.perl5.lang.perl.idea.sdk.versionManager;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.projectRoots.Sdk;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.actions.PerlActionBase;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PerlInstallAction extends PerlActionBase implements DumbAware {
  public PerlInstallAction() {
    Presentation presentation = getTemplatePresentation();
    presentation.setText(PerlBundle.message("perl.vm.install.perl"));
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    super.update(event);
    InstallPerlHandler installHandler = getHandler(event);
    event.getPresentation().setIcon(installHandler == null ? null : installHandler.getVersionManageHandler().getIcon());
  }

  @Override
  protected boolean alwaysHideDisabled() {
    return true;
  }

  @Override
  protected boolean isEnabled(AnActionEvent event) {
    return getHandler(event) != null;
  }

  private InstallPerlHandler getHandler(AnActionEvent event) {
    Sdk perlSdk = getPerlSdk(event);
    return perlSdk == null ? null : PerlVersionManagerData.notNullFrom(perlSdk).getInstallPerlHandler();
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    InstallPerlHandler handler = getHandler(e);
    if (handler != null) {
      handler.install(Objects.requireNonNull(PerlHostData.from(getPerlSdk(e))), Objects.requireNonNull(getEventProject(e)));
    }
  }
}
