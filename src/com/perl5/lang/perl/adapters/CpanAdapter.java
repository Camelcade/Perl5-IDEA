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

package com.perl5.lang.perl.adapters;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import org.jetbrains.annotations.NotNull;

public class CpanAdapter extends PackageManagerAdapter {
  private static final String PACKAGE_NAME = "CPAN";
  private static final String SCRIPT_NAME = "cpan";

  public CpanAdapter(@NotNull Sdk sdk, @NotNull Project project) {
    super(sdk, project);
  }

  @NotNull
  @Override
  public String getPresentableName() {
    return "cpan";
  }

  @NotNull
  @Override
  protected String getManagerScriptName() {
    return SCRIPT_NAME;
  }

  @NotNull
  @Override
  protected String getManagerPackageName() {
    return PACKAGE_NAME;
  }
}
