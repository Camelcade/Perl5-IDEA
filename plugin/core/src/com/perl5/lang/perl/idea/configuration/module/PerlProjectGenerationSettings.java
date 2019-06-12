/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.configuration.module;

import com.intellij.openapi.projectRoots.Sdk;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkManipulator;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import org.jetbrains.annotations.Nullable;

public class PerlProjectGenerationSettings implements Perl5SdkManipulator {
  @Nullable
  private Sdk mySdk;

  @Nullable
  @Override
  public Perl5SdkWrapper getCurrentSdkWrapper() {
    return Perl5RealSdkWrapper.create(mySdk);
  }

  @Nullable
  public Sdk getSdk() {
    return mySdk;
  }

  public void setSdk(@Nullable Sdk sdk) {
    mySdk = sdk;
  }
}
