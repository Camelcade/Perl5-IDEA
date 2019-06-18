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

package com.perl5.lang.mojolicious.idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.projectRoots.Sdk;
import com.perl5.lang.mojolicious.MojoBundle;
import com.perl5.lang.mojolicious.MojoIcons;
import com.perl5.lang.mojolicious.MojoUtil;
import com.perl5.lang.perl.adapters.PackageManagerAdapter;
import com.perl5.lang.perl.idea.actions.PerlActionBase;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;

public class InstallMojoliciousAction extends PerlActionBase {
  public InstallMojoliciousAction() {
    super(MojoBundle.message("mojo.action.install.action.title"), MojoIcons.MOJO_APP_ICON);
  }

  @Override
  protected boolean isEnabled(AnActionEvent event) {
    return super.isEnabled(event) && !MojoUtil.isMojoAvailable(event);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Sdk sdk = PerlProjectManager.getSdk(e);
    if (sdk == null) {
      return;
    }
    PackageManagerAdapter.create(sdk, getEventProject(e)).install(MojoUtil.MOJO_PACKAGE_NAME);
  }
}
