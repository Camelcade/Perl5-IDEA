/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.perl.moduleBuild.run;

import com.intellij.execution.BeforeRunTask;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.run.prove.PerlAbstractTestRunConfiguration;
import com.perl5.lang.perl.moduleBuild.PerlModuleBuildUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerlModuleBuildTestRunConfiguration extends PerlAbstractTestRunConfiguration {
  private static final Logger LOG = Logger.getInstance(PerlModuleBuildTestRunConfiguration.class);

  private static final String TEST_FILES_ARGUMENT = "--test_files";

  public PerlModuleBuildTestRunConfiguration(@NotNull Project project,
                                             @NotNull ConfigurationFactory factory,
                                             @Nullable String name) {
    super(project, factory, name);
  }

  @Override
  protected @NotNull String getFrameworkName() {
    return PerlModuleBuildUtil.MODULE_BUILD;
  }

  @Override
  protected @NotNull List<String> getDefaultTestRunnerArguments() {
    return Collections.singletonList("test");
  }

  @Override
  protected @NotNull String getTestRunnerLocalPath() throws ExecutionException {
    return Paths.get(getEffectiveContentRoot().getPath(), PerlModuleBuildUtil.BUILD).toString();
  }

  private @NotNull VirtualFile getEffectiveContentRoot() throws ExecutionException {
    VirtualFile contentRoot = getEffectiveContentRootSafe();
    if (contentRoot != null) {
      return contentRoot;
    }
    throw new ExecutionException(PerlBundle.message("perl.run.error.no.test.set"));
  }

  @Nullable VirtualFile getEffectiveContentRootSafe() {
    return ReadAction.compute(()->{
      var files = computeTargetFiles();
      if (!files.isEmpty()) {
        var projectFileIndex = ProjectFileIndex.getInstance(getProject());
        for (VirtualFile virtualFile : files) {
          var contentRoot = projectFileIndex.getContentRootForFile(virtualFile);
          if (contentRoot != null) {
            return contentRoot;
          }
        }
        LOG.warn("Unable to compute effective root with files: " + files);
      }
      else {
        LOG.warn("There is no target files to run");
      }
      return null;
    });
  }

  @Override
  protected void addTestPathArguments(@NotNull List<? super String> arguments,
                                      @NotNull String testFilePath,
                                      @NotNull VirtualFile testVirtualFile) {
    arguments.add(TEST_FILES_ARGUMENT);
    if (testVirtualFile.isDirectory()) {
      arguments.add(String.join("/", testFilePath, "*"));
    }
    else {
      arguments.add(testFilePath);
    }
  }

  @Override
  public @NotNull List<BeforeRunTask<?>> getBeforeRunTasks() {
    var realTasks = super.getBeforeRunTasks();
    if (realTasks.contains(PerlModuleBuildTaskProvider.PerlModuleBuildTask.INSTANCE)) {
      return realTasks;
    }
    var patchedTasks = new ArrayList<BeforeRunTask<?>>();
    patchedTasks.add(PerlModuleBuildTaskProvider.PerlModuleBuildTask.INSTANCE);
    patchedTasks.addAll(realTasks);
    return patchedTasks;
  }

  @Override
  protected @NotNull List<String> getTestsArguments() {
    return Collections.emptyList();
  }

  @Override
  protected @NotNull PerlModuleBuildTestRunConfigurationProducer getRunConfigurationProducer() {
    return PerlModuleBuildTestRunConfigurationProducer.getInstance();
  }
}
