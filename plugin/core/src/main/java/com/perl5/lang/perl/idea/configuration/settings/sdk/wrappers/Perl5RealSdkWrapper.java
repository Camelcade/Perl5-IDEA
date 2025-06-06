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

package com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Perl5RealSdkWrapper implements Perl5SdkWrapper {
  private final @NotNull Sdk mySdk;

  public Perl5RealSdkWrapper(@NotNull Sdk sdk) {
    mySdk = sdk;
  }

  @Override
  public void customizeRenderer(@NotNull ColoredListCellRenderer<Perl5SdkWrapper> renderer) {
    SdkType sdkType = (SdkType)mySdk.getSdkType();
    renderer.setIcon(sdkType.getIcon());
    appendSdkString(renderer, mySdk);
  }

  @Override
  public @NotNull Sdk getSdk() {
    return mySdk;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Perl5RealSdkWrapper realSdkWrapper)) {
      return false;
    }

    return getSdk().equals(realSdkWrapper.getSdk());
  }

  @Override
  public int hashCode() {
    return getSdk().hashCode();
  }

  @Override
  public String toString() {
    return "SDK: " + mySdk;
  }

  public static void appendSdkString(@NotNull ColoredListCellRenderer<Perl5SdkWrapper> renderer, @NotNull Sdk sdk) {
    PerlHostData<?, ?> hostData = PerlHostData.notNullFrom(sdk);
    renderPair(renderer, hostData.getPrimaryShortName(), hostData.getSecondaryShortName());
    PerlVersionManagerData<?, ?> versionManagerData = PerlVersionManagerData.notNullFrom(sdk);
    renderPair(renderer, versionManagerData.getPrimaryShortName(), versionManagerData.getSecondaryShortName());
    renderer.append(StringUtil.notNullize(sdk.getVersionString()));
  }

  private static void renderPair(@NotNull ColoredListCellRenderer<Perl5SdkWrapper> renderer,
                                 @NlsSafe @NotNull String primary,
                                 @NlsSafe @Nullable String secondary) {
    renderer.append(StringUtil.capitalize(primary));
    if (secondary != null) {
      renderer.append(secondary, SimpleTextAttributes.GRAY_ATTRIBUTES);
    }
    renderer.append(", ");
  }

  @Contract("null->null;!null->!null")
  public static @Nullable Perl5RealSdkWrapper create(@Nullable Sdk sdk) {
    return sdk == null ? null : new Perl5RealSdkWrapper(sdk);
  }
}
