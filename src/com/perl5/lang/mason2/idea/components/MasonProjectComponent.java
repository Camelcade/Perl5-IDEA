/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.mason2.idea.components;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.perl5.lang.mason2.idea.vfs.MasonVirtualFileListener;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 09.01.2016.
 */
public class MasonProjectComponent implements ProjectComponent {
  private final Project myProject;
  private final VirtualFileListener myFileListener;

  public MasonProjectComponent(Project project) {
    myProject = project;
    myFileListener = new MasonVirtualFileListener(project);
  }

  @Override
  public void initComponent() {
    VirtualFileManager.getInstance().addVirtualFileListener(myFileListener);
  }

  @Override
  public void disposeComponent() {
    VirtualFileManager.getInstance().removeVirtualFileListener(myFileListener);
  }

  @Override
  @NotNull
  public String getComponentName() {
    return "MasonProjectComponent";
  }

  @Override
  public void projectOpened() {
    // called when project is opened
  }

  @Override
  public void projectClosed() {
    // called when project is being closed
  }

  public Project getProject() {
    return myProject;
  }
}
