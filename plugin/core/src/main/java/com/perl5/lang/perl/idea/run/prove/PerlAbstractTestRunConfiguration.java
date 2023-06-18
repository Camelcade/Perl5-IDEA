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

package com.perl5.lang.perl.idea.run.prove;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil;
import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerConsoleView;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xmlb.annotations.Tag;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.execution.PerlTerminalExecutionConsole;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.CONSOLE;
import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.NONE;
import static com.perl5.lang.perl.util.PerlRunUtil.PERL5OPT;
import static com.perl5.lang.perl.util.PerlRunUtil.getPerlRunIncludeArguments;

public abstract class PerlAbstractTestRunConfiguration extends GenericPerlRunConfiguration {
  static final int DEFAULT_JOBS_NUMBER = 1;
  @Tag("JOBS_NUMBER")
  private int myJobsNumber = DEFAULT_JOBS_NUMBER;
  @Tag("TEST_SCRIPT_PARAMETERS")
  private String myTestScriptParameters = "";
  private static final String HARNESS_OPTIONS = "HARNESS_OPTIONS";
  private static final String HARNESS_OPTIONS_SEPARATOR = ":";
  private static final String HARNESS_OPTIONS_JOBS = "j";
  private static final String HARNESS_OPTIONS_FORMATTER = "f";
  private static final String HARNESS_OPTIONS_FORMATTER_CLASS = "TAP-Formatter-Camelcade";
  private static final String HARNESS_PERL_SWITCHES = "HARNESS_PERL_SWITCHES";

  public PerlAbstractTestRunConfiguration(@NotNull Project project,
                                          @NotNull ConfigurationFactory factory,
                                          @Nullable String name) {
    super(project, factory, name);
  }

  int getJobsNumber() {
    return myJobsNumber;
  }

  @VisibleForTesting
  public void setJobsNumber(int jobsNumber) {
    myJobsNumber = jobsNumber;
  }

  @Nullable
  String getTestScriptParameters() {
    return myTestScriptParameters;
  }

  void setTestScriptParameters(@Nullable String testScriptParameters) {
    myTestScriptParameters = testScriptParameters;
  }

  @NotNull
  List<String> getTestScriptParametersList() {
    String testScriptParameters = getTestScriptParameters();
    return StringUtil.isEmpty(testScriptParameters) ? Collections.emptyList() : StringUtil.split(testScriptParameters, " ");
  }

  @Override
  public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new PerlTestRunConfigurationEditor(getProject(), getRunConfigurationProducer());
  }

  protected abstract @NotNull PerlAbstractTestRunConfigurationProducer getRunConfigurationProducer();

  @Override
  public String suggestedName() {
    List<VirtualFile> testsVirtualFiles = computeTargetFiles();
    if (testsVirtualFiles.size() > 1) {
      VirtualFile firstTest = testsVirtualFiles.remove(0);
      return PerlBundle.message("perl.run.prove.configuration.name.multi", firstTest.getName(), testsVirtualFiles.size());
    }
    else if (testsVirtualFiles.size() == 1) {
      return PerlBundle.message("perl.run.prove.configuration.name.single", testsVirtualFiles.get(0).getName());
    }
    return super.suggestedName();
  }

  @Override
  public boolean isReconnect() {
    return true;
  }

  @Override
  protected boolean isUsePty() {
    return false;
  }

  protected abstract @NotNull String getFrameworkName();

  @Override
  public @NotNull ConsoleView createConsole(@NotNull PerlRunProfileState runProfileState) throws ExecutionException {
    PerlSMTRunnerConsoleProperties consoleProperties =
      new PerlSMTRunnerConsoleProperties(this, getFrameworkName(), runProfileState.getEnvironment().getExecutor());
    String splitterPropertyName = SMTestRunnerConnectionUtil.getSplitterPropertyName(getFrameworkName());
    var project = getProject();
    SMTRunnerConsoleView consoleView = new PerlSMTRunnerConsoleView(consoleProperties, splitterPropertyName)
      .withHostData(getEffectiveHostData());
    SMTestRunnerConnectionUtil.initConsoleView(consoleView, getFrameworkName());
    PerlTerminalExecutionConsole.updatePredefinedFiltersLater(project, consoleView);
    return consoleView;
  }

  protected abstract @NotNull List<String> getDefaultTestRunnerArguments();

  @Override
  protected @NotNull PerlCommandLine createBaseCommandLine(@NotNull PerlRunProfileState perlRunProfileState) throws ExecutionException {
    return new PerlCommandLine(getEffectiveInterpreterPath())
      .withParameters(getPerlRunIncludeArguments(getEffectiveHostData(), getProject()))
      .withParameters(getTestRunnerPath())
      .withParameters(getTestRunnerArguments())
      .withParameters(getTestsArguments())
      .withProject(getProject())
      .withSdk(getEffectiveSdk())
      .withEnvironment(computeEnvironment(perlRunProfileState))
      .withParentEnvironmentType(isPassParentEnvs() ? CONSOLE : NONE)      ;
  }

  protected @NotNull Map<String, String> computeEnvironment(@NotNull PerlRunProfileState perlRunProfileState)
    throws ExecutionException {

    // environment
    List<String> perlArgumentsList = new ArrayList<>(getPerlArgumentsList());
    perlArgumentsList.addAll(perlRunProfileState.getAdditionalPerlArguments(this));
    perlArgumentsList.addAll(getPerlRunIncludeArguments(getEffectiveHostData(), getProject()));

    Map<String, String> environment = new HashMap<>(getEnvs());
    environment.putAll(perlRunProfileState.getAdditionalEnvironmentVariables());
    PerlRunUtil.updatePerl5Opt(environment, perlArgumentsList);

    List<String> harnessOptions = new ArrayList<>();
    harnessOptions.add(HARNESS_OPTIONS_JOBS + Integer.toString(perlRunProfileState.isParallelRunAllowed() ? getJobsNumber() : 1));
    harnessOptions.add(HARNESS_OPTIONS_FORMATTER + HARNESS_OPTIONS_FORMATTER_CLASS);

    environment.put(HARNESS_OPTIONS, String.join(HARNESS_OPTIONS_SEPARATOR, harnessOptions));
    var perl5Opt = environment.remove(PERL5OPT);
    if (StringUtil.isNotEmpty(perl5Opt)) {
      environment.put(HARNESS_PERL_SWITCHES, perl5Opt);
    }
    return environment;
  }

  protected @NotNull PerlHostData<?, ?> getEffectiveHostData() throws ExecutionException {
    return PerlHostData.notNullFrom(getEffectiveSdk());
  }

  protected abstract @NotNull String getTestRunnerLocalPath() throws ExecutionException;

  protected @NotNull String getTestRunnerPath() throws ExecutionException {
    PerlHostData<?, ?> perlHostData = getEffectiveHostData();
    String remoteTestRunnerPath = perlHostData.getRemotePath(getTestRunnerLocalPath());
    if (StringUtil.isEmpty(remoteTestRunnerPath)) {
      throw new ExecutionException(PerlBundle.message("dialog.message.unable.to.map.remote.path.for", remoteTestRunnerPath, perlHostData));
    }
    return remoteTestRunnerPath;
  }

  private @NotNull List<String> getTestRunnerArguments() throws ExecutionException {
    List<String> testRunnerArguments = new ArrayList<>(getDefaultTestRunnerArguments());
    testRunnerArguments.addAll(getScriptArguments());
    testRunnerArguments.addAll(getTestsToRunArguments());
    return testRunnerArguments;
  }

  protected @NotNull List<String> getTestsToRunArguments() throws ExecutionException {
    var workingDirectory = computeExplicitWorkingDirectory();
    var perlHostData = getEffectiveHostData();
    List<String> testsPaths = new ArrayList<>();
    for (VirtualFile testVirtualFile : computeTargetFiles()) {
      if (testVirtualFile == null) {
        continue;
      }
      String virtualFilePath = testVirtualFile.getPath();
      String effectivePath;
      if (workingDirectory != null && VfsUtil.isAncestor(workingDirectory, testVirtualFile, true)) {
        effectivePath = VfsUtil.getRelativePath(testVirtualFile, workingDirectory);
      }
      else {
        effectivePath = perlHostData.getRemotePath(virtualFilePath);
      }
      addTestPathArguments(testsPaths, effectivePath, testVirtualFile);
    }
    return testsPaths;
  }

  @Override
  public @NotNull List<String> getRequiredModulesList() {
    return ContainerUtil.append(super.getRequiredModulesList(), PerlPackageUtil.TEST_HARNESS_MODULE, PerlPackageUtil.TAP_FORMATTER_MODULE);
  }

  protected void addTestPathArguments(@NotNull List<? super String> arguments, @NotNull String testFilePath, @NotNull VirtualFile testVirtualFile) {
    arguments.add(testFilePath);
  }

  protected abstract @NotNull List<String> getTestsArguments();
}
