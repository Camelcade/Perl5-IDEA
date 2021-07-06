/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package base;

import com.intellij.execution.*;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.ConfigurationFromContext;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.testframework.TestTreeView;
import com.intellij.execution.testframework.sm.runner.SMTestProxy;
import com.intellij.execution.testframework.sm.runner.ui.SMTestRunnerResultsForm;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.application.impl.NonBlockingReadActionImpl;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.HeavyPlatformTestCase;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.TestActionEvent;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.tree.TreeUtil;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.prove.PerlSMTRunnerConsoleView;
import com.perl5.lang.perl.idea.run.prove.PerlTestRunConfiguration;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.perlbrew.PerlBrewTestUtil;
import com.pty4j.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.Promise;
import org.junit.Assume;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

@RunWith(JUnit4.class)
public abstract class PerlPlatformTestCase extends HeavyPlatformTestCase {
  private static final int MAX_WAIT_TIME = 10_000;
  protected static final Logger LOG = Logger.getInstance(PerlPlatformTestCase.class);
  private static final String PERLBREW_HOME = "~/perl5/perlbrew/bin/perlbrew";
  private static final String PERL_532 = "perl-5.32.0";
  private static final String MOJO_LIB_SEPARATOR = "@";
  private static final Key<CapturingProcessAdapter> ADAPTER_KEY = Key.create("process.adapter");

  protected final Disposable myPerlLightTestCaseDisposable = Disposer.newDisposable();

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addPerlBrewSdk(getPerl532DistibutionId("plugin_test"));
  }

  protected void disposeOnPerlTearDown(@NotNull Disposable disposable) {
    Disposer.register(myPerlLightTestCaseDisposable, disposable);
  }

  @Override
  protected @NotNull Module doCreateRealModule(@NotNull String moduleName) {
    Module module = super.doCreateRealModule(moduleName);
    try {
      VirtualFile moduleRoot = getOrCreateProjectBaseDir().createChildDirectory(this, moduleName);
      ModuleRootModificationUtil.addContentRoot(module, moduleRoot);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    return module;
  }

  protected @NotNull VirtualFile getMainContentRoot() {
    return ModuleRootManager.getInstance(getModule()).getContentRoots()[0];
  }

  @Override
  protected void tearDown() throws Exception {
    try {
      ApplicationManager.getApplication().invokeAndWait(() -> {
        PerlProjectManager projectManager = PerlProjectManager.getInstance(getProject());
        projectManager.setProjectSdk(null);
        projectManager.setExternalLibraries(Collections.emptyList());
      });
      Disposer.dispose(myPerlLightTestCaseDisposable);
    }
    finally {
      super.tearDown();
    }
  }

  /**
   * As far as we can't provide additional rules, because of hacky way main rule works, you should
   * put additional logic in here
   *
   * @param description test method description
   */
  protected void doEvaluate(@NotNull Description description) {
  }

  protected void addPerlBrewSdk(@NotNull String distributionId) {
    addSdk(getPerlbrewPath(), distributionId, PerlBrewTestUtil.getVersionManagerHandler());
  }

  protected void addSdk(@NotNull String pathToVersionManager,
                        @NotNull String distributionId,
                        @NotNull PerlRealVersionManagerHandler<?, ?> versionManagerHandler) {
    versionManagerHandler.createInterpreter(
      distributionId,
      versionManagerHandler.createAdapter(pathToVersionManager, PerlHostHandler.getDefaultHandler().createData()),
      this::onSdkCreation,
      getProject()
    );
  }

  private void onSdkCreation(@NotNull Sdk sdk) {
    disposeOnPerlTearDown(() -> WriteAction.run(() -> PerlSdkTable.getInstance().removeJdk(sdk)));
    PerlProjectManager.getInstance(getProject()).setProjectSdk(sdk);
  }

  protected @Nullable Sdk getSdk() {
    return PerlProjectManager.getSdk(getModule());
  }

  protected @NotNull String getPerl532DistibutionId(@Nullable String libraryName) {
    return StringUtil.isEmpty(libraryName) ? PERL_532 : PERL_532 + MOJO_LIB_SEPARATOR + libraryName;
  }

  protected void runAction(@NotNull AnAction anAction) {
    runAction(anAction, null);
  }

  protected void runAction(@NotNull AnAction anAction, @Nullable VirtualFile virtualFile) {
    TestActionEvent e = new TestActionEvent(dataId -> {
      if (LangDataKeys.MODULE.is(dataId)) {
        return getModule();
      }
      else if (CommonDataKeys.PROJECT.is(dataId)) {
        return getProject();
      }
      else if (CommonDataKeys.VIRTUAL_FILE.is(dataId)) {
        return virtualFile;
      }
      return null;
    });
    anAction.update(e);
    assertTrue("Action unavailable: " + anAction, e.getPresentation().isEnabled());
    anAction.actionPerformed(e);
  }

  protected static void assumePerlbrewAvailable() {
    Assume.assumeTrue(getPerlbrewFile() != null);
  }

  protected static @NotNull String getPerlbrewPath() {
    assumePerlbrewAvailable();
    return Objects.requireNonNull(getPerlbrewFile()).getPath();
  }

  protected static @Nullable File getPerlbrewFile() {
    String perlbrewHome = FileUtil.expandUserHome(PERLBREW_HOME);
    File perlbrewFile = new File(perlbrewHome);
    return perlbrewFile.exists() ? perlbrewFile : null;
  }

  protected String getBaseDataPath() {
    return "";
  }

  protected final String getTestDataPath() {
    File file = new File(getBaseDataPath());
    assertTrue("File not found: " + file, file.exists());
    return file.getAbsolutePath();
  }

  protected void copyDirToModule(@NotNull String directoryName) {
    File file = new File(getTestDataPath(), directoryName);
    assertTrue("File not found: " + file, file.exists());
    VirtualFile virtualFile = refreshAndFindFile(file);
    assertNotNull("Unable to find virtual file for: " + file, virtualFile);
    virtualFile.refresh(false, true);
    copyDirContentsTo(virtualFile, getModuleRoot());
  }

  protected @NotNull VirtualFile getModuleRoot() {
    try {
      return getOrCreateModuleDir(getModule());
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected @NotNull VirtualFile getModuleFile(@NotNull String relativePath) {
    VirtualFile file = getModuleRoot().findFileByRelativePath(relativePath);
    assertNotNull("Unable to find file in module: " + relativePath, file);
    return file;
  }

  protected @NotNull GenericPerlRunConfiguration createOnlyRunConfiguration(@NotNull String relativePath) {
    List<ConfigurationFromContext> configurationsFromContext = getRunConfigurationsFromFileContext(relativePath);
    assertSize(1, configurationsFromContext);
    ConfigurationFromContext configurationFromContext = configurationsFromContext.get(0);
    RunConfiguration runConfiguration = configurationFromContext.getConfiguration();
    assertInstanceOf(runConfiguration, GenericPerlRunConfiguration.class);
    return (GenericPerlRunConfiguration)runConfiguration;
  }

  protected @NotNull List<ConfigurationFromContext> getRunConfigurationsFromFileContext(@NotNull String relativePath) {
    VirtualFile virtualFile = getModuleFile(relativePath);
    PsiElement psiElement = getPsiElement(virtualFile);
    ConfigurationContext configurationContext = ConfigurationContext.getFromContext(createDataContext(
      it -> LangDataKeys.PSI_ELEMENT_ARRAY.is(it) ? new PsiElement[]{psiElement} : null));
    List<ConfigurationFromContext> configurationsFromContext = configurationContext.getConfigurationsFromContext();
    assertNotNull(configurationsFromContext);
    return configurationsFromContext;
  }

  protected @NotNull PsiElement getPsiElement(VirtualFile virtualFile) {
    PsiManager psiManager = PsiManager.getInstance(getProject());
    PsiElement psiElement = virtualFile.isDirectory() ? psiManager.findDirectory(virtualFile) : psiManager.findFile(virtualFile);
    assertNotNull(psiElement);
    return psiElement;
  }

  private @NotNull DataContext createDataContext() {
    return createDataContext(it -> null);
  }

  protected @NotNull DataContext createDataContext(@NotNull Function<String, Object> additionalData) {
    return dataId -> {
      if (CommonDataKeys.PROJECT.is(dataId)) {
        return getProject();
      }
      else if (LangDataKeys.MODULE.is(dataId)) {
        return getModule();
      }
      return additionalData.apply(dataId);
    };
  }

  /**
   * Copy of {@link com.intellij.testFramework.PlatformTestUtil#executeConfiguration(RunConfiguration, String)} without waiting
   */
  protected Pair<ExecutionEnvironment, RunContentDescriptor> executeConfiguration(@NotNull RunConfiguration runConfiguration,
                                                                                  @NotNull String executorId) throws InterruptedException {
    Executor executor = ExecutorRegistry.getInstance().getExecutorById(executorId);
    assertNotNull("Unable to find executor: " + executorId, executor);
    return executeConfiguration(runConfiguration, executor);
  }

  protected @NotNull Pair<ExecutionEnvironment, RunContentDescriptor> executeConfiguration(@NotNull RunConfiguration runConfiguration,
                                                                                           Executor executor) throws InterruptedException {
    Project project = runConfiguration.getProject();
    ConfigurationFactory factory = runConfiguration.getFactory();
    if (factory == null) {
      fail("No factory found for: " + runConfiguration);
    }
    RunnerAndConfigurationSettings runnerAndConfigurationSettings =
      RunManager.getInstance(project).createConfiguration(runConfiguration, factory);
    ProgramRunner<?> runner = ProgramRunner.getRunner(executor.getId(), runConfiguration);
    if (runner == null) {
      fail("No runner found for: " + executor.getId() + " and " + runConfiguration);
    }
    Ref<RunContentDescriptor> refRunContentDescriptor = new Ref<>();
    ExecutionEnvironment executionEnvironment =
      new ExecutionEnvironment(executor, runner, runnerAndConfigurationSettings, project);
    CountDownLatch latch = new CountDownLatch(1);
    ProgramRunnerUtil.executeConfigurationAsync(executionEnvironment, false, false, descriptor -> {
      LOG.debug("Process started");
      refRunContentDescriptor.set(descriptor);
      ProcessHandler processHandler = descriptor.getProcessHandler();
      assertNotNull(processHandler);
      CapturingProcessAdapter capturingAdapter = new CapturingProcessAdapter();
      processHandler.addProcessListener(capturingAdapter);
      processHandler.addProcessListener(new ProcessAdapter() {
        @Override
        public void startNotified(@NotNull ProcessEvent event) {
          LOG.debug("Process notified");
        }

        @Override
        public void processTerminated(@NotNull ProcessEvent event) {
          LOG.debug("Process terminated with " + event.getExitCode());
        }

        @Override
        public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
          LOG.debug(outputType + ": " + event.getText());
        }
      });
      ADAPTER_KEY.set(executionEnvironment, capturingAdapter);
      latch.countDown();
    });
    NonBlockingReadActionImpl.waitForAsyncTaskCompletion();
    if (!latch.await(60, TimeUnit.SECONDS)) {
      fail("Process failed to start");
    }
    RunContentDescriptor runContentDescriptor = refRunContentDescriptor.get();
    ProcessHandler processHandler = runContentDescriptor.getProcessHandler();
    assertNotNull(processHandler);
    disposeOnPerlTearDown(runContentDescriptor);
    disposeOnPerlTearDown(() -> {
      if (!processHandler.isProcessTerminated()) {
        processHandler.destroyProcess();
      }
      UIUtil.dispatchAllInvocationEvents();
    });
    return Pair.create(executionEnvironment, runContentDescriptor);
  }

  protected final @Nullable CapturingProcessAdapter getCapturingAdapter(@NotNull ExecutionEnvironment environment) {
    return ADAPTER_KEY.get(environment);
  }

  protected String getResultsTestDataPath() {
    return getTestDataPath();
  }

  public String getTestResultsFilePath(@NotNull String appendix) {
    return getResultsTestDataPath() + "/" + computeAnswerFileName(appendix);
  }

  protected @NotNull String computeAnswerFileName(@NotNull String appendix) {
    return computeAnswerFileNameWithoutExtension(appendix) + "." + getResultsFileExtension();
  }

  protected @NotNull String computeAnswerFileNameWithoutExtension(@NotNull String appendix) {
    return getTestName(true) + appendix;
  }

  protected @NotNull String getResultsFileExtension() {
    return "txt";
  }

  protected void waitForProcessFinish(ProcessHandler processHandler) {
    waitWithEventsDispatching("Process failed to finish in time", processHandler::isProcessTerminated);
  }

  protected @NotNull String serializeOutput(@Nullable ProcessOutput processOutput) {
    if (processOutput == null) {
      return "null";
    }
    return "Exit code: " + processOutput.getExitCode() + PerlLightTestCaseBase.SEPARATOR_NEWLINES +
           "Stdout: " + processOutput.getStdout() + PerlLightTestCaseBase.SEPARATOR_NEWLINES +
           "Stderr: " + processOutput.getStderr();
  }

  protected @NotNull String serializeTestNode(@Nullable SMTestProxy node, @NotNull String indent) {
    if (node == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder(indent).append(node.getName());
    String state;
    if (node.isIgnored()) {
      state = "ignored";
    }
    else if (node.isInterrupted()) {
      state = "interrupted";
    }
    else if (node.isPassed()) {
      state = "passed";
    }
    else {
      state = "failed";
    }
    sb.append(" (").append(state);
    if (node.isSuite()) {
      sb.append(" suite");
    }
    else {
      sb.append(" test");
    }
    sb.append(")");
    String stacktrace = node.getStacktrace();
    if (StringUtil.isNotEmpty(stacktrace)) {
      sb.append(PerlLightTestCaseBase.SEPARATOR_NEWLINES)
        .append(stacktrace.replaceAll(Objects.requireNonNull(getProject().getBasePath()), "/DATA_PATH"))
        .append(PerlLightTestCaseBase.SEPARATOR_NEWLINES);
    }
    else {
      sb.append("\n");
    }


    for (SMTestProxy child : node.getChildren()) {
      sb.append(StringUtil.trimEnd(serializeTestNode(child, "  " + indent), '\n')).append("\n");
    }

    return sb.toString();
  }

  protected void runTestConfigurationWithExecutorAndCheckResultsWithFile(GenericPerlRunConfiguration runConfiguration, String executorId) {
    Pair<ExecutionEnvironment, RunContentDescriptor> execResult;
    try {
      execResult = executeConfiguration(runConfiguration, executorId);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    RunContentDescriptor contentDescriptor = execResult.second;
    ProcessHandler processHandler = contentDescriptor.getProcessHandler();
    assertNotNull(processHandler);
    waitForProcessFinish(processHandler);
    checkTestRunResultsWithFile(contentDescriptor);
  }

  protected void checkTestRunResultsWithFile(RunContentDescriptor contentDescriptor) {
    ExecutionConsole executionConsole = contentDescriptor.getExecutionConsole();
    assertInstanceOf(executionConsole, PerlSMTRunnerConsoleView.class);
    SMTestRunnerResultsForm resultsViewer = ((PerlSMTRunnerConsoleView)executionConsole).getResultsViewer();
    TestTreeView testTreeView = resultsViewer.getTreeView();
    assertNotNull(testTreeView);
    Promise<?> promise = TreeUtil.promiseExpandAll(testTreeView);
    waitWithEventsDispatching("Failed to expand tests tree", promise::isSucceeded);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(".tests"), serializeTestNode(resultsViewer.getTestsRootNode(), ""));
  }

  protected @NotNull PerlTestRunConfiguration createTestRunConfiguration(@NotNull String file) {
    GenericPerlRunConfiguration runConfiguration = createOnlyRunConfiguration(file);
    assertInstanceOf(runConfiguration, PerlTestRunConfiguration.class);
    return (PerlTestRunConfiguration)runConfiguration;
  }

  protected void waitWithEventsDispatching(@NotNull String errorMessage, @NotNull BooleanSupplier condition) {
    long start = System.currentTimeMillis();
    while (true) {
      try {
        if (System.currentTimeMillis() - start > MAX_WAIT_TIME) {
          fail(errorMessage);
        }
        if (condition.getAsBoolean()) {
          break;
        }
        PlatformTestUtil.dispatchAllEventsInIdeEventQueue();
        Thread.sleep(10);
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
