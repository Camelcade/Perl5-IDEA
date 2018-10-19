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

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.Tag;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.perl5.lang.perl.idea.sdk.versionManager.perlbrew.PerlBrewHandler.LIB_SEPARATOR;

class PerlBrewData extends PerlVersionManagerData<PerlBrewData, PerlBrewHandler> {
  @Tag("versionManagerPath")
  private String myVersionManagerPath;
  @Tag("perlVersion")
  private String myPerlVersionString;
  @Tag("libName")
  private String myLibName;

  public PerlBrewData(@NotNull PerlBrewHandler handler) {
    super(handler);
  }

  public PerlBrewData(@NotNull String versionManagerPath, @NotNull String distributionId, @NotNull PerlBrewHandler handler) {
    this(handler);
    myVersionManagerPath = versionManagerPath;
    List<String> parts = StringUtil.split(distributionId, LIB_SEPARATOR);
    myPerlVersionString = parts.remove(0);
    if (!parts.isEmpty()) {
      myLibName = parts.get(0);
    }
  }

  @Nullable
  String getDistributionId() {
    String versionString = getPerlVersionString();
    String libName = getLibName();
    return StringUtil.isEmpty(libName) ? versionString :
           StringUtil.isEmpty(versionString) ? null :
           versionString + LIB_SEPARATOR + libName;
  }

  @Nullable
  String getVersionManagerPath() {
    return myVersionManagerPath;
  }

  @NotNull
  @Override
  public String getShortName() {
    return super.getShortName() + ", " + getDistributionId();
  }

  @Nullable
  String getPerlVersionString() {
    return myPerlVersionString;
  }

  @Nullable
  String getLibName() {
    return myLibName;
  }

  @NotNull
  @Override
  public GeneralCommandLine patchCommandLine(@NotNull GeneralCommandLine originalCommandLine) {
    return originalCommandLine;
  }

  @Override
  protected final PerlBrewData self() {
    return this;
  }
}
