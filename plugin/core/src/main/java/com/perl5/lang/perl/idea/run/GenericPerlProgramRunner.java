/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.adapters.PackageManagerAdapter;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class GenericPerlProgramRunner implements ProgramRunner<RunnerSettings> {
  private static final Logger LOG = Logger.getInstance(GenericPerlProgramRunner.class);
  protected abstract @Nullable PerlRunProfileState createState(@NotNull ExecutionEnvironment executionEnvironment) throws
                                                                                                                   ExecutionException;

  @Override
  public final void execute(@NotNull ExecutionEnvironment environment) throws ExecutionException {
    FileDocumentManager.getInstance().saveAllDocuments();
    var missingModules = getMissingModules(environment);
    if (!missingModules.isEmpty() && handleMissingModules(environment, missingModules)) {
      return;
    }
    doExecute(environment);
  }

  /**
   * Handles missing modules by prompting the user to install them, and then installing them
   * using the package manager adapter. Executes the environment after the installation is complete.
   * @param environment the execution environment
   * @param missingModules a list of missing modules
   * @return true iff invoker should not call run configuration itself, because it's going to be handled by the callback
   */
  private boolean handleMissingModules(@NotNull ExecutionEnvironment environment, List<String> missingModules) {
    var project = environment.getProject();
    var sdk = PerlProjectManager.getSdk(project);
    if (sdk == null) {
      return false;
    }
    var request = Messages.showYesNoDialog(
      project,
      PerlBundle.message("dialog.message.are.missing.in.would.you.like.to.install", missingModules.stream().sorted().collect(Collectors.joining(", "))),
      PerlBundle.message("dialog.title.missing.modules"),
      Messages.getQuestionIcon());
    if (request != Messages.YES) {
      return false;
    }
    var packageManagerAdapter = PackageManagerAdapter.create(sdk, project);
    packageManagerAdapter.install(missingModules, ()->{
      if (project.isDisposed()) {
        return;
      }
      ApplicationManager.getApplication().invokeLater(()-> {
        try {
          doExecute(environment);
        }
        catch (ExecutionException e) {
          LOG.warn("Error running environment after installation: " + environment + "; " + e.getMessage());
        }
      });
    }, true);
    return true;
  }

  private List<String> getMissingModules(@NotNull ExecutionEnvironment environment){
    var project = environment.getProject();
    return ContainerUtil.filter(getRequiredModules(environment),
                                it-> PerlPackageUtil.getPackageVirtualFileByPackageName(project, it) == null);
  }

  /**
   * Returns a modifiable set of required modules for the given execution environment.
   * By default, the set is obtained from the "Required modules" field of the run configuration.
   * @param environment the execution environment
   * @return a modifiable set of required modules
   */
  protected Set<String> getRequiredModules(@NotNull ExecutionEnvironment environment){
    var runProfile = environment.getRunProfile();
    return runProfile instanceof GenericPerlRunConfiguration perlRunConfiguration
           ? new HashSet<>(perlRunConfiguration.getRequiredModulesList())
           : new HashSet<>();
  }

  protected abstract void doExecute(@NotNull ExecutionEnvironment environment) throws ExecutionException;
}
