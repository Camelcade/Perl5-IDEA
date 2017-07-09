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

package com.perl5.lang.perl.idea.configuration.settings.sdk;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Perl5ProjectConfigurable extends Perl5StructureConfigurable {
  @NotNull
  private final Project myProject;

  public Perl5ProjectConfigurable(@NotNull Project project) {
    myProject = project;
  }

  @Override
  public void apply() throws ConfigurationException {
    WriteAction.run(() -> ProjectRootManager.getInstance(myProject).setProjectSdk(getSelectedSdk()));
  }

  @Override
  protected List<Perl5SdkWrapper> getSdkItems() {
    List<Perl5SdkWrapper> defaultItems = super.getSdkItems();
    defaultItems.add(0, NOT_SELECTED_ITEM);
    return defaultItems;
  }

  @Nullable
  @Override
  protected Perl5SdkWrapper getDefaultSelectedItem() {
    Sdk projectSdk = ProjectRootManager.getInstance(myProject).getProjectSdk();
    return projectSdk == null ? NOT_SELECTED_ITEM : new Perl5RealSdkWrapper(projectSdk);
  }
}
