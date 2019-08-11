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

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.DirectoryProjectGeneratorBase;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlProjectGeneratorBase<Settings extends PerlProjectGenerationSettings>
  extends DirectoryProjectGeneratorBase<Settings> {
  @Override
  public final void generateProject(@Nullable Project project,
                                    @NotNull VirtualFile baseDir,
                                    @NotNull Settings settings,
                                    @NotNull Module module) {
    configureModule(module, settings);
  }

  @NotNull
  @Override
  public abstract PerlProjectGeneratorPeerBase<Settings> createPeer();

  public void configureModule(@NotNull Module module, @NotNull Settings settings) {
    Sdk sdk = settings.getSdk();
    if (sdk != null) {
      PerlProjectManager.getInstance(module.getProject()).setProjectSdk(sdk);
    }
  }
}
