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

package com.perl5.lang.perl.idea.sdk;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.implementation.PerlImplementationData;
import com.perl5.lang.perl.idea.sdk.implementation.PerlImplementationHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerHandler;
import org.jdom.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PerlSdkAdditionalData implements SdkAdditionalData {
  private final @NotNull PerlHostData<?, ?> myHostData;
  private final @NotNull PerlVersionManagerData<?, ?> myVersionManagerData;
  private final @NotNull PerlImplementationData<?, ?> myImplementationData;


  public PerlSdkAdditionalData(@NotNull PerlHostData<?, ?> hostData,
                               @NotNull PerlVersionManagerData<?, ?> versionManagerData,
                               @NotNull PerlImplementationData<?, ?> implementationData) {
    myHostData = hostData;
    myVersionManagerData = versionManagerData;
    myImplementationData = implementationData;
  }

  public @NotNull PerlHostData<?, ?> getHostData() {
    return myHostData;
  }

  public @NotNull PerlVersionManagerData<?, ?> getVersionManagerData() {
    return myVersionManagerData;
  }

  public @NotNull PerlImplementationData<?, ?> getImplementationData() {
    return myImplementationData;
  }

  @Contract("null -> null")
  public static @Nullable PerlSdkAdditionalData from(@Nullable Sdk sdk) {
    return sdk == null ? null : ObjectUtils.tryCast(sdk.getSdkAdditionalData(), PerlSdkAdditionalData.class);
  }

  public static @NotNull PerlSdkAdditionalData notNullFrom(@NotNull Sdk sdk) {
    return Objects.requireNonNull(from(sdk), () -> "No additional data in " + sdk);
  }

  void save(@NotNull Element target) {
    myHostData.save(target);
    myImplementationData.save(target);
    myVersionManagerData.save(target);
  }

  static @NotNull PerlSdkAdditionalData load(@NotNull Element source) {
    // fixme shouldn't we handle data corruption?
    return new PerlSdkAdditionalData(
      PerlHostHandler.load(source),
      PerlVersionManagerHandler.load(source),
      PerlImplementationHandler.load(source));
  }
}
