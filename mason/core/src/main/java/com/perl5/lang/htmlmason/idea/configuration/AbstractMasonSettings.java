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

package com.perl5.lang.htmlmason.idea.configuration;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotifications;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import com.perl5.lang.perl.idea.modules.PerlSourceRootType;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractMasonSettings {
  public List<VariableDescription> globalVariables = new ArrayList<>();

  @Transient
  protected int changeCounter = 0;

  @Transient protected final @NotNull Project myProject;

  public AbstractMasonSettings() {
    this(ProjectManager.getInstance().getDefaultProject());
  }

  public AbstractMasonSettings(@NotNull Project project) {
    myProject = project;
  }

  public void settingsUpdated() {
    changeCounter++;
    if (!ApplicationManager.getApplication().isUnitTestMode()) {
      EditorNotifications.getInstance(myProject).updateAllNotifications();
    }
  }

  protected abstract PerlSourceRootType getSourceRootType();

  public @NotNull List<VirtualFile> getComponentsRoots() {
    return PerlProjectManager.getInstance(myProject).getModulesRootsOfType(getSourceRootType());
  }

  public int getChangeCounter() {
    return changeCounter;
  }
}
