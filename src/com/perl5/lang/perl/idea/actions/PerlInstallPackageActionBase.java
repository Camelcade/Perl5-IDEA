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

package com.perl5.lang.perl.idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.adapters.PackageManagerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public abstract class PerlInstallPackageActionBase extends PerlActionBase {

  public PerlInstallPackageActionBase(@NotNull String managerName) {
    getTemplatePresentation().setText(PerlBundle.message("perl.action.install.packages", managerName));
  }

  @Override
  protected boolean isEnabled(AnActionEvent event) {
    return super.isEnabled(event) && getAdapter(event) != null;
  }

  @Override
  protected boolean alwaysHideDisabled() {
    return true;
  }

  @Nullable
  protected abstract PackageManagerAdapter getAdapter(@NotNull AnActionEvent e);

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    PackageManagerAdapter packageManagerAdapter = getAdapter(e);
    if (packageManagerAdapter == null) {
      return;
    }

    String packageNames = Messages.showInputDialog(
      getEventProject(e),
      "Enter packages names",
      getTemplatePresentation().getText(),
      null
    );

    if (StringUtil.isEmpty(packageNames)) {
      return;
    }

    packageManagerAdapter.install(Arrays.asList(packageNames.split("[^:\\w_]+")));
  }
}
