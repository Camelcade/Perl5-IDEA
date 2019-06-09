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

package com.perl5.lang.perl.idea.modules;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.impl.PerlModuleExtension;
import com.intellij.openapi.roots.ui.configuration.ModuleSourceRootEditHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.ex.JpsElementTypeWithDummyProperties;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import java.util.ArrayList;
import java.util.List;

public abstract class PerlSourceRootType extends JpsElementTypeWithDummyProperties implements JpsModuleSourceRootType<JpsDummyElement> {
  protected abstract PerlSourceRootType getRootType();

  /**
   * @return key for xml file on serialization
   */
  @NotNull
  public abstract String getSerializationKey();

  @NotNull
  public final List<VirtualFile> getRoots(@NotNull Project project) {
    List<VirtualFile> result = new ArrayList<>();
    for (Module module : ModuleManager.getInstance(project).getModules()) {
      result.addAll(getRoots(module));
    }
    return result;
  }

  @NotNull
  public final List<VirtualFile> getRoots(@NotNull Module module) {
    return PerlModuleExtension.getInstance(module).getRootsByType(getRootType());
  }

  @NotNull
  public final ModuleSourceRootEditHandler getEditHandler() {
    ModuleSourceRootEditHandler<JpsDummyElement> handler = ModuleSourceRootEditHandler.getEditHandler(getRootType());
    if (handler == null) {
      throw new IncorrectOperationException("Couldn't find handler for " + getRootType());
    }
    return handler;
  }
}
