/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.actions.PerlMarkLibrarySourceRootAction;
import com.perl5.lang.perl.idea.modules.PerlModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;

import static com.perl5.lang.perl.util.PerlPackageUtilCore.DEFAULT_LIB_DIR;
import static com.perl5.lang.perl.util.PerlPackageUtilCore.DEFAULT_TEST_DIR;

public class PerlProjectGenerator extends PerlProjectGeneratorBase<PerlProjectGenerationSettings> {
  private static final Logger LOG = Logger.getInstance(PerlProjectGenerator.class);
  @Override
  public @NotNull String getName() {
    return PerlModuleType.getInstance().getName();
  }

  @Override
  public @Nullable Icon getLogo() {
    return PerlModuleType.getInstance().getIcon();
  }

  @Override
  public @NotNull PerlProjectGeneratorPeer createPeer() {
    return new PerlProjectGeneratorPeer();
  }

  @Override
  public void configureModule(@NotNull Module module, @NotNull PerlProjectGenerationSettings settings) {
    super.configureModule(module, settings);
    ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
    for (VirtualFile contentRoot : moduleRootManager.getContentRoots()) {
      contentRoot.refresh(true, false, () -> {
        try {
          VirtualFile libDir = contentRoot.findChild(DEFAULT_LIB_DIR);
          if (libDir == null) {
            libDir = contentRoot.createChildDirectory(this, DEFAULT_LIB_DIR);
          }
          new PerlMarkLibrarySourceRootAction().markRoot(module, libDir);
        }
        catch (IOException e) {
          LOG.warn("Error creating lib directory for " + module, e);
        }

        if (contentRoot.findChild(DEFAULT_TEST_DIR) == null) {
          try {
            contentRoot.createChildDirectory(this, DEFAULT_TEST_DIR);
          }
          catch (IOException e) {
            LOG.warn("Error creating test directory for " + module);
          }
        }
      });
    }
  }
}
