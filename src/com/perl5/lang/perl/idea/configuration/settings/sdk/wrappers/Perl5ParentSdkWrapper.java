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
import com.intellij.ui.ColoredListCellRenderer;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5ProjectConfigurable;
import org.jetbrains.annotations.NotNull;

public class Perl5ParentSdkWrapper implements Perl5SdkWrapper {
  private final Perl5ProjectConfigurable myProjectConfigurable;

  public Perl5ParentSdkWrapper(Perl5ProjectConfigurable projectConfigurable) {
    myProjectConfigurable = projectConfigurable;
  }

  @Override
  public void customizeRenderer(@NotNull ColoredListCellRenderer<Perl5SdkWrapper> renderer) {
    Sdk selectedSdk = myProjectConfigurable.getSelectedSdk();
    if (selectedSdk == null) {
      renderer
        .append(PerlBundle.message("perl.settings.use.project.sdk", PerlBundle.message("perl.settings.unknown.sdk")));
      return;
    }

    SdkType sdkType = (SdkType)selectedSdk.getSdkType();

    renderer.setIcon(sdkType.getIcon());
    renderer.append(PerlBundle.message("perl.settings.use.project.sdk")).append("[");
    Perl5RealSdkWrapper.appendSdkString(renderer, selectedSdk);
    renderer.append("]");
  }

  @Override
  public String toString() {
    return "Parent SDK";
  }
}
