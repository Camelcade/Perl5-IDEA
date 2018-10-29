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

package com.perl5.lang.perl.idea.sdk.versionManager.plenv;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.projectRoots.Sdk;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlInstallAction;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlInstallFormOptions;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlenvInstallPerlAction extends PerlInstallAction {

  @Override
  protected boolean isEnabled(AnActionEvent event) {
    return PlenvData.from(getPerlSdk(event)) != null;
  }

  @NotNull
  @Override
  protected String doCleanDistributionItem(@NotNull String rawItem) {
    return rawItem;
  }

  @Override
  protected boolean doIsInstalled(@NotNull String rawItem) {
    return false;
  }

  @Nullable
  @Override
  protected PerlRealVersionManagerData getData(@Nullable Sdk sdk) {
    return PlenvData.from(sdk);
  }

  @NotNull
  @Override
  protected PerlVersionManagerAdapter createAdapter(@NotNull String vmPath, @NotNull PerlHostData hostData) {
    return new PlenvAdapter(vmPath, hostData);
  }

  @NotNull
  @Override
  protected PerlInstallFormOptions createOptionsForm() {
    return new PlenvInstallPerlForm();
  }
}
