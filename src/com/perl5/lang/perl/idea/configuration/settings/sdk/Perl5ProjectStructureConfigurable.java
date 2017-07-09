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

import com.intellij.application.options.ModuleAwareProjectConfigurable;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.project.Project;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Perl5ProjectStructureConfigurable extends ModuleAwareProjectConfigurable<UnnamedConfigurable> {
  private final Perl5ProjectConfigurable myProjectConfigurable;

  public Perl5ProjectStructureConfigurable(@NotNull Project project) {
    super(project, PerlBundle.message("perl.settings.project.structure"), null);
    myProjectConfigurable = new Perl5ProjectConfigurable(project);
  }

  @NotNull
  @Override
  protected UnnamedConfigurable createModuleConfigurable(Module module) {
    return new Perl5ModuleConfigurable(module, myProjectConfigurable);
  }

  @Nullable
  @Override
  protected UnnamedConfigurable createProjectConfigurable() {
    return myProjectConfigurable;
  }

  @NotNull
  @Override
  protected String getProjectConfigurableItemName() {
    return PerlBundle.message("perl.settings.project");
  }

  @Nullable
  @Override
  protected Icon getProjectConfigurableItemIcon() {
    return AllIcons.General.ProjectSettings;
  }
}
