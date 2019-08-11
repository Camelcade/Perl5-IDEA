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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.mojolicious.MojoUtil;
import com.perl5.lang.mojolicious.idea.actions.MojoGenerateAction;
import com.perl5.lang.mojolicious.model.MojoProjectManager;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public abstract class MojoProjectGenerator extends PerlProjectGeneratorBase<MojoProjectGenerationSettings> {
  private static final Logger LOG = Logger.getInstance(MojoProjectGenerator.class);

  @NotNull
  @Override
  public final PerlProjectGeneratorPeerBase<MojoProjectGenerationSettings> createPeer() {
    return new MojoProjectGeneratorPeer() {
      @Override
      protected String getEntityLabel() {
        return MojoProjectGenerator.this.getEntityLabel();
      }

      @Override
      protected boolean isNameValid(@NotNull String name) {
        return getNameValidator().checkInput(name);
      }

      @Nullable
      @Override
      public String suggestProjectName() {
        String entityName = getSettings().getEntityName();
        return StringUtil.isEmpty(entityName) ? null : MojoProjectGenerator.this.suggestProjectName(entityName);
      }
    };
  }

  @NotNull
  protected String suggestProjectName(@NotNull String entityName) {
    return StringUtil.join(PerlPackageUtil.split(entityName), "_");
  }

  @NotNull
  protected abstract String getEntityLabel();

  @NotNull
  protected abstract InputValidator getNameValidator();

  /**
   * @return an action which going to be used for generation
   */
  @NotNull
  protected abstract MojoGenerateAction getGenerationAction();

  @Override
  public void configureModule(@NotNull Module module, @NotNull MojoProjectGenerationSettings settings) {
    super.configureModule(module, settings);
    ApplicationManager.getApplication().invokeLater(() -> generateEntity(module, settings));
  }

  private void generateEntity(@NotNull Module module,
                              @NotNull MojoProjectGenerationSettings settings) {
    if (module.isDisposed()) {
      return;
    }

    VirtualFile[] roots = ModuleRootManager.getInstance(module).getContentRoots();
    if (roots.length < 1) {
      LOG.warn("Unable to generate entity, no content entries in module: " + module);
      return;
    }

    VirtualFile mojoScriptFile = MojoUtil.getMojoScript(module);
    if (mojoScriptFile == null) {
      LOG.warn("Unable to generate entity, no mojo script for " + PerlProjectManager.getSdk(module));
      return;
    }

    try {
      File tempDirFile = FileUtil.createTempDirectory("MojoGeneration", null);
      VirtualFile tempDirVirtualFile = VfsUtil.findFileByIoFile(tempDirFile, true);
      if (tempDirVirtualFile == null) {
        LOG.warn("Unable to find temp dir virtual file: " + tempDirFile);
        return;
      }

      PerlRunUtil.runInConsole(getGenerationAction().createCommandLine(
        module.getProject(), tempDirVirtualFile, settings.getEntityName(), mojoScriptFile, () ->
          ApplicationManager.getApplication().invokeLater(
            () -> moveEntityToTheModule(module, tempDirVirtualFile, roots[0]), module.getDisposed()
          )));
    }
    catch (IOException e) {
      LOG.warn("Error creating temporary directory for mojo generation: " + e.getMessage());
    }
  }

  private void moveEntityToTheModule(@NotNull Module module,
                                     @NotNull VirtualFile tempDir,
                                     @NotNull VirtualFile contentRoot) {
    if (!tempDir.isValid()) {
      LOG.warn("Temp dir invalidated: " + tempDir);
      return;
    }
    if (!contentRoot.isValid()) {
      LOG.warn("Content root invalidated: " + contentRoot);
      return;
    }
    VirtualFile[] children = tempDir.getChildren();
    if (children.length != 1) {
      LOG.warn("Expected a single child in the temp directory, got: " + Arrays.asList(children));
      return;
    }
    VirtualFile entityDirectory = children[0];
    if (!entityDirectory.isDirectory()) {
      LOG.warn("Entity directory is not a directory: " + entityDirectory);
      return;
    }
    try {
      WriteAction.run(() -> VfsUtil.copyDirectory(this, entityDirectory, contentRoot, null));
      MojoProjectManager.getInstance(module.getProject()).scheduleUpdate();
    }
    catch (IOException e) {
      LOG.warn("Error moving " + entityDirectory + " content to " + contentRoot + "; " + e.getMessage());
    }
  }
}
