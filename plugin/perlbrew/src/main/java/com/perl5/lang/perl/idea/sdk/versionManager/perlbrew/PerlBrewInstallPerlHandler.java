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

package com.perl5.lang.perl.idea.sdk.versionManager.perlbrew;

import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.InstallPerlHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlInstallFormOptions;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

class PerlBrewInstallPerlHandler extends InstallPerlHandler {
  static final String PERL_BLEAD = "perl-blead";
  static final String PERL_STABLE = "perl-stable";
  static final List<String> PRE_DEFINED_VERSIONS = List.of(PERL_BLEAD, PERL_STABLE);

  static final String INSTALLED_PREFIX = "i ";

  public PerlBrewInstallPerlHandler(@NotNull String versionManagerPath,
                                    @NotNull PerlRealVersionManagerHandler<?, ?> versionManageHandler) {
    super(versionManagerPath, versionManageHandler);
  }

  @Override
  protected @NotNull PerlInstallFormOptions createOptionsForm() {
    return new PerlBrewInstallPerlForm();
  }

  @Override
  protected int doCompareVersions(String a, String b) {
    if( PRE_DEFINED_VERSIONS.contains(a)){
      return PRE_DEFINED_VERSIONS.contains(b) ? a.compareTo(b): -1;
    }
    else if(PRE_DEFINED_VERSIONS.contains(b)){
      return 1;
    }
    int wordIndex = a.indexOf("-");
    a = wordIndex == -1 ? a : a.substring(wordIndex + 1);
    wordIndex = b.indexOf("-");
    b = wordIndex == -1 ? a : b.substring(wordIndex + 1);
    return super.doCompareVersions(a, b);
  }

  @Override
  public @Nullable Icon doGetIcon(@NotNull String distribution) {
    return StringUtil.contains(distribution, "cperl") ? null : super.doGetIcon(distribution);
  }

  @Override
  protected @NotNull PerlVersionManagerAdapter createAdapter(@NotNull String vmPath, @NotNull PerlHostData<?, ?> hostData) {
    return new PerlBrewAdapter(vmPath, hostData);
  }

  @Override
  protected @NotNull String doCleanDistributionItem(@NotNull String rawItem) {
    return StringUtil.trimStart(rawItem, INSTALLED_PREFIX).trim();
  }

  @Override
  protected boolean doIsInstalled(@NotNull String rawItem) {
    return rawItem.startsWith(INSTALLED_PREFIX);
  }
}
