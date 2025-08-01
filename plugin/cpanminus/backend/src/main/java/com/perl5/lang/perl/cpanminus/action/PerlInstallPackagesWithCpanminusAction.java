/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.cpanminus.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.perl5.lang.perl.adapters.PackageManagerAdapter;
import com.perl5.lang.perl.cpanminus.adapter.CpanminusAdapter;
import com.perl5.lang.perl.idea.actions.PerlInstallPackageActionBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlInstallPackagesWithCpanminusAction extends PerlInstallPackageActionBase {
  public PerlInstallPackagesWithCpanminusAction() {
    super(CpanminusAdapter.SCRIPT_NAME);
  }

  @Override
  protected @Nullable PackageManagerAdapter getAdapter(@NotNull AnActionEvent e) {
    return CpanminusAdapter.create(e.getProject());
  }
}
