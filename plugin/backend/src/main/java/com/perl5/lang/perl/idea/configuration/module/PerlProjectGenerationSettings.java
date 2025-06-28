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

package com.perl5.lang.perl.idea.configuration.module;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkManipulator;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.Nullable;

public class PerlProjectGenerationSettings implements Perl5SdkManipulator {
  private @Nullable Sdk mySdk;
  private @Nullable Project myProject;

  @Override
  public @Nullable Perl5SdkWrapper getCurrentSdkWrapper() {
    return Perl5RealSdkWrapper.create(mySdk);
  }

  public @Nullable Sdk getSdk() {
    return mySdk;
  }

  @Override
  public void setSdk(@Nullable Sdk sdk) {
    mySdk = sdk;
  }

  public @Nullable Project getProject() {
    return myProject;
  }

  public void setProject(@Nullable Project project) {
    myProject = project;
    if (mySdk == null) {
      mySdk = PerlProjectManager.getSdk(myProject);
    }
  }
}
