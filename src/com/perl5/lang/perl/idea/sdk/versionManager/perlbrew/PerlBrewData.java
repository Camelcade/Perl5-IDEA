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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.ObjectUtils;
import com.intellij.util.xmlb.annotations.Tag;
import com.perl5.lang.perl.adapters.CpanminusAdapter;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.perl5.lang.perl.idea.sdk.versionManager.perlbrew.PerlBrewAdapter.*;

class PerlBrewData extends PerlRealVersionManagerData<PerlBrewData, PerlBrewHandler> {
  @Tag("info")
  private final Map<String, String> myInfo = new HashMap<>();

  PerlBrewData(@NotNull PerlBrewHandler handler) {
    super(handler);
  }

  /**
   * @param infoData see {@link PerlBrewHandler#computeInfoData}
   */
  PerlBrewData(@NotNull String versionManagerPath,
               @NotNull String distributionId,
               @NotNull PerlBrewHandler handler,
               @NotNull Map<String, String> infoData) {
    super(versionManagerPath, distributionId, handler);
    myInfo.putAll(infoData);
  }

  @NotNull
  @Override
  public List<File> getBinDirsPath() {
    String perlbrewRoot = getPerlbrewRoot();
    return perlbrewRoot == null ? Collections.emptyList() : Collections.singletonList(new File(perlbrewRoot, "bin"));
  }

  @Nullable
  private String getPerlbrewRoot() {
    return myInfo.get(PERLBREW_ROOT);
  }

  /**
   * @see PerlBrewAdapter#execWith(java.lang.String, java.lang.String...)
   */
  @NotNull
  @Override
  public PerlCommandLine patchCommandLine(@NotNull PerlCommandLine originalCommandLine) {
    return originalCommandLine.prependLineWith(getVersionManagerPath(), PERLBREW_EXEC, PERLBREW_QUIET, PERLBREW_WITH, getDistributionId());
  }

  @Override
  public void installCpanminus(@Nullable Project project) {
    PerlBrewAdapter perlBrewAdapter = create(project);
    if (perlBrewAdapter == null) {
      return;
    }
    perlBrewAdapter.runInstallInConsole(project, CpanminusAdapter.PACKAGE_NAME, PerlBrewAdapter.PERLBREW_INSTALL_CPANM);
  }

  @NotNull
  @Override
  protected final PerlBrewData self() {
    return this;
  }

  @Nullable
  public static PerlBrewData from(@Nullable Sdk sdk) {
    return ObjectUtils.tryCast(PerlVersionManagerData.from(sdk), PerlBrewData.class);
  }
}
