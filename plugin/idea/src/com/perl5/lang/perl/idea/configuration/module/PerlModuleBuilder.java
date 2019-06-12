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

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkManipulator;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import com.perl5.lang.perl.idea.modules.PerlModuleType;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlModuleBuilder extends ModuleBuilder implements Perl5SdkManipulator {
  @Nullable
  private Sdk mySdk;

  @Override
  public ModuleType getModuleType() {
    return PerlModuleType.getInstance();
  }

  @Override
  public boolean isSuitableSdkType(SdkTypeId sdkType) {
    return false;
  }

  @Override
  public void setupRootModel(@NotNull ModifiableRootModel modifiableRootModel) throws ConfigurationException {
    doAddContentEntry(modifiableRootModel);
  }

  @Nullable
  @Override
  public Project createProject(String name, String path) {
    Project project = super.createProject(name, path);
    if (project != null) {
      PerlProjectManager.getInstance(project).setProjectSdk(mySdk);
    }
    return project;
  }

  @Nullable
  @Override
  public Perl5SdkWrapper getCurrentSdkWrapper() {
    return Perl5RealSdkWrapper.create(mySdk);
  }

  public void setSdk(@Nullable Sdk sdk) {
    mySdk = sdk;
  }
}
