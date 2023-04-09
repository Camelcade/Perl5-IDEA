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
import com.intellij.execution.BeforeRunTaskProvider;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsContexts.NotificationContent;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.moduleBuild.ModuleBuildBundle;
import com.perl5.lang.perl.moduleBuild.PerlModuleBuildUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

class PerlModuleBuildTaskProvider extends BeforeRunTaskProvider<PerlModuleBuildTaskProvider.PerlModuleBuildTask> {
  private static final Logger LOG = Logger.getInstance(PerlModuleBuildTaskProvider.class);
  private static final Key<PerlModuleBuildTask> ID = Key.create("Module::Builder Before Run Task");

  @Override
  public Key<PerlModuleBuildTask> getId() {
    return ID;
  }

  @Override
  public @Nls(capitalization = Nls.Capitalization.Title) String getName() {
    return ModuleBuildBundle.message("generate.build.file");
  }

  @Override
  public @Nullable PerlModuleBuildTask createTask(@NotNull RunConfiguration runConfiguration) {
    return runConfiguration instanceof PerlModuleBuildTestRunConfiguration ? PerlModuleBuildTask.INSTANCE : null;
  }

  @Override
  public @Nullable Icon getIcon() {
    return PerlIcons.PERL_LANGUAGE_ICON;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public boolean canExecuteTask(@NotNull RunConfiguration configuration, @NotNull PerlModuleBuildTask task) {
    return super.canExecuteTask(configuration, task) && configuration instanceof PerlModuleBuildTestRunConfiguration;
  }

  @Override
  public @Nls(capitalization = Nls.Capitalization.Sentence) String getDescription(PerlModuleBuildTask task) {
    return ModuleBuildBundle.message("re.generates.build.file.by.running.build.pl.when.necessary");
  }

  @Override
  public boolean executeTask(@NotNull DataContext context,
                             @NotNull RunConfiguration configuration,
                             @NotNull ExecutionEnvironment environment,
                             @NotNull PerlModuleBuildTask task) {
    if (!(configuration instanceof PerlModuleBuildTestRunConfiguration moduleBuildTestRunConfiguration)) {
      LOG.warn("Trying to run task in wrong configuration: " + configuration);
      return false;
    }

    var contentRoot = moduleBuildTestRunConfiguration.getEffectiveContentRootSafe();
    if (contentRoot == null) {
      showError(configuration.getProject(), ModuleBuildBundle.message("notification.content.no.effective.content.root.for", configuration));
      return false;
    }

    return updateBuildfile(environment.getProject(), contentRoot);
  }

  public static boolean updateBuildfile(@NotNull Project project, @NotNull VirtualFile contentRoot) {
    var buildPlFile = contentRoot.findChild(PerlModuleBuildUtil.BUILD_PL);
    var buildFile = contentRoot.findChild(PerlModuleBuildUtil.BUILD);

    if (buildPlFile == null) {
      showError(project, ModuleBuildBundle.message("notification.content.no.build.pl.file.in.content.root", contentRoot));
      return false;
    }

    if (buildFile != null && PerlUtil.getLastModifiedTime(buildFile) >= PerlUtil.getLastModifiedTime(buildPlFile)) {
      return true;
    }

    Ref<RunContentDescriptor> descriptorRef = Ref.create();
    var commandLine = PerlRunUtil.getPerlCommandLine(project, buildPlFile);

    if( commandLine == null){
      showError(project, ModuleBuildBundle.message("notification.content.unable.to.create.command.line.to.process", buildPlFile));
      return false;
    }
      commandLine.withWorkDirectory(contentRoot.getPath());
    ApplicationManager.getApplication().invokeAndWait(
      () -> descriptorRef.set(PerlRunUtil.runInConsole(commandLine)));

    var processHandler = descriptorRef.get().getProcessHandler();
    if( processHandler == null){
      return false;
    }
    if (processHandler.waitFor()) {
      var exitCode = processHandler.getExitCode();
      if (exitCode != null && exitCode == 0) {
        return true;
      }
      showError(project, ModuleBuildBundle.message("notification.content.process.didn.t.end.with.exit.code", String.valueOf(exitCode)));
    }
    else {
      showError(project, ModuleBuildBundle.message("notification.content.process.didn.t.end", processHandler));
    }
    return false;
  }

  private static void showError(@NotNull Project project, @NotNull @NotificationContent String message){
    LOG.warn(message);
    Notifications.Bus.notify(new Notification(
      "PERL_MODULE_BUILD_ERROR",
      ModuleBuildBundle.message("perl.module.build.notification.generating.error.title"),
      message,
      NotificationType.ERROR
    ), project);
  }


  static final class PerlModuleBuildTask extends BeforeRunTask<PerlModuleBuildTask> {
    static final PerlModuleBuildTask INSTANCE = new PerlModuleBuildTask();

    public PerlModuleBuildTask() {
      super(ID);
    }
  }
}
