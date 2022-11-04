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
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.testframework.TestTreeView;
import com.intellij.execution.testframework.sm.runner.SMTestProxy;
import com.intellij.execution.testframework.sm.runner.ui.SMTestRunnerResultsForm;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.impl.NonBlockingReadActionImpl;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.testFramework.*;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.util.ObjectUtils;
import com.intellij.util.concurrency.Semaphore;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.tree.TreeUtil;
import com.perl5.lang.perl.adapters.CpanAdapter;
import com.perl5.lang.perl.adapters.CpanminusAdapter;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.prove.PerlSMTRunnerConsoleView;
import com.perl5.lang.perl.idea.run.prove.PerlTestRunConfiguration;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import com.pty4j.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.Promise;
import org.junit.Assume;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

@RunWith(Parameterized.class)
public abstract class PerlPlatformTestCase extends HeavyPlatformTestCase {
  private static final int MAX_WAIT_TIME = 10_000;
  protected static final Logger LOG = Logger.getInstance(PerlPlatformTestCase.class);
  private static final Key<CapturingProcessAdapter> ADAPTER_KEY = Key.create("process.adapter");
  static final @NotNull String PERL_TEST_VERSION =
    ObjectUtils.notNull(System.getenv("PERL_TEST_VERSION"), System.getProperty("perl.test.version"));

  protected final Disposable myPerlTestCaseDisposable = Disposer.newDisposable();

  private @NotNull PerlInterpreterConfigurator myInterpreterConfigurator;

  public PerlPlatformTestCase(@NotNull PerlInterpreterConfigurator interpreterConfigurator) {
    myInterpreterConfigurator = interpreterConfigurator;
  }

  @org.junit.runners.Parameterized.Parameters(name = "sdk: {0}")
  public static Iterable<Object[]> fakeData() {
    return Collections.emptyList();
  }

  @com.intellij.testFramework.Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> realData(Class<?> clazz) {
    return Arrays.asList(new Object[][]{
      {PerlBrewLocalInterpreterConfigurator.INSTANCE},
      {PlenvLocalInterpreterConfigurator.INSTANCE},
      {PerlSystemDockerInterpreterConfigurator.INSTANCE}
    });
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myInterpreterConfigurator.setUpPerlInterpreter(myProject);
    PerlRunUtil.setUpForTests(myPerlTestCaseDisposable);
  }

  protected void disposeOnPerlTearDown(@NotNull Disposable disposable) {
    Disposer.register(myPerlTestCaseDisposable, disposable);
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

  @Override
  protected void tearDown() throws Exception {
    try {
      ApplicationManager.getApplication().invokeAndWait(() -> {
        PerlProjectManager projectManager = PerlProjectManager.getInstance(getProject());
        projectManager.setProjectSdk(null);
        projectManager.setExternalLibraries(Collections.emptyList());

        var sdkTable = PerlSdkTable.getInstance();
        for (Sdk sdk : sdkTable.getAllJdks()) {
          sdkTable.removeJdk(sdk);
        }
      });

      Disposer.dispose(myPerlTestCaseDisposable);
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


  protected @Nullable Sdk getSdk() {
    return PerlProjectManager.getSdk(getModule());
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
    PlatformTestUtil.dispatchAllEventsInIdeEventQueue();
  }

  protected void runActionWithTestEvent(@NotNull AnAction action) {
    var actionEvent = new TestActionEvent(action);
    action.update(actionEvent);
    var eventPresentation = actionEvent.getPresentation();
    assertTrue("Action is not visible: " + action, eventPresentation.isVisible());
    assertTrue("Action is not enabled: " + action, eventPresentation.isEnabled());
    action.actionPerformed(actionEvent);
    PlatformTestUtil.dispatchAllEventsInIdeEventQueue();
  }

  protected String getBaseDataPath() {
    return "";
  }

  protected final String getTestDataPath() {
    File file = new File(PerlLightTestCaseBase.TEST_RESOURCES_ROOT, getBaseDataPath());
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

  protected @NotNull VirtualFile getFileInModule(@NotNull String relativePath) {
    var moduleRoot = getModuleRoot();
    var result = moduleRoot.findFileByRelativePath(relativePath);
    assertNotNull("Could not find file " + relativePath + " in module " + getModule(), result);
    return result;
  }

  protected FileEditor @NotNull [] openModuleFileInEditor(@NotNull String relativePath) {
    return FileEditorManager.getInstance(getProject()).openFile(getFileInModule(relativePath), true, true);
  }

  /**
   * @return current module only content root
   */
  protected @NotNull VirtualFile getModuleRoot() {
    var contentRoots = ModuleRootManager.getInstance(getModule()).getContentRoots();
    if (contentRoots.length != 1) {
      fail("Expected a single root module, got: " + List.of(contentRoots));
    }
    return contentRoots[0];
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

  protected @NotNull Pair<ExecutionEnvironment, RunContentDescriptor> runConfigurationAndWait(GenericPerlRunConfiguration runConfiguration) {
    Pair<ExecutionEnvironment, RunContentDescriptor> execResult;
    try {
      execResult = executeConfiguration(runConfiguration, DefaultRunExecutor.EXECUTOR_ID);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    RunContentDescriptor contentDescriptor = execResult.second;
    ProcessHandler processHandler = contentDescriptor.getProcessHandler();
    assertNotNull(processHandler);
    waitForProcessFinish(processHandler);
    return execResult;
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
      var localRoot = Objects.requireNonNull(getProject().getBasePath());
      var sdk = getSdk();
      assertNotNull(sdk);
      PerlHostData<?, ?> hostData = PerlHostData.notNullFrom(sdk);
      var remoteRoot = hostData.getRemotePath(localRoot);
      assertNotNull("No remote root for local root: " + localRoot, remoteRoot);
      sb.append(PerlLightTestCaseBase.SEPARATOR_NEWLINES)
        .append(stacktrace.replace(remoteRoot, "/DATA_PATH"))
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
    waitWithEventsDispatching(errorMessage, condition, MAX_WAIT_TIME);
  }

  protected void waitWithEventsDispatching(@NotNull String errorMessage, @NotNull BooleanSupplier condition, int maxWaitTime) {
    long start = System.currentTimeMillis();
    while (true) {
      try {
        if (System.currentTimeMillis() - start > maxWaitTime) {
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

  protected void waitForAllDescriptorsToFinish() {
    var descriptors = PerlRunUtil.getTestConsoleDescriptors();
    for (RunContentDescriptor descriptor : descriptors) {
      var processHandler = descriptor.getProcessHandler();
      if (processHandler != null) {
        waitForProcessFinish(processHandler);
        disposeOnPerlTearDown(descriptor.getExecutionConsole());
      }
    }
  }

  protected @NotNull PsiFile getPackageFile(@NotNull PsiFile contextPsiFile, @NotNull String packageName) {
    PsiFile packagePsiFile;
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    packagePsiFile = PerlPackageUtil.resolvePackageNameToPsi(contextPsiFile, packageName);
    assertNotNull("Package file is missing: " + packageName, packagePsiFile);
    return packagePsiFile;
  }

  protected void assertPackageNotExists(@NotNull PsiFile contextPsiFile, @NotNull String packageName) {
    var packagePsiFile = PerlPackageUtil.resolvePackageNameToPsi(contextPsiFile, packageName);
    var packageVirtualFile = PsiUtilCore.getVirtualFile(packagePsiFile);
    if (packageVirtualFile != null) {
      packageVirtualFile.getParent().refresh(false, false);
      packagePsiFile = PerlPackageUtil.resolvePackageNameToPsi(contextPsiFile, packageName);
      packageVirtualFile = PsiUtilCore.getVirtualFile(packagePsiFile);
    }
    assertNull("Package presence is not expected to be installed, but got " + packageVirtualFile, packagePsiFile);
  }

  protected @NotNull PsiFile installPackageWithCpanminusAndGetPackageFile(@NotNull PsiFile contextPsiFile, @NotNull String packageName) {
    return installPackageAndGetPackageFile(
      contextPsiFile, packageName,
      (sdk, callback) -> CpanminusAdapter.createInstallAction(sdk, getProject(), List.of(packageName), callback)
    );
  }

  protected @NotNull PsiFile installPackageWithCpanAndGetPackageFile(@NotNull PsiFile contextPsiFile, @NotNull String packageName) {
    return installPackageAndGetPackageFile(
      contextPsiFile, packageName, (sdk, callback) -> CpanAdapter.createInstallAction(sdk, getProject(), List.of(packageName), callback)
    );
  }

  private @NotNull PsiFile installPackageAndGetPackageFile(@NotNull PsiFile contextPsiFile,
                                                           @NotNull String packageName,
                                                           @NotNull BiFunction<Sdk, Runnable, AnAction> actionFunction) {
    var sdk = getSdk();
    assertNotNull(sdk);
    var semaphore = new Semaphore(1);
    var installAction = actionFunction.apply(sdk, semaphore::up);
    assertNotNull(installAction);
    runActionWithTestEvent(installAction);
    waitForAllDescriptorsToFinish();
    waitWithEventsDispatching("Install action hasn't finished in time", semaphore::isUp, 30_000);
    var sdkRefreshSemaphore = PerlRunUtil.getSdkRefreshSemaphore();
    waitWithEventsDispatching("Sdk refresh hasn't finished", sdkRefreshSemaphore::isUp);
    return getPackageFile(contextPsiFile, packageName);
  }

  protected @NotNull VirtualFile openAndGetModuleFileInEditor(@NotNull String path) {
    var editors = openModuleFileInEditor(path);
    assertSize(1, editors);
    var openedVirtualFile = editors[0].getFile();
    assertNotNull(openedVirtualFile);
    return openedVirtualFile;
  }

  protected @NotNull Ref<Notification> createNotificationSink() {
    Ref<Notification> notificationRef = Ref.create();
    getProject().getMessageBus().connect(myPerlTestCaseDisposable).subscribe(Notifications.TOPIC, new Notifications() {
      @Override
      public void notify(@NotNull Notification notification) {
        notificationRef.set(notification);
      }
    });
    return notificationRef;
  }

  protected void assumeStatefulSdk() {
    Assume.assumeTrue("Not applicable in stateless sdk", myInterpreterConfigurator.isStateful());
  }
}
