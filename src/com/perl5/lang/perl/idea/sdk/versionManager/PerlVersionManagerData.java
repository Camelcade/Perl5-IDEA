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

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.sdk.AbstractPerlData;
import com.perl5.lang.perl.idea.sdk.PerlSdkAdditionalData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Data necessary for version manager. E.g. system, perbrew, plenv, asdf, berrybrew
 */
public abstract class PerlVersionManagerData<Data extends PerlVersionManagerData<Data, Handler>, Handler extends PerlVersionManagerHandler<Data, Handler>>
  extends AbstractPerlData<Data, Handler> {

  public PerlVersionManagerData(@NotNull Handler handler) {
    super(handler);
  }

  /**
   * Patched commandline according to the rules of the version manager and current version manager data
   *
   * @param originalCommandLine non-patched commandline
   * @return patched or original commandline
   */
  @NotNull
  public abstract GeneralCommandLine patchCommandLine(@NotNull GeneralCommandLine originalCommandLine);

  @Contract("null->null")
  @Nullable
  static PerlVersionManagerData from(@Nullable Sdk sdk) {
    return ObjectUtils.doIfNotNull(PerlSdkAdditionalData.from(sdk), PerlSdkAdditionalData::getVersionManagerData);
  }
}
