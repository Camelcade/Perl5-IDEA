/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ColoredListCellRenderer;
import org.jetbrains.annotations.NotNull;

public class Perl5RealSdkWrapper implements Perl5SdkWrapper {
  @NotNull
  private final Sdk mySdk;

  public Perl5RealSdkWrapper(@NotNull Sdk sdk) {
    mySdk = sdk;
  }

  @Override
  public void customizeRenderer(@NotNull ColoredListCellRenderer<Perl5SdkWrapper> renderer) {
    SdkType sdkType = (SdkType)mySdk.getSdkType();
    renderer.setIcon(sdkType.getIcon());
    renderer.append(getSdkString(mySdk));
  }


  @NotNull
  public Sdk getSdk() {
    return mySdk;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Perl5RealSdkWrapper)) return false;

    Perl5RealSdkWrapper wrapper = (Perl5RealSdkWrapper)o;

    return getSdk().equals(wrapper.getSdk());
  }

  @Override
  public int hashCode() {
    return getSdk().hashCode();
  }

  @Override
  public String toString() {
    return "SDK: " + mySdk;
  }

  @NotNull
  public static String getSdkString(@NotNull Sdk sdk) {
    String result = sdk.getName();
    String versionString = sdk.getVersionString();
    if (StringUtil.isNotEmpty(versionString)) {
      return result + " (" + versionString + ")";
    }
    return result;
  }
}
