/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil;
import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerConsoleView;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.annotations.Tag;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.CONSOLE;
import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.NONE;
import static com.perl5.lang.perl.util.PerlRunUtil.PERL_I;

class PerlTestRunConfiguration extends GenericPerlRunConfiguration {
  private static final String PROVE = "prove";
  private static final String TEST_HARNESS = "Test::Harness";
  private static final String PROVE_PASS_PREFIX = "PROVE_PASS_";
  static final int DEFAULT_JOBS_NUMBER = 1;
  private static final String PROVE_FORMAT_PARAMETER = "--formatter";
  private static final String PROVE_FRAMEWORK_NAME = TEST_HARNESS;
  private static final Pattern MISSING_FILTER_PATTERN = Pattern.compile("Can't load module (\\S+) at .+?/prove line");
  private static final String PROVE_PLUGIN_NAMESPACE = "App::Prove::Plugin";
  private static final List<String> PROVE_DEFAULT_PARAMETERS = Arrays.asList(
    "-PPassEnv", PROVE_FORMAT_PARAMETER, "TAP::Formatter::Camelcade", "--merge", "--recurse");
  private static final String PROVE_JOBS_SHORT_PREFIX = "-j";
  private static final String PROVE_JOBS_PARAMETER = "--jobs";
  private static final Logger LOG = Logger.getInstance(PerlTestRunConfiguration.class);
  @Tag("JOBS_NUMBER")
  private int myJobsNumber = DEFAULT_JOBS_NUMBER;

  public PerlTestRunConfiguration(Project project,
                                  @NotNull ConfigurationFactory factory,
                                  String name) {
    super(project, factory, name);
  }

  int getJobsNumber() {
    return myJobsNumber;
  }

  void setJobsNumber(int jobsNumber) {
    myJobsNumber = jobsNumber;
  }

  @NotNull
  @Override
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new PerlTestRunConfigurationEditor(getProject());
  }

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

  @NotNull
  @Override
  protected PerlCommandLine createBaseCommandLine(@NotNull PerlRunProfileState perlRunProfileState) throws ExecutionException {
    ExecutionEnvironment executionEnvironment = perlRunProfileState.getEnvironment();
    Project project = executionEnvironment.getProject();
    List<String> additionalPerlParameters = perlRunProfileState.getAdditionalPerlParameters(this);
    Map<String, String> additionalEnvironmentVariables = perlRunProfileState.getAdditionalEnvironmentVariables();

    Sdk perlSdk = getEffectiveSdk();
    VirtualFile proveScript = PerlRunUtil.findLibraryScriptWithNotification(perlSdk, getProject(), PROVE, TEST_HARNESS);
    if (proveScript == null) {
      throw new ExecutionException(PerlBundle.message("perl.run.error.prove.missing", perlSdk.getName()));
    }

    PerlHostData<?, ?> perlHostData = PerlHostData.notNullFrom(perlSdk);

    Set<String> proveParameters = new LinkedHashSet<>(PROVE_DEFAULT_PARAMETERS);
    proveParameters.addAll(getScriptParameters());
    proveParameters.add(PROVE_JOBS_PARAMETER);
    proveParameters.add(Integer.toString(perlRunProfileState.isParallelRunAllowed() ? getJobsNumber() : 1));
    VirtualFile workingDirectory = computeExplicitWorkingDirectory();

    List<String> testsPaths = new ArrayList<>();
    for (VirtualFile testVirtualFile : computeTargetFiles()) {
      if (testVirtualFile == null) {
        continue;
      }
      String virtualFilePath = testVirtualFile.getPath();
      if (workingDirectory != null && VfsUtil.isAncestor(workingDirectory, testVirtualFile, true)) {
        testsPaths.add(VfsUtil.getRelativePath(testVirtualFile, workingDirectory));
      }
      else {
        testsPaths.add(perlHostData.getRemotePath(virtualFilePath));
      }
    }

    PerlCommandLine commandLine = new PerlCommandLine(getEffectiveInterpreterPath())
      .withParameters(perlHostData.getRemotePath(proveScript.getPath()))
      .withParameters(proveParameters)
      .withParameters(testsPaths)
      .withProject(project)
      .withSdk(perlSdk);

    ArrayList<String> testPerlParameters = new ArrayList<>(getPerlParametersList());
    testPerlParameters.addAll(additionalPerlParameters);
    for (VirtualFile libRoot : PerlProjectManager.getInstance(project).getModulesLibraryRoots()) {
      testPerlParameters.add(PERL_I + perlHostData.getRemotePath(libRoot.getCanonicalPath()));
    }

    // environment
    Map<String, String> environment = new HashMap<>(getEnvs());
    environment.putAll(additionalEnvironmentVariables);
    if (!testPerlParameters.isEmpty()) {
      String currentOpt = environment.get(PerlRunUtil.PERL5OPT);
      if (StringUtil.isNotEmpty(currentOpt)) {
        testPerlParameters.addAll(0, StringUtil.split(currentOpt, " "));
      }
      environment.put(PerlRunUtil.PERL5OPT, StringUtil.join(testPerlParameters, " "));
    }
    environment.forEach((key, val) -> commandLine.withEnvironment(PROVE_PASS_PREFIX + key, val));

    commandLine.withParentEnvironmentType(isPassParentEnvs() ? CONSOLE : NONE);
    return commandLine;
  }

  @NotNull
  @Override
  protected List<String> getScriptParameters() {

    List<String> userParameters = super.getScriptParameters();
    for (Iterator<String> iterator = userParameters.iterator(); iterator.hasNext(); ) {
      String userParameter = iterator.next();
      if (StringUtil.startsWith(userParameter, PROVE_JOBS_SHORT_PREFIX)) {
        iterator.remove();
      }
      else if (PROVE_FORMAT_PARAMETER.equals(userParameter) || PROVE_JOBS_PARAMETER.equals(userParameter)) {
        iterator.remove();
        if (iterator.hasNext()) {
          iterator.next();
          iterator.remove();
        }
      }
    }
    return userParameters;
  }

  @Override
  public boolean isReconnect() {
    return true;
  }

  @NotNull
  @Override
  public ConsoleView createConsole(@NotNull PerlRunProfileState runProfileState) throws ExecutionException {
    PerlSMTRunnerConsoleProperties consoleProperties =
      new PerlSMTRunnerConsoleProperties(this, PROVE_FRAMEWORK_NAME, runProfileState.getEnvironment().getExecutor());
    String splitterPropertyName = SMTestRunnerConnectionUtil.getSplitterPropertyName(PROVE_FRAMEWORK_NAME);
    SMTRunnerConsoleView consoleView = new PerlSMTRunnerConsoleView(getProject(), consoleProperties, splitterPropertyName)
      .withHostData(PerlHostData.notNullFrom(getEffectiveSdk()));
    SMTestRunnerConnectionUtil.initConsoleView(consoleView, PROVE_FRAMEWORK_NAME);
    return consoleView;
  }

  @Override
  protected boolean isUsePty() {
    return false;
  }

  @NotNull
  @Override
  protected ProcessHandler doPatchProcessHandler(@NotNull ProcessHandler processHandler, @NotNull PerlRunProfileState runProfileState) {
    try {
      Sdk effectiveSdk = getEffectiveSdk();
      processHandler.addProcessListener(new ProcessAdapter() {
        @Override
        public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
          String inputText = event.getText();
          Matcher matcher = MISSING_FILTER_PATTERN.matcher(inputText);
          if (matcher.find()) {
            String libraryName = PROVE_PLUGIN_NAMESPACE + PerlPackageUtil.PACKAGE_SEPARATOR + matcher.group(1);
            PerlRunUtil.showMissingLibraryNotification(getProject(), effectiveSdk, Collections.singletonList(libraryName));
          }
        }
      });
    }
    catch (ExecutionException e) {
      LOG.warn("Missing effective sdk for test configuration: " + getName());
    }
    return processHandler;
  }

  @Override
  protected void checkConfigurationScriptPath() throws RuntimeConfigurationException {
    if (computeTargetFiles().isEmpty()) {
      throw new RuntimeConfigurationException(PerlBundle.message("perl.run.error.no.tests.found"));
    }
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException {
    try {
      if (PerlRunUtil.findScript(getEffectiveSdk(), PROVE) == null) {
        throw new RuntimeConfigurationException(PerlBundle.message("perl.run.error.no.prove.found"));
      }
    }
    catch (ExecutionException e) {
      throw new RuntimeConfigurationException(e.getMessage());
    }
  }
}
