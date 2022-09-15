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

package run;

import base.PerlInterpreterConfigurator;
import base.PerlPlatformTestCase;
import categories.Heavy;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.progress.EmptyProgressIndicator;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.profiler.*;
import com.intellij.profiler.api.*;
import com.intellij.profiler.api.configurations.ProfilerConfigurationState;
import com.intellij.profiler.api.configurations.ProfilerRunConfigurationsManager;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.prove.PerlTestRunConfiguration;
import com.perl5.lang.perl.profiler.configuration.PerlProfilerConfigurationState;
import com.perl5.lang.perl.profiler.parser.PerlProfilerCollapsedDumpFileParserProvider;
import com.perl5.lang.perl.profiler.parser.PerlProfilerDumpFileParserProvider;
import com.perl5.lang.perl.profiler.parser.PerlProfilerDumpWriter;
import com.perl5.lang.perl.profiler.run.PerlProfilerProcess;
import com.perl5.lang.perl.profiler.run.PerlProfilerRunProfileState;
import com.perl5.lang.perl.profiler.run.PerlProfilerStartupMode;
import com.pty4j.util.Pair;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("UnconstructableJUnitTestCase")
@Category(Heavy.class)
public class PerlProfilerTest extends PerlPlatformTestCase {
  private Element myConfigurations;

  public PerlProfilerTest(@NotNull PerlInterpreterConfigurator interpreterConfigurator) {
    super(interpreterConfigurator);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myConfigurations = ProfilerRunConfigurationsManager.getInstance().getState();
  }

  @Override
  protected void tearDown() throws Exception {
    try {
      ProfilerRunConfigurationsManager.getInstance().loadState(myConfigurations);
      PlatformTestUtil.dispatchAllEventsInIdeEventQueue();
    }
    finally {
      super.tearDown();
    }
  }

  @Override
  protected String getBaseDataPath() {
    return "run/profiler";
  }

  @Override
  protected String getResultsTestDataPath() {
    return super.getResultsTestDataPath() + "/answers";
  }

  @Test
  public void testRunWithProfilerBegin() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.BEGIN));
    checkProfilingResultsWithFile(runScriptWithProfilingAndWait("simple", "testscript.pl"));
  }

  @Test
  public void testProfilerSerialization() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.BEGIN));
    var executionResult = runScriptWithProfilingAndWait("simple", "testscript.pl");

    // data on execution
    checkProfilingResultsWithFile(executionResult);

    // explicit reading of the nytprof.out
    RunProfileState runProfileState = null;
    try {
      runProfileState = executionResult.first.first.getState();
    }
    catch (ExecutionException e) {
      fail(e.getMessage());
    }
    assertInstanceOf(runProfileState, PerlProfilerRunProfileState.class);
    var resultsPath = ((PerlProfilerRunProfileState)runProfileState).getProfilingResultsPath();
    var outputFiles = resultsPath.toFile().listFiles();
    assertNotNull("Result directory is not a directory: " + resultsPath, outputFiles);
    if (outputFiles.length != 1) {
      fail("Expected one resulting file, got: " + Arrays.asList(outputFiles));
    }
    var nytprofResultsFile = outputFiles[0];
    var nytprofParser = new PerlProfilerDumpFileParserProvider().createParser(getProject());
    var loadedNytprofResults = nytprofParser.parse(nytprofResultsFile, new EmptyProgressIndicator());
    assertInstanceOf(loadedNytprofResults, Success.class);
    var treeBuilder = getCallTreeBuilder(((Success)loadedNytprofResults).getData());
    checkProfilingResultsWithFile(treeBuilder);

    // writing of collapsed tree
    var collapsedDumpWriter = new PerlProfilerDumpWriter(nytprofResultsFile, treeBuilder);
    var collapsedDumpFile = new File(nytprofResultsFile.getParent(), "nytprof.gz");
    collapsedDumpWriter.writeDump(collapsedDumpFile, new EmptyProgressIndicator());
    assertTrue("Can't find collapsed dump file: " + collapsedDumpFile, collapsedDumpFile.exists());

    // reading of collapsed tree
    var collapsedDumpParser = new PerlProfilerCollapsedDumpFileParserProvider().createParser(getProject());
    var collapsedParsingResults = collapsedDumpParser.parse(collapsedDumpFile, new EmptyProgressIndicator());
    assertInstanceOf(collapsedParsingResults, Success.class);
    checkProfilingResultsWithFile(getCallTreeBuilder(((Success)collapsedParsingResults).getData()));
  }

  @Test
  public void testRunWithProfilerInit() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.INIT));
    checkProfilingResultsWithFile(runScriptWithProfilingAndWait("simple", "testscript.pl"));
  }

  @Test
  public void testRunWithProfilerEnd() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.END));
    checkProfilingResultsWithFile(runScriptWithProfilingAndWait("simple", "testscript.pl"));
  }

  @Test
  public void testRunWithProfilerNo() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.NO));
    checkProfilingResultsWithFile(runScriptWithProfilingAndWait("simple", "testscript_manual.pl"));
  }

  protected void checkProfilingResultsWithFile(Pair<Pair<ExecutionEnvironment, RunContentDescriptor>, Pair<Executor, PerlProfilerConfigurationState>> executionResult) {
    checkProfilingResultsWithFile(getProfilingResults(executionResult));
  }

  private @NotNull ProfilerState getProfilingResults(Pair<Pair<ExecutionEnvironment, RunContentDescriptor>, Pair<Executor, PerlProfilerConfigurationState>> executionResult) {
    return getProfilingResults(executionResult.first.first.getRunProfile(), executionResult.second.second);
  }

  @Test
  public void testProfilingTests() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.INIT));
    copyDirToModule("../run/testMore");
    var runConfiguration = createTestRunConfiguration("t");
    runTestsWithPRofiler(runConfiguration);
  }

  @Test
  public void testProfilingTestsParallel() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.INIT));
    copyDirToModule("../run/testMore");
    var runConfiguration = createTestRunConfiguration("t");
    assertInstanceOf(runConfiguration, PerlTestRunConfiguration.class);
    runConfiguration.setJobsNumber(4);
    runTestsWithPRofiler(runConfiguration);
  }

  protected void runTestsWithPRofiler(PerlTestRunConfiguration runConfiguration) {
    var execResults = runConfigurationWithProfilingAndWait(runConfiguration);
    Throwable failure = null;
    try {
      checkTestRunResultsWithFile(execResults.first.second);
    }
    catch (Throwable e) {
      failure = e;
    }
    checkProfilingResultsWithFile(execResults);
    if (failure != null) {
      throw new RuntimeException(failure);
    }
  }


  private Pair<Pair<ExecutionEnvironment, RunContentDescriptor>, Pair<Executor, PerlProfilerConfigurationState>> runScriptWithProfilingAndWait(
    @NotNull String directory,
    @NotNull String script) {
    copyDirToModule(directory);
    return runConfigurationWithProfilingAndWait(createOnlyRunConfiguration(script));
  }

  private void checkProfilingResultsWithFile(@NotNull ProfilerState profilerState) {
    checkProfilingResultsWithFile(getCallTreeBuilder(profilerState));
  }

  private void checkProfilingResultsWithFile(@NotNull CallTreeBuilder<BaseCallStackElement> treeBuilder) {
    assertInstanceOf(treeBuilder, DummyCallTreeBuilder.class);
    var stacks = ((DummyCallTreeBuilder)treeBuilder).getAllStacks();
    List<String> serializedFrames = new ArrayList<>();
    for (Object stack : stacks) {
      assertInstanceOf(stack, Stack.class);
      var frames = ((Stack)stack).getFrames();
      List<String> serializedStack = new ArrayList<>();
      for (Object frame : frames) {
        var frameString = frame.toString();
        var prefix = "ANON__[";
        var prefixOffset = frameString.indexOf(prefix);
        if (prefixOffset > -1) {
          frameString = frameString.substring(0, prefixOffset + prefix.length()) +
                        frameString.substring(frameString.lastIndexOf('/'));
        }
        serializedStack.add(frameString);
      }
      serializedFrames.add(String.join("; ", serializedStack));
    }
    Collections.sort(serializedFrames);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(".profiling"), String.join("\n", serializedFrames));
  }

  private @NotNull CallTreeBuilder<BaseCallStackElement> getCallTreeBuilder(@NotNull ProfilerState profilerState) {
    assertInstanceOf(profilerState, DataReady.class);
    return getCallTreeBuilder(((DataReady)profilerState).getData());
  }

  private @NotNull CallTreeBuilder<BaseCallStackElement> getCallTreeBuilder(ProfilerData profilerData) {
    assertInstanceOf(profilerData, NewCallTreeOnlyProfilerData.class);
    return ((NewCallTreeOnlyProfilerData)profilerData).getBuilder();
  }

  private @NotNull Pair<Pair<ExecutionEnvironment, RunContentDescriptor>, Pair<Executor, PerlProfilerConfigurationState>> runConfigurationWithProfilingAndWait(
    @NotNull GenericPerlRunConfiguration runConfiguration) {
    var executorAndSettings = getExecutorAndSettings();
    Pair<ExecutionEnvironment, RunContentDescriptor> pair;
    try {
      pair = executeConfiguration(runConfiguration, executorAndSettings.first);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    RunContentDescriptor contentDescriptor = pair.second;
    ProcessHandler processHandler = contentDescriptor.getProcessHandler();
    assertNotNull(processHandler);
    waitForProcessFinish(processHandler);
    Integer exitCode = processHandler.getExitCode();
    LOG.debug("Profiling process finished with exit code: ", exitCode);
    return Pair.create(pair, executorAndSettings);
  }

  private @NotNull Pair<Executor, PerlProfilerConfigurationState> getExecutorAndSettings() {
    var executorGroup = DefaultProfilerExecutorGroup.Companion.getInstance();
    assertNotNull(executorGroup);
    var executors = executorGroup.childExecutors();
    Pair<Executor, PerlProfilerConfigurationState> result = null;
    for (Executor executor : executors) {
      var settings = executorGroup.getRegisteredSettings(executor.getId());
      assertNotNull(settings);
      var configurationState = settings.getState();
      if (configurationState instanceof PerlProfilerConfigurationState) {
        assertNull(result);
        result = Pair.create(executor, (PerlProfilerConfigurationState)configurationState);
      }
    }
    assertNotNull(result);
    return result;
  }

  private ProfilerState getProfilingResults(@NotNull RunProfile runConfiguration,
                                            @NotNull PerlProfilerConfigurationState profilerConfiguration) {
    var perlProfilerProcess = getProfilerProcess(runConfiguration, profilerConfiguration);
    waitWithEventsDispatching("Unable to fetch profiling results", () -> !perlProfilerProcess.getState().isActive());
    return perlProfilerProcess.getState();
  }

  private @NotNull PerlProfilerProcess getProfilerProcess(@NotNull RunProfile runConfiguration,
                                                          @NotNull PerlProfilerConfigurationState profilerConfiguration) {
    var toolWindow = getProfilerToolWindow();
    var toolWindowContents = toolWindow.getContentManager().getContents();
    assertSize(1, toolWindowContents);
    var firstComponent = toolWindowContents[0].getComponent();
    assertInstanceOf(firstComponent, ProfilerProcessPanel.class);
    ProfilerProcess<?> profilerProcess = ((ProfilerProcessPanel)firstComponent).getProfilerProcess();
    assertInstanceOf(profilerProcess, PerlProfilerProcess.class);
    assertEquals(profilerConfiguration, ((PerlProfilerProcess)profilerProcess).getProfilerConfiguration());
    assertEquals(runConfiguration, ((PerlProfilerProcess)profilerProcess).getRunProfile());
    return (PerlProfilerProcess)profilerProcess;
  }

  private @NotNull ToolWindow getProfilerToolWindow() {
    var toolWindow = ToolWindowManager.getInstance(myProject).getToolWindow(ProfilerToolWindowManager.TOOLWINDOW_ID);
    assertNotNull(toolWindow);
    return toolWindow;
  }

  private void modifyOnlyConfiguration(@NotNull Consumer<PerlProfilerConfigurationState> stateModifier) {
    var profilerRunConfigurationsManager = ProfilerRunConfigurationsManager.getInstance();
    var configurations = profilerRunConfigurationsManager.getConfigurations();
    for (ProfilerConfigurationState configuration : configurations) {
      if (configuration instanceof PerlProfilerConfigurationState) {
        stateModifier.accept((PerlProfilerConfigurationState)configuration);
        break;
      }
    }
    profilerRunConfigurationsManager.setConfigurations(configurations);
  }
}
