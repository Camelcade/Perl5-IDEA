/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler.LIB_SEPARATOR;


/**
 * Real version manager, except the system one
 */
public abstract class PerlRealVersionManagerData<Data extends PerlRealVersionManagerData<Data, Handler>, Handler extends PerlRealVersionManagerHandler<Data, Handler>>
  extends PerlVersionManagerData<Data, Handler> {

  @Tag("versionManagerPath")
  private String myVersionManagerPath;
  @Tag("perlVersion")
  private String myPerlVersionString;
  @Tag("libName")
  private String myLibName;

  public PerlRealVersionManagerData(@NotNull Handler handler) {
    super(handler);
  }

  public PerlRealVersionManagerData(@NotNull String versionManagerPath, @NotNull String distributionId, @NotNull Handler handler) {
    this(handler);
    myVersionManagerPath = versionManagerPath;
    List<String> parts = StringUtil.split(distributionId, LIB_SEPARATOR);
    myPerlVersionString = parts.remove(0);
    if (!parts.isEmpty()) {
      myLibName = parts.get(0);
    }
  }

  public @NotNull String getVersionManagerPath() {
    return Objects.requireNonNull(myVersionManagerPath);
  }

  @Override
  public @Nullable String getSecondaryShortName() {
    return "[" + getDistributionId() + "]";
  }

  public @NotNull String getPerlVersionString() {
    return Objects.requireNonNull(myPerlVersionString);
  }

  public @Nullable String getLibName() {
    return myLibName;
  }

  public @NotNull String getDistributionId() {
    String versionString = getPerlVersionString();
    String libName = getLibName();
    return StringUtil.isEmpty(libName) ? versionString :
           StringUtil.isEmpty(versionString) ? "" :
           versionString + LIB_SEPARATOR + libName;
  }

  public static @NotNull PerlRealVersionManagerData<?, ?> notNullFrom(@NotNull Sdk sdk) {
    PerlVersionManagerData<?, ?> data = PerlVersionManagerData.notNullFrom(sdk);
    if (data instanceof PerlRealVersionManagerData) {
      return (PerlRealVersionManagerData<?, ?>)data;
    }
    throw new NullPointerException("Additional data supposed to be " + PerlRealVersionManagerData.class.getSimpleName() + " not " + data);
  }

  @Override
  public String toString() {
    return getHandler().getShortName() + "[" + getDistributionId() + "]";
  }

  @Override
  public @Nullable InstallPerlHandler getInstallPerlHandler() {
    return getHandler().createInstallHandler(getVersionManagerPath());
  }
}
