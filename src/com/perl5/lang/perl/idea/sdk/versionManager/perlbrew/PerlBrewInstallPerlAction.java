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
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlInstallAction;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlInstallFormOptions;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlBrewInstallPerlAction extends PerlInstallAction {

  @Override
  protected boolean isEnabled(AnActionEvent event) {
    return PerlBrewData.from(getPerlSdk(event)) != null;
  }

  @Nullable
  @Override
  protected PerlBrewData getData(@Nullable Sdk perlSdk) {
    return PerlBrewData.from(perlSdk);
  }

  @NotNull
  @Override
  protected PerlInstallFormOptions createOptionsForm() {
    return new PerlBrewInstallPerlForm();
  }

  @Override
  protected int doCompareVersions(String a, String b) {
    int wordIndex = a.indexOf("-");
    a = wordIndex == -1 ? a : a.substring(wordIndex + 1);
    wordIndex = b.indexOf("-");
    b = wordIndex == -1 ? a : b.substring(wordIndex + 1);
    return super.doCompareVersions(a, b);
  }

  @NotNull
  @Override
  protected PerlVersionManagerAdapter createAdapter(@NotNull String vmPath, @NotNull PerlHostData hostData) {
    return new PerlBrewAdapter(vmPath, hostData);
  }

  @NotNull
  @Override
  protected String doCleanDistributionItem(@NotNull String rawItem) {
    return StringUtil.trimStart(rawItem, "i ").trim();
  }

  @Override
  protected boolean doIsInstalled(@NotNull String rawItem) {
    return rawItem.startsWith("i ");
  }

}
